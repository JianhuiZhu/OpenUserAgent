package com.jianhui_zhu.openuseragent.util;

import android.content.Context;

/**
 * Created by jianhuizhu on 2016-03-06.
 */
public class SettingSingleton {
    private static SettingSingleton instance;
    private Context context;

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
        SharePreferenceUtil.saveString(context,"HOME_PAGE",homePage);
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
        SharePreferenceUtil.saveString(context,"SEARCH_ENGINE",searchEngine);
    }

    private String homePage;
    private String searchEngine;
    public static SettingSingleton getInstance(Context context){
        if(instance==null){
            return new SettingSingleton(context);
        }
        return instance;
    }

    private SettingSingleton(Context context) {
        this.context=context;
        if(SharePreferenceUtil.getByKey(context,"HOME_PAGE").equals("NOT_FOUND")){
            SharePreferenceUtil.saveString(context,"HOME_PAGE","https://www.google.com");

        }
        if(SharePreferenceUtil.getByKey(context,"SEARCH_ENGINE").equals("NOT_FOUND")){
            SharePreferenceUtil.saveString(context,"SEARCH_ENGINE",Constant.GOOGLE_PREFIX);

        }
        homePage=SharePreferenceUtil.getByKey(context,"HOME_PAGE");
        searchEngine=SharePreferenceUtil.getByKey(context,"SEARCH_ENGINE");
    }

}
