package cn.zzq0324.alarm.bot.core.spi;

import cn.zzq0324.alarm.bot.core.spi.extension.DemoExtension;
import cn.zzq0324.alarm.bot.core.spi.extension.ExtensionImpl1;
import cn.zzq0324.alarm.bot.core.spi.extension.ExtensionImpl2;
import cn.zzq0324.alarm.bot.core.spi.extension2.DemoExtension2;
import cn.zzq0324.alarm.bot.core.spi.extension2.ExtensionImpl3;
import cn.zzq0324.alarm.bot.core.spi.extension3.DemoExtension3;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * description: ExtensionLoaderTest <br>
 * date: 2022/2/5 9:44 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ExtensionLoaderTest {

    @Test
    public void testInitLoader() {
        ExtensionLoader<DemoExtension> extensionLoader = ExtensionLoader.getExtensionLoader(DemoExtension.class);
        Assertions.assertNotNull(extensionLoader);
    }

    @Test
    public void testGetInstance() {
        DemoExtension extension = ExtensionLoader.getExtension(DemoExtension.class, "extensionImpl1");

        Assertions.assertEquals(extension.getClass(), ExtensionImpl1.class);
    }

    @Test
    public void testGetDefaultExtension() {
        // 未通过配置文件指定默认实现节点
        DemoExtension2 extension2 = ExtensionLoader.getDefaultExtension(DemoExtension2.class);
        Assertions.assertEquals(extension2.getClass(), ExtensionImpl3.class);

        // 通过配置指定默认实现节点
        DemoExtension extension = ExtensionLoader.getDefaultExtension(DemoExtension.class);
        Assertions.assertEquals(extension.getClass(), ExtensionImpl2.class);

        // 注解未指定并且配置也未指定
        Assertions.assertNull(ExtensionLoader.getDefaultExtension(DemoExtension3.class));
    }
}

