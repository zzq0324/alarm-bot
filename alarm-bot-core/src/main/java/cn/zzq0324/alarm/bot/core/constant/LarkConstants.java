package cn.zzq0324.alarm.bot.core.constant;

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

    /**
     * 消息类型-文本
     */
    String MESSAGE_TYPE_TEXT = "text";

    /**
     * 消息类型-图片
     */
    String MESSAGE_TYPE_IMAGE = "image";

    /**
     * 富文本
     */
    String MESSAGE_TYPE_POST = "post";

    /**
     * 消息类型-卡片
     */
    String MESSAGE_TYPE_INTERACTIVE = "interactive";

    /**
     * 消息类型-系统消息，例如：xxx加入群聊
     */
    String MESSAGE_TYPE_SYSTEM = "system";

    String ID_TYPE = "app_id";

    /**
     * 发送类型
     */
    String SENDER_TYPE = "app";
}
