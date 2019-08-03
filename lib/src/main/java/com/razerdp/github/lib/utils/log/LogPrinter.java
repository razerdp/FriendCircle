package com.razerdp.github.lib.utils.log;

import android.view.MotionEvent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

/**
 * Created by 大灯泡 on 2019/8/3.
 */
class LogPrinter {

    static String parseContent(Object... obj) {
        StringBuilder buffer = new StringBuilder();
        if (obj != null) {
            if (obj.length > 1) {
                buffer.append(" {  ");
            }
            int i = 0;
            for (Object o : obj) {
                buffer.append("params【")
                        .append(i)
                        .append("】")
                        .append(" = ")
                        .append(parseContentInternal(o));
                if (i < obj.length - 1) {
                    buffer.append(" , ");
                }
                i++;
            }
            if (obj.length > 1) {
                buffer.append("  }");
            }
        }
        return buffer.toString();
    }

    private static String parseContentInternal(Object obj) {
        String result = null;
        if (obj instanceof String) {
            result = (String) obj;
        } else if (obj instanceof Throwable) {
            result = fromThrowable((Throwable) obj);
        } else if (obj instanceof Collection) {
            result = fromCollection((Collection) obj);
        } else if (obj instanceof Map) {
            result = fromMap((Map) obj);
        } else if (obj instanceof MotionEvent) {
            result = fromMotionEvent((MotionEvent) obj);
        } else {
            result = String.valueOf(obj);
        }
        return result;
    }

    private static String fromMotionEvent(MotionEvent motionEvent) {
        return MotionEvent.actionToString(motionEvent.getAction());
    }

    private static String fromThrowable(Throwable tr) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        tr.printStackTrace(printWriter);
        Throwable cause = tr.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        String crashInfo = writer.toString();
        printWriter.close();
        return crashInfo;
    }

    private static String fromMap(Map map) {
        if (map == null) {
            return "map is null";
        }
        if (map.isEmpty()) {
            return "map is empty";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("\n")
                .append("{")
                .append("\n")
                .append("\t");
        for (Object o : map.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            builder.append(String.format("\t%1$s : %2$s", String.valueOf(entry.getKey()), String.valueOf(entry.getValue())));
            builder.append("\n");
        }
        builder.append("}");
        return builder.toString();
    }

    private static String fromCollection(Collection list) {
        if (list == null) {
            return "list is null";
        }
        if (list.isEmpty()) {
            return "list is empty";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("\n")
                .append("{\n ");
        for (Object o : list) {
            if (o instanceof Collection) {
                builder.append(fromCollection((Collection) o));
            } else {
                builder.append(String.valueOf(o))
                        .append(" ,\n ");
            }
        }
        builder.append("}");
        return builder.toString();

    }

}
