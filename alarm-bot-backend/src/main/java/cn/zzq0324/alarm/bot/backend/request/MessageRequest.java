package cn.zzq0324.alarm.bot.backend.request;

import lombok.Data;

/**
 * description: MessageRequest <br>
 * date: 2022/2/28 3:51 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Data
public class MessageRequest extends PageRequest{

    /**
     * 群聊ID
     */
    private String chatGroupId;
}
