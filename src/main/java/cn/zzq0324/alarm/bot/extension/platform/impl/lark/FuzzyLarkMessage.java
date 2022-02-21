package cn.zzq0324.alarm.bot.extension.platform.impl.lark;

import cn.zzq0324.alarm.bot.vo.CallbackData;
import com.larksuite.oapi.core.utils.Jsons;
import com.larksuite.oapi.service.im.v1.model.EventMessage;
import com.larksuite.oapi.service.im.v1.model.Mention;
import com.larksuite.oapi.service.im.v1.model.MentionEvent;
import com.larksuite.oapi.service.im.v1.model.Message;
import com.larksuite.oapi.service.im.v1.model.MessageBody;
import com.larksuite.oapi.service.im.v1.model.MessageReceiveEventData;
import com.larksuite.oapi.service.im.v1.model.Sender;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 混淆的飞书消息 <br>
 * date: 2022/2/21 10:33 上午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
public class FuzzyLarkMessage extends Message {

    /**
     * 将回调的事件数据转为跟查询聊天消息列表一样的格式
     */
    public FuzzyLarkMessage(CallbackData callbackData) {
        // 回调数据分sender和message
        MessageReceiveEventData eventData =
            Jsons.DEFAULT_GSON.fromJson(callbackData.getData().toJSONString(), MessageReceiveEventData.class);

        // 设置Sender
        Sender sender = new Sender();
        sender.setId(eventData.getSender().getSenderId().getOpenId());
        sender.setIdType("open_id");
        sender.setSenderType(eventData.getSender().getSenderType());
        sender.setTenantKey(eventData.getSender().getTenantKey());
        this.setSender(sender);

        // 设置消息属性
        EventMessage eventMessage = eventData.getMessage();
        BeanUtils.copyProperties(eventMessage, this);
        // 设置消息类型
        this.setMsgType(eventMessage.getMessageType());

        //设置消息内容
        MessageBody messageBody = new MessageBody();
        messageBody.setContent(eventMessage.getContent());
        this.setBody(messageBody);

        // 设置@信息
        setMentionWithEventData(eventMessage);
    }

    public FuzzyLarkMessage(Message message) {
        BeanUtils.copyProperties(message, this);
    }

    private void setMentionWithEventData(EventMessage eventMessage) {
        if (eventMessage.getMentions() == null) {
            return;
        }

        List<Mention> mentionList = new ArrayList<>();
        for (MentionEvent mentionEvent : eventMessage.getMentions()) {
            Mention mention = new Mention();
            BeanUtils.copyProperties(mentionEvent, mention);

            mentionList.add(mention);
        }

        this.setMentions(mentionList.toArray(new Mention[mentionList.size()]));
    }
}
