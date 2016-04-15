package com.jianhui_zhu.openuseragent.presenter;

import android.content.Context;

import com.jianhui_zhu.openuseragent.model.DownloadModel;
import com.jianhui_zhu.openuseragent.model.beans.FileDescriptor;
import com.jianhui_zhu.openuseragent.view.interfaces.DownloadViewInterface;

import java.io.IOException;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by Jianhui Zhu on 2016-02-06.
 */
public class DownloadPresenter {
    private Context context;
    private DownloadModel model;
    private DownloadViewInterface view;
    public DownloadPresenter(Context context, DownloadViewInterface view){
        this.context = context;
        this.view = view;
//        try {
//            model = new DownloadModel(context);
//        } catch (IOException e) {
//            view.showTag(e.getMessage());
//        }
    }
    public void getAllFiles(){
        model.getAllFilesInDownloadFolder()
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.showTag(throwable.getMessage());
                    }
                })
                .subscribe(new Action1<List<FileDescriptor>>() {
                    @Override
                    public void call(List<FileDescriptor> fileDescriptors) {
                        view.refreshWithfileList(fileDescriptors);
                    }
                });
    }
}
