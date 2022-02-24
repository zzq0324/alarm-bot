package cn.zzq0324.alarm.bot.core.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONValue;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description: 文件工具类 <br>
 * date: 2022/2/4 4:08 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Slf4j
public class FileUtils {

    // 正则表达式，提取${}里面的参数
    private static final String EXTRACT_PARAM_REG = "\\$\\{(\\w+)\\}";

    private static Map<String, String> FILE_CONTENT_CACHE = new ConcurrentHashMap<>();

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

    public static String getFileContent(String resource) {
        return FILE_CONTENT_CACHE.computeIfAbsent(resource, key -> readResourceAsString(key));
    }

    public static String getFileContent(String resource, JSONObject data, boolean isEscape) {
        String content = getFileContent(resource);

        String result = content;
        Pattern pattern = Pattern.compile(EXTRACT_PARAM_REG);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String param = matcher.group(1);
            if (data.containsKey(param)) {
                String value = data.getString(param);

                // 转换
                if (isEscape) {
                    value = JSONValue.escape(value);
                }

                result = result.replace(matcher.group(0), value);
            }
        }

        return result;
    }
}
