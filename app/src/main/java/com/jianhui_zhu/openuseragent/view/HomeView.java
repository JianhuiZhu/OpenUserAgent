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
import android.support.v4.app.Fragment;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;
import com.google.common.net.InternetDomainName;
import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.BookmarkManager;
import com.jianhui_zhu.openuseragent.model.HistoryManager;
import com.jianhui_zhu.openuseragent.model.HomeViewManager;
import com.jianhui_zhu.openuseragent.util.Constant;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.util.RxBus;
import com.jianhui_zhu.openuseragent.util.Setting;
import com.jianhui_zhu.openuseragent.util.WebUtil;
import com.jianhui_zhu.openuseragent.util.activity.MainActivity;
import com.jianhui_zhu.openuseragent.util.event.GlobalBlackListEvent;
import com.jianhui_zhu.openuseragent.util.event.ThirdPartyTabSpecificEvent;
import com.jianhui_zhu.openuseragent.util.event.WebViewRefreshEvent;
import com.jianhui_zhu.openuseragent.view.adapter.NavigationHomeAdapter;
import com.jianhui_zhu.openuseragent.view.adapter.SearchSuggestionAdapter;
import com.jianhui_zhu.openuseragent.view.adapter.WebViewAdapter;
import com.jianhui_zhu.openuseragent.view.custom.CustomDrawerLayout;
import com.jianhui_zhu.openuseragent.view.custom.CustomWebView;
import com.jianhui_zhu.openuseragent.view.dialogs.TabStackDialog;
import com.jianhui_zhu.openuseragent.view.dialogs.ThirdPartyContentDialog;
import com.jianhui_zhu.openuseragent.view.interfaces.HomeViewInterface;
import com.jianhui_zhu.openuseragent.viewmodel.HomeViewModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Jianhui Zhu on 2016-01-27.
 */
public class HomeView extends Fragment implements HomeViewInterface,SwipeRefreshLayout.OnRefreshListener,FragmentBackHandler{
    private HashSet<String> globalBlackList = new HashSet<>();
    HomeView homeView;
    HistoryManager historyManager = new HistoryManager();
    BookmarkManager bookmarkManager = new BookmarkManager();
    HomeViewManager homeViewManager = new HomeViewManager();
    HomeViewModel viewModel = new HomeViewModel();
    CoordinatorLayout container;
    @Bind(R.id.webview_holder)
    FrameLayout webviewContainer;
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
    @Bind(R.id.refresh)
            ImageView refreshIcon;
    @Bind(R.id.add_bookmark)
            ImageView addBookmarkIcon;
    SearchSuggestionAdapter suggestionAdapter;
    WebViewAdapter webViewAdapter;
    CompositeSubscription compositeSubscription = new CompositeSubscription();
    @OnClick({R.id.home_menu_icon,R.id.refresh_area,R.id.tab,R.id.add_bookmark,R.id.backward,R.id.forward})
    public void click(final View view) {

        switch (view.getId()) {
            case R.id.home_menu_icon:
                final TextView title = (TextView)view.getRootView().findViewById(R.id.third_party_title);
                SeekBar thirdPartySeekBar = (SeekBar) view.getRootView().findViewById(R.id.third_party_switch);
                final TextView jsTitle = (TextView)view.getRootView().findViewById(R.id.js_tab_setting_title);
                Switch jsSwitch = (Switch)view.getRootView().findViewById(R.id.js_switch);
                if(webHolder!=null) {
                    title.setVisibility(View.VISIBLE);
                    switch (Setting.getInstance().getThirdPartyPolicy()){
                        case Constant.ALLOW_ALL:
                            title.setText(R.string.allow_all);
                            thirdPartySeekBar.setProgress(Constant.ALLOW_ALL);
                            break;
                        case Constant.BLOCK_BLACK_LIST:
                            title.setText(R.string.block_blacklist);
                            thirdPartySeekBar.setProgress(Constant.BLOCK_BLACK_LIST);
                            break;
                        case Constant.BLOCK_ALL_THIRD_PARTY:
                            title.setText(R.string.block_all_third_party);
                            thirdPartySeekBar.setProgress(Constant.BLOCK_ALL_THIRD_PARTY);
                            break;
                    }
                    thirdPartySeekBar.setVisibility(View.VISIBLE);
                    jsTitle.setVisibility(View.VISIBLE);
                    jsSwitch.setVisibility(View.VISIBLE);
                    String jsSetting = Setting.getInstance().getJsPolicy();
                    if(jsSetting.equals(Constant.JS_ALLOW)){
                        jsSwitch.setChecked(true);
                        jsTitle.setText(Constant.JS_ALLOW);
                    }else{
                        jsSwitch.setChecked(false);
                        jsTitle.setText(Constant.JS_BLOCK);
                    }
                    jsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                jsTitle.setText(Constant.JS_ALLOW);
                            }else{
                                jsTitle.setText(Constant.JS_BLOCK);
                            }
                            webHolder.changeJsSetting(isChecked);
                        }
                    });
                    final Button entry = (Button)view.getRootView().findViewById(R.id.third_party_dialog_entry);
                    entry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String host = webHolder.getClient().getCurHost();
                            HashMap<String,Boolean> tabPolicy = webHolder.getClient().getTabPolicy();
                            ThirdPartyContentDialog dialog = ThirdPartyContentDialog.newInstance(globalBlackList,tabPolicy,host);
                            FragmenUtil.switchToFragment(getActivity(),dialog);
                            settingDrawer.closeDrawers();
                        }
                    });
                    if(webHolder.getClient().getCurrentPolicy()!=Constant.BLOCK_BLACK_LIST){
                        entry.setClickable(false);
                        entry.setTextColor(getResources().getColor(R.color.mdtp_light_gray));
                    }
                    thirdPartySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            switch (progress){
                                case Constant.ALLOW_ALL:
                                    title.setText(R.string.allow_all);
                                    break;
                                case Constant.BLOCK_BLACK_LIST:
                                    title.setText(R.string.block_blacklist);
                                    break;
                                case Constant.BLOCK_ALL_THIRD_PARTY:
                                    title.setText(R.string.block_all_third_party);
                                    break;
                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            int progress = seekBar.getProgress();
                            webHolder.getClient().changePolicy(progress);
                            viewModel.changeButtonText(getActivity(),progress,webHolder.getClient().getTabPolicy(),entry);
                        }
                    });
                    int currentPolicy = webHolder.getClient().getCurrentPolicy();
                    viewModel.changeButtonText(getActivity(),currentPolicy,webHolder.getClient().getTabPolicy(),entry);

                }else{
                    title.setText("NO TAB OPEN");
                    thirdPartySeekBar.setVisibility(View.GONE);
                    jsTitle.setVisibility(View.GONE);
                    jsSwitch.setVisibility(View.GONE);
                }
                settingDrawer.openDrawer(Gravity.RIGHT);
                break;
            case R.id.refresh:
                onRefresh();
                break;
            case R.id.tab:
                if(webViewAdapter.getItemCount()==0){
                    Snackbar.make(container,"No tab exists",Snackbar.LENGTH_SHORT).show();
                }else {
                    FragmenUtil.switchToFragment(getActivity(), TabStackDialog.newInstance(homeView));
                }
                break;
            case R.id.add_bookmark:
                if(webHolder!=null) {
                    viewModel.saveBookmark(bookmarkManager.saveBookmark(webHolder.getUrl(), webHolder.getTitle()),container);
                }
                break;
            case R.id.home_area:
                String homePageUrl = Setting.getInstance().getHomePage();
                if(homePageUrl!=null)
                    loadTargetUrl(homePageUrl,false);
                break;
            case R.id.backward:
                if(webHolder!=null&&webHolder.canGoBack()){
                    webHolder.goBack();
                }
                break;
            case R.id.forward:
                if(webHolder!=null&&webHolder.canGoForward()){
                    webHolder.goForward();
                }
                break;
            default:
                Log.d("id",view.getId()+"");
        }
    }

    public void configViews(){
        profileTitle.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.setting_option:
                        FragmenUtil.switchToFragment(getActivity(),new SettingView());
                        break;
                    case R.id.bookmark_option:
                        FragmenUtil.switchToFragment(getActivity(), BookmarkView.newInstance(homeView));
                        break;
                    case R.id.history_option:
                        FragmenUtil.switchToFragment(getActivity(), HistoryView.newInstance(homeView));
                        break;
                    case R.id.download_option:
                        FragmenUtil.switchToFragment(getActivity(),new DownloadView());
                        break;
                    case R.id.add_tab_option:
                        loadTargetUrl(Setting.getInstance().getHomePage(),true);
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
                    loadTargetUrl(query,false);
                } else {
                    String url = Setting.getInstance().getSearchEngine() + query;
                    loadTargetUrl(url,false);
                }
                homeViewManager.saveQueryText(query);
                suggestionAdapter.changeCursor(null);
                changeToolBarVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(urlBar.getWindowToken(), 0);
                urlBar.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    suggestionAdapter.changeCursor(null);
                } else {
                    viewModel.queryText(homeViewManager.queryText(newText),homeView);
                }
                return true;
            }
        });

        settingDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                String path=getActivity().getDir("icons", Context.MODE_PRIVATE).getPath();
                WebIconDatabase.getInstance().open(path);
                suggestionAdapter = new SearchSuggestionAdapter(getActivity());
                webViewAdapter = WebViewAdapter.getInstance(homeView);

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
        homeViewManager.initGlobalBlackList(globalBlackList);
        Subscription subscription = RxBus.getInstance().toObserverable().subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                if(o instanceof WebViewRefreshEvent){
                    if(webHolder!=null){
                        webHolder.reload();
                    }
                }
            }
        });
        compositeSubscription.add(subscription);
    }


    @Override
    public void loadTargetUrl(String url,boolean newTabFlag) {
        swipeRefreshLayout.setRefreshing(true);
        if(webHolder==null||newTabFlag) {
            webHolder = initWebView();
        }
        if (url != null && !url.equals("")&&URLUtil.isValidUrl(url)) {
            this.webHolder.loadUrl(url);
            urlBar.setQuery(url,false);
        } else {
            //this.webHolder.loadUrl(Setting.getInstance().getHomePage());
            this.webHolder.loadUrl(Setting.getInstance().getSearchEngine()+url);
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
        loadTargetUrl(Setting.getInstance().getSearchEngine() + word,false);
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
        webHolder.destroy();
        webHolder = null;
    }

    @Override
    public void changeTextInSearchBar(String url) {
        urlBar.setQuery(url,false);
    }

    @Override
    public void onRefresh() {
        webHolder.reload();
    }

    @Override
    public boolean onBackPressed() {
       if(webHolder!=null&&webHolder.canGoBack()){
           webHolder.goBack();
           return true;
       }else{
           return BackHandlerHelper.handleBackPress(this);
       }
    }


    public class CustomWebViewClient extends WebViewClient {
        public int getCurrentPolicy() {
            return currentPolicy;
        }
        private boolean firstLoad = true;
        private int currentPolicy = 0;

        private CompositeSubscription compositeSubscription;
        private long startTime;
        private boolean isRedirect =false;
        String curHost;
        int total;
        int count;
        public void changePolicy(int policy){
            if(policy<=2&&policy>=0){
                if(currentPolicy!=policy) {
                    currentPolicy = policy;
                    if(webHolder!=null) {
                        webHolder.reload();
                    }
                }
            }
        }
        public String getCurHost() {
            return curHost;
        }
        HashMap<String,Integer> thirdPartyCounter = new HashMap<>();
        BufferedWriter recordForThirdParty;
        public HashMap<String, Boolean> getTabPolicy() {
            return tabPolicy;
        }

        private HashMap<String,Boolean> tabPolicy = new HashMap<>();
        public void unsubscribe(){
            compositeSubscription.unsubscribe();
        }
        private void init(){
            Observable.create(new Observable.OnSubscribe<Object>() {
                @Override
                public void call(Subscriber<? super Object> subscriber) {
                    for(String domain : globalBlackList){
                        tabPolicy.put(domain , true);
                    }
                }
            }).subscribeOn(Schedulers.computation()).subscribe();
            if(compositeSubscription==null) {
                compositeSubscription = new CompositeSubscription();
            }
            Subscription subscription = RxBus.getInstance().toObserverable()
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            if(o instanceof ThirdPartyTabSpecificEvent){
                                ThirdPartyTabSpecificEvent event =(ThirdPartyTabSpecificEvent)o;
                                tabPolicy.put(event.getDomain(),event.isStatus());
                                Log.d("Tab policy ",tabPolicy.toString());
                            }else if(o instanceof GlobalBlackListEvent){
                                GlobalBlackListEvent event = (GlobalBlackListEvent)o;
                                switch (event.getAction()){
                                    case GlobalBlackListEvent.ADD:
                                        viewModel.modifyGlobalBlackList(homeViewManager.addToGlobalBlackList(event.getDomains(),globalBlackList),container);
                                        globalBlackList.addAll(event.getDomains());
                                        break;
                                    case GlobalBlackListEvent.CLEAR:
                                        globalBlackList.clear();
                                        viewModel.modifyGlobalBlackList(homeViewManager.deleteAllFromGlobalBlackList(),container);
                                        break;
                                    case GlobalBlackListEvent.REMOVE:
                                        globalBlackList.removeAll(event.getDomains());
                                        viewModel.modifyGlobalBlackList(homeViewManager.removeFromGlobalBlackList(event.getDomains()),container);
                                        break;
                                }
                            }
                        }
                    });
            compositeSubscription.add(subscription);
        }

        public CustomWebViewClient(){
            super();
            init();
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
                            if(thirdPartyCounter.containsKey(resourceHost)){
                                int count = thirdPartyCounter.get(resourceHost);
                                thirdPartyCounter.put(resourceHost,(count+1));
                            }else{
                                thirdPartyCounter.put(resourceHost,1);
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
            String resourceHost;
                try {
                    resourceHost = WebUtil.getDomain(url);
                }catch (Exception e){
                    return super.shouldInterceptRequest(view, url);
                }
                if(!resourceHost.equals(curHost)) {
                    if (!tabPolicy.containsKey(resourceHost)) {
                        if (globalBlackList.contains(resourceHost)) {
                            tabPolicy.put(resourceHost, true);
                        } else {
                            tabPolicy.put(resourceHost, false);
                        }
                    }
                }
                switch (currentPolicy) {
                    case Constant.ALLOW_ALL:
                        break;
                    case Constant.BLOCK_BLACK_LIST:
                        if (tabPolicy.containsKey(resourceHost) && tabPolicy.get(resourceHost)&&!resourceHost.equals(curHost)) {
                            return new WebResourceResponse("text/css", "UTF-8", null);
                        }
                        break;
                    case Constant.BLOCK_ALL_THIRD_PARTY:
                        if (!resourceHost.equals(curHost)) {
                            return new WebResourceResponse("text/css", "UTF-8", null);
                        }
                        break;
                }

            return super.shouldInterceptRequest(view, url);
        }
        @Override
        public void onPageStarted(final WebView view, String url, Bitmap favicon) {
            curHost = WebUtil.getDomain(view.getUrl());
            try {
                File path = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS);
                if (!path.exists()) {
                    if (!path.mkdir()) {
                        Log.e(this.getClass().getSimpleName(), "cannot create directory");
                    }
                }
                File file = new File(path, "record.txt");
                recordForThirdParty = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
                startTime = System.currentTimeMillis();
                homeViewManager.startRecord(recordForThirdParty,curHost);
            }catch (Exception e){
                e.printStackTrace();
            }

            swipeRefreshLayout.setRefreshing(true);
//            URI uri= null;
//            try {
//                uri = new URI(view.getUrl());
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
//            if (uri != null) {
//                this.curHost =uri.getHost();
//            }
            isRedirect = false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if(isRedirect==false) {
                firstLoad = false;
                Observable.create(new Observable.OnSubscribe<Object>() {
                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        if(recordForThirdParty!=null){
                            homeViewManager.flushRecord(recordForThirdParty,thirdPartyCounter,currentPolicy,total,count,startTime);
                            total = 0;
                            count = 0;
                            recordForThirdParty=null;
                        }
                    }
                }).subscribeOn(Schedulers.io()).subscribe();

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                webviewContainer.removeAllViewsInLayout();
                webviewContainer.addView(webHolder, 0);
                if (url != null && !url.equals("about:blank")) {
                    webViewAdapter.addWebView(webHolder);
                    changeNumTabsIcon(webViewAdapter.getItemCount());
                    viewModel.saveHistory(historyManager.saveHistoryLocal(url,view.getTitle()));
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



    public static Fragment newInstance() {
        HomeView homeView = new HomeView();
        homeView.homeView = homeView;
        Bundle bundle = new Bundle();
        bundle.putBoolean("hasUrl", false);
        homeView.setArguments(bundle);
        return homeView;
    }

    public static Fragment newInstanceWithUrl(String url) {
        HomeView homeView = new HomeView();
        homeView.homeView = homeView;
        Bundle bundle = new Bundle();
        bundle.putBoolean("hasUrl", true);
        bundle.putString("url", url);
        homeView.setArguments(bundle);
        return homeView;
    }

    private LinearLayout initPanelView(){
        /**
         * when display panel view, webholder will be none, forward,backward tab etc make no sense
         */
        forwardIcon.setColorFilter(R.color.cardview_shadow_end_color, PorterDuff.Mode.MULTIPLY);
        backwardIcon.setColorFilter(R.color.cardview_shadow_end_color, PorterDuff.Mode.MULTIPLY);
        tabIcon.setColorFilter(R.color.cardview_shadow_end_color, PorterDuff.Mode.MULTIPLY);
        refreshIcon.setColorFilter(R.color.cardview_shadow_end_color, PorterDuff.Mode.MULTIPLY);
        addBookmarkIcon.setColorFilter(R.color.cardview_shadow_end_color, PorterDuff.Mode.MULTIPLY);
        LinearLayout layout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        params.setMargins(0,8,0,0);
        layout.setLayoutParams(params);
        final RecyclerView bookmarkRecycler = new RecyclerView(getActivity());
        bookmarkRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookmarkRecycler.setItemAnimator(new DefaultItemAnimator());
        bookmarkRecycler.setHasFixedSize(true);
        gridBookmarkAdapter = new NavigationHomeAdapter(homeView);
        viewModel.inflatePanelView(historyManager.getTop8FrequentWebPage(),gridBookmarkAdapter,bookmarkRecycler);
        layout.addView(bookmarkRecycler);
        return layout;
    }

    private CustomWebView initWebView(){
        WebViewClient client = new CustomWebViewClient();
        CustomWebView web = new CustomWebView(getActivity(),client);

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
    public void onResume() {
        super.onResume();
        if(webHolder==null||webViewAdapter.getItemCount()==0) {
            webviewContainer.removeAllViews();
            webviewContainer.addView(initPanelView());
            configViews();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        compositeSubscription.unsubscribe();
    }

    @Override
    public void onDestroy() {
        webViewAdapter.destoryAll();
        if(webHolder!=null){
            ((ViewGroup)webHolder.getParent()).removeView(webHolder);
            webHolder.destroy();
        }
        webHolder = null;
        super.onDestroy();
    }
}
