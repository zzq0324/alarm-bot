package cn.zzq0324.alarm.bot.extension.cmd.impl;

import cn.zzq0324.alarm.bot.config.AlarmBotProperties;
import cn.zzq0324.alarm.bot.constant.CommandConstants;
import cn.zzq0324.alarm.bot.entity.Event;
import cn.zzq0324.alarm.bot.entity.Message;
import cn.zzq0324.alarm.bot.extension.cmd.Command;
import cn.zzq0324.alarm.bot.extension.cmd.context.CommandContext;
import cn.zzq0324.alarm.bot.extension.cmd.context.SolveEventContext;
import cn.zzq0324.alarm.bot.extension.platform.PlatformExt;
import cn.zzq0324.alarm.bot.service.EventService;
import cn.zzq0324.alarm.bot.spi.Extension;
import cn.zzq0324.alarm.bot.spi.ExtensionLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

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

    @Override
    public CommandContext matchCommand(Message message) {
        // 解决事件
        if (message.getContent().contains(CommandConstants.SOLVE_EVENT)) {
            Event event = eventService.getByChatGroupId(message.getChatGroupId());

            return SolveEventContext.builder().command(CommandConstants.SOLVE_EVENT).message(message).event(event)
                .build();
        }

        return null;
    }

    @Override
    public void execute(SolveEventContext context) {
        Message message = context.getMessage();

        // 代表根据群在event列表找不到记录，说明是其他地方发起，不处理
        if (context.getEvent() == null) {
            ExtensionLoader.getDefaultExtension(PlatformExt.class)
                .replyText(message.getThirdMessageId(), alarmBotProperties.getNotInAlarmGroup());

            return;
        }

        // 如果命令未带小结信息，则推送消息告知完善
        if (isSummaryMissing(message.getContent())) {
            ExtensionLoader.getDefaultExtension(PlatformExt.class)
                .replyText(message.getThirdMessageId(), alarmBotProperties.getSolveSummaryMissing());

            return;
        }

        Event event = context.getEvent();
        String chatGroupId = event.getChatGroupId();

        //  回复群消息，感谢并告知即将解散
        ExtensionLoader.getDefaultExtension(PlatformExt.class)
            .replyText(message.getThirdMessageId(), alarmBotProperties.getReplyEventSolved());

        // TODO 下载群聊消息

        // 如果是群聊发起，解散群聊
        ExtensionLoader.getDefaultExtension(PlatformExt.class).destroyChatGroup(chatGroupId);

        // 回复原来的群聊消息，告知告警已解除以及处理时效
        long currentTimeMillis = System.currentTimeMillis();
        long eventCreateTimeMillis = event.getCreateTime().getTime();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(currentTimeMillis - eventCreateTimeMillis);
        String replyMessage = String.format(alarmBotProperties.getReplySolvedToChatGroup(), minutes);
        ExtensionLoader.getDefaultExtension(PlatformExt.class).replyText(event.getThirdMessageId(), replyMessage);

        // 增加事件日志
    }

    /**
     * 是否缺失告警小结
     */
    private boolean isSummaryMissing(String content) {
        // 替换@机器人信息
        content = content.replace("@" + alarmBotProperties.getBotName(), "");
        // 替换/solve命令
        content = content.replace(CommandConstants.SOLVE_EVENT, "").trim();
        if (StringUtils.hasLength(content)) {
            return false;
        }

        return true;
    }
}
