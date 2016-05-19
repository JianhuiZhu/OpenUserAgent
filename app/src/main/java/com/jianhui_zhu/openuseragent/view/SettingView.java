package com.jianhui_zhu.openuseragent.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;
import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.util.Constant;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.util.Setting;
import com.jianhui_zhu.openuseragent.view.adapter.SearchEngineAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jianhui Zhu on 2016-02-05.
 */
public class SettingView extends Fragment implements FragmentBackHandler{
    @Bind(R.id.third_party_setting_status)
    TextView thirdPartyStatus;
    @Bind(R.id.cookie_status)
    TextView cookieStatus;
    @Bind(R.id.full_screen_status)
    TextView fullScreenStatus;
    @Bind(R.id.popup_status)
    TextView popupStatus;
    @Bind(R.id.location_status)
    TextView locationStatus;
    @Bind(R.id.js_status)
    TextView jsStatus;
    @Bind(R.id.setting_home_page)
    EditText homePage;
    @Bind(R.id.search_engine_spinner)
    Spinner searchEngine;
    Setting settings;
    @OnClick({R.id.general_tool_bar_go_back,R.id.cookie_setting_area,R.id.third_party_setting_area,R.id.js_setting_area})
    public void click(View view){
        switch (view.getId()) {
            case R.id.general_tool_bar_go_back:
            FragmenUtil.backToPreviousFragment(getActivity(), this);
                break;
            case R.id.cookie_setting_area:
                switch (cookieStatus.getText().toString()){
                    case Constant.COOKIE_ALLOW_ALL:
                        cookieStatus.setText(Constant.COOKIE_ALLOW_FIRST_PARTY);
                        Setting.getInstance().setCookiePolicy(Constant.COOKIE_ALLOW_FIRST_PARTY);
                        break;
                    case Constant.COOKIE_ALLOW_FIRST_PARTY:
                        cookieStatus.setText(Constant.COOKIE_NONE);
                        Setting.getInstance().setCookiePolicy(Constant.COOKIE_NONE);
                        break;
                    case Constant.COOKIE_NONE:
                        cookieStatus.setText(Constant.ALLOW_ALL);
                        Setting.getInstance().setCookiePolicy(Constant.COOKIE_ALLOW_ALL);
                        break;
                }
                break;
            case R.id.third_party_setting_area:
                String cur = thirdPartyStatus.getText().toString();
                if(getString(R.string.allow_all).equals(cur)){
                    thirdPartyStatus.setText(R.string.block_blacklist);
                    Setting.getInstance().setThirdPartyPolicy(Constant.BLOCK_BLACK_LIST);
                }else if(getString(R.string.block_blacklist).equals(cur)){
                    thirdPartyStatus.setText(R.string.block_all_third_party);
                    Setting.getInstance().setThirdPartyPolicy(Constant.BLOCK_ALL_THIRD_PARTY);
                }else if(getString(R.string.block_all_third_party).equals(cur)){
                    thirdPartyStatus.setText(R.string.allow_all);
                    Setting.getInstance().setThirdPartyPolicy(Constant.ALLOW_ALL);
                }
                break;
            case R.id.js_setting_area:
                if(jsStatus.getText().toString().equals(Constant.JS_ALLOW)){
                    jsStatus.setText(Constant.JS_BLOCK);
                    Setting.getInstance().setJsPolicy(Constant.JS_BLOCK);
                }else{
                    jsStatus.setText(Constant.JS_ALLOW);
                    Setting.getInstance().setJsPolicy(Constant.JS_ALLOW);
                }

        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings= Setting.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.view_setting,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homePage.setText(settings.getHomePage());
        String curSearchEngine= Setting.getInstance().getSearchEngine();
        final SearchEngineAdapter adapter=new SearchEngineAdapter(getActivity(),curSearchEngine);
        //ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(getActivity(),R.array.search_engines,R.layout.item_spinner);
        //adapter.setDropDownViewResource(R.layout.item_spinner);
        //int position=arrayAdapter.getPosition(curSearchEngine);
        switch (Setting.getInstance().getThirdPartyPolicy()){
            case 0:
                thirdPartyStatus.setText(R.string.allow_all);
                break;
            case 1:
                thirdPartyStatus.setText(R.string.block_blacklist);
                break;
            case 2:
                thirdPartyStatus.setText(R.string.block_all_third_party);
                break;
        }

        cookieStatus.setText(Setting.getInstance().getCookiePolicy());
        fullScreenStatus.setText(Setting.getInstance().getFullScreenPolicy());
        popupStatus.setText(Setting.getInstance().getPopupPolicy());
        locationStatus.setText(Setting.getInstance().getLocationPolicy());
        jsStatus.setText(Setting.getInstance().getJsPolicy());
        searchEngine.setAdapter(adapter);
        int position=adapter.getPosition(curSearchEngine);
        searchEngine.setSelection(position);
        searchEngine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String engine=adapter.getItem(position).toString();
                String defaultEngine= Setting.getInstance().getSearchEngine();
                if(!engine.equals(defaultEngine)){
                    Setting.getInstance().setSearchEngine(engine);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //RxBus.getInstance().send(new GlobalSettingChangeEvent());
    }

    @Override
    public boolean onBackPressed() {
        return BackHandlerHelper.handleBackPress(this);
    }
}
