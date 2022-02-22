package cn.zzq0324.alarm.bot.extension.storage.impl;

import cn.zzq0324.alarm.bot.config.AlarmBotProperties;
import cn.zzq0324.alarm.bot.extension.storage.StorageExt;
import cn.zzq0324.alarm.bot.spi.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;

import java.io.File;

/**
 * description: LocalAttach <br>
 * date: 2022/2/18 11:00 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Extension(name = "localStorage", isDefault = true, summary = "附件本地存储")
public class LocalStorage implements StorageExt {

    @Autowired
    private AlarmBotProperties alarmBotProperties;

    @Override
    public String upload(byte[] data, String fileName) {
        try {
            checkResourceFolderExists();

            // 格式如：/resource/xxx.png
            String suffix = alarmBotProperties.getResourceDownloadFolder() + "/" + fileName;

            File file = new File(alarmBotProperties.getResourceDownloadFolder(), suffix);
            FileCopyUtils.copy(data, file);

            return alarmBotProperties.getUrlPrefix() + suffix;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void checkResourceFolderExists() {
        File folder = new File(alarmBotProperties.getResourceDownloadFolder());
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}
