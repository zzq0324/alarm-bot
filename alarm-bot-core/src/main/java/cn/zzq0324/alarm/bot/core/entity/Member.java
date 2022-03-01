package cn.zzq0324.alarm.bot.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * description: 项目成员 <br>
 * date: 2022/2/10 10:40 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Data
public class Member {

    public static final int STATUS_DISABLE = 0;

    public static final int STATUS_NORMAL = 1;

    /**
     * 成员ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 标识，可以是手机号或者邮箱，由使用方统一规范
     */
    private String identity;

    /**
     * 三方平台的openId
     */
    private String openId;

    /**
     * 三方平台的unionId
     */
    private String unionId;

    /**
     * 状态
     */
    private int status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd mm:HH:ss", timezone = "GMT+8")
    private Date createTime;
}
