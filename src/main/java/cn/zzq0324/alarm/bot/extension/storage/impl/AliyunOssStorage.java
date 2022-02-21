package cn.zzq0324.alarm.bot.extension.storage.impl;

import cn.zzq0324.alarm.bot.extension.storage.StorageExt;
import cn.zzq0324.alarm.bot.spi.Extension;

/**
 * description: OSS附件存储 <br>
 * date: 2022/2/18 11:00 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Extension(name = "aliyunOssStorage", summary = "阿里云oss存储")
public class AliyunOssStorage implements StorageExt {

    @Override
    public String upload(byte[] data) {
        return null;
    }
}