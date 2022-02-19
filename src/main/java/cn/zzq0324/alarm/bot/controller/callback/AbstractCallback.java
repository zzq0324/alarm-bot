package cn.zzq0324.alarm.bot.controller.callback;

import cn.zzq0324.alarm.bot.constant.CallbackType;
import cn.zzq0324.alarm.bot.entity.Message;
import cn.zzq0324.alarm.bot.extension.cmd.CommandExecutor;
import cn.zzq0324.alarm.bot.extension.platform.PlatformExt;
import cn.zzq0324.alarm.bot.spi.ExtensionLoader;
import cn.zzq0324.alarm.bot.vo.CallbackData;
import cn.zzq0324.alarm.bot.vo.CallbackRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * description: AbstractCallback <br>
 * date: 2022/2/18 10:07 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Slf4j
public abstract class AbstractCallback {

    @Autowired
    private CommandExecutor commandExecutor;

    @RequestMapping(value = "/callback")
    public Object callback(@RequestBody CallbackRequest request) {
        CallbackData callbackData = unmarshal(request.getEncrypt());

        // url校验
        if (callbackData.getCallbackType() == CallbackType.URL_VALIDATION) {
            return formatResponse(callbackData);
        }

        // 进行回调处理
        handleCallback(callbackData);

        return formatResponse(callbackData);
    }

    /**
     * 处理回调信息
     */
    public void handleCallback(CallbackData callbackData) {
        // 判断是否为监听的事件，如果不是监听的事件，不做任何处理
        if (!isAtRobotEvent(callbackData.getEventType())) {
            log.info("eventType: {}  not at robot will ignored.", callbackData.getEventType());

            return;
        }

        // 解析消息，机器人只支持文本消息，暂时不考虑富文本，因此只解析出一条记录
        List<Message> messageList = ExtensionLoader.getDefaultExtension(PlatformExt.class).parseMessage(callbackData);

        // 执行对应的指令
        commandExecutor.execute(messageList.get(0));
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
     * 拼接url校验的响应信息
     */
    public abstract Object formatResponse(CallbackData callbackData);
}
