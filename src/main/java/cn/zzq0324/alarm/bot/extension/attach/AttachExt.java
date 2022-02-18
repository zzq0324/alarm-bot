package cn.zzq0324.alarm.bot.extension.attach;

import cn.zzq0324.alarm.bot.spi.SPI;

import java.io.InputStream;

/**
 * description: AttachExt <br>
 * date: 2022/2/18 11:00 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@SPI("attach")
public interface AttachExt {

    /**
     * 下载附件
     *
     * @param url 附件url，支持HTTP URL
     * @return 返回下载到本地的路径
     */
    String download(String url);

    /**
     * 上传附件
     *
     * @param stream 文件流
     * @return 返回上传后的HTTP URL
     */
    String upload(InputStream stream);
}
