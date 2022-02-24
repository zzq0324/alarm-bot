package cn.zzq0324.alarm.bot.constant;

/**
 * description: 飞书事件 <br>
 * date: 2022/2/8 2:57 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
public enum LarkEvent {
    RECEIVE_MESSAGE("im.message.receive_v1", "接收消息"),
    ADD_USER("im.chat.member.user.added_v1", "拉用户入群"),
    DISBAND_GROUP("im.chat.disbanded_v1", "解散群聊");

    // 事件类型
    private String eventType;
    // 事件描述
    private String desc;

    LarkEvent(String eventType, String desc) {
        this.eventType = eventType;
        this.desc = desc;
    }

    public static LarkEvent getByType(String eventType) {
        for (LarkEvent event : values()) {
            if (event.eventType.equals(eventType)) {
                return event;
            }
        }

        return null;
    }

    public String getEventType() {
        return eventType;
    }

    public String getDesc() {
        return desc;
    }
}
