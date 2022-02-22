package cn.zzq0324.alarm.bot.extension.storage;

import cn.zzq0324.alarm.bot.spi.SPI;

/**
 * description: 存储扩展节点 <br>
 * date: 2022/2/18 11:00 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@SPI("storage")
public interface StorageExt {

    /**
     * 上传附件
     */
    String upload(byte[] data, String fileName);
}
