package razerdp.friendcircle.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 大灯泡 on 2016/2/25.
 * 时间工具类
 */
public class TimeUtil {
    /**
     * <1分钟：刚刚
     * <1小时：N分钟前
     * 当天：N小时前
     * 昨天：昨天 HH:mm
     * N天前：MM-dd HH:mm
     * 去年：yyyy-MM-dd HH:mm
     */
    public static String getTimeString(long milliseconds){
        long dt = System.currentTimeMillis() - milliseconds;
        if (dt < 60000) {
            return "刚刚";
        } else if (dt < 3600000) {
            return (dt / 60000) + "分钟前";
        }

        Calendar nowCal = Calendar.getInstance();
        String theTimeStr = getTime("yyyy-MM-dd", milliseconds);

        String str = getTime("yyyy-MM-dd", nowCal.getTimeInMillis());
        if (theTimeStr.equals(str)) {
            return (dt / 3600000) + "小时前";
        }

        nowCal.add(Calendar.DATE, -1);
        str = getTime("yyyy-MM-dd", nowCal.getTimeInMillis());
        if (theTimeStr.equals(str)) {
            return "昨天" + " " + getTime("HH:mm", milliseconds);
        }

        nowCal.add(Calendar.DATE, 1);
        theTimeStr = getTime("yyyy", milliseconds);
        str = getTime("yyyy", nowCal.getTimeInMillis());
        if (theTimeStr.equals(str)) {
            return getTime("MM-dd HH:mm", milliseconds);
        }

        return getTime("yyyy-MM-dd HH:mm", milliseconds);
    }
    private static String getTime(String formatString, long milliseconds){
        return new SimpleDateFormat(formatString, Locale.getDefault()).format(new Date(milliseconds));
    }
}
