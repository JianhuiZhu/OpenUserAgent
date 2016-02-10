package com.jianhui_zhu.openuseragent.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.util.AbstractFragment;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jianhuizhu on 2016-02-08.
 *
 * THIS FRAGMENT IS FOR TESTING ONLY
 */
public class TestView extends AbstractFragment {
    @Bind(R.id.go_to_home_fragment)
    Button homeBtn;
    @Bind(R.id.go_to_bookmark_fragment)
    Button bookmarkBtn;
    @Bind(R.id.go_to_download_fragment)
    Button downloadBtn;
    @Bind(R.id.go_to_login_fragment)
    Button loginBtn;
    @Bind(R.id.go_to_profile_fragment)
    Button profileBtn;
    @Bind(R.id.go_to_setting_fragment)
    Button settingBtn;
    @OnClick({R.id.go_to_home_fragment,R.id.go_to_setting_fragment,R.id.go_to_profile_fragment,R.id.go_to_login_fragment,R.id.go_to_download_fragment,R.id.go_to_bookmark_fragment})
    public void changeFragment(Button btn){
        switch(btn.getId()){
            case R.id.go_to_home_fragment:
                FragmenUtil.switchToFragment(getActivity(),new HomeView());
                break;
            case R.id.go_to_bookmark_fragment:
                FragmenUtil.switchToFragment(getActivity(),new BookmarkView());
                break;
            case R.id.go_to_download_fragment:
                FragmenUtil.switchToFragment(getActivity(),new DownloadView());
                break;
            case R.id.go_to_login_fragment:
                FragmenUtil.switchToFragment(getActivity(),new LoginView());
                break;
            case R.id.go_to_profile_fragment:
                FragmenUtil.switchToFragment(getActivity(),new ProfileView());
                break;
            case R.id.go_to_setting_fragment:
                FragmenUtil.switchToFragment(getActivity(),new SettingView());
                break;
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.view_test,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

}
