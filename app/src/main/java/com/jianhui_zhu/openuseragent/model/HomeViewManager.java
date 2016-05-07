package com.jianhui_zhu.openuseragent.model;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.model.beans.History;
import com.jianhui_zhu.openuseragent.util.Constant;
import com.jianhui_zhu.openuseragent.util.LocalDatabaseSingleton;
import com.jianhui_zhu.openuseragent.util.TrafficStatisticUtil;
import com.jianhui_zhu.openuseragent.view.HomeView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by jianhuizhu on 2016-02-16.
 */
public class HomeViewManager {
    public static final String BLACK_LIST_DIR = "blacklist";

    public void incrementRecordLocally(int id,int count){
        LocalDatabaseSingleton.getInstance().incrementQueryRecordCount(id,count);
    }
    public Observable<Cursor> queryText(String text){
        return LocalDatabaseSingleton.getInstance().getQueryRecord(text);
    }
    public void saveQueryText(String text){
        LocalDatabaseSingleton.getInstance().saveQueryRecord(text);
    }
    public void initGlobalBlackList(final Set<String> globalBlackList){
        LocalDatabaseSingleton.getInstance().getBlackList().subscribe(new Action1<Set<String>>() {
            @Override
            public void call(Set<String> strings) {
                globalBlackList.addAll(strings);
            }
        });
    }
    public void startRecord(final BufferedWriter recordForThirdParty, final String viewUrl){

        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {

                try {
                    recordForThirdParty.append("\nThe target url to be visited").append(viewUrl).append("\n").append("third party web are:\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }
    public void flushRecord(BufferedWriter recordForThirdParty,HashMap<String,Integer> thirdPartyCounter,int curpolicy,int total,int count,long startTime){
        try {
            for(Map.Entry<String,Integer> entry: thirdPartyCounter.entrySet()){
                recordForThirdParty.append(entry.getKey()+" "+entry.getValue()+"\n");
            }
            recordForThirdParty.append("Policy used: ");
            switch (curpolicy){
                case Constant.ALLOW_ALL:
                    recordForThirdParty.append("ALLOW ALL CONTENT\n");
                    break;
                case Constant.BLOCK_BLACK_LIST:
                    recordForThirdParty.append("BLOCK BLACK LIST\n");
                    break;
                case Constant.BLOCK_ALL_THIRD_PARTY:
                    recordForThirdParty.append("BLOCK ALL THIRD PARTY\n");
                    break;
            }
            recordForThirdParty.append("Time comsume to load the page: "+(System.currentTimeMillis()-startTime)+" milliseconds\n");
            recordForThirdParty.append("total resource requested: "+total+"  third party count: "+count+"\n");
            recordForThirdParty.append("Total bandwidth consumed: "+ TrafficStatisticUtil.getInstance().getCurTotalBytesConsume()+" Bytes\n");
            thirdPartyCounter.clear();
            recordForThirdParty.flush();
            recordForThirdParty.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Observable<String> addToGlobalBlackList(Set<String> domains,Set<String>oldDomains){
        domains.removeAll(oldDomains);
        return LocalDatabaseSingleton.getInstance().addToBlackList(domains);
    }
    public Observable<String> removeFromGlobalBlackList(Set<String> domains){
        return LocalDatabaseSingleton.getInstance().removeFromGlobalBlackList(domains);
    }
    public Observable<String> deleteAllFromGlobalBlackList(){
        return LocalDatabaseSingleton.getInstance().deleteAllFromBlackList();
    }
}
