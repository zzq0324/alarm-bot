package cn.zzq0324.alarm.bot.core.extension.platform;

import cn.zzq0324.alarm.bot.core.config.AlarmBotProperties;
import cn.zzq0324.alarm.bot.core.extension.platform.impl.lark.LarkHelper;
import com.alibaba.fastjson.JSONObject;
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
        Assertions.assertNotNull(larkHelper.getOpenIdByIdentify("zhengzq@homeking365.com"));
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
        System.out.println(
            JSONObject.toJSONString(larkHelper.downloadChatMessage("oc_b49eb724e3c8c2d922d17bd0a014b300", 0, 0)));
    }

    @Test
    public void testGetBotInfo() {
        Assertions.assertNotNull(larkHelper.getBotInfo());
    }

    @Test
    public void testSendWebHookMsg() {
        larkHelper.sendWebHookMsg("https://open.feishu.cn/open-apis/bot/v2/hook/78cda34b-e647-4c96-be4f-5637a60ca791",
            "测试消息", "<at user_id=\"all\">所有人</at> 以下人员离职，请注意跟进：\n姓名：xxx，身份标识：xxx，影响项目：xxx\n姓名：xxx，身份标识：xxx，影响项目：xxx");
    }
}
