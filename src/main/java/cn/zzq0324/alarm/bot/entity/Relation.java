package cn.zzq0324.alarm.bot.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * description: 关系表 <br>
 * date: 2022/2/11 1:04 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Data
public class Relation {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联的源头id，例如项目id
     */
    private Long sourceId;

    /**
     * 关联的目标id，例如成员id
     */
    private Long destId;

    /**
     * 关系类型，例如项目-成员关系
     */
    private String type;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
