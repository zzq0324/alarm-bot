package cn.zzq0324.alarm.bot.core.task;

import cn.zzq0324.alarm.bot.core.constant.Status;
import cn.zzq0324.alarm.bot.core.constant.TaskType;
import cn.zzq0324.alarm.bot.core.spi.ExtensionLoader;
import cn.zzq0324.alarm.bot.core.entity.Event;
import cn.zzq0324.alarm.bot.core.entity.Task;
import cn.zzq0324.alarm.bot.core.extension.platform.PlatformExt;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * description: DestroyChatGroupTask <br>
 * date: 2022/2/22 11:15 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Slf4j
@Component
public class DestroyChatGroupTask extends AbstractTask {

    @Override
    public TaskType getTaskType() {
        return TaskType.DESTROY_CHAT_GROUP;
    }

    @Override
    public void run(Task task) {
        Event event = JSONObject.parseObject(task.getData(), Event.class);

        ExtensionLoader.getDefaultExtension(PlatformExt.class).destroyChatGroup(event.getChatGroupId());
        log.info("destroy chat group: {}, eventId: {}", event.getChatGroupId(), event.getId());

        task.setStatus(Status.FINISH);
        taskService.update(task);
    }

    @Scheduled(initialDelay = 60000, fixedRate = 60000)
    @Override
    public void execute() {
        super.execute();
    }
}
