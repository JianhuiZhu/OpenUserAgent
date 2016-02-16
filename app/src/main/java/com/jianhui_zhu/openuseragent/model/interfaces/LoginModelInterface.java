package com.jianhui_zhu.openuseragent.model.interfaces;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.jianhui_zhu.openuseragent.model.beans.User;

import rx.Observable;
import rx.Observer;

/**
 * Created by jianhuizhu on 2016-02-12.
 */
public interface LoginModelInterface {

     User getUserObject(DataSnapshot currentPath, AuthData authData, Firebase ref);
     void logout();
}
