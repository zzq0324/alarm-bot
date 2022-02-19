package cn.zzq0324.alarm.bot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * description: 事件日志 <br>
 * date: 2022/2/11 6:31 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Data
public class EventLog {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
}
