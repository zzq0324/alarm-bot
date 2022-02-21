package cn.zzq0324.alarm.bot.extension.platform.impl.lark;

import cn.zzq0324.alarm.bot.constant.LarkConstants;
import cn.zzq0324.alarm.bot.constant.MessageType;
import cn.zzq0324.alarm.bot.entity.Message;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.larksuite.oapi.service.im.v1.model.Mention;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * description: 飞书消息解析，包括文本、图片等 <br>
 * date: 2022/2/20 8:40 上午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Component
public class LarkMessageParser {

    private static final Pattern AT_USER_PATTERN = Pattern.compile("(@_user_\\d)");

    /**
     * 解析的格式详见：https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/reference/im-v1/message/list
     *
     * @param message
     * @return
     */
    public List<Message> parse(FuzzyLarkMessage message) {
        // @信息解析成map
        Map<String, Mention> mentionMap = getMentionMap(message);

        switch (message.getMsgType()) {
            case LarkConstants.MESSAGE_TYPE_TEXT:
                return parseText(message, mentionMap);

            case LarkConstants.MESSAGE_TYPE_POST:
                return parsePost(message, mentionMap);

            default:
                throw new UnsupportedOperationException("unsupport message type: " + message.getMsgType());
        }
    }

    /**
     * 获取@信息的map，key为
     *
     * @param message
     * @return
     */
    private Map<String, Mention> getMentionMap(FuzzyLarkMessage message) {
        if (message.getMentions() == null) {
            return Collections.EMPTY_MAP;
        }

        return Arrays.stream(message.getMentions()).collect(Collectors.toMap(Mention::getKey, mention -> mention));
    }

    private List<Message> parseText(FuzzyLarkMessage larkMessage, Map<String, Mention> mentionMap) {
        JSONObject content = JSONObject.parseObject(larkMessage.getBody().getContent());

        Message message = newMessage(larkMessage);
        message.setMessageType(MessageType.TEXT);
        message.setContent(formatAtInfo(content.getString("text"), mentionMap));

        return Arrays.asList(message);
    }

    private List<Message> parsePost(FuzzyLarkMessage larkMessage, Map<String, Mention> mentionMap) {
        JSONArray content = JSONObject.parseArray(larkMessage.getBody().getContent());
        List<Message> messageList = new ArrayList<>();

        for (int i = 0; i < content.size(); i++) {
            JSONArray lineContent = content.getJSONArray(i);
            Message message = newMessage(larkMessage);
            readPostLineContent(lineContent, message, mentionMap);

            if (StringUtils.hasLength(message.getContent())) {
                messageList.add(message);
            }
        }

        return messageList;
    }

    /**
     * 读取富文本的行内容
     */
    private void readPostLineContent(JSONArray lineContent, Message message, Map<String, Mention> mentionMap) {
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
                default:
            }
        }

        message.setContent(contentBuilder.toString());
        message.setMessageType(messageType);
    }

    private String formatAtInfo(String text, Map<String, Mention> mentionMap) {
        Matcher matcher = AT_USER_PATTERN.matcher(text);
        String result = text;

        while (matcher.find()) {
            // key格式为@_user_xx
            String key = matcher.group();
            if (mentionMap.containsKey(key) && result.contains(key)) {
                result = result.replace(key, mentionMap.get(key).getName());
            }
        }

        return result;
    }

    private Message newMessage(FuzzyLarkMessage larkMessage) {
        Message message = new Message();
        message.setThirdMessageId(larkMessage.getMessageId());
        message.setChatGroupId(larkMessage.getChatId());
        // TODO set sender info
        // message.setSender(new Operator(larkMessage.getSender().get));
        message.setSendTime(new Date(larkMessage.getCreateTime()));

        return message;
    }
}
