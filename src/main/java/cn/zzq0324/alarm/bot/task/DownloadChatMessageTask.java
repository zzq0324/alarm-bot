package cn.zzq0324.alarm.bot.task;

import cn.zzq0324.alarm.bot.constant.Status;
import cn.zzq0324.alarm.bot.constant.TaskType;
import cn.zzq0324.alarm.bot.entity.Event;
import cn.zzq0324.alarm.bot.entity.Message;
import cn.zzq0324.alarm.bot.entity.Task;
import cn.zzq0324.alarm.bot.extension.platform.PlatformExt;
import cn.zzq0324.alarm.bot.service.MessageService;
import cn.zzq0324.alarm.bot.spi.ExtensionLoader;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * description: 下载群聊记录 <br>
 * date: 2022/2/22 12:52 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Slf4j
@Component
public class DownloadChatMessageTask extends AbstractTask {

    @Autowired
    private MessageService messageService;

    @Override
    public TaskType getTaskType() {
        return TaskType.DOWNLOAD_CHAT_MESSAGE;
    }

    @Transactional
    @Override
    public void run(Task task) {
        Event event = JSONObject.parseObject(task.getData(), Event.class);

        // 下载聊天消息并保存
        downloadAndSaveChatGroupMessage(event);

        // 解散群聊
        ExtensionLoader.getDefaultExtension(PlatformExt.class).destroyChatGroup(event.getChatGroupId());

        // 更新任务为已解决
        task.setStatus(Status.FINISH);
        taskService.update(task);
    }

    /**
     * 固定60秒执行一次，如上次执行超过60秒，结束后会立马执行本次操作
     */
    @Scheduled(initialDelay = 60000, fixedRate = 60000)
    @Override
    public void execute() {
        super.execute();
    }

    private void downloadAndSaveChatGroupMessage(Event event) {
        List<Message> messageList =
            ExtensionLoader.getDefaultExtension(PlatformExt.class).downloadChatGroupMessage(event.getChatGroupId());

        if (messageList == null) {
            log.warn("chat group: {}, message is empty.", event.getChatGroupId());

            return;
        }

        messageList.stream().forEach(message -> {
            message.setChatGroupId(event.getChatGroupId());
            message.setProjectId(event.getProjectId());

            messageService.add(message);
        });
    }
}
