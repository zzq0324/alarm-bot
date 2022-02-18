package cn.zzq0324.alarm.bot.constant;

/**
 * description: 飞书常量定义 <br>
 * date: 2021/12/29 1:18 下午 <br>
 * author: Eric <br>
 * version: 1.0 <br>
 */
public interface LarkConstants {
    /**
     * 飞书调用成功返回code=0
     */
    int RESPONSE_SUCCESS_CODE = 0;

    /**
     * url校验的回调类型
     */
    String CALLBACK_TYPE_URL_VERIFICATION = "url_verification";

    /**
     * 事件通知的回调类型
     */
    String CALLBACK_TYPE_EVENT_CALLBACK = "event_callback";
}
