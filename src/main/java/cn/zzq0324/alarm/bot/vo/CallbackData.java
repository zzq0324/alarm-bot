package cn.zzq0324.alarm.bot.vo;

import cn.zzq0324.alarm.bot.constant.CallbackType;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * description: 回调数据 <br>
 * date: 2022/2/7 10:27 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Data
public class CallbackData {

    /**
     * 回调类型，目前有两类：地址校验回调(配置url的时候调用）、事件通知回调。
     */
    private CallbackType callbackType = CallbackType.EVENT_CALLBACK;

    /**
     * 回调事件
     */
    private String eventType;

    /**
     * 回调数据
     */
    private JSONObject data;

    /**
     * 随机字符串，当回调类型是地址校验回调的时候会携带该字段
     */
    private String nonce;
}
