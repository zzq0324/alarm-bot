package cn.zzq0324.alarm.bot.core.util;

/**
 * description: ChatGroupUtils <br>
 * date: 2022/3/1 9:18 上午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
public class ChatGroupUtils {
    public static String CHAT_GROUP_NAME_SUFFIX = "️告警专项处理群";

    public static final String CHAT_GROUP_NAME_FMT = "️[%s]" + CHAT_GROUP_NAME_SUFFIX;

    public static String getChatGroupName(String projectName) {
        return String.format(CHAT_GROUP_NAME_FMT, projectName);
    }

    /**
     * 判断是否告警群，简单判断
     */
    public static boolean isAlertChatGroupName(String chatGroupName) {
        return chatGroupName.endsWith(CHAT_GROUP_NAME_SUFFIX);
    }
}
