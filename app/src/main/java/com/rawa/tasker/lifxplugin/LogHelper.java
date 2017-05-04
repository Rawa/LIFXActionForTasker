package com.rawa.tasker.lifxplugin;

import android.util.Log;

/**
 * Created by rawa on 2016-04-30.
 */
public class LogHelper {

    public static void LogDebug(Boolean enabled, String tag, String message){
        if (enabled){
            Log.d(tag, message);
        }
    }

    public static void LogError(Boolean enabled, String tag, String message){
        if (enabled){
            Log.e(tag, message);
        }
    }
}
