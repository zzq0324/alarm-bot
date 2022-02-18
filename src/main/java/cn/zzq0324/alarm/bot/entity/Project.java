package cn.zzq0324.alarm.bot.entity;

import lombok.Data;

/**
 * description: 项目 <br>
 * date: 2022/2/10 10:36 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Data
public class Project  {

    /**
     * 项目id
     */
    private Long id;

    /**
     * 项目名称，一般是英文名字，例如service-xxx
     */
    private String name;

    /**
     * 项目描述
     */
    private String description;
}
