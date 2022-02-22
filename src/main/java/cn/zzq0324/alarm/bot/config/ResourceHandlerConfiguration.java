package cn.zzq0324.alarm.bot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * description: ResourceHandlerConfiguration <br>
 * date: 2022/2/22 5:56 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Configuration
public class ResourceHandlerConfiguration extends WebMvcConfigurationSupport {

    /**
     * 附件下载前缀
     */
    public static final String RESOURCE_URL_PREFIX = "/resource";

    @Autowired
    private AlarmBotProperties alarmBotProperties;

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(RESOURCE_URL_PREFIX, alarmBotProperties.getResourceDownloadFolder());
        super.addResourceHandlers(registry);
    }
}
