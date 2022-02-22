package cn.zzq0324.alarm.bot.extension.platform;

import cn.zzq0324.alarm.bot.extension.platform.impl.lark.FuzzyLarkMessage;
import cn.zzq0324.alarm.bot.extension.platform.impl.lark.LarkHelper;
import cn.zzq0324.alarm.bot.extension.platform.impl.lark.LarkMessageParser;
import com.alibaba.fastjson.JSONObject;
import com.larksuite.oapi.service.im.v1.model.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * description: LarkMessageParserTest <br>
 * date: 2022/2/22 6:07 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@SpringBootTest
public class LarkMessageParserTest {

    @Autowired
    private LarkHelper larkHelper;

    @Autowired
    private LarkMessageParser larkMessageParser;

    @Test
    public void testParse() {
        List<Message> messageList = larkHelper.downloadChatMessage("oc_3f78935d535c6999005def650a37d43c");

        messageList.stream().forEach(message -> {
            System.out.println(JSONObject.toJSONString(larkMessageParser.parse(new FuzzyLarkMessage(message))));
        });
    }
}
