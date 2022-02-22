package cn.zzq0324.alarm.bot.extension.lock;

import cn.zzq0324.alarm.bot.spi.SPI;

/**
 * description: 简易分布式锁，通过数据库插入唯一id来控制，插入成功代表获得锁，插入不成功则表示未获得锁 <br>
 * date: 2022/2/21 11:47 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@SPI("distributeLock")
public interface LockExt {

    /**
     * 分布式锁，获得锁返回true，未获得锁返回false
     */
    boolean lock(String key);
}
