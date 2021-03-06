package cn.zzq0324.alarm.bot.core.entity;

import cn.zzq0324.alarm.bot.core.constant.MessageType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * description: Message <br>
 * date: 2022/2/12 1:59 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Data
public class Message {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 消息类型
     */
    private MessageType messageType;

    /**
     * 三方消息ID
     */
    private String thirdMessageId;

    /**
     * 群组ID
     */
    private String chatGroupId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sendTime;

    /**
     * 发送人
     */
    private String sender;
}
