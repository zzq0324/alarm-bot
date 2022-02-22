package cn.zzq0324.alarm.bot.entity;

import cn.zzq0324.alarm.bot.constant.Status;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * description: Task <br>
 * date: date: 2022/2/21 11:47 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Data
public class Task {

    /**
     * 主键，事件id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 任务类型
     */
    private String type;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 触发任务携带的日志
     */
    private String data;

    /**
     * 任务状态
     */
    private Status status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 下一次触发时间，如果为空代表不需要触发
     */
    private Date nextTriggerTime;

    /**
     * 上一次触发时间
     */
    private Date lastTriggerTime;

    /**
     * 上一次触发结果，如有异常此处填写异常信息
     */
    private String lastTriggerResult;
}
