package cn.zzq0324.alarm.bot.extension.cmd.impl;

import cn.zzq0324.alarm.bot.constant.CommandConstants;
import cn.zzq0324.alarm.bot.extension.cmd.Command;
import cn.zzq0324.alarm.bot.extension.cmd.context.HelpContext;
import cn.zzq0324.alarm.bot.extension.platform.PlatformExt;
import cn.zzq0324.alarm.bot.spi.Extension;
import cn.zzq0324.alarm.bot.spi.ExtensionLoader;

/**
 * description: Help <br>
 * date: 2022/2/19 11:44 上午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Extension(name = CommandConstants.HELP, summary = "指令-帮助")
public class Help implements Command<HelpContext> {
    @Override
    public void execute(HelpContext context) {
        ExtensionLoader.getDefaultExtension(PlatformExt.class).help(context.getMessage());
    }
}
