package cn.zzq0324.alarm.bot.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * description: 消息类型 <br>
 * date: 2022/2/16 2:00 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
public enum MessageType {

    TEXT(1, "文本"),
    IMAGE(2, "图片"),
    OTHER(99, "其他");

    @EnumValue
    private int value;
    private String desc;

    MessageType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
