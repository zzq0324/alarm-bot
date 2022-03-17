package cn.zzq0324.alarm.bot.core.extension.platform;

import cn.zzq0324.alarm.bot.core.entity.Event;
import cn.zzq0324.alarm.bot.core.entity.Member;
import cn.zzq0324.alarm.bot.core.entity.Message;
import cn.zzq0324.alarm.bot.core.entity.Project;
import cn.zzq0324.alarm.bot.core.spi.SPI;
import cn.zzq0324.alarm.bot.core.vo.CallbackData;
import cn.zzq0324.alarm.bot.core.vo.IMMessage;
import cn.zzq0324.alarm.bot.core.vo.MemberThirdAuthInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * description: 告警推送的平台，例如飞书、钉钉、企业微信等 <br>
 * date: 2022/2/10 10:24 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@SPI("platform")
public interface PlatformExt {

    /**
     * 回复告警信息
     */
    void replyText(String messageId, String text);

    /**
     * 发送信息到群聊
     */
    void pushEvent(Event event, Project project);

    /**
     * 未处理完的任务提醒
     */
    void pendingTaskNotify(Event event);

    /**
     * 解析消息
     */
    IMMessage parseIMMessage(CallbackData callbackData);

    /**
     * 创建群聊，返回群聊ID
     */
    String createChatGroup(String name, String description);

    /**
     * 添加成员到群聊
     *
     * @param chatGroupId  群聊ID
     * @param memberIdList 成员id列表，指openID列表
     */
    void addMemberToChatGroup(String chatGroupId, List<String> memberIdList);

    /**
     * 解散群聊
     *
     * @param chatGroupId 群聊ID
     */
    void destroyChatGroup(String chatGroupId);

    /**
     * 帮助信息
     */
    void help(Message message);

    /**
     * 根据手机号查询成员三方信息
     */
    MemberThirdAuthInfo getMemberInfo(String identity);

    /**
     * 成员离职通知
     */
    void memberLeaveNotify(Map<Member, Set<Project>> memberProjectMap);
}
