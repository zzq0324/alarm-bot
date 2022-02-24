package cn.zzq0324.alarm.bot.core.extension.platform.impl.lark.parser;

import cn.zzq0324.alarm.bot.core.config.AlarmBotProperties;
import cn.zzq0324.alarm.bot.core.entity.Message;
import cn.zzq0324.alarm.bot.core.extension.platform.impl.lark.LarkHelper;
import cn.zzq0324.alarm.bot.core.extension.storage.StorageExt;
import cn.zzq0324.alarm.bot.core.spi.ExtensionLoader;
import cn.zzq0324.alarm.bot.core.vo.Operator;
import com.larksuite.oapi.service.contact.v3.model.User;
import com.larksuite.oapi.service.im.v1.model.EventMessage;
import com.larksuite.oapi.service.im.v1.model.EventSender;
import com.larksuite.oapi.service.im.v1.model.MentionEvent;
import com.larksuite.oapi.service.im.v1.model.MessageReceiveEventData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * description: BaseLarkMessageParser <br>
 * date: 2022/2/22 9:25 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
public class BaseLarkMessageParser {

    private static final Pattern AT_USER_PATTERN = Pattern.compile("(@_user_\\d)");

    @Autowired
    private AlarmBotProperties alarmBotProperties;

    @Autowired
    private LarkHelper larkHelper;

    /**
     * 获取@信息的map，key为@_user_xx，value为对应的人员信息
     */
    public Map<String, MentionEvent> getMentionMap(EventMessage eventMessage) {
        if (eventMessage.getMentions() == null) {
            return Collections.EMPTY_MAP;
        }

        return Arrays.stream(eventMessage.getMentions())
            .collect(Collectors.toMap(MentionEvent::getKey, mention -> mention));
    }

    /**
     * 格式化@XXX信息
     */
    public String formatAtInfo(String text, Map<String, MentionEvent> mentionMap) {
        Matcher matcher = AT_USER_PATTERN.matcher(text);
        String result = text;

        while (matcher.find()) {
            // key格式为@_user_xx
            String key = matcher.group();
            if (mentionMap.containsKey(key) && result.contains(key)) {
                result = result.replace(key, "@" + mentionMap.get(key).getName());
            }
        }

        return result;
    }

    public Message newMessage(MessageReceiveEventData eventData) {
        EventMessage eventMessage = eventData.getMessage();

        Message message = new Message();
        message.setThirdMessageId(eventMessage.getMessageId());
        message.setChatGroupId(eventMessage.getChatId());
        message.setSender(getOperator(eventData.getSender()));
        message.setSendTime(new Date(eventMessage.getCreateTime()));

        return message;
    }

    /**
     * 从飞书下载资源上传到自己的存储
     */
    public String downloadAndUpload(String messageId, String imageKey) {
        String fileName = messageId + ".png";
        byte[] imageData = larkHelper.downloadResource(messageId, imageKey, "image");

        return ExtensionLoader.getDefaultExtension(StorageExt.class).upload(imageData, fileName);
    }

    public Operator getOperator(EventSender sender) {
        User user = larkHelper.getLarkUser(sender.getSenderId().getOpenId(), "open_id");

        return new Operator(user.getName(), user.getOpenId(), user.getUnionId());
    }

    /**
     * 是否包含@机器人信息
     */
    public boolean isAtRobot(Map<String, MentionEvent> mentionMap) {
        if (CollectionUtils.isEmpty(mentionMap)) {
            return false;
        }

        for (MentionEvent mention : mentionMap.values()) {
            // 没有UserID并且是和application.yml里面的BotName配置一致，则判定为有@机器人
            if (!StringUtils.hasLength(mention.getId().getUserId()) && mention.getName()
                .equals(alarmBotProperties.getBotName())) {
                return true;
            }
        }

        return false;
    }
}
