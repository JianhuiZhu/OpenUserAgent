package com.jianhui_zhu.openuseragent.util;

import android.content.Context;

/**
 * Created by jianhuizhu on 2016-03-06.
 */
public class SettingSingleton {
    private String name = "Jianhui";
    private String email="z_jianhu@encs.concordia.ca";
    private String password ="123456";
    private String username="z_jianhu";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

    private  boolean loginStatus = false;

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
        instance =this;
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
