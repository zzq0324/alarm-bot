package cn.zzq0324.alarm.bot.core.service;

import cn.zzq0324.alarm.bot.core.dao.LockDao;
import cn.zzq0324.alarm.bot.core.entity.Lock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * description: LockService <br>
 * date: 2022/2/22 1:01 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Service
public class LockService {

    @Autowired
    private LockDao lockDao;

    public void lock(Lock lock) {
        lockDao.insert(lock);
    }
}
