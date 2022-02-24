package cn.zzq0324.alarm.bot.core.util;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * description: DateUtils <br>
 * date: 2022/2/22 2:05 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
public class DateUtils {

    // 1天的毫秒数
    public static long ONE_DAY_MILLS = TimeUnit.DAYS.toMillis(1);
    // 1小时的毫秒数
    public static long ONE_HOUR_MILLS = TimeUnit.HOURS.toMillis(1);
    // 1分钟的毫秒数
    public static long ONE_MINUTE_MILLS = TimeUnit.MINUTES.toMillis(1);

    public static String getDiffText(Date startTime, Date endTime) {
        long mills = endTime.getTime() - startTime.getTime();
        long day = mills / ONE_DAY_MILLS;

        mills = mills % ONE_DAY_MILLS;
        long hour = mills / ONE_HOUR_MILLS;

        mills = mills % ONE_HOUR_MILLS;
        long minute = mills / ONE_MINUTE_MILLS;

        StringBuilder builder = new StringBuilder();
        if (day > 0) {
            builder.append(day).append("天");
        }

        if (hour > 0) {
            builder.append(hour).append("时");
        }

        builder.append(minute).append("分钟");

        return builder.toString();
    }

    public static Date add(Date date, int amount, int calendarField) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendarField, amount);

        return calendar.getTime();
    }
}
