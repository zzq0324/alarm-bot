package cn.zzq0324.alarm.bot.task;

import cn.zzq0324.alarm.bot.constant.TaskType;
import cn.zzq0324.alarm.bot.entity.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
    @Override
    public TaskType getTaskType() {
        return TaskType.DOWNLOAD_CHAT_MESSAGE;
    }

    @Override
    public void execute() {
        List<Task> taskList = getUnFinishedTaskList();
    }
}
