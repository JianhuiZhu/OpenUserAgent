package com.jianhui_zhu.openuseragent.model;

import com.firebase.client.Firebase;
import com.jianhui_zhu.openuseragent.model.beans.Record;
import com.jianhui_zhu.openuseragent.model.beans.User;
import com.jianhui_zhu.openuseragent.util.Constant;

/**
 * Created by jianhuizhu on 2016-02-16.
 */
public class HomeModel {
    private static boolean userLoggedIn = false;
    private User user;

    public void saveHistory(Record record) {
        Firebase ref = new Firebase(Constant.urlRoot);
        if (userLoggedIn) {
            ref.child(user.getuID()).child("records").push().setValue(record);
        } else {

        }
    }


}
