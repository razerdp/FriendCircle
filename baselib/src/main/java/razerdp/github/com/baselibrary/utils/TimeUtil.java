package razerdp.github.com.baselibrary.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import razerdp.github.com.baselibrary.R;

/**
 * Created by 大灯泡 on 2016/11/1.
 * <p>
 * 时间工具
 */

public class TimeUtil {

    public final static String FORMAT_DATE_ALL = "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_DATE = "yyyy-MM-dd";
    public final static String FORMAT_TIME = "hh:mm";
    public final static String FORMAT_DATE_TIME = "yyyy-MM-dd hh:mm";
    public final static String FORMAT_MONTH_DAY_TIME = "MM月dd日 hh:mm";
    private static SimpleDateFormat sdf = new SimpleDateFormat();
    private static final int YEAR = 365 * 24 * 60 * 60;// 年
    private static final int MONTH = 30 * 24 * 60 * 60;// 月
    private static final int DAY = 24 * 60 * 60;// 天
    private static final int HOUR = 60 * 60;// 小时
    private static final int MINUTE = 60;// 分钟

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

    private static String formatTimeFromResource(int resource, int time) {
        return StringUtil.getResourceStringAndFormat(resource, time);
    }


    private static SimpleDateFormat dataFormate = new SimpleDateFormat(FORMAT_DATE_ALL, Locale.getDefault());

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
