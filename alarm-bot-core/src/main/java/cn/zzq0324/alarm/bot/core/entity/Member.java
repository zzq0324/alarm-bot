package cn.zzq0324.alarm.bot.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
     * 手机号
     */
    private String mobile;

    /**
     * 三方平台的openId
     */
    private String openId;

    /**
     * 三方平台的unionId
     */
    private String unionId;

    /**
     * 创建时间
     */
    private Date createTime;
}
