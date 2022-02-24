package cn.zzq0324.alarm.bot.controller.callback;

import cn.zzq0324.alarm.bot.constant.CallbackType;
import cn.zzq0324.alarm.bot.constant.LarkConstants;
import cn.zzq0324.alarm.bot.constant.LarkEvent;
import cn.zzq0324.alarm.bot.vo.CallbackData;
import com.alibaba.fastjson.JSONObject;
import com.larksuite.oapi.core.Config;
import com.larksuite.oapi.core.Decrypt;
import com.larksuite.oapi.core.card.mode.Card;
import com.larksuite.oapi.core.card.mode.Challenge;
import com.larksuite.oapi.core.event.model.Fuzzy;
import com.larksuite.oapi.core.utils.Jsons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description: 回调处理 <br>
 * date: 2022/2/18 9:58 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@RestController
@RequestMapping("/lark")
@Slf4j
public class LarkCallbackController extends AbstractCallback {

    @Autowired
    private Config config;

    /**
     * 消息卡片互动
     */
    @RequestMapping(value = "/interactiveCallback")
    public Object interactiveCallback(@RequestBody JSONObject requestBody) {
        // URL校验，判断token
        if (requestBody.containsKey("challenge")) {
            Challenge challenge = Jsons.DEFAULT_GSON.fromJson(requestBody.toJSONString(), Challenge.class);
            if (!config.getAppSettings().getVerificationToken().equals(challenge.getToken())) {
                throw new RuntimeException("illegal token");
            }

            return formatChallengeJson(challenge.getChallenge());
        }

        // 卡片交互处理
        log.info("receive card operation callback: {}", requestBody.toJSONString());
        Card card = Jsons.DEFAULT_GSON.fromJson(requestBody.toJSONString(), Card.class);

        return null;
    }

    @Override
    public boolean isImMessageEvent(String eventType) {
        return LarkEvent.RECEIVE_MESSAGE.getEventType().equals(eventType);
    }

    @Override
    public boolean isAttentionEvent(String eventType) {
        return false;
    }

    @Override
    public CallbackData unmarshal(String data) {
        // 如果配置了encrypt key，先解密
        if (!StringUtils.isEmpty(config.getAppSettings().getEncryptKey())) {
            data = (new Decrypt(config.getAppSettings().getEncryptKey())).decrypt(data);
        }

        data = data.trim();
        log.info("receive lark callback data: {}", data);

        Fuzzy fuzzy = Jsons.DEFAULT_GSON.fromJson(data, Fuzzy.class);

        String token = fuzzy.getToken();
        String eventType = null;

        // V1.0回调版本的获取方式
        if (fuzzy.getEvent() != null) {
            eventType = fuzzy.getEvent().getType();
        }

        // V2.0回调版本的获取方式
        if (fuzzy.getHeader() != null) {
            token = fuzzy.getHeader().getToken();
            eventType = fuzzy.getHeader().getEventType();
        }

        // 校验token是否符合
        if (!token.equals(config.getAppSettings().getVerificationToken())) {
            throw new IllegalArgumentException("token invalid!");
        }

        JSONObject callbackJson = JSONObject.parseObject(data);

        CallbackData callbackData = new CallbackData();
        callbackData.setData(callbackJson.getJSONObject("event"));
        callbackData.setNonce(fuzzy.getChallenge());
        callbackData.setEventType(eventType);

        if (StringUtils.hasLength(fuzzy.getType()) && fuzzy.getType()
            .equals(LarkConstants.CALLBACK_TYPE_URL_VERIFICATION)) {
            callbackData.setCallbackType(CallbackType.URL_VALIDATION);
        }

        return callbackData;
    }

    @Override
    public Object formatResponse(CallbackData callbackData) {
        if (callbackData.getCallbackType() == CallbackType.URL_VALIDATION) {
            return formatChallengeJson(callbackData.getNonce());
        }

        // 飞书官方只认HTTP Status，只要等于200就认为回调成功
        return null;
    }

    private JSONObject formatChallengeJson(String challenge) {
        JSONObject json = new JSONObject();
        json.put("challenge", challenge);

        return json;
    }
}
