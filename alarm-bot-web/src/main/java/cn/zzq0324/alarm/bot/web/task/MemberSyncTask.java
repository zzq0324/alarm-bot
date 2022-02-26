package cn.zzq0324.alarm.bot.web.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * description: 成员同步定时任务 <br>
 * date: 2022/2/22 1:43 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Component
@Slf4j
public class MemberSyncTask {

    public void execute() {
        try {
            // 检测配置成员的状态，有离职的标记，同时检测起关联负责的项目，如果有负责项目提醒告诉交接
        } catch (Exception e) {
            log.error("sync member error", e);
        }
    }
}
