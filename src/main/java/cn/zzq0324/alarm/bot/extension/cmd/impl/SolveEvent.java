package cn.zzq0324.alarm.bot.extension.cmd.impl;

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

    @Override
    public CommandContext matchCommand(Message message) {
        // 解决事件
        if (message.getContent().contains(CommandConstants.SOLVE_EVENT)) {
            Event event = eventService.getByChatGroupId(message.getChatGroupId());

            return SolveEventContext.builder().command(CommandConstants.SOLVE_EVENT).event(event).build();
        }

        return null;
    }

    @Override
    public void execute(SolveEventContext context) {
        // 代表根据群在event列表找不到记录，说明是其他地方发起，不处理
        if (context.getEvent() == null) {
            return;
        }

        String chatGroupId = context.getEvent().getChatGroupId();

        // TODO 推送感谢，告知即将解散

        // TODO 下载群聊消息

        // 如果是群聊发起，解散群聊
        if (StringUtils.hasLength(chatGroupId)) {
            ExtensionLoader.getDefaultExtension(PlatformExt.class).destroyChatGroup(chatGroupId);
        }

        // 回复原来的群聊消息，告知告警已解除以及处理时效

        // 增加事件日志
    }
}
