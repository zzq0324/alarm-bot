package cn.zzq0324.alarm.bot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * description: AlarmBotProperties <br>
 * date: 2022/2/8 1:23 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Configuration
@ConfigurationProperties(prefix = "alarm-bot")
@Data
public class AlarmBotProperties {

    /**
     * 机器人名称
     */
    private String botName;

    /**
     * 创建命令demo
     */
    private String createAlarmDemo;

    /**
     * 服务ID对应的正则表达式，从正则表达式中解析出serviceId
     */
    private String projectNameRegExp;

    /**
     * 告警回复消息
     */
    private String replyAlarm;

    /**
     * 回复已解决到告警群聊
     */
    private String replySolvedToChatGroup;

    /**
     * 回复告警已解决
     */
    private String replyEventSolved;

    /**
     * 非告警群中发起/solve命令时提示
     */
    private String notInAlarmGroup;

    /**
     * 告警小结缺失
     */
    private String solveSummaryMissing;

    /**
     * N分钟后解散群聊
     */
    private int destroyGroupAfterMinutes;

    /**
     * URL前缀
     */
    private String urlPrefix;

    /**
     * 资源文件下载目录，本地下载使用
     */
    private String resourceDownloadFolder;

    /**
     * 项目成员缺失
     */
    private String projectMemberMissing;

    /**
     * 未完成的事件通知定时任务
     */
    private String pendingEventNotifyCron;

    private String alarmGroupReplyPendingTask;
}
