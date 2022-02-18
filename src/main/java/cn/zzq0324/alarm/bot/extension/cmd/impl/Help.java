package cn.zzq0324.alarm.bot.extension.cmd.impl;

import cn.zzq0324.alarm.bot.entity.Message;
import cn.zzq0324.alarm.bot.extension.cmd.CommandExt;
import cn.zzq0324.alarm.bot.spi.Extension;

/**
 * description: Help <br>
 * date: 2022/2/18 11:00 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Extension(name = "help", isDefault = true, summary = "帮助指令")
public class Help implements CommandExt {

    @Override
    public void execute(Message message) {

    }
}
