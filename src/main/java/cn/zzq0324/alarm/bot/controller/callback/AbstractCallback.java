package cn.zzq0324.alarm.bot.controller.callback;

import cn.zzq0324.alarm.bot.config.AlarmBotProperties;
import cn.zzq0324.alarm.bot.constant.CallbackType;
import cn.zzq0324.alarm.bot.entity.Message;
import cn.zzq0324.alarm.bot.extension.platform.PlatformExt;
import cn.zzq0324.alarm.bot.spi.ExtensionLoader;
import cn.zzq0324.alarm.bot.vo.CallbackData;
import cn.zzq0324.alarm.bot.vo.CallbackRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description: AbstractCallback <br>
 * date: 2022/2/18 10:07 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
public abstract class AbstractCallback {

    @Autowired
    protected AlarmBotProperties alarmBotProperties;
    private Pattern pattern = null;

    @RequestMapping(value = "/callback")
    public Object callback(@RequestBody CallbackRequest request) {
        CallbackData callbackData = unmarshal(request.getEncrypt());

        // url校验
        if (callbackData.getCallbackType() == CallbackType.URL_VALIDATION) {
            return formatResponse(callbackData);
        }

        // 进行逻辑处理
        return handleCallback(callbackData);
    }

    /**
     * 处理回调信息
     */
    public Object handleCallback(CallbackData callbackData) {
        // 判断是否为监听的事件，如果不是监听的事件，不做任何处理
        if (!isAtRobotEvent(callbackData.getEventType())) {
            return formatResponse(callbackData);
        }

        // 解析消息，机器人只支持文本消息，暂时不考虑富文本
        Message message = parseMessage(callbackData);

        // 根据正则表达式查找serviceId
        String serviceId = extractServiceId(message.getContent());
        if (StringUtils.isEmpty(serviceId)) {
            serviceIdNotFoundTip(message.getThirdMessageId());

            return formatResponse(callbackData);
        }

        // 找到直接创建群聊，拉人并发送消息
        ExtensionLoader.getDefaultExtension(PlatformExt.class).createChatGroup("", "");

        // 查询对应的人

        // 插入数据库记录

        return formatResponse(callbackData);
    }

    /**
     * 未找到服务ID，给出对应的提示
     */
    private void serviceIdNotFoundTip(String thirdMessageId) {
        String text = "根据配置值[" + alarmBotProperties.getServiceIdRegExp() + "]无法解析出serviceId，请检查配置是否正确！";

        // 群聊发起，推送正则表达式错误的提示到群聊
        if (!StringUtils.isEmpty(thirdMessageId)) {
            ExtensionLoader.getDefaultExtension(PlatformExt.class).reply(thirdMessageId, "【温馨提醒】", text);
        } else {
            throw new IllegalArgumentException(text);
        }
    }

    /**
     * 从消息体中解析出serviceId
     */
    private String extractServiceId(String messageContent) {
        if (pattern == null) {
            pattern = Pattern.compile(alarmBotProperties.getServiceIdRegExp());
        }

        Matcher matcher = pattern.matcher(messageContent);
        if (matcher.find()) {
            return matcher.group(0);
        }

        return null;
    }

    /**
     * 判断当前消息是否@机器人事件
     */
    public abstract boolean isAtRobotEvent(String eventType);

    /**
     * 解码回调数据，解密 -> 校验token等
     *
     * @param data 加密数据
     */
    public abstract CallbackData unmarshal(String data);

    /**
     * 解析消息
     */
    public abstract Message parseMessage(CallbackData callbackData);

    /**
     * 拼接url校验的响应信息
     */
    public abstract Object formatResponse(CallbackData callbackData);
}
