package cn.zzq0324.alarm.bot.core.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * description: 解决SpringContextHolder静态访问报错的问题 <br>
 * date: 2022/2/10 6:19 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Configuration
@Import(SpringContextHolder.class)
public class SpringConfiguration {
}
