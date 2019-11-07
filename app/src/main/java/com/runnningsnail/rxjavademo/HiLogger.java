package com.runnningsnail.rxjavademo;

import android.os.Process;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author create by yongjie on 2018/6/29
 * Log工具类
 */
public class HiLogger {

    private static volatile boolean OUTPUT_LOG = true;

    public static void logEnable(boolean enable) {
        OUTPUT_LOG = enable;
    }

    public static void v(String tag, String message) {
        if (OUTPUT_LOG) {
            Log.v(tag, basicMessage(message));
        }
    }

    public static void v(String tag, String format, Object... obj) {
        if (OUTPUT_LOG) {
            String message = String.format(format, obj);
            Log.v(tag, basicMessage(message));
        }
    }


    public static void d(String tag, String message) {
        if (OUTPUT_LOG) {
            Log.d(tag, basicMessage(message));
        }
    }

    public static void d(String tag, String format, Object... obj) {
        if (OUTPUT_LOG) {
            String message = String.format(format, obj);
            Log.d(tag, basicMessage(message));
        }
    }

    public static void i(String tag, String message) {
        if (OUTPUT_LOG) {
            Log.i(tag, basicMessage(message));
        }
    }

    public static void i(String tag, String format, Object... obj) {
        if (OUTPUT_LOG) {
            String message = String.format(format, obj);
            Log.i(tag, basicMessage(message));
        }
    }

    public static void w(String tag, String message) {
        if (OUTPUT_LOG) {
            Log.w(tag, basicMessage(message));
        }
    }

    public static void w(String tag, String format, Object... obj) {
        if (OUTPUT_LOG) {
            String message = String.format(format, obj);
            Log.w(tag, basicMessage(message));
        }
    }

    public static void e(String tag, String message) {
        if (OUTPUT_LOG) {
            Log.e(tag, basicMessage(message));
        }
    }

    public static void e(String tag, String message, Throwable throwable) {
        if (OUTPUT_LOG) {
            Log.e(tag, basicMessage(message), throwable);
        }
    }

    public static void e(String tag, String format, Object... obj) {
        if (OUTPUT_LOG) {
            String message = String.format(format, obj);
            Log.e(tag, basicMessage(message));
        }
    }

    private static String basicMessage(String message) {
        if (message == null) {
            message = "null";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[ ")
                .append(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").
                        format(new Date(System.currentTimeMillis())))
                .append(" PID:").append(Process.myPid())
                .append(", TID:").append(Thread.currentThread().getId())
                .append(", TName:").append(Thread.currentThread().getName())
                .append(" ]")
                .append("==>");

        String format = String.format("%s %s", stringBuilder.toString(), message);
        return format;
    }


    /**
     * 打印方法栈帧信息，写5的原因是根据栈的入栈规律，第5个元素就是我们需要的信息
     * 暂时测试没有问题
     */
    private static String getMethodMessage() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StringBuilder builder = new StringBuilder();
        String className = stackTrace[5].getClassName();
        className = className.substring(className.lastIndexOf(".") + 1);
        String methodName = stackTrace[5].getMethodName();
        int lineName = stackTrace[5].getLineNumber();
        builder.append("[").append(className).append(", ").append(methodName).append("(), ").append(lineName).append("]");
        return builder.toString();
    }


}

