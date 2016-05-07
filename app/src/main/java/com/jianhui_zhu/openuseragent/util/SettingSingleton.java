package com.jianhui_zhu.openuseragent.util;

import android.content.Context;

import com.jianhui_zhu.openuseragent.view.HomeView;

/**
 * Created by jianhuizhu on 2016-03-06.
 */
public class SettingSingleton {
    boolean allowAutomaticDownload;

    public int getThirdPartyPolicy() {
        return thirdPartyPolicy;
    }

    public void setThirdPartyPolicy(int thirdPartyPolicy) {
        this.thirdPartyPolicy = thirdPartyPolicy;
        SharePreferenceUtil.saveInteger(context,THIRD_PARTY_POLICY,thirdPartyPolicy);
    }

    private int thirdPartyPolicy;

    public String getLocationPolicy() {
        return locationPolicy;
    }

    public void setLocationPolicy(String locationPolicy) {
        this.locationPolicy = locationPolicy;
        SharePreferenceUtil.saveString(context,LOCATION_POLICY,locationPolicy);
    }

    public String getCookiePolicy() {
        return cookiePolicy;
    }

    public void setCookiePolicy(String cookiePolicy) {
        this.cookiePolicy = cookiePolicy;
        SharePreferenceUtil.saveString(context,COOKIE_POLICY,cookiePolicy);
    }



    public String getFullScreenPolicy() {
        return fullScreenPolicy;
    }

    public void setFullScreenPolicy(String fullScreenPolicy) {
        this.fullScreenPolicy = fullScreenPolicy;
        SharePreferenceUtil.saveString(context,FULL_SCREEN_POLICY,fullScreenPolicy);
    }

    public String getPopupPolicy() {
        return popupPolicy;
    }

    public void setPopupPolicy(String popupPolicy) {
        this.popupPolicy = popupPolicy;
        SharePreferenceUtil.saveString(context,POPUP_POLICY,popupPolicy);
    }

    private  String cookiePolicy;
    private  String fullScreenPolicy;
    private String popupPolicy;
    private String locationPolicy;
    private String jsPolicy;
    public static final String HOME_PAGE = "HOME_PAGE";
    public static final String SEARCH_ENGINE = "SEARCH_ENGINE";
    public static final String NOT_FOUND = "NOT_FOUND";
    public static final String THIRD_PARTY_POLICY = "THIRD_PARTY_POLICY";
    public static final String COOKIE_POLICY = "COOKIE_POLICY";
    public static final String FULL_SCREEN_POLICY = "FULL_SCREEN_POLICY";
    public static final String POPUP_POLICY = "POPUP_POLICY";
    public static final String LOCATION_POLICY = "LOCATION_POLICY";
    public static final String JS_POLICY = "JS_POLICY";
    private static SettingSingleton instance;
    private Context context;

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
        SharePreferenceUtil.saveString(context,HOME_PAGE,homePage);
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
        SharePreferenceUtil.saveString(context,SEARCH_ENGINE,searchEngine);
    }

    private String homePage;
    private String searchEngine;
    public synchronized static SettingSingleton getInstance(Context context){
        if(instance==null){
            return new SettingSingleton(context);
        }
        return instance;
    }

    public String getJsPolicy() {
        return jsPolicy;
    }

    public void setJsPolicy(String jsPolicy) {
        this.jsPolicy = jsPolicy;
        SharePreferenceUtil.saveString(context,JS_POLICY,jsPolicy);
    }

    private SettingSingleton(Context context) {
        this.context=context;
        instance =this;
        if(SharePreferenceUtil.getStringByKey(context,HOME_PAGE).equals(NOT_FOUND)){
            SharePreferenceUtil.saveString(context,HOME_PAGE,"https://www.google.com");
        }
        if(SharePreferenceUtil.getStringByKey(context,SEARCH_ENGINE).equals(NOT_FOUND)){
            SharePreferenceUtil.saveString(context,SEARCH_ENGINE,Constant.GOOGLE_PREFIX);
        }
        if(SharePreferenceUtil.getIntegerByKey(context,THIRD_PARTY_POLICY)==-1){
            SharePreferenceUtil.saveInteger(context,THIRD_PARTY_POLICY, HomeView.CustomWebViewClient.ALLOW_ALL);
        }
        if(SharePreferenceUtil.getStringByKey(context,COOKIE_POLICY).equals(NOT_FOUND)){
            SharePreferenceUtil.saveString(context,COOKIE_POLICY,Constant.COOKIE_ALLOW_ALL);
        }
        if(SharePreferenceUtil.getStringByKey(context,FULL_SCREEN_POLICY).equals(NOT_FOUND)){
            SharePreferenceUtil.saveString(context,FULL_SCREEN_POLICY,Constant.FULL_SCREEN_ALLOW_ALL);
        }
        if(SharePreferenceUtil.getStringByKey(context,POPUP_POLICY).equals(NOT_FOUND)){
            SharePreferenceUtil.saveString(context,POPUP_POLICY,Constant.POPUP_BLOCK_ALL);
        }
        if(SharePreferenceUtil.getStringByKey(context,LOCATION_POLICY).equals(NOT_FOUND)){
            SharePreferenceUtil.saveString(context,LOCATION_POLICY,Constant.LOCATION_BLOCK);
        }
        if(SharePreferenceUtil.getStringByKey(context,JS_POLICY).equals(NOT_FOUND)){
            SharePreferenceUtil.saveString(context,JS_POLICY,Constant.JS_ALLOW);
        }
        homePage=SharePreferenceUtil.getStringByKey(context,HOME_PAGE);
        searchEngine=SharePreferenceUtil.getStringByKey(context,SEARCH_ENGINE);
        thirdPartyPolicy=SharePreferenceUtil.getIntegerByKey(context, THIRD_PARTY_POLICY);
        cookiePolicy = SharePreferenceUtil.getStringByKey(context,COOKIE_POLICY);
        fullScreenPolicy = SharePreferenceUtil.getStringByKey(context,FULL_SCREEN_POLICY);
        popupPolicy = SharePreferenceUtil.getStringByKey(context,POPUP_POLICY);
        locationPolicy = SharePreferenceUtil.getStringByKey(context,LOCATION_POLICY);
        jsPolicy = SharePreferenceUtil.getStringByKey(context,JS_POLICY);
    }

}
