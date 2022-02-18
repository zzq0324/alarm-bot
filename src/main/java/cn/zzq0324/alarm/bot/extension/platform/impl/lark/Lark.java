package cn.zzq0324.alarm.bot.extension.platform.impl.lark;

import cn.zzq0324.alarm.bot.entity.Message;
import cn.zzq0324.alarm.bot.extension.platform.PlatformExt;
import cn.zzq0324.alarm.bot.spi.Extension;

import java.util.List;

/**
 * description: Lark <br>
 * date: 2022/2/18 9:57 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Extension(name = "lark",isDefault = true,summary = "飞书")
public class Lark implements PlatformExt {
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
