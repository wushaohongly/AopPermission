package com.wushaohong.library.log;

import android.util.Log;

public class MyLog {

    public static boolean isLog = false;

    public static void i(String s) {
        if (isLog) {
            Log.i("AopPermissions", s);
        }
    }

    public static void d(String s) {
        if (isLog) {
            Log.d("AopPermissions", s);
        }
    }

    public static void e(String s) {
        if (isLog) {
            Log.e("AopPermissions", s);
        }
    }

}
