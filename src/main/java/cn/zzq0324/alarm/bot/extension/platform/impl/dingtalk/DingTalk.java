package cn.zzq0324.alarm.bot.extension.platform.impl.dingtalk;

import cn.zzq0324.alarm.bot.constant.PlatformType;
import cn.zzq0324.alarm.bot.entity.Event;
import cn.zzq0324.alarm.bot.entity.MemberPlatformInfo;
import cn.zzq0324.alarm.bot.entity.Message;
import cn.zzq0324.alarm.bot.entity.Project;
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
@Extension(name = PlatformType.DING_TALK, summary = "钉钉")
public class DingTalk implements PlatformExt {

    @Override
    public void replyText(String messageId, String text) {

    }

    @Override
    public void pushEvent(Event event, Project project) {

    }

    @Override
    public List<Message> parseCallbackMessage(CallbackData callbackData) {
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

    @Override
    public MemberPlatformInfo getMemberInfo(String mobile) {
        return null;
    }
}
