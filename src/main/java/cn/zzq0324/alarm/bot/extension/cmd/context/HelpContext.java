package cn.zzq0324.alarm.bot.extension.cmd.context;

import cn.zzq0324.alarm.bot.entity.Message;
import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.Getter;

/**
 * description: CreateEventContext <br>
 * date: 2022/2/19 3:36 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Getter
public class HelpContext extends CommandContext {

    @Builder
    public HelpContext(String command, JSONObject extra, Message message) {
        super(command, extra);
        this.message = message;
    }

    /**
     * 消息
     */
    private Message message;
}