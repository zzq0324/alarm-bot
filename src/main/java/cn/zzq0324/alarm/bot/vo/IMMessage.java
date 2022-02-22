package cn.zzq0324.alarm.bot.vo;

import cn.zzq0324.alarm.bot.entity.Message;
import cn.zzq0324.alarm.bot.extension.cmd.context.CommandContext;
import lombok.Data;

import java.util.List;

/**
 * description: 回调的IM消息 <br>
 * date: 2022/2/22 7:56 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Data
public class IMMessage {

    /**
     * 是否有@机器人
     */
    private boolean isAtRobot;

    /**
     * 命令上下文，有匹配到命令的时候才出现
     */
    private CommandContext commandContext;

    /**
     * 解析后的消息
     */
    private List<Message> messageList;
}
