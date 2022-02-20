package cn.zzq0324.alarm.bot.extension.platform;

import cn.zzq0324.alarm.bot.config.AlarmBotProperties;
import cn.zzq0324.alarm.bot.extension.platform.impl.Lark;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * description: LarkTest <br>
 * date: 2022/2/19 9:55 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@SpringBootTest
public class LarkTest {

    @Autowired
    private Lark lark;
    @Autowired
    private AlarmBotProperties alarmBotProperties;

    @Test
    public void testGetOpenIdByMobile() {
        Assertions.assertNotNull(lark.getOpenIdByMobile("180xxxxx"));
    }

    @Test
    public void testDestroyChatGroup() {
        lark.destroyChatGroup("oc_3fd1a44c8698de6ce122d3de2600ecd5");
    }

    @Test
    public void testReplyText() {
        lark.replyText("om_bf38ecfdd34d8d5e7dc9f0df70036c6d", alarmBotProperties.getReplyAlarm());
    }
}
