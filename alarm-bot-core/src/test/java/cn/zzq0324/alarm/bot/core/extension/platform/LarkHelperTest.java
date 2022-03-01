package cn.zzq0324.alarm.bot.core.extension.platform;

import cn.zzq0324.alarm.bot.core.config.AlarmBotProperties;
import cn.zzq0324.alarm.bot.core.extension.platform.impl.lark.LarkHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * description: LarkHelperTest <br>
 * date: 2022/2/19 9:55 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@SpringBootTest
public class LarkHelperTest {

    @Autowired
    private LarkHelper larkHelper;
    @Autowired
    private AlarmBotProperties alarmBotProperties;

    @Test
    public void testGetOpenIdByMobile() {
        Assertions.assertNotNull(larkHelper.getOpenIdByIdentify("x"));
    }

    @Test
    public void testDestroyChatGroup() {
        larkHelper.destroyChatGroup("oc_3fd1a44c8698de6ce122d3de2600ecd5");
    }

    @Test
    public void testReplyText() {
        larkHelper.replyText("om_bf38ecfdd34d8d5e7dc9f0df70036c6d", "", alarmBotProperties.getReplyAlarm());
    }

    @Test
    public void downloadResource() {
        Assertions.assertNotNull(larkHelper.downloadResource("om_90c029cf7679401d4f1a910ee31a3fdc",
            "img_v2_16efb8ae-13ca-4ad7-93f2-18a2ae61e3bg", "image"));
    }

    @Test
    public void downloadChatGroupMessage() {
        larkHelper.downloadChatMessage("oc_3f78935d535c6999005def650a37d43c");
    }
}
