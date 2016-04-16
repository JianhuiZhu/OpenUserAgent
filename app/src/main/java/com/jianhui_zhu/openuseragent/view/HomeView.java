package com.jianhui_zhu.openuseragent.view;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebIconDatabase;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import com.google.common.net.InternetDomainName;
import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.DownloadModel;
import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.model.beans.User;
import com.jianhui_zhu.openuseragent.presenter.HomePresenter;
import com.jianhui_zhu.openuseragent.util.AbstractFragment;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.util.RemoteDatabaseSingleton;
import com.jianhui_zhu.openuseragent.util.SettingSingleton;
import com.jianhui_zhu.openuseragent.util.WebUtil;
import com.jianhui_zhu.openuseragent.util.activity.MainActivity;
import com.jianhui_zhu.openuseragent.view.adapter.NavigationHomeAdapter;
import com.jianhui_zhu.openuseragent.view.adapter.SearchSuggestionAdapter;
import com.jianhui_zhu.openuseragent.view.adapter.WebViewAdapter;
import com.jianhui_zhu.openuseragent.view.custom.CustomDrawerLayout;
import com.jianhui_zhu.openuseragent.view.custom.CustomWebView;
import com.jianhui_zhu.openuseragent.view.dialogs.TabStackDialog;
import com.jianhui_zhu.openuseragent.view.interfaces.HomeViewInterface;
import com.squareup.picasso.Picasso;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Jianhui Zhu on 2016-01-27.
 */
public class HomeView extends AbstractFragment implements HomeViewInterface,SwipeRefreshLayout.OnRefreshListener {
    int total;
    int count;
    String curHost;
    HashMap<String,Integer> thirdPartyCounter;
    BufferedWriter recordForThirdParty;
    CoordinatorLayout container;
    @Bind(R.id.webview_holder)
    FrameLayout webviewContainer;
    User user;
    CircleImageView avatar;
    TextView homeName;
    NavigationHomeAdapter gridBookmarkAdapter;
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
    @Bind(R.id.tab)
    ImageView tabIcon;
    SearchSuggestionAdapter suggestionAdapter;
    WebViewAdapter webViewAdapter;
    @OnClick({R.id.home_menu_icon,R.id.refresh_area,R.id.tab_area,R.id.add_bookmark_area,R.id.backward_area,R.id.forward_area})
    public void click(View view) {

        switch (view.getId()) {
            case R.id.home_menu_icon:
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
                break;
            case R.id.refresh_area:
                onRefresh();
                break;
            case R.id.tab_area:
                if(webViewAdapter.isEmpty()){
                    Snackbar.make(container,"No tab exists",Snackbar.LENGTH_SHORT).show();
                }else {
                    FragmenUtil.switchToFragment(getActivity(), new TabStackDialog());
                }
                break;
            case R.id.add_bookmark_area:
                if(webHolder!=null) {
                    presenter.saveBookmark(webHolder.getUrl(), webHolder.getTitle(), user == null ? null : user.getuID());
                }
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
        avatar = (CircleImageView)profileTitle.getHeaderView(0).findViewById(R.id.home_avatar);
        homeName = (TextView)profileTitle.getHeaderView(0).findViewById(R.id.home_name);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SettingSingleton.getInstance(getActivity()).isLoginStatus()==false){
                    Picasso.with(getActivity())
                            .load(R.drawable.ic_avatar_konata)
                            .fit()
                            .into(avatar);
                    SettingSingleton.getInstance(getActivity())
                            .setLoginStatus(true);
                    Snackbar.make(container,"login successful",Snackbar.LENGTH_SHORT).show();
                    settingDrawer.closeDrawers();
                    //homeName.setText(SettingSingleton.getInstance(getActivity()).getName());
                }
            }
        });
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
                    case R.id.download_option:
                        FragmenUtil.switchToFragment(getActivity(),new DownloadView());
                        break;
                    case R.id.add_tab_option:
                        loadTargetUrl(SettingSingleton.getInstance(getActivity()).getHomePage());
                        break;
                    case R.id.sign_out_option:
                        Picasso.with(getActivity())
                                .load(R.drawable.ic_placeholder)
                                .fit()
                                .into(avatar);
                        SettingSingleton.getInstance(getActivity())
                                .setLoginStatus(false);
                        homeName.setText("Please click avatar to login");
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
        this.container=((MainActivity)getActivity()).getContainer();
    }


    @Override
    public void loadTargetUrl(String url) {
        swipeRefreshLayout.setRefreshing(true);
        webHolder = initWebView();
        if (url != null && !url.equals("")&&URLUtil.isValidUrl(url)) {
            this.webHolder.loadUrl(url);
        } else {
            this.webHolder.loadUrl(SettingSingleton.getInstance(getActivity()).getHomePage());
        }


    }

    @Override
    public void showTag(String info) {
        Snackbar.make(container,info,Snackbar.LENGTH_SHORT).show();
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
                webHolder = null;
                webviewContainer.removeAllViews();
                webviewContainer.addView(initPanelView());
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
        private boolean blockAllThirdParty;
        private boolean blockBlackList;
        private boolean isRedirect =false;
        private String host;
        public void blockAllThirdParty(){
            blockAllThirdParty = true;
            blockBlackList = false;
        }
        public void blockBlackList(){
            blockAllThirdParty = false;
            blockBlackList = true;
        }
        public void allowAllThirdParty(){
            blockAllThirdParty = false;
            blockBlackList = false;
        }
        @Override
        public void onLoadResource(WebView view, final String url) {
            final String viewUrl = view.getUrl();
            Observable.create(new Observable.OnSubscribe<Object>() {
                @Override
                public void call(Subscriber<? super Object> subscriber) {
                    String host;
                    String resourceHost;

                    host = Uri.parse(viewUrl).getHost();
                    resourceHost = Uri.parse(url).getHost();
                    if (host != null) {
                        try {
                            host = InternetDomainName.from(host).topPrivateDomain().toString();
                        }catch (Exception e){
                            Log.d(this.getClass().getSimpleName(),e.getMessage());
                        }
                    }
                    if(resourceHost!=null) {
                        try {
                            resourceHost = InternetDomainName.from(resourceHost).topPrivateDomain().toString();
                        }catch (Exception e){
                            Log.d(this.getClass().getSimpleName(),e.getMessage());
                        }
                    }
                    total++;
                    if(host!=null&&resourceHost!=null&&!host.equals(resourceHost)){
                        if(recordForThirdParty!=null){
                            try {
                                recordForThirdParty.append
                                        (resourceHost+"\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        count++;
                    }
                }
            }).subscribeOn(Schedulers.io()).subscribe();

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            isRedirect = true;
            return true;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            if(blockAllThirdParty) {
                String resourceHost = InternetDomainName.from(Uri.parse(url).getHost()).topPrivateDomain().toString();
                if (!resourceHost.equals(curHost)) {
                    return new WebResourceResponse("text/css", "UTF-8", null);
                }
            }
            return super.shouldInterceptRequest(view, url);
        }

        @Override
        public void onPageStarted(final WebView view, String url, Bitmap favicon) {
            final String viewUrl=view.getUrl();
            curHost = InternetDomainName.from(Uri.parse(viewUrl).getHost()).topPrivateDomain().toString();
            Observable.create(new Observable.OnSubscribe<Object>() {
                @Override
                public void call(Subscriber<? super Object> subscriber) {
                    try {
                        File path = Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOWNLOADS);
                        if(!path.exists()){
                            if(!path.mkdir()){
                                Log.e(this.getClass().getSimpleName(),"cannot create directory");
                            }
                        }
                        File file = new File(path,"record.txt");
                        recordForThirdParty =
                                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true)));
                        recordForThirdParty.append("\nThe target url to be visited").append(viewUrl).append("\n").append("third party web are:\n");
                        thirdPartyCounter = new HashMap<>();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).subscribeOn(Schedulers.io()).subscribe();

            swipeRefreshLayout.setRefreshing(true);
            URI uri= null;
            try {
                uri = new URI(view.getUrl());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            if (uri != null) {
                this.host =uri.getHost();
            }
            isRedirect = false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if(isRedirect==false) {
                Observable.create(new Observable.OnSubscribe<Object>() {
                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        if(recordForThirdParty!=null){
                            try {
                                recordForThirdParty.flush();
                                recordForThirdParty.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            recordForThirdParty=null;
                        }
                    }
                }).subscribeOn(Schedulers.io()).subscribe();

                System.out.println("total resource loaded"+total);
                System.out.println("third party resource loaded"+count);
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                webviewContainer.removeAllViewsInLayout();
                webviewContainer.addView(webHolder, 0);
                if (url != null && !url.equals("about:blank")) {
                    webViewAdapter.addWebView(webHolder);
                    presenter.changeNumTabsIcon(webViewAdapter.getCount());
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

    private LinearLayout initPanelView(){
        LinearLayout layout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        params.setMargins(0,8,0,0);
        layout.setLayoutParams(params);
        TextView title = new TextView(getActivity());
        title.setText("Visit Often visited website");
        title.setGravity(Gravity.CENTER);
        layout.addView(title);
        final RecyclerView bookmarkRecycler = new RecyclerView(getActivity());
        bookmarkRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookmarkRecycler.setItemAnimator(new DefaultItemAnimator());
        bookmarkRecycler.setHasFixedSize(true);
        gridBookmarkAdapter = new NavigationHomeAdapter(presenter);
        presenter.getNavigationBookmark().subscribe(new Action1<List<Bookmark>>() {
            @Override
            public void call(List<Bookmark> bookmarks) {
                gridBookmarkAdapter.setBookmarks(bookmarks);
                bookmarkRecycler.setAdapter(gridBookmarkAdapter);
            }
        });
        layout.addView(bookmarkRecycler);
        return layout;
    }

    private CustomWebView initWebView(){
        CustomWebView web = new CustomWebView(getActivity());
        WebViewClient client = new CustomWebViewClient();
        web.setWebViewClient(client);
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
        if(webHolder==null||webViewAdapter.isEmpty()) {
            webviewContainer.removeAllViews();
            webviewContainer.addView(initPanelView());
            configViews();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
