package cn.zzq0324.alarm.bot.core.extension.platform.impl.lark.parser.impl;

import cn.zzq0324.alarm.bot.core.constant.LarkConstants;
import cn.zzq0324.alarm.bot.core.constant.MessageType;
import cn.zzq0324.alarm.bot.core.entity.Message;
import cn.zzq0324.alarm.bot.core.spi.Extension;
import cn.zzq0324.alarm.bot.core.vo.IMMessage;
import cn.zzq0324.alarm.bot.core.extension.platform.impl.lark.parser.BaseLarkMessageParser;
import cn.zzq0324.alarm.bot.core.extension.platform.impl.lark.parser.LarkMessageParserExt;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.larksuite.oapi.service.im.v1.model.EventMessage;
import com.larksuite.oapi.service.im.v1.model.MentionEvent;
import com.larksuite.oapi.service.im.v1.model.MessageReceiveEventData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * description: 飞书富文本解析 <br>
 * date: 2022/2/22 9:50 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Slf4j
@Extension(name = LarkConstants.MESSAGE_TYPE_POST, summary = "飞书富文本消息解析")
public class LarkPostMessageParser extends BaseLarkMessageParser implements LarkMessageParserExt {

    @Override
    public void parse(IMMessage imMessage, MessageReceiveEventData eventData) {
        EventMessage eventMessage = eventData.getMessage();
        Map<String, MentionEvent> mentionMap = getMentionMap(eventMessage);
        List<Message> messageList = new ArrayList<>();

        JSONObject contentJson = JSONObject.parseObject(eventMessage.getContent());
        JSONArray lineContentArr = JSONObject.parseArray(contentJson.getString("content"));

        for (int i = 0; i < lineContentArr.size(); i++) {
            JSONArray lineContent = lineContentArr.getJSONArray(i);
            Message message = newMessage(eventData);
            readPostLineContent(lineContent, message, mentionMap);

            if (StringUtils.hasLength(message.getContent())) {
                messageList.add(message);
            }
        }

        imMessage.setAtRobot(isAtRobot(mentionMap));
        imMessage.setCommandContext(null);
        imMessage.setMessageList(messageList);
    }

    /**
     * 读取富文本的行内容
     */
    private void readPostLineContent(JSONArray lineContent, Message message, Map<String, MentionEvent> mentionMap) {
        StringBuilder contentBuilder = new StringBuilder();
        MessageType messageType = MessageType.TEXT;

        for (int i = 0; i < lineContent.size(); i++) {
            JSONObject tagContent = lineContent.getJSONObject(i);
            String tag = tagContent.getString("tag");
            switch (tag) {
                case "at":
                    contentBuilder.append("@").append(tagContent.getString("user_name")).append(" ");
                    break;

                case "text":
                    contentBuilder.append(tagContent.getString("text"));
                    break;

                // 图片
                case "img":
                    messageType = MessageType.IMAGE;
                    String url = downloadAndUpload(message.getThirdMessageId(), tagContent.getString("image_key"));
                    contentBuilder.append(url);
                    break;

                // 多媒体及其他暂时不处理
                case "media":
                    messageType = MessageType.MEDIA;
                    break;

                default:
                    log.warn("unsupport tag type: {}, messageId: {}", tag, message.getThirdMessageId());
            }
        }

        message.setContent(contentBuilder.toString());
        message.setMessageType(messageType);
    }
}
