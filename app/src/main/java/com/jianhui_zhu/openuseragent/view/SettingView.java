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
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.util.SettingSingleton;
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
    @OnClick(R.id.general_tool_bar_go_back)
    public void click(){
        FragmenUtil.backToPreviousFragment(getActivity(),this);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings=SettingSingleton.getInstance(getActivity());
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
        String curSearchEngine=SettingSingleton.getInstance(getActivity()).getSearchEngine();
        final SearchEngineAdapter adapter=new SearchEngineAdapter(getActivity(),curSearchEngine);
        //ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(getActivity(),R.array.search_engines,R.layout.item_spinner);
        //adapter.setDropDownViewResource(R.layout.item_spinner);
        //int position=arrayAdapter.getPosition(curSearchEngine);
        switch (SettingSingleton.getInstance(getActivity()).getThirdPartyPolicy()){
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

        cookieStatus.setText(SettingSingleton.getInstance(getActivity()).getCookiePolicy());
        fullScreenStatus.setText(SettingSingleton.getInstance(getActivity()).getFullScreenPolicy());
        popupStatus.setText(SettingSingleton.getInstance(getActivity()).getPopupPolicy());
        locationStatus.setText(SettingSingleton.getInstance(getActivity()).getLocationPolicy());
        jsStatus.setText(SettingSingleton.getInstance(getActivity()).getJsPolicy());
        searchEngine.setAdapter(adapter);
        int position=adapter.getPosition(curSearchEngine);
        searchEngine.setSelection(position);
        searchEngine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String engine=adapter.getItem(position).toString();
                String defaultEngine=SettingSingleton.getInstance(getActivity()).getSearchEngine();
                if(!engine.equals(defaultEngine)){
                    SettingSingleton.getInstance(getActivity()).setSearchEngine(engine);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        SettingSingleton singleton = SettingSingleton.getInstance(getActivity());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        Log.d(this.getClass().getName(),"On destroy View Called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(this.getClass().getName(),"On destroy Called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(this.getClass().getName(),"On pause Called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(this.getClass().getName(),"On stop Called");
    }
}
