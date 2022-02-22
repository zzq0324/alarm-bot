package cn.zzq0324.alarm.bot.entity;

import cn.zzq0324.alarm.bot.constant.Status;
import cn.zzq0324.alarm.bot.constant.TaskType;
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
    private TaskType taskType;

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
     * 触发时间
     */
    private Date triggerTime;
}
