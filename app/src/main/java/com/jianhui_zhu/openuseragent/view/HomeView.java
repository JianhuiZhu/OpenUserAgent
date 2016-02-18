package com.jianhui_zhu.openuseragent.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.beans.User;
import com.jianhui_zhu.openuseragent.presenter.HomePresenter;
import com.jianhui_zhu.openuseragent.util.AbstractFragment;
import com.jianhui_zhu.openuseragent.view.interfaces.HomeViewInterface;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Jianhui Zhu on 2016-01-27.
 */
public class HomeView extends AbstractFragment implements HomeViewInterface{
    @Bind(R.id.web_container) WebView webHolder;
    @Bind(R.id.home_avatar)
    de.hdodenhof.circleimageview.CircleImageView avatar;
    @Bind(R.id.home_name)
    TextView username;
    @Bind(R.id.profile_area)
    RelativeLayout profileArea;

    @OnClick({R.id.home_refresh_icon, R.id.add_bookmark_icon, R.id.home_history_icon, R.id.home_bookmark_icon, R.id.home_menu_icon, R.id.home_url_bar})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.home_refresh_icon:
                webHolder.reload();
                break;
            case R.id.add_bookmark_icon:
                String url = webHolder.getUrl();
                String urlTitle = webHolder.getTitle();
                Toast.makeText(getActivity(), "current url is " + url + " \n current title is " + urlTitle, Toast.LENGTH_LONG).show();
                //TO-DO add bookmark function
                break;
            case R.id.home_history_icon:
                Toast.makeText(getActivity(), "Home history icon clicked", Toast.LENGTH_LONG).show();
                break;
            case R.id.home_bookmark_icon:
                Toast.makeText(getActivity(), "Home bookmark icon clicked", Toast.LENGTH_LONG).show();
                break;
            case R.id.home_menu_icon:
                Toast.makeText(getActivity(), "menu clicked", Toast.LENGTH_LONG).show();
                break;
            case R.id.home_url_bar:
                break;
        }
    }
    private HomePresenter presenter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter=new HomePresenter(this);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.view_home,container,false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle.getBoolean("hasUser")) {
            User user = bundle.getParcelable("user");
            Picasso.with(getActivity()).load(user.getAvatarUrl()).fit().into(avatar);
            username.setText(user.getUsername());
        }
        initBrowserSettings();
        loadTargetUrl("");
    }

    @Override
    public void initBrowserSettings() {
        WebSettings settings = this.webHolder.getSettings();
        WebViewClient client = new CustomWebView();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setJavaScriptEnabled(true);
        this.webHolder.setWebViewClient(client);
    }

    @Override
    public void loadTargetUrl(String url) {
        if(url!=null&&!url.equals("")){
            this.webHolder.loadUrl(url);
        }else{
            this.webHolder.loadUrl("http://www.google.com");
        }
    }

    private class CustomWebView extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }

    public static AbstractFragment newInstanceWithUser(User user) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("hasUser", true);
        bundle.putParcelable("user", user);
        HomeView homeView = new HomeView();
        homeView.setArguments(bundle);
        return homeView;

    }

    public static AbstractFragment newInstance() {
        HomeView homeView = new HomeView();
        Bundle bundle = new Bundle();
        bundle.putBoolean("hasUser", false);
        homeView.setArguments(bundle);
        return homeView;
    }


}
