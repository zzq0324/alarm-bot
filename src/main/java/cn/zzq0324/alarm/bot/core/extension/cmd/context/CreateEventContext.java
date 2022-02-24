package cn.zzq0324.alarm.bot.core.extension.cmd.context;

import cn.zzq0324.alarm.bot.core.entity.Message;
import cn.zzq0324.alarm.bot.core.entity.Project;
import lombok.Builder;
import lombok.Getter;

/**
 * description: CreateEventContext <br>
 * date: 2022/2/19 3:36 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Getter
public class CreateEventContext extends CommandContext {

    @Builder
    public CreateEventContext(String command, Message message, Project project) {
        super(command);
        this.message = message;
        this.project = project;
    }

    /**
     * 消息
     */
    private Message message;

    /**
     * 项目
     */
    private Project project;
}
