package cn.zzq0324.alarm.bot.core.extension.platform.impl.lark.parser.impl;

import cn.zzq0324.alarm.bot.core.config.AlarmBotProperties;
import cn.zzq0324.alarm.bot.core.constant.LarkConstants;
import cn.zzq0324.alarm.bot.core.constant.MessageType;
import cn.zzq0324.alarm.bot.core.entity.Message;
import cn.zzq0324.alarm.bot.core.extension.cmd.Command;
import cn.zzq0324.alarm.bot.core.extension.cmd.context.CommandContext;
import cn.zzq0324.alarm.bot.core.extension.platform.impl.lark.parser.BaseLarkMessageParser;
import cn.zzq0324.alarm.bot.core.extension.platform.impl.lark.parser.LarkMessageParserExt;
import cn.zzq0324.alarm.bot.core.spi.Extension;
import cn.zzq0324.alarm.bot.core.spi.ExtensionLoader;
import cn.zzq0324.alarm.bot.core.vo.IMMessage;
import com.alibaba.fastjson.JSONObject;
import com.larksuite.oapi.service.im.v1.model.EventMessage;
import com.larksuite.oapi.service.im.v1.model.MentionEvent;
import com.larksuite.oapi.service.im.v1.model.MessageReceiveEventData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * description: 飞书IM-文本消息解析 <br>
 * date: 2022/2/22 8:55 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Extension(name = LarkConstants.MESSAGE_TYPE_TEXT, summary = "飞书文本消息解析")
public class LarkTextMessageParser extends BaseLarkMessageParser implements LarkMessageParserExt {

    @Autowired
    private AlarmBotProperties alarmBotProperties;

    @Override
    public void parse(IMMessage imMessage, MessageReceiveEventData eventData) {
        EventMessage eventMessage = eventData.getMessage();
        Map<String, MentionEvent> mentionMap = getMentionMap(eventMessage);

        JSONObject content = JSONObject.parseObject(eventMessage.getContent());

        Message message = newMessage(eventData);
        message.setMessageType(MessageType.TEXT);
        message.setContent(formatAtInfo(content.getString("text"), mentionMap));

        // 是否@机器人
        imMessage.setAtRobot(isAtRobot(mentionMap));
        // 设置消息列表
        imMessage.setMessageList(Arrays.asList(message));
        // 只有文本消息才需要解析命令
        imMessage.setCommandContext(getCommandContext(imMessage));
    }

    private CommandContext getCommandContext(IMMessage imMessage) {
        List<Command> commandList = ExtensionLoader.getExtensionLoader(Command.class).getExtensionList();

        for (Command command : commandList) {
            CommandContext commandContext = command.matchCommand(imMessage);

            if (commandContext != null) {
                return commandContext;
            }
        }

        return null;
    }
}
