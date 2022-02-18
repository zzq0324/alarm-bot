package cn.zzq0324.alarm.bot.entity;

import lombok.Data;

/**
 * description: 配置表 <br>
 * date: 2022/2/10 10:42 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Data
public class Config {

    /**
     * 主键
     */
    private Long id;

    /**
     * 配置-键
     */
    public String key;

    /**
     * 配置-值
     */
    private String value;

    /**
     * 配置描述
     */
    private String description;
}
