package cn.zzq0324.alarm.bot.extension.platform;

import cn.zzq0324.alarm.bot.entity.Message;
import cn.zzq0324.alarm.bot.spi.SPI;
import cn.zzq0324.alarm.bot.vo.CallbackData;

import java.util.List;

/**
 * description: 告警推送的平台，例如飞书、钉钉、企业微信等 <br>
 * date: 2022/2/10 10:24 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@SPI("platform")
public interface PlatformExt {

    /**
     * 回复信息
     */
    void reply(String messageId, String title, String text);

    /**
     * 发送信息
     */
    void send(String receiveId, String title, String text);

    /**
     * 解析消息
     */
    Message parseMessage(CallbackData callbackData);

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
}