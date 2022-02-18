package cn.zzq0324.alarm.bot.extension.attach.impl;

import cn.zzq0324.alarm.bot.extension.attach.AttachExt;
import cn.zzq0324.alarm.bot.spi.Extension;

import java.io.InputStream;

/**
 * description: OSSAttach <br>
 * date: 2022/2/18 11:00 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Extension(name = "ossAttach", summary = "附件下载后上传到阿里云OSS")
public class OSSAttach implements AttachExt {
    @Override
    public String download(String url) {
        return null;
    }

    @Override
    public String upload(InputStream stream) {
        return null;
    }
}
