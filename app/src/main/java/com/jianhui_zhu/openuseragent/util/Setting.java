package com.jianhui_zhu.openuseragent.util;

import android.content.Context;

/**
 * Created by jianhuizhu on 2016-03-06.
 */
public class Setting {
    public static final String GOOGLE_PREFIX ="https://www.google.com/search?q=";
    public static final String BAIDU_PREFIX ="http://www.baidu.com/s?wd=";
    public static final String BING_PREFIX="http://www.bing.com/search?q=";

    public static final String COOKIE_ALLOW_ALL = "ALLOW_ALL_COOKIES";
    public static final String COOKIE_ALLOW_FIRST_PARTY = "ALLOW_FIRST_PARTY_COOKIES";
    public static final String COOKIE_NONE = "BLOCK_ALL_COOKIES";

    public static final String FULL_SCREEN_ALLOW_ALL = "FULL_SCREEN_ALLOW_ALL";
    public static final String FULL_SCREEN_BLOCK_ALL = "TO_BE_IMPLEMENTED";
    public static final String FULL_SCREEN_ASK = "FULL_SCREEN_ASK";

    public static final String POPUP_ALLOW_ALL = "POPUP_ALLOW_ALL";
    public static final String POPUP_BLOCK_ALL = "POPUP_BLOCK_ALL";

    public static final String LOCATION_BLOCK ="BLOCK_OTHER_TO_BE_IMPLEMENTED";
    public static final String LOCATION_ALLOW = "ALLOW";
    public static final String LOCATION_ASK = "ASK_USER";

    public static final String JS_ALLOW = "ALLOW_JAVASCRIPT";
    public static final String JS_BLOCK = "BLOCK_JAVASCRIPT";

    public static final int ALLOW_ALL = 0;
    public static final int BLOCK_BLACK_LIST = 1;
    public static final int BLOCK_ALL_THIRD_PARTY = 2;
    boolean allowAutomaticDownload;
    public static void instantiate(Context context){
        new Setting(context);
    }
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
    private static Setting instance;
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
    public synchronized static Setting getInstance(){
        return instance;
    }

    public String getJsPolicy() {
        return jsPolicy;
    }

    public void setJsPolicy(String jsPolicy) {
        this.jsPolicy = jsPolicy;
        SharePreferenceUtil.saveString(context,JS_POLICY,jsPolicy);
    }

    private Setting(Context context) {
        this.context=context;
        instance =this;
        if(SharePreferenceUtil.getStringByKey(context,HOME_PAGE).equals(NOT_FOUND)){
            SharePreferenceUtil.saveString(context,HOME_PAGE,"https://www.google.com");
        }
        if(SharePreferenceUtil.getStringByKey(context,SEARCH_ENGINE).equals(NOT_FOUND)){
            SharePreferenceUtil.saveString(context,SEARCH_ENGINE,GOOGLE_PREFIX);
        }
        if(SharePreferenceUtil.getIntegerByKey(context,THIRD_PARTY_POLICY)==-1){
            SharePreferenceUtil.saveInteger(context,THIRD_PARTY_POLICY, ALLOW_ALL);
        }
        if(SharePreferenceUtil.getStringByKey(context,COOKIE_POLICY).equals(NOT_FOUND)){
            SharePreferenceUtil.saveString(context,COOKIE_POLICY,COOKIE_ALLOW_ALL);
        }
        if(SharePreferenceUtil.getStringByKey(context,FULL_SCREEN_POLICY).equals(NOT_FOUND)){
            SharePreferenceUtil.saveString(context,FULL_SCREEN_POLICY,FULL_SCREEN_BLOCK_ALL);
        }
        if(SharePreferenceUtil.getStringByKey(context,POPUP_POLICY).equals(NOT_FOUND)){
            SharePreferenceUtil.saveString(context,POPUP_POLICY,POPUP_BLOCK_ALL);
        }
        if(SharePreferenceUtil.getStringByKey(context,LOCATION_POLICY).equals(NOT_FOUND)){
            SharePreferenceUtil.saveString(context,LOCATION_POLICY,LOCATION_BLOCK);
        }
        if(SharePreferenceUtil.getStringByKey(context,JS_POLICY).equals(NOT_FOUND)){
            SharePreferenceUtil.saveString(context,JS_POLICY,JS_ALLOW);
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
