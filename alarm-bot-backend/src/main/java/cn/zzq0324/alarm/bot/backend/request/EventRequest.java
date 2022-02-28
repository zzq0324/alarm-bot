package cn.zzq0324.alarm.bot.backend.request;

import lombok.Data;

/**
 * description: EventRequest <br>
 * date: 2022/2/28 1:02 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Data
public class EventRequest extends PageRequest{

    public static final int ALL = -1;

    /**
     * 项目ID
     */
    private long projectId;

    /**
     * 项目状态
     */
    private int status = ALL;

    /**
     * 开始时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;
}
