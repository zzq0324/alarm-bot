package cn.zzq0324.alarm.bot.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * description: 应用入口类 <br>
 * date: 2022/2/18 9:46 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@SpringBootApplication(scanBasePackages = "cn.zzq0324")
@EnableConfigurationProperties
@EnableScheduling
public class Start {
    public static void main(String[] args) {
        SpringApplication.run(Start.class);
    }
}
