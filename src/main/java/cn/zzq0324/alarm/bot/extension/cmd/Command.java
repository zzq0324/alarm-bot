package cn.zzq0324.alarm.bot.extension.cmd;

import cn.zzq0324.alarm.bot.entity.Message;
import cn.zzq0324.alarm.bot.extension.cmd.context.CommandContext;
import cn.zzq0324.alarm.bot.spi.SPI;

/**
 * description: Command <br>
 * date: 2022/2/19 11:35 上午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@SPI("command")
public interface Command<T extends CommandContext> {

    CommandContext matchCommand(Message message);

    /**
     * 执行命令
     */
    void execute(T context);
}
