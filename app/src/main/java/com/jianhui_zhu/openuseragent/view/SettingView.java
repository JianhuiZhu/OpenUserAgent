package com.jianhui_zhu.openuseragent.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.util.Constant;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.util.RxBus;
import com.jianhui_zhu.openuseragent.util.SettingSingleton;
import com.jianhui_zhu.openuseragent.util.event.GlobalSettingChangeEvent;
import com.jianhui_zhu.openuseragent.view.adapter.SearchEngineAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jianhui Zhu on 2016-02-05.
 */
public class SettingView extends Fragment {
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
    SettingSingleton settings;
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
                        SettingSingleton.getInstance().setCookiePolicy(Constant.COOKIE_ALLOW_FIRST_PARTY);
                        break;
                    case Constant.COOKIE_ALLOW_FIRST_PARTY:
                        cookieStatus.setText(Constant.COOKIE_NONE);
                        SettingSingleton.getInstance().setCookiePolicy(Constant.COOKIE_NONE);
                        break;
                    case Constant.COOKIE_NONE:
                        cookieStatus.setText(Constant.ALLOW_ALL);
                        SettingSingleton.getInstance().setCookiePolicy(Constant.COOKIE_ALLOW_ALL);
                        break;
                }
                break;
            case R.id.third_party_setting_area:
                String cur = thirdPartyStatus.getText().toString();
                if(getString(R.string.allow_all).equals(cur)){
                    thirdPartyStatus.setText(R.string.block_blacklist);
                    SettingSingleton.getInstance().setThirdPartyPolicy(Constant.BLOCK_BLACK_LIST);
                }else if(getString(R.string.black_list).equals(cur)){
                    thirdPartyStatus.setText(R.string.block_all_third_party);
                    SettingSingleton.getInstance().setThirdPartyPolicy(Constant.BLOCK_ALL_THIRD_PARTY);
                }else if(getString(R.string.block_all_third_party).equals(cur)){
                    thirdPartyStatus.setText(R.string.allow_all);
                    SettingSingleton.getInstance().setThirdPartyPolicy(Constant.ALLOW_ALL);
                }
                break;
            case R.id.js_setting_area:
                if(jsStatus.getText().toString().equals(Constant.JS_ALLOW)){
                    jsStatus.setText(Constant.JS_BLOCK);
                    SettingSingleton.getInstance().setJsPolicy(Constant.JS_BLOCK);
                }else{
                    jsStatus.setText(Constant.JS_ALLOW);
                    SettingSingleton.getInstance().setJsPolicy(Constant.JS_ALLOW);
                }

        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings=SettingSingleton.getInstance();
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
        String curSearchEngine=SettingSingleton.getInstance().getSearchEngine();
        final SearchEngineAdapter adapter=new SearchEngineAdapter(getActivity(),curSearchEngine);
        //ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(getActivity(),R.array.search_engines,R.layout.item_spinner);
        //adapter.setDropDownViewResource(R.layout.item_spinner);
        //int position=arrayAdapter.getPosition(curSearchEngine);
        switch (SettingSingleton.getInstance().getThirdPartyPolicy()){
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

        cookieStatus.setText(SettingSingleton.getInstance().getCookiePolicy());
        fullScreenStatus.setText(SettingSingleton.getInstance().getFullScreenPolicy());
        popupStatus.setText(SettingSingleton.getInstance().getPopupPolicy());
        locationStatus.setText(SettingSingleton.getInstance().getLocationPolicy());
        jsStatus.setText(SettingSingleton.getInstance().getJsPolicy());
        searchEngine.setAdapter(adapter);
        int position=adapter.getPosition(curSearchEngine);
        searchEngine.setSelection(position);
        searchEngine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String engine=adapter.getItem(position).toString();
                String defaultEngine=SettingSingleton.getInstance().getSearchEngine();
                if(!engine.equals(defaultEngine)){
                    SettingSingleton.getInstance().setSearchEngine(engine);
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

}
