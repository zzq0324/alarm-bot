package cn.zzq0324.alarm.bot.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * description: 项目 <br>
 * date: 2022/2/10 10:36 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Data
public class Project {

    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_OFFLINE = 0;

    /**
     * 项目id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目名称，一般是英文名字，例如service-xxx
     */
    private String name;

    /**
     * 所有者ID
     */
    private Long ownerId;

    /**
     * 所有者姓名
     */
    private String ownerName;

    /**
     * 项目成员id列表
     */
    private String memberIds;

    /**
     * 项目状态
     */
    private int status;

    /**
     * 项目描述
     */
    private String description;
}
