package cn.zzq0324.alarm.bot.core.task;

import cn.zzq0324.alarm.bot.core.config.AlarmBotProperties;
import cn.zzq0324.alarm.bot.core.entity.Event;
import cn.zzq0324.alarm.bot.core.extension.platform.PlatformExt;
import cn.zzq0324.alarm.bot.core.service.EventService;
import cn.zzq0324.alarm.bot.core.spi.ExtensionLoader;
import cn.zzq0324.alarm.bot.core.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * description: 挂起的任务提醒 <br>
 * date: 2022/2/22 1:23 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Component
@Slf4j
public class PendingEventNotifyTask {

    @Autowired
    private EventService eventService;

    @Autowired
    private AlarmBotProperties alarmBotProperties;

    @Scheduled(cron = "${alarm-bot.pending-event-notify-cron}")
    public void execute() {
        List<Event> eventList = eventService.getPendingEventList();

        eventList.stream().forEach(event -> {
            try {
                // 告警处理群提示告警未处理
                ExtensionLoader.getDefaultExtension(PlatformExt.class).pendingTaskNotify(event);

                // 总群回复
                String durationText = DateUtils.getDiffText(event.getCreateTime(), new Date());
                String text = String.format(alarmBotProperties.getAlarmGroupReplyPendingTask(), durationText);
                ExtensionLoader.getDefaultExtension(PlatformExt.class).replyText(event.getThirdMessageId(), text);
            } catch (Exception e) {
                log.error("send notify to pending task error", e);
            }
        });
    }
}
