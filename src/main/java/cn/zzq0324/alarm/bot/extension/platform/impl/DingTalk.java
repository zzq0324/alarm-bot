package cn.zzq0324.alarm.bot.extension.platform.impl;

import cn.zzq0324.alarm.bot.entity.Message;
import cn.zzq0324.alarm.bot.extension.platform.PlatformExt;
import cn.zzq0324.alarm.bot.spi.Extension;
import cn.zzq0324.alarm.bot.vo.CallbackData;

import java.util.List;

/**
 * description: DingTalk <br>
 * date: 2022/2/18 9:57 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Extension(name = "dingtalk", summary = "钉钉")
public class DingTalk implements PlatformExt {
    @Override
    public void reply(String messageId, String title, String text) {

    }

    @Override
    public void send(String receiveId, String messageType, String content) {

    }

    @Override
    public List<Message> parseMessage(CallbackData callbackData) {
        return null;
    }

    @Override
    public String createChatGroup(String name, String description) {
        return null;
    }

    @Override
    public void addMemberToChatGroup(String chatGroupId, List<String> memberIdList) {

    }

    @Override
    public void destroyChatGroup(String chatGroupId) {

    }

    @Override
    public void help(Message message) {

    }
}
