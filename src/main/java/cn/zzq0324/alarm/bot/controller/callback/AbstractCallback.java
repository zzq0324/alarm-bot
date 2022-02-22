package cn.zzq0324.alarm.bot.controller.callback;

import cn.zzq0324.alarm.bot.constant.CallbackType;
import cn.zzq0324.alarm.bot.constant.CommandConstants;
import cn.zzq0324.alarm.bot.entity.Event;
import cn.zzq0324.alarm.bot.entity.Message;
import cn.zzq0324.alarm.bot.extension.cmd.Command;
import cn.zzq0324.alarm.bot.extension.cmd.CommandExecutor;
import cn.zzq0324.alarm.bot.extension.cmd.context.CommandContext;
import cn.zzq0324.alarm.bot.extension.cmd.context.HelpContext;
import cn.zzq0324.alarm.bot.extension.platform.PlatformExt;
import cn.zzq0324.alarm.bot.service.EventService;
import cn.zzq0324.alarm.bot.service.MessageService;
import cn.zzq0324.alarm.bot.spi.ExtensionLoader;
import cn.zzq0324.alarm.bot.vo.CallbackData;
import cn.zzq0324.alarm.bot.vo.CallbackRequest;
import cn.zzq0324.alarm.bot.vo.IMMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
    @Autowired
    private MessageService messageService;
    @Autowired
    private EventService eventService;

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
    @Transactional
    public void handleCallback(CallbackData callbackData) {
        String eventType = callbackData.getEventType();

        // 不是否关注的事件并且不是IM消息回调，不予处理
        if (!isAttentionEvent(eventType) && !isImMessageEvent(eventType)) {
            return;
        }

        if (isAttentionEvent(eventType)) {
            // TODO 记录event_log

            return;
        }

        // 以下开始为IM消息解析消息
        IMMessage imMessage = ExtensionLoader.getDefaultExtension(PlatformExt.class).parseIMMessage(callbackData);

        // 解析完消息为空，不处理，例如发送的是卡片消息
        if (CollectionUtils.isEmpty(imMessage.getMessageList())) {
            return;
        }

        CommandContext commandContext = imMessage.getCommandContext();
        Message firstMessage = imMessage.getMessageList().get(0);

        // 如果是@机器人但是又没有命中命令，仍旧推送帮助信息
        if (commandContext == null && imMessage.isAtRobot()) {
            commandContext = HelpContext.builder().command(CommandConstants.HELP).message(firstMessage).build();
        }

        // 执行对应的指令
        if (commandContext != null) {
            ExtensionLoader.getExtension(Command.class, commandContext.getCommand()).execute(commandContext);
        }

        Event event = eventService.getByChatGroupId(firstMessage.getChatGroupId());
        // 找得到记录，插入聊天记录
        if (event != null) {
            imMessage.getMessageList().stream().forEach(message -> messageService.add(message));
        }
    }

    /**
     * 判断当前消息是否@机器人事件
     */
    public abstract boolean isImMessageEvent(String eventType);

    /**
     * 判断当前事件是否关注的事件，例如用户入群、解散群聊等
     */
    public abstract boolean isAttentionEvent(String eventType);

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
