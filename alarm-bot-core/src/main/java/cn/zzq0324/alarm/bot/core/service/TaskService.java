package cn.zzq0324.alarm.bot.core.service;

import cn.zzq0324.alarm.bot.core.constant.Status;
import cn.zzq0324.alarm.bot.core.constant.TaskType;
import cn.zzq0324.alarm.bot.core.dao.TaskDao;
import cn.zzq0324.alarm.bot.core.entity.Task;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * description: TaskService <br>
 * date: 2022/2/22 12:55 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Service
public class TaskService {

    @Autowired
    private TaskDao taskDao;

    public void addTask(TaskType taskType, Date triggerTime, String data) {
        Task task = new Task();
        task.setTaskType(taskType);
        task.setData(data);
        task.setStatus(Status.CREATED);
        task.setTriggerTime(triggerTime);

        taskDao.insert(task);
    }

    public void update(Task task) {
        taskDao.updateById(task);
    }

    /**
     * 获取未完成的任务列表
     */
    public List<Task> getUnFinishedTaskList(TaskType taskType) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.ne("status", Status.FINISH.getValue());
        queryWrapper.eq("task_type", taskType.getValue());
        queryWrapper.lt("trigger_time", new Date());

        return taskDao.selectList(queryWrapper);
    }
}
