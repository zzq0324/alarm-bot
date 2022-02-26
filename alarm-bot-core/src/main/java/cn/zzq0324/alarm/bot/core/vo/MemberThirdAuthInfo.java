package cn.zzq0324.alarm.bot.core.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * description: 成员三方授权信息 <br>
 * date: 2022/2/26 8:51 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Data
@AllArgsConstructor
public class MemberThirdAuthInfo {

    /**
     * 姓名
     */
    private String name;

    /**
     * 三方平台的openId
     */
    private String openId;

    /**
     * 三方平台的unionId
     */
    private String unionId;
}
