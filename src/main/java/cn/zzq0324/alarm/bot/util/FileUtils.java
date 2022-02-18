package cn.zzq0324.alarm.bot.util;

import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

/**
 * description: 文件工具类 <br>
 * date: 2022/2/4 4:08 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
public class FileUtils {

    /**
     * 读取资源文件内容，并返回字符串
     *
     * @param resource 资源文件，相对classpath而言，例如"/lark/help.json"
     * @return 返回文件内容
     */
    public static String readResourceAsString(String resource) {
        try (Reader reader = new InputStreamReader(FileUtils.class.getResourceAsStream(resource),
            StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
