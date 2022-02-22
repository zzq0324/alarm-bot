package cn.zzq0324.alarm.bot.extension.platform.impl.lark.parser.impl;

import cn.zzq0324.alarm.bot.constant.LarkConstants;
import cn.zzq0324.alarm.bot.constant.MessageType;
import cn.zzq0324.alarm.bot.entity.Message;
import cn.zzq0324.alarm.bot.extension.platform.impl.lark.parser.BaseLarkMessageParser;
import cn.zzq0324.alarm.bot.extension.platform.impl.lark.parser.LarkMessageParserExt;
import cn.zzq0324.alarm.bot.spi.Extension;
import cn.zzq0324.alarm.bot.vo.IMMessage;
import com.alibaba.fastjson.JSONObject;
import com.larksuite.oapi.service.im.v1.model.EventMessage;
import com.larksuite.oapi.service.im.v1.model.MessageReceiveEventData;

import java.util.Arrays;

/**
 * description: LarkImageMessageParser <br>
 * date: 2022/2/22 9:41 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Extension(name = LarkConstants.MESSAGE_TYPE_IMAGE, summary = "飞书图片消息解析")
public class LarkImageMessageParser extends BaseLarkMessageParser implements LarkMessageParserExt {
    @Override
    public void parse(IMMessage imMessage, MessageReceiveEventData eventData) {
        EventMessage eventMessage = eventData.getMessage();
        JSONObject contentJson = JSONObject.parseObject(eventMessage.getContent());
        String imageKey = contentJson.getString("image_key");

        // 下载飞书文件上传到存储
        String imageUrl = downloadAndUpload(eventMessage.getMessageId(), imageKey);

        Message message = newMessage(eventData);
        message.setMessageType(MessageType.IMAGE);
        message.setContent(imageUrl);

        imMessage.setAtRobot(false);
        imMessage.setCommandContext(null);
        imMessage.setMessageList(Arrays.asList(message));
    }
}
