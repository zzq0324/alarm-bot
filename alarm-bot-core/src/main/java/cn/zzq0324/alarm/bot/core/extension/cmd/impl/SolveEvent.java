package cn.zzq0324.alarm.bot.core.extension.cmd.impl;

import cn.zzq0324.alarm.bot.core.config.AlarmBotProperties;
import cn.zzq0324.alarm.bot.core.constant.CommandConstants;
import cn.zzq0324.alarm.bot.core.constant.TaskType;
import cn.zzq0324.alarm.bot.core.entity.Event;
import cn.zzq0324.alarm.bot.core.entity.Message;
import cn.zzq0324.alarm.bot.core.extension.cmd.Command;
import cn.zzq0324.alarm.bot.core.service.EventService;
import cn.zzq0324.alarm.bot.core.service.TaskService;
import cn.zzq0324.alarm.bot.core.spi.Extension;
import cn.zzq0324.alarm.bot.core.spi.ExtensionLoader;
import cn.zzq0324.alarm.bot.core.util.DateUtils;
import cn.zzq0324.alarm.bot.core.extension.cmd.context.CommandContext;
import cn.zzq0324.alarm.bot.core.extension.cmd.context.SolveEventContext;
import cn.zzq0324.alarm.bot.core.extension.platform.PlatformExt;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * description: SolveEvent <br>
 * date: 2022/2/19 11:45 上午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Extension(name = CommandConstants.SOLVE_EVENT, summary = "指令-解决事件")
@Slf4j
public class SolveEvent implements Command<SolveEventContext> {

    @Autowired
    private EventService eventService;
    @Autowired
    private AlarmBotProperties alarmBotProperties;
    @Autowired
    private TaskService taskService;

    @Override
    public CommandContext matchCommand(Message message) {
        // 解决事件
        if (message.getContent().contains(CommandConstants.SOLVE_EVENT)) {
            Event event = eventService.getByChatGroupId(message.getChatGroupId());

            return SolveEventContext.builder().command(CommandConstants.SOLVE_EVENT).message(message).build();
        }

        return null;
    }

    @Transactional
    @Override
    public void execute(SolveEventContext context) {
        Message message = context.getMessage();
        Event event = eventService.getByChatGroupId(message.getChatGroupId());

        // 代表根据群在event列表找不到记录，说明是其他地方发起，不处理
        if (event == null) {
            ExtensionLoader.getDefaultExtension(PlatformExt.class)
                .replyText(message.getThirdMessageId(), alarmBotProperties.getNotInAlarmGroup());

            return;
        }

        String summary = getSummary(message.getContent());
        // 如果命令未带小结信息，则推送消息告知完善
        if (StringUtils.isEmpty(summary)) {
            ExtensionLoader.getDefaultExtension(PlatformExt.class)
                .replyText(message.getThirdMessageId(), alarmBotProperties.getSolveSummaryMissing());

            return;
        }

        Date now = new Date();
        event.setFinishTime(now);

        //  回复群消息，感谢并告知即将解散
        String replyDestroyChatGroupText =
            String.format(alarmBotProperties.getReplyEventSolved(), alarmBotProperties.getDestroyGroupAfterMinutes());
        ExtensionLoader.getDefaultExtension(PlatformExt.class)
            .replyText(message.getThirdMessageId(), replyDestroyChatGroupText);

        // 增加解散群的定时任务
        Date triggerTime = DateUtils.add(new Date(), alarmBotProperties.getDestroyGroupAfterMinutes(), Calendar.MINUTE);
        taskService.addTask(TaskType.DESTROY_CHAT_GROUP, triggerTime, JSONObject.toJSONString(event));

        // 回复原来的群聊消息，告知告警已解除以及处理时效
        replyAlarmGroupSolved(event, summary);

        // 更新事件状态并增加日志
        event.setSummary(summary);
        eventService.closeEvent(event);
    }

    /**
     * 回复原来的群聊消息告警已处理
     */
    private void replyAlarmGroupSolved(Event event, String summary) {
        String replyMessage = String.format(alarmBotProperties.getReplySolvedToChatGroup(),
            DateUtils.getDiffText(event.getCreateTime(), event.getFinishTime()), summary);
        
        ExtensionLoader.getDefaultExtension(PlatformExt.class).replyText(event.getThirdMessageId(), replyMessage);
    }

    /**
     * 是否缺失告警小结
     */
    private String getSummary(String content) {
        // 替换@机器人信息
        content = content.replace("@" + alarmBotProperties.getBotName(), "");
        // 替换/solve命令
        content = content.replace(CommandConstants.SOLVE_EVENT, "").trim();

        return content;
    }
}
