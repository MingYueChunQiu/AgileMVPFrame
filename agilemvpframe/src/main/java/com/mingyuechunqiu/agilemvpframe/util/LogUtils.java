package com.mingyuechunqiu.agilemvpframe.util;

import android.text.TextUtils;
import android.util.Log;

/**
 * <pre>
 *     author : xyj
 *     e-mail : yujie.xi@ehailuo.com
 *     time   : 2017/12/26
 *     desc   : 日志记录工具类
 *     version: 1.0
 * </pre>
 */
public class LogUtils {

    private static int current = 4;//记录当前日志记录级别，当正式上线时，将日志屏蔽
    private static final int VERBOSE = 4;
    private static final int DEBUG = 3;
    private static final int INFO = 2;
    private static final int WARN = 1;
    private static final int ERROR = 0;

    public static int getCurrent() {
        return current;
    }

    public static void setCurrent(int current) {
        LogUtils.current = current;
    }

    public static void v(String tag, String msg){
        if (current < VERBOSE){
            return;
        }
        if (TextUtils.isEmpty(msg)){
            LogUtils.d(tag, "传入日志内容为空");
            return;
        }
        Log.v(tag, msg);
    }

    public static void d(String tag, String msg){
        if (current < DEBUG){
            return;
        }
        if (TextUtils.isEmpty(msg)){
            LogUtils.d(tag, "传入日志内容为空");
            return;
        }
        Log.d(tag, msg);
    }

    public static void i(String tag, String msg){
        if (current < INFO){
            return;
        }
        if (TextUtils.isEmpty(msg)){
            LogUtils.d(tag, "传入日志内容为空");
            return;
        }
        Log.i(tag, msg);
    }

    public static void w(String tag, String msg){
        if (current < WARN){
            return;
        }
        if (TextUtils.isEmpty(msg)){
            LogUtils.d(tag, "传入日志内容为空");
            return;
        }
        Log.v(tag, msg);
    }

    public static void e(String tag, String msg){
        if (current < ERROR){
            return;
        }
        if (TextUtils.isEmpty(msg)){
            LogUtils.d(tag, "传入日志内容为空");
            return;
        }
        Log.e(tag, msg);
    }

}
