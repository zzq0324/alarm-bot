package cn.zzq0324.alarm.bot.web.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
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

    @Scheduled(cron = "")
    public void execute() {
        try {
            // 获取全部员工
        } catch (Exception e) {
            log.error("sync member error", e);
        }
    }
}
