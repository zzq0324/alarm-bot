package cn.zzq0324.alarm.bot.core.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * description: 事件状态 <br>
 * date: 2022/2/18 3:21 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
public enum Status {
    CREATED(1, "已创建"),
    PROCESSING(2, "处理中"),
    FINISH(3, "处理完毕"),
    TIMEOUT_CLOSED(99, "超时关闭");

    @EnumValue
    private int value;
    private String desc;

    Status(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return this.value;
    }
}
