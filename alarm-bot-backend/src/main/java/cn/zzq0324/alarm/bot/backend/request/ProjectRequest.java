package cn.zzq0324.alarm.bot.backend.request;

import lombok.Data;

/**
 * description: ProjectRequest <br>
 * date: 2022/2/27 7:15 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Data
public class ProjectRequest extends PageRequest {

    public static final int ALL = -1;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 所有者姓名
     */
    private String ownerName;

    /**
     * 项目状态
     */
    private int status = ALL;
}
