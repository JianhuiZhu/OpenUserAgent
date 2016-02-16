package com.jianhui_zhu.openuseragent.view.interfaces;

import android.os.Bundle;

import com.jianhui_zhu.openuseragent.model.beans.User;

/**
 * Created by jianhuizhu on 2016-02-10.
 */
public interface LoginViewInterface {
    void showTag(String s);

    void switchFragmentWithUser(User user);
}
