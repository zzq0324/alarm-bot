package cn.zzq0324.alarm.bot.task;

import cn.zzq0324.alarm.bot.constant.TaskType;
import cn.zzq0324.alarm.bot.entity.Task;
import cn.zzq0324.alarm.bot.service.TaskService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * description: AbstractTask <br>
 * date: 2022/2/22 12:49 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Slf4j
public abstract class AbstractTask {

    @Autowired
    protected TaskService taskService;

    /**
     * 获取任务类型
     */
    public abstract TaskType getTaskType();

    /**
     * 执行任务
     */
    public abstract void run(Task task);

    /**
     * 获取未完成任务列表
     */
    public void execute() {
        try {
            List<Task> taskList = taskService.getUnFinishedTaskList(getTaskType());
            log.info("taskType: {}, unfinished task size: {}", getTaskType(), taskList.size());

            taskList.stream().forEach(task -> {
                try {
                    run(task);
                } catch (Exception e) {
                    log.error("execute task: {} error.", JSONObject.toJSONString(task), e);
                }
            });
        } catch (Exception e) {
            log.error("execute task error, taskType: {}", getTaskType(), e);
        }
    }
}
