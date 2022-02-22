package cn.zzq0324.alarm.bot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * description: Lock <br>
 * date: date: 2022/2/21 11:47 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Data
@AllArgsConstructor
@TableName("`lock`")
public class Lock {

    @TableField("`key`")
    private String key;
}
