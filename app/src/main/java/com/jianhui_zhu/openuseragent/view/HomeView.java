package com.jianhui_zhu.openuseragent.view;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebIconDatabase;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.beans.User;
import com.jianhui_zhu.openuseragent.presenter.HomePresenter;
import com.jianhui_zhu.openuseragent.util.AbstractFragment;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.util.RemoteDatabaseSingleton;
import com.jianhui_zhu.openuseragent.util.SettingSingleton;
import com.jianhui_zhu.openuseragent.util.WebUtil;
import com.jianhui_zhu.openuseragent.view.adapter.SearchSuggestionAdapter;
import com.jianhui_zhu.openuseragent.view.adapter.WebViewAdapter;
import com.jianhui_zhu.openuseragent.view.custom.CustomDrawerLayout;
import com.jianhui_zhu.openuseragent.view.custom.CustomWebView;
import com.jianhui_zhu.openuseragent.view.dialogs.TabStackDialog;
import com.jianhui_zhu.openuseragent.view.interfaces.HomeViewInterface;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Jianhui Zhu on 2016-01-27.
 */
public class HomeView extends AbstractFragment implements HomeViewInterface,SwipeRefreshLayout.OnRefreshListener {
    private Bundle webViewBundle;
    @Bind(R.id.webview_holder)
    FrameLayout webviewContainer;
    User user;
    private boolean drawerIsOpen = false;
    @Bind(R.id.tool_bar_area)
    PercentRelativeLayout toolBarArea;
    boolean isHomePage=true;
    CustomWebView webHolder;
    @Bind(R.id.setting_drawer)
    CustomDrawerLayout settingDrawer;
    @Bind(R.id.profile_title)
    NavigationView profileTitle;
    @Bind(R.id.home_url_bar)
    SearchView urlBar;
    @Bind(R.id.search_suggestion_list)
    ListView suggestionList;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.backward)
    ImageView backwardIcon;
    @Bind(R.id.forward)
    ImageView forwardIcon;
    @Bind(R.id.backward_area)
    RippleView backward;
    @Bind(R.id.forward_area)
    RippleView forward;
    @Bind(R.id.refresh_area)
    RippleView refresh;
    @Bind(R.id.tab_area)
    RippleView tab;
    @Bind(R.id.add_bookmark_area)
    RippleView addBookmark;
    @Bind(R.id.home_area)
    RippleView home;
    @Bind(R.id.tab)
    ImageView tabIcon;
    SearchSuggestionAdapter suggestionAdapter;
    WebViewAdapter webViewAdapter;
    @OnClick({R.id.menu_area,R.id.refresh_area,R.id.tab_area,R.id.add_bookmark_area,R.id.backward_area,R.id.forward_area})
    public void click(RippleView view) {

        switch (view.getId()) {
            case R.id.menu_area:
            if (RemoteDatabaseSingleton.getInstance().isUserLoggedIn()) {
                this.user = RemoteDatabaseSingleton.getInstance().getUser();
            }
            if (user != null) {
                ImageView avatar = (ImageView) getActivity().findViewById(R.id.home_avatar);
                TextView username = (TextView) getActivity().findViewById(R.id.home_name);
                Picasso.with(getActivity()).load(user.getAvatarUrl()).fit().into(avatar);
                username.setText(user.getUsername());
            }
            settingDrawer.openDrawer(Gravity.RIGHT);
            drawerIsOpen = true;
                break;
            case R.id.refresh_area:
                onRefresh();
                break;
            case R.id.tab_area:
                FragmenUtil.switchToFragment(getActivity(),new TabStackDialog());
                break;
            case R.id.add_bookmark_area:

                presenter.saveBookmark(webHolder.getUrl(),webHolder.getTitle(),user == null ? null : user.getuID());
                break;
            case R.id.home_area:
                String homePageUrl = SettingSingleton.getInstance(getActivity()).getHomePage();
                if(homePageUrl!=null)
                    loadTargetUrl(homePageUrl);
                break;
            case R.id.backward_area:
                if(webHolder!=null&&webHolder.canGoBack()){
                    webHolder.goBack();
                }
                break;
            case R.id.forward_area:
                if(webHolder!=null&&webHolder.canGoForward()){
                    webHolder.goForward();
                }
                break;
            default:
                Log.d("id",view.getId()+"");
        }
    }

    private HomePresenter presenter;
    public void configViews(){
        profileTitle.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.setting_option:
                        FragmenUtil.switchToFragment(getActivity(),new SettingView());
                        break;
                    case R.id.bookmark_option:
                        FragmenUtil.switchToFragment(getActivity(), new BookmarkView());
                        break;
                    case R.id.history_option:
                        FragmenUtil.switchToFragment(getActivity(), new HistoryView());
                        break;
                }
                settingDrawer.closeDrawers();
                return true;
            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);
        urlBar.setIconifiedByDefault(true);
        urlBar.setSubmitButtonEnabled(true);
        suggestionList.setAdapter(suggestionAdapter);
        urlBar.onActionViewExpanded();
        urlBar.setFocusable(false);
        urlBar.clearFocus();
        urlBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (URLUtil.isValidUrl(query)) {
                    loadTargetUrl(query);

                } else {
                    loadTargetUrl(SettingSingleton.getInstance(getActivity()).getSearchEngine() + query);

                }
                presenter.saveQuery(query);
                suggestionAdapter.changeCursor(null);
                changeToolBarVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    suggestionAdapter.changeCursor(null);
                } else {
                    presenter.queryText(newText);
                }
                return true;
            }
        });
        boolean hasUrl = getArguments().getBoolean("hasUrl");
        if (hasUrl) {
            loadTargetUrl(getArguments().getString("url"));
        } else {
            loadTargetUrl("");
        }
        settingDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity();
        presenter = new HomePresenter(this, getActivity());
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                String path=getActivity().getDir("icons", Context.MODE_PRIVATE).getPath();
                WebIconDatabase.getInstance().open(path);
                suggestionAdapter = new SearchSuggestionAdapter(getActivity(), presenter);
                webViewAdapter =WebViewAdapter.getInstance(getActivity());
                webViewAdapter.setHomePresenter(presenter);
                if (RemoteDatabaseSingleton.getInstance().isUserLoggedIn()) {
                    user = RemoteDatabaseSingleton.getInstance().getUser();
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_home, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(webHolder==null) {
            webHolder = initWebView();
            webHolder.setDrawingCacheEnabled(true);
            configViews();
        }

    }


    @Override
    public void loadTargetUrl(String url) {
        swipeRefreshLayout.setRefreshing(true);
        if (url != null && !url.equals("")) {
            this.webHolder.loadUrl(url);
        } else {
            this.webHolder.loadUrl(SettingSingleton.getInstance(getActivity()).getHomePage());
        }


    }

    @Override
    public void showTag(String info) {
        Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void swapCursor(Cursor cursor) {
        suggestionAdapter.changeCursor(cursor);
        suggestionAdapter.notifyDataSetChanged();
    }

    @Override
    public void searchTargetWord(String word) {
        loadTargetUrl(SettingSingleton.getInstance(getActivity()).getSearchEngine() + word);
    }

    @Override
    public void changeToolBarVisibility(int VIEW_CODE) {
//        switch (VIEW_CODE){
//            case View.VISIBLE:
//                toolBarArea.setVisibility(View.VISIBLE);
//                break;
//            case View.GONE:
//                toolBarArea.setVisibility(View.GONE);
//                break;
//        }
    }

    @Override
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(urlBar.getWindowToken(), 0);
    }

    @Override
    public void changeNumTabsIcon(int num) {
        switch(num){
            case 0:
                tabIcon.setImageResource(R.drawable.ic_tab);
                break;
            case 1:
                tabIcon.setImageResource(R.drawable.ic_tab_1);
                break;
            case 2:
                tabIcon.setImageResource(R.drawable.ic_tab_2);
                break;
            case 3:
                tabIcon.setImageResource(R.drawable.ic_tab_3);
                break;
            case 4:
                tabIcon.setImageResource(R.drawable.ic_tab_4);
                break;
            case 5:
                tabIcon.setImageResource(R.drawable.ic_tab_5);
                break;
            case 6:
                tabIcon.setImageResource(R.drawable.ic_tab_6);
                break;
            case 7:
                tabIcon.setImageResource(R.drawable.ic_tab_7);
                break;
            case 8:
                tabIcon.setImageResource(R.drawable.ic_tab_8);
                break;
            case 9:
                tabIcon.setImageResource(R.drawable.ic_tab_9);
                break;
            default:
                tabIcon.setImageResource(R.drawable.ic_tab_more);
                break;
        }
    }

    @Override
    public void changeWebView(CustomWebView webView) {
        webHolder = webView;
    }

    @Override
    public void clearWebHolder() {

        webviewContainer.removeAllViews();
        webHolder = null;
    }

    @Override
    public void onRefresh() {
        webHolder.reload();
    }


    private class CustomWebViewClient extends WebViewClient {
        private boolean isRedirect =false;
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            isRedirect = true;
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            swipeRefreshLayout.setRefreshing(true);
            isRedirect = false;
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            if(isRedirect==false) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                if (url != null && !url.equals("about:blank")) {
                    webViewAdapter.addWebView(webHolder);
                    presenter.saveHistory(url, view.getTitle());
                }
                if (webHolder != null && webHolder.canGoBack() == false) {
                    backwardIcon.setColorFilter(R.color.cardview_shadow_end_color, PorterDuff.Mode.MULTIPLY);
                } else {
                    backwardIcon.clearColorFilter();
                }
                if (webHolder != null && webHolder.canGoForward() == false) {
                    forwardIcon.setColorFilter(R.color.cardview_shadow_end_color, PorterDuff.Mode.MULTIPLY);
                } else {
                    forwardIcon.clearColorFilter();
                }
            }
        }


    }

    private class CustomWebChrome extends WebChromeClient {

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
            try {
                URL uri=new URL(view.getUrl()) ;
                String host=uri.getHost();
                WebUtil webUtil = WebUtil.getInstance();
                webUtil.setIcon(icon,host);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public static AbstractFragment newInstance() {
        HomeView homeView = new HomeView();
        Bundle bundle = new Bundle();
        bundle.putBoolean("hasUrl", false);
        homeView.setArguments(bundle);
        return homeView;
    }

    public static AbstractFragment newInstanceWithUrl(String url) {
        HomeView homeView = new HomeView();
        Bundle bundle = new Bundle();
        bundle.putBoolean("hasUrl", true);
        bundle.putString("url", url);
        homeView.setArguments(bundle);
        return homeView;
    }

    private void swapWebView(){
            CustomWebView web = initWebView();
            if(webHolder!=null&&webHolder.getHeight()>0&&webHolder.getWidth()>0){
                webViewAdapter.addWebView(webHolder);
            }
            webHolder=web;
    }

    private CustomWebView initWebView(){
        CustomWebView web = new CustomWebView(getActivity());
        WebViewClient client = new CustomWebViewClient();

        WebChromeClient chromeClient = new CustomWebChrome();
        web.setWebViewClient(client);
        web.setSaveEnabled(true);
        web.setWebChromeClient(chromeClient);
        web.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        FrameLayout.LayoutParams para = new FrameLayout
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        para.setMargins(0, 0, 0, 0);
        web.setLayoutParams(para);
        web.setHomeViewInterface(this);
        webviewContainer.removeAllViewsInLayout();
        webviewContainer.addView(web, 0);
        backwardIcon.setColorFilter(R.color.cardview_shadow_end_color,PorterDuff.Mode.MULTIPLY);
        forwardIcon.setColorFilter(R.color.cardview_shadow_end_color,PorterDuff.Mode.MULTIPLY);
        return web;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        webHolder.destroy();
    }
}
