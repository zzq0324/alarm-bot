package cn.zzq0324.alarm.bot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * description: ResourceHandlerConfiguration <br>
 * date: 2022/2/22 5:56 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Configuration
public class ResourceHandlerConfiguration implements WebMvcConfigurer {

    /**
     * 附件下载前缀
     */
    public static final String RESOURCE_URL_PREFIX = "/files";

    @Autowired
    private AlarmBotProperties alarmBotProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 静态资源文件
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");

        // 如果文件存储在本地，则开启映射
        if (StringUtils.hasLength(alarmBotProperties.getResourceDownloadFolder())) {
            registry.addResourceHandler(RESOURCE_URL_PREFIX + "/**").addResourceLocations(
                "file:" + alarmBotProperties.getResourceDownloadFolder() + RESOURCE_URL_PREFIX + "/");
        }
    }
}
