package cn.zzq0324.alarm.bot.core.extension.cmd.impl;

import cn.zzq0324.alarm.bot.core.constant.CommandConstants;
import cn.zzq0324.alarm.bot.core.entity.Message;
import cn.zzq0324.alarm.bot.core.extension.cmd.Command;
import cn.zzq0324.alarm.bot.core.extension.cmd.context.CommandContext;
import cn.zzq0324.alarm.bot.core.extension.cmd.context.HelpContext;
import cn.zzq0324.alarm.bot.core.extension.platform.PlatformExt;
import cn.zzq0324.alarm.bot.core.spi.Extension;
import cn.zzq0324.alarm.bot.core.spi.ExtensionLoader;
import cn.zzq0324.alarm.bot.core.vo.IMMessage;

/**
 * description: Help <br>
 * date: 2022/2/19 11:44 上午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Extension(name = CommandConstants.HELP, summary = "指令-帮助")
public class Help implements Command<HelpContext> {

    @Override
    public CommandContext matchCommand(IMMessage imMessage) {
        Message message = imMessage.getMessageList().get(0);
        if (message.getContent().contains(CommandConstants.HELP)) {
            return HelpContext.builder().command(CommandConstants.HELP).message(message).build();
        }

        return null;
    }

    @Override
    public boolean execute(HelpContext context) {
        ExtensionLoader.getDefaultExtension(PlatformExt.class).help(context.getMessage());

        return true;
    }
}
