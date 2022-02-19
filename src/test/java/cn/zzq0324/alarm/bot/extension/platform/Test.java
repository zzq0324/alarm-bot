package cn.zzq0324.alarm.bot.extension.platform;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description: Test <br>
 * date: 2022/2/20 12:08 上午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
public class Test {
    public static void main(String[] args) {
        String s = "[PINPOINT Alarm - service-test] ERROR RATE is 20% (Threshold : 10%)";

        Pattern pattern = Pattern.compile("PINPOINT Alarm - (\\w+-\\w+)");
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }
}
