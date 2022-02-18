package cn.zzq0324.alarm.bot.controller.callback;

import cn.zzq0324.alarm.bot.constant.CallbackType;
import cn.zzq0324.alarm.bot.entity.Message;
import cn.zzq0324.alarm.bot.vo.CallbackData;
import cn.zzq0324.alarm.bot.vo.CallbackRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * description: AbstractCallback <br>
 * date: 2022/2/18 10:07 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
public abstract class AbstractCallback {

    @RequestMapping(value = "/callback")
    public Object callback(@RequestBody CallbackRequest request) {
        CallbackData callbackData = unmarshal(request.getEncrypt());

        // url校验
        if (callbackData.getCallbackType() == CallbackType.URL_VALIDATION) {
            return formatUrlValidationResponse(callbackData);
        }

        // 进行逻辑处理
        return handleCallback(callbackData);
    }

    /**
     * 处理回调信息
     */
    public Object handleCallback(CallbackData callbackData) {
        // 解析消息，机器人只支持文本消息
        Message message = parseMessage(callbackData);
        return null;
    }

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
    public abstract Object formatUrlValidationResponse(CallbackData callbackData);
}
