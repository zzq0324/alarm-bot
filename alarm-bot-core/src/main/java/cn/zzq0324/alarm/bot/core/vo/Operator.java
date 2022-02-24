package cn.zzq0324.alarm.bot.core.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * description: Operator <br>
 * date: 2022/2/12 2:14 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Data
@AllArgsConstructor
public class Operator {

    /**
     * 用户名
     */
    private String name;

    /**
     * openID
     */
    private String openID;

    /**
     * unionID
     */
    private String unionID;
}
