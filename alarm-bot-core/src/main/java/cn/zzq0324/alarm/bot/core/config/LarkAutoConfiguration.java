package cn.zzq0324.alarm.bot.core.config;

import com.larksuite.oapi.core.AppSettings;
import com.larksuite.oapi.core.Config;
import com.larksuite.oapi.core.DefaultStore;
import com.larksuite.oapi.core.Domain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * description: LarkAutoConfiguration <br>
 * date: 2022/2/7 10:30 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Configuration
public class LarkAutoConfiguration {

    @Autowired
    private LarkAppProperties larkAppProperties;

    @Bean
    public Config initConfig() {
        AppSettings appSettings =
            Config.createInternalAppSettings(larkAppProperties.getAppId(), larkAppProperties.getAppSecret(),
                larkAppProperties.getVerifyToken(), larkAppProperties.getEncryptKey());

        return new Config(Domain.FeiShu, appSettings, new DefaultStore());
    }
}
