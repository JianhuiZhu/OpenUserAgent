package com.jianhui_zhu.openuseragent.model;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.jianhui_zhu.openuseragent.model.beans.FileDescriptor;
import com.jianhui_zhu.openuseragent.presenter.DownloadPresenter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
/**
 * Created by jianhuizhu on 2016-04-08.
 */
public class DownloadModel {
    private boolean ALLOWED_NO_WIFI_DOWNLOAD = false;
    private static Context context;
    private static DownloadModel instance;
    private String downloadPath;
    private static DownloadPresenter presenter;
    public DownloadModel(Context context,DownloadPresenter presenter) throws IOException {
        if(instance==null) {
            DownloadModel.context = context;
            DownloadModel.presenter = presenter;
            this.downloadPath = context.getDir("downloads",Context.MODE_PRIVATE).getAbsolutePath()+File.separator;
            File downloadFolder = new File(downloadPath);
            if(!downloadFolder.exists()||!downloadFolder.isDirectory()){

                if(!downloadFolder.mkdir()){
                    throw new IOException("Cannot create folder in such directory");
                }
            }
            instance = this;
        }
    }
    private String getFileType(final String filename){
        int pos = filename.lastIndexOf('.');
        if (pos != -1) {
            String ext = filename.substring(filename.lastIndexOf('.') + 1,filename.length());
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        }else{
            return "unknown";
        }
    }
    public Observable<List<FileDescriptor>> getAllFilesInDownloadFolder(){

        return Observable.create(new Observable.OnSubscribe<List<FileDescriptor>>() {
            @Override
            public void call(Subscriber<? super List<FileDescriptor>> subscriber) {
                File downloadFolder = new File(downloadPath);
                if(!downloadFolder.exists()||!downloadFolder.isDirectory()){
                    if(!downloadFolder.mkdir()){
                        subscriber.onError(new Exception("Cannot create folder in such directory"));
                    }
                }
                File[] files = downloadFolder.listFiles();
                if(files==null){
                    subscriber.onError(new Exception("this is not a directory"));
                }else {
                    List<FileDescriptor> result = new ArrayList<>();
                    for (File file : files) {
                        FileDescriptor descriptor = new FileDescriptor();
                        descriptor.setName(file.getName());
                        descriptor.setPath(file.getAbsolutePath());
                        /**
                        TODO: get the size and permission of the file,if possible, get the icon of the file
                         */
                        descriptor.setFileType(getFileType(file.getName()));
                        result.add(descriptor);
                    }
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Long> saveFileToDownloadFolder(final String url){
        return Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                DownloadManager manager =(DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                if(ALLOWED_NO_WIFI_DOWNLOAD){
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
                }
                long id = manager.enqueue(request);
                subscriber.onNext(id);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

}
