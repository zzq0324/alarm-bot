package cn.zzq0324.alarm.bot.extension.cmd;

import cn.zzq0324.alarm.bot.entity.Message;
import cn.zzq0324.alarm.bot.spi.SPI;

/**
 * description: 互动指令 <br>
 * date: 2022/2/10 10:23 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@SPI("command")
public interface CommandExt {

    /**
     * 根据命令执行
     */
    void execute(Message message);
}
