package razerdp.friendcircle.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * Created by 大灯泡 on 2016/11/1.
 * <p>
 * 时间工具
 */

public class TimeUtil {
    private static StringBuffer result = new StringBuffer();

    public static String getTimeString(long milliseconds) {
        result.delete(0, result.length());

        long time = System.currentTimeMillis() - (milliseconds * 1000);
        long mill = (long) Math.ceil(time / 1000);//秒前

        long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前

        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时

        long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前

        if (day - 1 > 0 && day - 1 < 30) {
            result.append(day + "天");
        } else if (day - 1 >= 30) {
            result.append(Math.round((day - 1) / 30) + "个月");
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                result.append("1天");
            } else {
                result.append(hour + "小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                result.append("1小时");
            } else {
                result.append(minute + "分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                result.append("1分钟");
            } else {
                result.append(mill + "秒");
            }
        } else {
            result.append("刚刚");
        }
        if (!result.toString().equals("刚刚")) {
            result.append("前");
        }
        return result.toString();
    }


    private static SimpleDateFormat dataFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getTimeStringFromBmob(String time) {
        //格式:2016-10-28 18:48:23
        try {
            Date date = dataFormate.parse(time);
            return getTimeString(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return getTimeString(System.currentTimeMillis());
        }
    }
}
