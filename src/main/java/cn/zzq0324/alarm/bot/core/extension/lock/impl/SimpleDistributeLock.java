package cn.zzq0324.alarm.bot.core.extension.lock.impl;

import cn.zzq0324.alarm.bot.core.entity.Lock;
import cn.zzq0324.alarm.bot.core.extension.lock.LockExt;
import cn.zzq0324.alarm.bot.core.spi.Extension;
import cn.zzq0324.alarm.bot.core.service.LockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

/**
 * description: 简易分布式锁，通过数据库插入唯一id来控制，插入成功代表获得锁，插入不成功则表示未获得锁 <br>
 * date: 2022/2/21 11:47 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Extension(name = "simpleDistributeLock", isDefault = true, summary = "简易分布式锁")
@Slf4j
public class SimpleDistributeLock implements LockExt {

    @Autowired
    private LockService lockService;

    @Override
    public boolean lock(String key) {
        Lock lock = new Lock(key);

        try {
            lockService.lock(lock);

            return true;
        } catch (DuplicateKeyException e) {
            // ignore
        } catch (Exception e) {
            log.error("get simple distribute lock: {} error", key, e);
        }

        return false;
    }
}
