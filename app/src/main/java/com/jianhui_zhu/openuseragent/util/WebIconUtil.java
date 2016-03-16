package com.jianhui_zhu.openuseragent.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.observables.AsyncOnSubscribe;
import rx.schedulers.Schedulers;

/**
 * Created by jianhuizhu on 2016-03-13.
 */
public class WebIconUtil {
    private String DEFAULT_IMAGE_SUFFIX=".png";
    private String DEFAULT_ICON_FOLDER="icons";
    ContextWrapper cw;
    Context context;
    private static WebIconUtil instance;
    private WebIconUtil(Context context){
        this.context=context;
        this.cw=new ContextWrapper(context);
    }
    private boolean isIconExist(String name){

            String path = context.getDir("icons",Context.MODE_PRIVATE).getAbsolutePath()+File.separator;
            File icon=new File(path+name+DEFAULT_IMAGE_SUFFIX);
            boolean isExist=icon.exists();
            return isExist;
    }

    public static void instantiate(Context context){
        WebIconUtil.instance=new WebIconUtil(context);
    }
    public static WebIconUtil getInstance(){
        if(instance==null){
            Log.e(WebIconUtil.class.getSimpleName(),"Web Icon manager have not been instantiated");
        }
        return instance;
    }
    public Observable<Bitmap> getIconByName(final String name){
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                String path = context.getDir("icons",Context.MODE_PRIVATE).getAbsolutePath()+File.separator+name+DEFAULT_IMAGE_SUFFIX;
                try {
                    Bitmap bitmap= BitmapFactory.decodeStream(new FileInputStream(new File(path)));
                    if(bitmap!=null){
                        subscriber.onNext(bitmap);
                        subscriber.onCompleted();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
    public void setIcon(final Bitmap bitmap, final String name){
        if(!isIconExist(name)) {
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    File directory = context.getDir("icons",Context.MODE_PRIVATE);

                    File path = new File(directory, name + DEFAULT_IMAGE_SUFFIX);
                    FileOutputStream fout = null;
                    try {
                        fout = new FileOutputStream(path);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
                        subscriber.onNext("done");
                        subscriber.onCompleted();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (fout != null) {
                                fout.flush();
                                fout.close();
                                subscriber.onNext("done");
                                subscriber.onCompleted();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).subscribeOn(Schedulers.io()).subscribe();
        }
    }
    public File getIconByNameAsFile(final String name) {
        String path=cw.getDir(DEFAULT_ICON_FOLDER, Context.MODE_PRIVATE).getPath()+ File.separator+name+DEFAULT_IMAGE_SUFFIX;
        return new File(path);
    }
}
