package com.jianhui_zhu.openuseragent.util;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by jianhuizhu on 2016-05-01.
 */
public class RxBus {
    private static final RxBus instance = new RxBus();
    private static final Subject<Object, Object> bus = new SerializedSubject<>(PublishSubject.create());

    public void send(Object o) {
        bus.onNext(o);
    }

    public Observable<Object> toObserverable() {
        return bus;
    }

    public static RxBus getInstance() {
        return instance;
    }
}
