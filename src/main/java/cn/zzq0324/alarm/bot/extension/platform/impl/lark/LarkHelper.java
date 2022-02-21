package cn.zzq0324.alarm.bot.extension.platform.impl.lark;

import cn.zzq0324.alarm.bot.constant.LarkConstants;
import com.alibaba.fastjson.JSONObject;
import com.larksuite.oapi.core.Config;
import com.larksuite.oapi.core.api.AccessTokenType;
import com.larksuite.oapi.core.api.Api;
import com.larksuite.oapi.core.api.ReqCaller;
import com.larksuite.oapi.core.api.request.Request;
import com.larksuite.oapi.core.api.request.RequestOptFn;
import com.larksuite.oapi.core.api.response.Response;
import com.larksuite.oapi.service.contact.v3.ContactService;
import com.larksuite.oapi.service.contact.v3.model.User;
import com.larksuite.oapi.service.im.v1.ImService;
import com.larksuite.oapi.service.im.v1.model.ChatCreateReqBody;
import com.larksuite.oapi.service.im.v1.model.ChatCreateResult;
import com.larksuite.oapi.service.im.v1.model.ChatMembersCreateReqBody;
import com.larksuite.oapi.service.im.v1.model.MessageCreateReqBody;
import com.larksuite.oapi.service.im.v1.model.MessageReplyReqBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * description: LarkHelper <br>
 * date: 2022/2/21 1:31 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Component
@Slf4j
public class LarkHelper {

    private static final RequestOptFn TIMEOUT_OPT = Request.setTimeout(2, TimeUnit.MINUTES);

    @Autowired
    private Config config;

    /**
     * 获取飞书用户信息
     */
    public User getLarkUser(String userId, String userIdType) {
        ContactService contactService = new ContactService(config);
        ContactService.UserGetReqCall caller =
            contactService.getUsers().get(TIMEOUT_OPT).setUserId(userId).setUserIdType(userIdType);

        return executeCaller(caller).getUser();
    }

    /**
     * 添加成员到群组
     */
    public void addMemberToChatGroup(String chatGroupId, List<String> memberIdList) {
        ImService imService = new ImService(config);
        ChatMembersCreateReqBody reqBody = new ChatMembersCreateReqBody();
        reqBody.setIdList(memberIdList.toArray(new String[memberIdList.size()]));

        executeCaller(imService.getChatMemberss().create(reqBody, TIMEOUT_OPT).setChatId(chatGroupId));
    }

    /**
     * 创建群聊
     */
    public String createChatGroup(String name, String description) {
        ImService imService = new ImService(config);
        ChatCreateReqBody reqBody = new ChatCreateReqBody();
        reqBody.setName(name);
        reqBody.setDescription(description);

        ChatCreateResult result = executeCaller(imService.getChats().create(reqBody, TIMEOUT_OPT));

        log.info("create lark chat group, name: {}, chatGroupId: {}", name, result.getChatId());

        return result.getChatId();
    }

    /**
     * 解散群聊
     */
    public void destroyChatGroup(String chatGroupId) {
        ImService imService = new ImService(config);

        executeCaller(imService.getChats().delete(TIMEOUT_OPT).setChatId(chatGroupId));
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
        executeCaller(imService.getMessages().create(reqBody, TIMEOUT_OPT).setReceiveIdType("chat_id"));
    }

    /**
     * 回复消息
     */
    public void reply(String messageId, String messageType, String content) {
        ImService imService = new ImService(config);
        MessageReplyReqBody reqBody = new MessageReplyReqBody();
        reqBody.setMsgType(messageType);
        reqBody.setContent(content);

        executeCaller(imService.getMessages().reply(reqBody, TIMEOUT_OPT).setMessageId(messageId));
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

    public FileOutputStream downloadResource(String messageId, String fileKey) throws IOException {
        FileOutputStream outputStream = new FileOutputStream("/Users/zhengzhq/Downloads/aaa.png");
        ImService imService = new ImService(config);

        ImService.MessageResourceGetReqCall caller =
            imService.getMessageResources().get(TIMEOUT_OPT).setMessageId(messageId).setFileKey(fileKey)
                .setResponseStream(outputStream).setType("image");

        executeCaller(caller);

        outputStream.flush();

        return outputStream;
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
