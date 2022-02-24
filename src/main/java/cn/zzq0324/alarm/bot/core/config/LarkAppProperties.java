package cn.zzq0324.alarm.bot.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * description: LarkAppProperties <br>
 * date: 2022/2/7 6:39 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Configuration
@ConfigurationProperties(prefix = "lark")
@Data
public class LarkAppProperties {

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 应用密钥
     */
    private String appSecret;

    /**
     * 校验token，确认回调数据是否是飞书后台，必须与飞书开发者后台配置保持一致。收到回调先解密后校验
     */
    private String verifyToken;

    /**
     * 回调数据加密的key，收到回调数据后需要先用key解密获得回调数据
     */
    private String encryptKey;
}
