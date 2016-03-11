package com.jianhui_zhu.openuseragent.view;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.beans.User;
import com.jianhui_zhu.openuseragent.presenter.HomePresenter;
import com.jianhui_zhu.openuseragent.util.AbstractFragment;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.util.RemoteDatabaseSingleton;
import com.jianhui_zhu.openuseragent.util.SettingSingleton;
import com.jianhui_zhu.openuseragent.view.adapter.SearchSuggestionAdapter;
import com.jianhui_zhu.openuseragent.view.custom.CustomDrawerLayout;
import com.jianhui_zhu.openuseragent.view.custom.CustomWebView;
import com.jianhui_zhu.openuseragent.view.interfaces.HomeViewInterface;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Jianhui Zhu on 2016-01-27.
 */
public class HomeView extends AbstractFragment implements HomeViewInterface {
    User user;
    private boolean drawerIsOpen = false;
    @Bind(R.id.tool_bar_area)
    PercentRelativeLayout toolBarArea;
    @Bind(R.id.web_container)
    CustomWebView webHolder;
    @Bind(R.id.setting_drawer)
    CustomDrawerLayout settingDrawer;
    @Bind(R.id.profile_title)
    NavigationView profileTitle;
    @Bind(R.id.home_url_bar)
    SearchView urlBar;
    @Bind(R.id.search_suggestion_list)
    ListView suggestionList;
    SearchSuggestionAdapter suggestionAdapter;

    @OnClick({R.id.home_refresh_icon, R.id.add_bookmark_icon, R.id.home_menu_icon})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.home_refresh_icon:
                webHolder.reload();
                break;
            case R.id.add_bookmark_icon:
                if (RemoteDatabaseSingleton.getInstance(getActivity()).isUserLoggedIn()) {
                    this.user = RemoteDatabaseSingleton.getInstance(getActivity()).getUser();
                }
                String url = webHolder.getUrl();
                String urlTitle = webHolder.getTitle();
                if (user == null) {
                    presenter.saveBookmark(url, urlTitle, null);
                } else {
                    presenter.saveBookmark(url, urlTitle, user.getuID());
                }

                break;
            case R.id.home_menu_icon:
                if (RemoteDatabaseSingleton.getInstance(getActivity()).isUserLoggedIn()) {
                    this.user = RemoteDatabaseSingleton.getInstance(getActivity()).getUser();
                }
                if (user != null) {
                    CircleImageView avatar = (CircleImageView) getActivity().findViewById(R.id.home_avatar);
                    TextView username = (TextView) getActivity().findViewById(R.id.home_name);
                    Picasso.with(getActivity()).load(user.getAvatarUrl()).fit().into(avatar);
                    username.setText(user.getUsername());
                }
                settingDrawer.openDrawer(Gravity.RIGHT);
                drawerIsOpen = true;
                break;
        }
    }

    private HomePresenter presenter;
    public void configViews(){
        profileTitle.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.setting_option:
                        break;
                    case R.id.bookmark_option:
                        FragmenUtil.switchToFragment(getActivity(), new BookmarkView());
                        break;
                    case R.id.history_option:
                        FragmenUtil.switchToFragment(getActivity(), new HistoryView());
                        break;
                }
                return true;
            }
        });
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
        webHolder.setHomeViewInterface(this);
        settingDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        webHolder.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new HomePresenter(this, getActivity());
        suggestionAdapter = new SearchSuggestionAdapter(getActivity(), presenter);

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
        if (RemoteDatabaseSingleton.getInstance(getActivity()).isUserLoggedIn()) {
            this.user = RemoteDatabaseSingleton.getInstance(getActivity()).getUser();
        }
        initBrowserSettings();
        configViews();

    }

    @Override
    public void initBrowserSettings() {
        WebSettings settings = this.webHolder.getSettings();
        WebViewClient client = new CustomWebViewClient();
        WebChromeClient chromeClient = new CustomWebChrome();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setJavaScriptEnabled(true);
        this.webHolder.setWebViewClient(client);
        this.webHolder.setWebChromeClient(chromeClient);
    }

    @Override
    public void loadTargetUrl(String url) {
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


//    @Override
//    public void onClick(View v) {
//
//        if (drawerIsOpen) {
//            settingDrawer.closeDrawer(Gravity.LEFT);
//            drawerIsOpen = false;
//        }
//    }

    private class CustomWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            presenter.saveRecordLocally(view.getUrl(), view.getTitle());

        }


    }

    private class CustomWebChrome extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {

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

}
