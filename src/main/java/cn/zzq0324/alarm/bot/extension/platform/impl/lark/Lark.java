package cn.zzq0324.alarm.bot.extension.platform.impl.lark;

import cn.zzq0324.alarm.bot.config.AlarmBotProperties;
import cn.zzq0324.alarm.bot.constant.LarkConstants;
import cn.zzq0324.alarm.bot.constant.Platform;
import cn.zzq0324.alarm.bot.entity.Event;
import cn.zzq0324.alarm.bot.entity.MemberPlatformInfo;
import cn.zzq0324.alarm.bot.entity.Message;
import cn.zzq0324.alarm.bot.entity.Project;
import cn.zzq0324.alarm.bot.extension.platform.PlatformExt;
import cn.zzq0324.alarm.bot.spi.Extension;
import cn.zzq0324.alarm.bot.util.FileUtils;
import cn.zzq0324.alarm.bot.vo.CallbackData;
import com.alibaba.fastjson.JSONObject;
import com.larksuite.oapi.core.Config;
import com.larksuite.oapi.core.api.AccessTokenType;
import com.larksuite.oapi.core.api.Api;
import com.larksuite.oapi.core.api.ReqCaller;
import com.larksuite.oapi.core.api.request.Request;
import com.larksuite.oapi.core.api.request.RequestOptFn;
import com.larksuite.oapi.core.api.response.Response;
import com.larksuite.oapi.service.contact.v3.ContactService;
import com.larksuite.oapi.service.contact.v3.model.UserGetResult;
import com.larksuite.oapi.service.im.v1.ImService;
import com.larksuite.oapi.service.im.v1.model.ChatCreateReqBody;
import com.larksuite.oapi.service.im.v1.model.ChatCreateResult;
import com.larksuite.oapi.service.im.v1.model.ChatMembersCreateReqBody;
import com.larksuite.oapi.service.im.v1.model.MessageCreateReqBody;
import com.larksuite.oapi.service.im.v1.model.MessageReplyReqBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * description: Lark <br>
 * date: 2022/2/18 9:57 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Extension(name = Platform.LARK, isDefault = true, summary = "飞书")
@Slf4j
public class Lark implements PlatformExt {

    private static final RequestOptFn TIMEOUT_OPT = Request.setTimeout(1, TimeUnit.MINUTES);

    @Autowired
    private Config config;

    @Autowired
    private AlarmBotProperties alarmBotProperties;

    @Autowired
    private LarkMessageParser larkMessageParser;

    @Override
    public void replyText(String messageId, String text) {
        replyText(messageId, null, text);
    }

    @Override
    public void pushEvent(Event event, Project project) {
        JSONObject data = (JSONObject)JSONObject.toJSON(event);
        data.put("projectName", project.getName());
        String cardContent = FileUtils.getFileContent("/lark/event-info.json", data, true);

        send(event.getChatGroupId(), LarkConstants.MESSAGE_TYPE_INTERACTIVE, cardContent);
    }

    @Override
    public List<Message> parseCallbackMessage(CallbackData callbackData) {
        FuzzyLarkMessage message = new FuzzyLarkMessage(callbackData);

        return larkMessageParser.parse(message);
    }

    @Override
    public String createChatGroup(String name, String description) {
        ImService imService = new ImService(config);
        ChatCreateReqBody reqBody = new ChatCreateReqBody();
        reqBody.setName(name);
        reqBody.setDescription(description);

        ChatCreateResult result = executeCaller(imService.getChats().create(reqBody));

        log.info("create lark chat group, name: {}, chatGroupId: {}", name, result.getChatId());

        return result.getChatId();
    }

    @Override
    public void addMemberToChatGroup(String chatGroupId, List<String> memberIdList) {
        ImService imService = new ImService(config);
        ChatMembersCreateReqBody reqBody = new ChatMembersCreateReqBody();
        reqBody.setIdList(memberIdList.toArray(new String[memberIdList.size()]));

        executeCaller(imService.getChatMemberss().create(reqBody).setChatId(chatGroupId));
    }

    @Override
    public void destroyChatGroup(String chatGroupId) {
        ImService imService = new ImService(config);

        executeCaller(imService.getChats().delete().setChatId(chatGroupId));
    }

    @Override
    public void help(Message message) {
        // 获取帮助模板
        String content = FileUtils.getFileContent("/lark/help.json");

        // 响应式卡片
        send(message.getChatGroupId(), LarkConstants.MESSAGE_TYPE_INTERACTIVE, content);
    }

    @Override
    public MemberPlatformInfo getMemberInfo(String mobile) {
        String openId = getOpenIdByMobile(mobile);
        if (StringUtils.isEmpty(openId)) {
            return null;
        }

        // 查询人员信息
        ContactService contactService = new ContactService(config);
        ContactService.UserGetReqCall caller = contactService.getUsers().get().setUserId(openId);

        UserGetResult userGetResult = executeCaller(caller);

        MemberPlatformInfo memberPlatformInfo = new MemberPlatformInfo();
        memberPlatformInfo.setOpenId(openId);
        memberPlatformInfo.setUnionId(userGetResult.getUser().getUnionId());
        memberPlatformInfo.setName(userGetResult.getUser().getName());

        return memberPlatformInfo;
    }

    /**
     * 发送消息
     */
    public void send(String receiveId, String messageType, String content) {
        ImService imService = new ImService(config);
        MessageCreateReqBody reqBody = new MessageCreateReqBody();
        reqBody.setMsgType(messageType);
        reqBody.setContent(content);
        reqBody.setReceiveId(receiveId);

        // receiveIdType为chat_id代表群组id
        executeCaller(imService.getMessages().create(reqBody).setReceiveIdType("chat_id"));
    }

    /**
     * 回复消息
     */
    public void reply(String messageId, String messageType, String content) {
        ImService imService = new ImService(config);
        MessageReplyReqBody reqBody = new MessageReplyReqBody();
        reqBody.setMsgType(messageType);
        reqBody.setContent(content);

        executeCaller(imService.getMessages().reply(reqBody).setMessageId(messageId));
    }

    /**
     * 发送文本
     */
    public void sendText(String receiveId, String title, String text) {
        send(receiveId, LarkConstants.MESSAGE_TYPE_TEXT, buildTextContent(title, text));
    }

    /**
     * 回复文本
     */
    public void replyText(String messageId, String title, String text) {
        reply(messageId, LarkConstants.MESSAGE_TYPE_TEXT, buildTextContent(title, text));
    }

    /**
     * 构建文本内容
     */
    private String buildTextContent(String title, String text) {
        JSONObject content = new JSONObject();
        content.put("text", text);
        if (StringUtils.hasLength(title)) {
            content.put("title", title);
        }

        return content.toJSONString();
    }

    public String getOpenIdByMobile(String mobile) {
        Map<String, Object> params = new HashMap<>();
        params.put("mobiles", mobile);

        Request<Map, JSONObject> request =
            Request.newRequest("user/v1/batch_get_id", "POST", AccessTokenType.Tenant, null, new JSONObject(),
                Request.setQueryParams(params));

        JSONObject response = invoke(request);
        JSONObject mobileUsers = response.getJSONObject("mobile_users");

        if (mobileUsers.containsKey(mobile)) {
            return mobileUsers.getJSONArray(mobile).getJSONObject(0).getString("open_id");
        }

        return null;
    }

    private <T> T executeCaller(ReqCaller<?, T> caller) {
        try {
            Response<T> response = caller.execute();
            checkHttpStatus(response);

            return response.getData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <I, O> O invoke(Request<I, O> request) {
        try {
            request.getRequestOptFns().add(TIMEOUT_OPT);
            Response<O> response = Api.send(config, request);

            checkHttpStatus(response);

            return response.getData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void checkHttpStatus(Response response) {
        if (response.getHTTPStatusCode() != HttpStatus.OK.value()) {
            log.error("invoke feishu error, response: {}", JSONObject.toJSONString(response));

            throw new RuntimeException("invoke feishu error");
        }
    }
}
