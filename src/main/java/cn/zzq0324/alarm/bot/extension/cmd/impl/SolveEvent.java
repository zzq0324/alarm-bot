package cn.zzq0324.alarm.bot.extension.cmd.impl;

import cn.zzq0324.alarm.bot.constant.CommandConstants;
import cn.zzq0324.alarm.bot.extension.cmd.Command;
import cn.zzq0324.alarm.bot.extension.cmd.context.SolveEventContext;
import cn.zzq0324.alarm.bot.extension.platform.PlatformExt;
import cn.zzq0324.alarm.bot.spi.Extension;
import cn.zzq0324.alarm.bot.spi.ExtensionLoader;
import org.springframework.util.StringUtils;

/**
 * description: SolveEvent <br>
 * date: 2022/2/19 11:45 上午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Extension(name = CommandConstants.SOLVE_EVENT, summary = "指令-解决事件")
public class SolveEvent implements Command<SolveEventContext> {
    @Override
    public void execute(SolveEventContext context) {
        String chatGroupId = context.getEvent().getChatGroupId();
        
        // 推送感谢，告知即将解散
        ExtensionLoader.getDefaultExtension(PlatformExt.class).send(chatGroupId, "【已解决】", "感谢各位");

        // 下载群聊消息

        // 如果是群聊发起，解散群聊
        if (StringUtils.hasLength(chatGroupId)) {
            ExtensionLoader.getDefaultExtension(PlatformExt.class).destroyChatGroup(chatGroupId);
        }

        // 回复原来的群聊消息，告知告警已解除以及处理时效

        // 增加事件日志
    }
}
