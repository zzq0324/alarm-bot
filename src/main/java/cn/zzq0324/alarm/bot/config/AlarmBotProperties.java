package cn.zzq0324.alarm.bot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * description: AlarmBotProperties <br>
 * date: 2022/2/8 1:23 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Configuration
@ConfigurationProperties(prefix = "alarm-bot")
@Data
public class AlarmBotProperties {

    /**
     * 服务ID对应的正则表达式，从正则表达式中解析出serviceId
     */
    private String projectNameRegExp;
}
