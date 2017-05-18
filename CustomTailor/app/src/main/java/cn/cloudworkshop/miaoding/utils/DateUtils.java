package cn.cloudworkshop.miaoding.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Author：binge on 2016/12/19 10:11
 * Email：1993911441@qq.com
 * Describe：时间戳
 */
public class DateUtils {

    /**
     * @param type
     * @param time
     * @return 获取日期
     */
    public static String getDate(String type, long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(type, Locale.CHINA);
        String str = sdf.format(new Date(time * 1000));
        return str;
    }

    public static long getSeconds(String type, String time) {
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        long seconds = 0;
        try {
            seconds = sdf.parse(time).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return seconds;

    }
}
