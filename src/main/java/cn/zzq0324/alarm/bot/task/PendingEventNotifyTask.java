package cn.zzq0324.alarm.bot.task;

import cn.zzq0324.alarm.bot.entity.Event;
import cn.zzq0324.alarm.bot.extension.platform.PlatformExt;
import cn.zzq0324.alarm.bot.service.EventService;
import cn.zzq0324.alarm.bot.spi.ExtensionLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

    @Scheduled(cron = "${alarm-bot.pending-event-notify-cron}")
    public void execute() {
        List<Event> eventList = eventService.getPendingEventList();

        eventList.stream().forEach(event -> {
            try {
                ExtensionLoader.getDefaultExtension(PlatformExt.class).pendingTaskNotify(event);
            } catch (Exception e) {
                log.error("send notify to pending task error", e);
            }
        });
    }
}
