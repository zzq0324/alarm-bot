package cn.zzq0324.alarm.bot.backend.request;

import lombok.Data;

/**
 * description: MemberRequest <br>
 * date: 2022/2/26 10:13 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Data
public class MemberRequest extends PageRequest {

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;
}
