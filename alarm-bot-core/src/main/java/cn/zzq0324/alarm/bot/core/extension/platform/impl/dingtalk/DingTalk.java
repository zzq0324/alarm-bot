package cn.zzq0324.alarm.bot.core.extension.platform.impl.dingtalk;

import cn.zzq0324.alarm.bot.core.constant.PlatformType;
import cn.zzq0324.alarm.bot.core.entity.Event;
import cn.zzq0324.alarm.bot.core.entity.Member;
import cn.zzq0324.alarm.bot.core.entity.Message;
import cn.zzq0324.alarm.bot.core.entity.Project;
import cn.zzq0324.alarm.bot.core.extension.platform.PlatformExt;
import cn.zzq0324.alarm.bot.core.spi.Extension;
import cn.zzq0324.alarm.bot.core.vo.CallbackData;
import cn.zzq0324.alarm.bot.core.vo.IMMessage;
import cn.zzq0324.alarm.bot.core.vo.MemberThirdAuthInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public void pendingTaskNotify(Event event) {

    }

    @Override
    public IMMessage parseIMMessage(CallbackData callbackData) {
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
    public MemberThirdAuthInfo getMemberInfo(String mobile) {
        return null;
    }

    @Override
    public void memberLeaveNotify(Map<Member, Set<Project>> memberProjectMap) {

    }
}
