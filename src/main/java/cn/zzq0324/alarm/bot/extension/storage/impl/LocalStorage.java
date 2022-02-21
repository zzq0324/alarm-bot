package cn.zzq0324.alarm.bot.extension.storage.impl;

import cn.zzq0324.alarm.bot.extension.storage.StorageExt;
import cn.zzq0324.alarm.bot.spi.Extension;

/**
 * description: LocalAttach <br>
 * date: 2022/2/18 11:00 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Extension(name = "localStorage", isDefault = true, summary = "附件本地存储")
public class LocalStorage implements StorageExt {

    @Override
    public String upload(byte[] data) {
        return null;
    }
}
