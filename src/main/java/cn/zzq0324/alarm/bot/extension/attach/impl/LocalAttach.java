package cn.zzq0324.alarm.bot.extension.attach.impl;

import cn.zzq0324.alarm.bot.extension.attach.AttachExt;
import cn.zzq0324.alarm.bot.spi.Extension;

import java.io.InputStream;

/**
 * description: LocalAttach <br>
 * date: 2022/2/18 11:00 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Extension(name = "localAttach", isDefault = true, summary = "附件下载保存到本地")
public class LocalAttach implements AttachExt {
    @Override
    public String download(String url) {
        return null;
    }

    @Override
    public String upload(InputStream stream) {
        return null;
    }
}
