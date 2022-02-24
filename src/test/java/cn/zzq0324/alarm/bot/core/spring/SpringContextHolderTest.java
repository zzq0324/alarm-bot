package cn.zzq0324.alarm.bot.core.spring;

import cn.zzq0324.alarm.bot.core.extension.platform.PlatformExt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

/**
 * description: SpringContextHolderTest <br>
 * date: 2022/2/19 5:45 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@SpringBootTest
public class SpringContextHolderTest {

    @Test
    public void testGetBeansOfType() {
        Map<String, PlatformExt> beanMap = SpringContextHolder.getContext().getBeansOfType(PlatformExt.class);

        Assertions.assertEquals(2, beanMap.size());
    }
}
