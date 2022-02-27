package cn.zzq0324.alarm.bot.core.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * description: 成员状态 <br>
 * date: 2022/2/18 3:21 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
public enum MemberStatus {
    NORMAL(1, "正常状态"),
    DISABLE(0, "禁用状态");

    @EnumValue
    private int value;
    private String desc;

    MemberStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return this.value;
    }

    public static MemberStatus getByValue(int value){
        for(MemberStatus status: values()){
            if(status.getValue() == value){
                return status;
            }
        }

        return null;
    }
}
