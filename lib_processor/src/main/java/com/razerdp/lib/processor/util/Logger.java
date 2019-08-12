package com.razerdp.lib.processor.util;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * Created by razerdp on 2019/8/12.
 */
public class Logger {
    private Messager msg;

    public Logger(Messager msg) {
        this.msg = msg;
    }

    public void i(CharSequence info) {
        msg.printMessage(Diagnostic.Kind.NOTE, info);
    }

    public void e(CharSequence error) {
        msg.printMessage(Diagnostic.Kind.ERROR, error);
    }

    public void w(CharSequence warning) {
        msg.printMessage(Diagnostic.Kind.WARNING, warning);
    }

    public void e(Throwable error) {
        msg.printMessage(Diagnostic.Kind.ERROR, "Catch Exception : [" + error.getMessage() + "]\n" + loadStackTrace(error));
    }


    private String loadStackTrace(Throwable error) {
        StringBuilder builder = new StringBuilder();
        StackTraceElement[] traceElements = error.getStackTrace();
        for (StackTraceElement traceElement : traceElements) {
            builder.append("\tat ")
                    .append(traceElement.toString())
                    .append('\n');
        }
        return builder.toString();

    }
}
