package cn.zzq0324.alarm.bot.entity;

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
     * 创建时间
     */
    private Date createTime;
}
