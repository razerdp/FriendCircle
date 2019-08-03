package com.razerdp.github.lib.utils.log;

import android.text.TextUtils;
import android.util.Log;

import com.razerdp.github.lib.api.AppContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;

/**
 * Created by 大灯泡 on 2019/7/30.
 */
public class KLog {

    public enum LogLevel {
        v(5),
        i(4),
        d(3),
        w(2),
        e(1);

        public final int level;

        LogLevel(int level) {
            this.level = level;
        }
    }

    private static final String TAG = AppContext.getAppContext().getPackageName();
    //logcat最大长度，超过则分割
    private static final int MAX_LOG_LENGTH = 4000;

    public static void i(Object... objects) {
        i(TAG, objects);
    }


    public static void i(String s, Object... objects) {
        logInternal(LogLevel.i, s, objects);
    }

    public static void d(Object... objects) {
        d(TAG, objects);
    }

    public static void d(String s, Object... objects) {
        logInternal(LogLevel.d, s, objects);
    }

    public static void w(Object... objects) {
        w(TAG, objects);
    }

    public static void w(String s, Object... objects) {
        logInternal(LogLevel.w, s, objects);
    }

    public static void e(Object... objects) {
        e(TAG, objects);
    }

    public static void e(String s, Object... objects) {
        logInternal(LogLevel.e, s, objects);
    }

    public static void v(Object... objects) {
        v(TAG, objects);
    }

    public static void v(String s, Object... objects) {
        logInternal(LogLevel.v, s, objects);
    }


    private static void logInternal(LogLevel level, String tag, Object... content) {
        String logCat = format(LogPrinter.parseContent(content));
        try {
            long len = logCat.length();
            if (len <= MAX_LOG_LENGTH) {
                doLog(level, tag, logCat);
            } else {
                while (logCat.length() > MAX_LOG_LENGTH) {
                    String subLog = logCat.substring(0, MAX_LOG_LENGTH);
                    logCat = logCat.replace(subLog, "");
                    doLog(level, tag, logCat);
                }
                doLog(level, tag, logCat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void doLog(LogLevel level, String tag, String logCat) {
        switch (level) {
            case d:
                Log.d(TAG, logCat);
                break;
            case e:
                Log.e(TAG, logCat);
                break;
            case i:
                Log.i(TAG, logCat);
                break;
            case v:
                Log.v(TAG, logCat);
                break;
            case w:
                Log.w(TAG, logCat);
                break;
            default:
                Log.i(TAG, logCat);
                break;
        }

    }

    static String format(String logCat) {
        StackTraceElement element = getCurrentStackTrace();
        String className = "unknow";
        String methodName = "unknow";
        int lineNumber = -1;
        if (element != null) {
            className = element.getFileName();
            methodName = element.getMethodName();
            lineNumber = element.getLineNumber();
        }

        StringBuilder sb = new StringBuilder();
        logCat = wrapJson(logCat);
        sb.append("  (")
                .append(className)
                .append(":")
                .append(lineNumber)
                .append(") #")
                .append(methodName)
                .append("：")
                .append('\n')
                .append(logCat);
        return sb.toString();
    }

    private static StackTraceElement getCurrentStackTrace() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();

        int stackOffset = getStackOffset(trace, KLog.class);
        if (stackOffset == -1) {
            stackOffset = getStackOffset(trace, Logger.class);
            if (stackOffset == -1) {
                stackOffset = getStackOffset(trace, Log.class);
                if (stackOffset == -1) {
                    return null;
                }
            }
        }
        return trace[stackOffset];
    }

    private static int getStackOffset(StackTraceElement[] trace, Class cla) {
        if (cla == null) return -1;
        int logIndex = -1;
        for (int i = 0; i < trace.length; i++) {
            StackTraceElement element = trace[i];
            String tClass = element.getClassName();
            if (TextUtils.equals(tClass, cla.getName())) {
                logIndex = i;
            } else {
                if (logIndex > -1) break;
            }
        }
        if (logIndex != -1) {
            logIndex++;
            if (logIndex >= trace.length) {
                logIndex = trace.length - 1;
            }
        }
        return logIndex;
    }

    public static String wrapLocation(Class cla, int lineNumber) {
        return ".(" + cla.getSimpleName() + ".java:" + lineNumber + ")";
    }

    public static String wrapJson(String jsonStr) {
        String message;
        if (TextUtils.isEmpty(jsonStr)) return "json为空";
        try {
            if (jsonStr.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                message = jsonObject.toString(2);
                message = "\n================JSON================\n"
                        + message + '\n'
                        + "================JSON================\n";
            } else if (jsonStr.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(jsonStr);
                message = jsonArray.toString(4);
                message = "\n================JSONARRAY================\n"
                        + message + '\n'
                        + "================JSONARRAY================\n";
            } else {
                message = jsonStr;
            }
        } catch (JSONException e) {
            message = jsonStr;
        }

        return message;
    }
}
