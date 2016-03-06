package com.jianhui_zhu.openuseragent.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by jianhuizhu on 2016-03-06.
 */
public final class SharePreferenceUtil {
    public static void saveString(Context context,String key,String value){
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putString(key,value)
                .commit();
    }

    public static String getByKey(Context context,String key){
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(key,"NOT_FOUND");

    }
    public static void removeByKey(Context context,String key){
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .remove(key);
    }
}
