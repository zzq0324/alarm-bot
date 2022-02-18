package cn.zzq0324.alarm.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * description: 应用入口类 <br>
 * date: 2022/2/18 9:46 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@org.springframework.boot.autoconfigure.SpringBootApplication
@EnableConfigurationProperties
public class AlarmBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(AlarmBotApplication.class);
    }
}
