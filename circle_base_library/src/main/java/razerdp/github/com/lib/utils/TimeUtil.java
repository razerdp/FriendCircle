package razerdp.github.com.lib.utils;

import android.support.annotation.StringDef;
import android.text.TextUtils;
import android.util.Log;

import com.socks.library.KLog;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import razerdp.github.com.lib.R;

/**
 * Created by 大灯泡 on 2016/11/1.
 * <p>
 * 时间工具
 */

public class TimeUtil {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({YYYYMMDD, YYYYMM, HHMM, YYYYMD, YYYYMMDDHHMM, YYYYMMDDHHMMSS, YYYYMMDDHHMM_EX, MMDDHHMM_CHINESE_EX, YYYYMMLNDDHHMM, MMDDHHMM_CHINESE, YYYYMMDD_SLASH, YYYYMM_SLASH})
    public @interface FormateType {
    }

    public final static String YYYYMMDD = "yyyy-MM-dd";
    public final static String YYYYMMDDHHMM_EX = "yyyy_MM_dd_hh_mm";
    public final static String YYYYMD = "yyyy-M-d";
    public final static String YYYYMM = "yyyy-MM";
    public final static String HHMM = "hh:mm";
    public final static String YYYYMMDDHHMM = "yyyy-MM-dd hh:mm";
    public final static String YYYYMMLNDDHHMM = "yyyy-MM-dd\nhh:mm";
    public final static String YYYYMMDDHHMMSS = "yyyy-MM-dd hh:mm:ss";
    public final static String MMDDHHMM_CHINESE = "MM月dd日_hh时mm分";
    public final static String MMDDHHMM_CHINESE_EX = "MM月dd日_hh_mm";
    public final static String YYYYMMDD_SLASH = "yyyy/MM/dd";
    public final static String YYYYMM_SLASH = "yyyy/MM";

    private static SimpleDateFormat sdf = new SimpleDateFormat();
    public static final int YEAR = 365 * 24 * 60 * 60;// 年
    public static final int MONTH = 30 * 24 * 60 * 60;// 月
    public static final int DAY = 24 * 60 * 60;// 天
    public static final int HOUR = 60 * 60;// 小时
    public static final int MINUTE = 60;// 分钟

    public static String getTimeString(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
        String timeStr = null;
        if (timeGap > YEAR) {
            timeStr = formatTimeFromResource(R.string.format_time_year, (int) (timeGap / YEAR));
        } else if (timeGap > MONTH) {
            timeStr = formatTimeFromResource(R.string.format_time_month, (int) (timeGap / MONTH));

        } else if (timeGap > DAY) {// 1天以上
            timeStr = formatTimeFromResource(R.string.format_time_day, (int) (timeGap / DAY));
        } else if (timeGap > HOUR) {// 1小时-24小时
            timeStr = formatTimeFromResource(R.string.format_time_hour, (int) (timeGap / HOUR));
        } else if (timeGap > MINUTE) {// 1分钟-59分钟
            timeStr = formatTimeFromResource(R.string.format_time_minute, (int) (timeGap / MINUTE));
        } else {// 1秒钟-59秒钟
            timeStr = StringUtil.getResourceString(R.string.format_time_sec);
        }
        return timeStr;
    }

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

    private static String formatTimeFromResource(int resource, int time) {
        return StringUtil.getResourceStringAndFormat(resource, time);
    }


    private static SimpleDateFormat dataFormate = new SimpleDateFormat(YYYYMMDDHHMMSS, Locale.getDefault());
    private static SimpleDateFormat yyyymmddFormate = new SimpleDateFormat(YYYYMMDD, Locale.getDefault());

    /**
     * 获取未来 第 past 天的日期
     *
     * @param past
     * @return
     */
    public static String getFetureDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        Log.e(null, result);
        return result;
    }

    /**
     * 获取未来 第 past 天的日期
     *
     * @param past
     * @return
     */
    public static String getFetureDate(int past, String dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        String result = format.format(today);
        return result;
    }

    public static String getWhichDay(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("EEEE");
        String result = format.format(today);
        return result;
    }

    public static String formatDate(String date, String dateFormat) throws ParseException {
        SimpleDateFormat inputSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTime = inputSdf.parse(date);
        SimpleDateFormat ouputSdf = new SimpleDateFormat(dateFormat);
        return ouputSdf.format(dateTime);
    }

    /**
     * 获取未来 第 past 天的日期
     *
     * @param date 20170412
     * @return 04-12
     */
    public static String formatDate(String date) {
        if (!TextUtils.isEmpty(date) && date.length() == 8) {
            return new StringBuilder(date.substring(4)).insert(2, "-").toString();
        }
        return date;
    }

    /**
     * 时间戳转格式化日期
     *
     * @param timestamp 单位毫秒
     * @param format    日期格式
     * @return
     */
    public static String longToTimeStr(long timestamp, @FormateType String format) {
        return transferLongToDate(timestamp, format);
    }

    /**
     * 时间戳转格式化日期
     *
     * @param timestamp 单位毫秒
     * @param format    日期格式
     * @return
     */
    private static String transferLongToDate(long timestamp, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = new Date(timestamp);
            return sdf.format(date);
        } catch (Exception e) {
            KLog.e(e);
            return "null";
        }
    }

    public static String dateToString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 格式化日期转时间戳
     *
     * @param format 日期格式
     * @return
     */
    public static long strToTimestamp(String date, String format) {
        long timestamp = 0;
        try {
            timestamp = new SimpleDateFormat(format).parse(date).getTime() / 1000;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    /**
     * 比较timeTwo是否比TimeOne晚超过distanceMinute分钟
     *
     * @param TimeOne        单位 unixtime
     * @param timeTwo        单位 unixtime
     * @param distanceMinute 单位分钟
     * @return
     */
    public static boolean isGreaterThan(long TimeOne, long timeTwo, int distanceMinute) {
        long timeDifference = timeTwo - TimeOne;// 两者相距的秒数
        long oneMinute = 60;
        if (timeDifference > distanceMinute * oneMinute) {
            return true;
        } else {
            return false;
        }
    }

    public static String transferDateFromate(String originalDate, @FormateType String oldFormate, @FormateType String newFormate) {
        long time = stringToTimeStamp(originalDate, oldFormate);
        return transferDateFromate(time, newFormate);
    }

    public static String transferDateFromate(long originalDate, @FormateType String newFormate) {
        return longToTimeStr(originalDate, newFormate);
    }


    public static String formatLongToTimeStr(Long l) {
        String str = "";
        int hour = 0;
        int minute = 0;
        int second = 0;
        second = l.intValue() / 1000;
        if (second > 60) {
            minute = second / 60;
            second = second % 60;
        }

        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }

        String strtime = "";
        if (hour != 0) {
            strtime = hour + ":" + minute + ":" + second;
        } else {
            strtime = minute + ":" + (second >= 10 ? second : "0" + second);
        }
        return strtime;
    }

    public static long stringToTimeStamp(String timeString, @FormateType String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(timeString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long stringToTimeStamp(String timeString, @FormateType String format, TimeZone timeZone) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        sdf.setTimeZone(timeZone);
        try {
            return sdf.parse(timeString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean isToday(long timeInMilis) {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(timeInMilis);
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.DATE) == time.get(Calendar.DATE)) {
            return true;
        } else {
            return false;
        }
    }

    public static int getSubDay(long start, long end) {
        int result = Math.round((end - start) / (1000 * DAY));
        return result < 0 ? -1 : result;
    }



    public static String getWeek(String pTime) {
        String week = "";
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(yyyymmddFormate.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            week = "周日";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            week = "周一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            week = "周二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            week = "周三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            week = "周四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            week = "周五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            week = "周六";
        }
        return week;
    }

    public static long getNextTime(long curTime, long time) {
        return ((curTime / 1000) + time) * 1000;
    }

}
