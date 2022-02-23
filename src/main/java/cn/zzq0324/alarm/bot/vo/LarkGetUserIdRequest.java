package cn.zzq0324.alarm.bot.vo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * description: GetUserIdRequest <br>
 * date: 2022/2/23 5:51 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Data
public class LarkGetUserIdRequest {

    @SerializedName("mobiles")
    private String[] mobiles;
}
