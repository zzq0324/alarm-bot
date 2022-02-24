package cn.zzq0324.alarm.bot.core.extension.cmd.context;

import cn.zzq0324.alarm.bot.core.entity.Message;
import lombok.Builder;
import lombok.Getter;

/**
 * description: CreateEventContext <br>
 * date: 2022/2/19 3:36 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Getter
public class SolveEventContext extends CommandContext {

    @Builder
    public SolveEventContext(String command, Message message) {
        super(command);
        this.message = message;
    }

    private Message message;
}
