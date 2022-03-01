package cn.zzq0324.alarm.bot.web.task;

import cn.zzq0324.alarm.bot.core.config.AlarmBotProperties;
import cn.zzq0324.alarm.bot.core.constant.LarkConstants;
import cn.zzq0324.alarm.bot.core.constant.PlatformType;
import cn.zzq0324.alarm.bot.core.entity.Event;
import cn.zzq0324.alarm.bot.core.extension.cmd.Command;
import cn.zzq0324.alarm.bot.core.extension.cmd.context.CommandContext;
import cn.zzq0324.alarm.bot.core.extension.cmd.context.CreateEventContext;
import cn.zzq0324.alarm.bot.core.extension.platform.PlatformExt;
import cn.zzq0324.alarm.bot.core.extension.platform.impl.lark.LarkHelper;
import cn.zzq0324.alarm.bot.core.service.EventService;
import cn.zzq0324.alarm.bot.core.spi.ExtensionLoader;
import cn.zzq0324.alarm.bot.core.util.ChatGroupUtils;
import cn.zzq0324.alarm.bot.core.util.DateUtils;
import cn.zzq0324.alarm.bot.core.vo.IMMessage;
import com.larksuite.oapi.service.bot.v3.model.BotInfo;
import com.larksuite.oapi.service.im.v1.model.EventMessage;
import com.larksuite.oapi.service.im.v1.model.EventSender;
import com.larksuite.oapi.service.im.v1.model.ListChat;
import com.larksuite.oapi.service.im.v1.model.Mention;
import com.larksuite.oapi.service.im.v1.model.MentionEvent;
import com.larksuite.oapi.service.im.v1.model.Message;
import com.larksuite.oapi.service.im.v1.model.MessageReceiveEventData;
import com.larksuite.oapi.service.im.v1.model.UserId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * description: 飞书机器人HOOK消息监听，主要原因是飞书官方的消息回调不包含机器人信息 <br>
 * date: 2022/3/1 9:26 上午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Component
@Slf4j
public class LarkRobotHookMessageScanner {

    @Autowired
    private LarkHelper larkHelper;

    @Autowired
    private AlarmBotProperties alarmBotProperties;

    @Autowired
    private EventService eventService;

    @Scheduled(initialDelay = 60000, fixedRate = 60000)
    public void execute() {
        String platform = ExtensionLoader.getDefaultExtensionName(PlatformExt.class);

        // 当前不是用飞书，不处理
        if (!PlatformType.LARK.equals(platform)) {
            return;
        }

        // 获取机器人信息
        BotInfo botInfo = larkHelper.getBotInfo();

        // 获取机器人所在的所有群组
        try {
            List<ListChat> chatGroupList = larkHelper.getRobotChatGroups();

            chatGroupList.stream().forEach(chatGroup -> {
                try {
                    checkChatGroupHookMessage(chatGroup, botInfo);
                } catch (Exception e) {
                    log.error("check chatGroup: {} hook message error", chatGroup.getName(), e);
                }
            });

        } catch (Exception e) {
            log.error("lark robot hook message scan error", e);
        }
    }

    private void checkChatGroupHookMessage(ListChat chatGroup, BotInfo botInfo) {
        // 如果机器人是群主，代表当前是告警群，不处理
        if (isAlertGroup(chatGroup, botInfo)) {
            return;
        }

        // 获取最近消息
        Date now = new Date();
        // 单位都是秒
        long endTime = now.getTime() / 1000;
        long startTime = getStartTime(now);

        // 查询对应的群聊消息并遍历，仅处理机器人的消息
        List<Message> messageList = larkHelper.downloadChatMessage(chatGroup.getChatId(), startTime, endTime);
        for (Message message : messageList) {
            if (message.getSender().getIdType().equals(LarkConstants.ID_TYPE)) {
                parseAndProcessMessage(message);
            }
        }
    }

    private long getStartTime(Date date) {
        long mills = DateUtils.add(date, -1 * alarmBotProperties.getCheckRobotHookMessage(), Calendar.MINUTE).getTime();
        return mills / 1000;
    }

    /**
     * 解析并处理消息
     */
    private void parseAndProcessMessage(Message message) {
        // 转化为MessageReceiveEventData，方便复用其他的类
        MessageReceiveEventData eventData = convert2EventData(message);

        // 解析消息
        IMMessage imMessage = larkHelper.parse(eventData);
        if (CollectionUtils.isEmpty(imMessage.getMessageList())) {
            return;
        }

        CommandContext commandContext = imMessage.getCommandContext();
        // 只处理创建命令
        if (!(commandContext instanceof CreateEventContext)) {
            return;
        }

        cn.zzq0324.alarm.bot.core.entity.Message firstMessage = imMessage.getMessageList().get(0);
        // 判断消息是否已处理过，未处理过直接执行
        Event existEvent = eventService.getByThirdMessageId(firstMessage.getThirdMessageId());
        if (existEvent != null) {
            return;
        }

        // 执行逻辑
        ExtensionLoader.getExtension(Command.class, commandContext.getCommand()).execute(commandContext);
    }

    private MessageReceiveEventData convert2EventData(Message message) {
        EventSender eventSender = new EventSender();
        BeanUtils.copyProperties(message.getSender(), eventSender);
        UserId senderId = new UserId();
        senderId.setOpenId(message.getSender().getId());
        eventSender.setSenderId(senderId);

        EventMessage eventMessage = new EventMessage();
        BeanUtils.copyProperties(message, eventMessage);
        eventMessage.setContent(message.getBody().getContent());
        eventMessage.setMessageType(message.getMsgType());
        // 设置mention信息
        List<MentionEvent> mentionEvents = new ArrayList<>();
        if (message.getMentions() != null && message.getMentions().length > 0) {
            for (Mention mention : message.getMentions()) {
                MentionEvent mentionEvent = new MentionEvent();
                BeanUtils.copyProperties(mention, mentionEvent);

                UserId userId = new UserId();
                userId.setOpenId(mention.getId());
                mentionEvent.setId(userId);

                mentionEvents.add(mentionEvent);
            }
        }
        eventMessage.setMentions(mentionEvents.toArray(new MentionEvent[mentionEvents.size()]));

        MessageReceiveEventData eventData = new MessageReceiveEventData();
        eventData.setMessage(eventMessage);
        eventData.setSender(eventSender);

        return eventData;
    }

    private boolean isAlertGroup(ListChat chatGroup, BotInfo botInfo) {
        if (!ChatGroupUtils.isAlertChatGroupName(chatGroup.getName())) {
            return false;
        }

        // 群主id为空或者群主id等于机器人的openID，代表是告警专项处理群
        if (StringUtils.isEmpty(chatGroup.getOwnerId()) || botInfo.getOpenId().equals(chatGroup.getOwnerId())) {
            return true;
        }

        return false;
    }
}
