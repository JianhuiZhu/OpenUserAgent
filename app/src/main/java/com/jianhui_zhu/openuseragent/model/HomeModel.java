package com.jianhui_zhu.openuseragent.model;

import android.content.Context;

import com.jianhui_zhu.openuseragent.model.beans.Record;
import com.jianhui_zhu.openuseragent.model.beans.User;
import com.jianhui_zhu.openuseragent.util.LocalDatabaseSingleton;
import com.jianhui_zhu.openuseragent.util.RemoteDatabaseSingleton;

/**
 * Created by jianhuizhu on 2016-02-16.
 */
public class HomeModel {
    private static boolean userLoggedIn = false;
    private User user;
    private Context context;

    public HomeModel(Context context) {
        this.context = context;
    }

    public void saveHistory(String url, String uID) {
        Record record = new Record();
        record.setUrl(url);
        record.setTimestamp(System.currentTimeMillis());
        if (userLoggedIn) {
            RemoteDatabaseSingleton.getInstance(user.getuID()).saveHistory(record);
        } else {
            LocalDatabaseSingleton.getInstance(context).saveHistory(record);
        }
    }


}
