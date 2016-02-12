package com.jianhui_zhu.openuseragent.model.interfaces;

import rx.Observable;
import rx.Observer;

/**
 * Created by jianhuizhu on 2016-02-12.
 */
public interface LoginModelInterface {
    public Observable<String> login();
    public void logout();
}
