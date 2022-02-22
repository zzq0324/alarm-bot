package cn.zzq0324.alarm.bot.entity;

import cn.zzq0324.alarm.bot.constant.Status;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * description: 事件模型 <br>
 * date: 2022/2/11 2:52 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Data
public class Event {

    /**
     * 主键，事件id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 事件对应的消息id
     */
    private String thirdMessageId;

    /**
     * 聊天群组id
     */
    private String chatGroupId;

    /**
     * 事件详细信息
     */
    private String detail;

    /**
     * 事件状态
     */
    private Status eventStatus;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 事件完成时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;

    /**
     * 小结信息
     */
    private String summary;
}
