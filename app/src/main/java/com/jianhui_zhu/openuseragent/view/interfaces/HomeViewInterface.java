package com.jianhui_zhu.openuseragent.view.interfaces;

import android.database.Cursor;
import android.webkit.WebView;

import com.jianhui_zhu.openuseragent.view.custom.CustomWebView;

/**
 * Created by jianhuizhu on 2016-02-09.
 */
public interface HomeViewInterface {
    void loadTargetUrl(String url,boolean newTabFlag);
    void showTag(String info);
    void swapCursor(Cursor cursor);
    void searchTargetWord(String word);
    void changeToolBarVisibility(int VIEW_CODE);
    void hideKeyboard();
    void changeNumTabsIcon(int num);
    void changeWebView(CustomWebView webView);
    void clearWebHolder();
    void changeTextInSearchBar(String url);
}
