package com.yj.hotfixsdk.log;

import android.util.Log;

/**
 * Created by yangjian on 2019/3/1.
 */

public class GLogger {

    private static boolean isDebug = true;

    private static final String TAG = "GLogger";

    public static void setLogDebug(boolean debug){

        isDebug = debug;
    }

    public static void d(String message){

        Log.d(TAG,"=======****GLogger****====== "+message + "=======****GLogger****======");
    }

    public static void e(String message){

        Log.e(TAG,"=======****GLogger****====== "+message + "=======****GLogger****======");
    }

    public static void i(String message){

        Log.i(TAG,"=======****GLogger****====== "+message + "=======****GLogger****======");
    }

    public static void w(String message){

        Log.w(TAG,"=======****GLogger****====== "+message + "=======****GLogger****======");
    }
}
