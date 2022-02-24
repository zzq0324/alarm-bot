package cn.zzq0324.alarm.bot.core.extension.cmd.context;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * description: CommandContext <br>
 * date: 2022/2/19 3:32 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Data
@AllArgsConstructor
public class CommandContext {

    /**
     * 命令类型
     */
    private String command;
}
