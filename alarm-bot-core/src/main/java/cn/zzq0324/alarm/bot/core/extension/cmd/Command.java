package cn.zzq0324.alarm.bot.core.extension.cmd;

import cn.zzq0324.alarm.bot.core.extension.cmd.context.CommandContext;
import cn.zzq0324.alarm.bot.core.spi.SPI;
import cn.zzq0324.alarm.bot.core.vo.IMMessage;

/**
 * description: Command <br>
 * date: 2022/2/19 11:35 上午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@SPI("command")
public interface Command<T extends CommandContext> {

    CommandContext matchCommand(IMMessage imMessage);

    /**
     * 执行命令
     */
    boolean execute(T context);
}
