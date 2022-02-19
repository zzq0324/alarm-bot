package cn.zzq0324.alarm.bot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * description: 成员在三方平台的信息表，每个平台允许有一条 <br>
 * date: 2022/2/19 9:14 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Data
public class MemberPlatformInfo {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 成员ID
     */
    private Long memberId;

    /**
     * 平台，详见Platform里面的定义，有lark、dingtalk以及wechat等
     */
    private String platform;

    /**
     * 三方平台的openId
     */
    private String openId;

    /**
     * 三方平台的unionId
     */
    private String unionId;

    /**
     * 三方平台的name
     */
    private String name;

    /**
     * 创建时间
     */
    private Date createTime;
}
