package cn.zzq0324.alarm.bot.core.extension.storage.impl;

import cn.zzq0324.alarm.bot.core.config.AlarmBotProperties;
import cn.zzq0324.alarm.bot.core.config.ResourceHandlerConfiguration;
import cn.zzq0324.alarm.bot.core.extension.storage.StorageExt;
import cn.zzq0324.alarm.bot.core.spi.Extension;
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
            // 格式如：/files/xxx.png
            String suffix = ResourceHandlerConfiguration.RESOURCE_URL_PREFIX + "/" + fileName;
            File file = new File(alarmBotProperties.getResourceDownloadFolder() + suffix);

            checkFolderExists(file);

            FileCopyUtils.copy(data, file);

            return alarmBotProperties.getUrlPrefix() + suffix;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void checkFolderExists(File file) {
        File folder = file.getParentFile();
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}
