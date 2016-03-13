package com.jianhui_zhu.openuseragent.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.util.AbstractFragment;
import com.jianhui_zhu.openuseragent.util.SettingSingleton;
import com.jianhui_zhu.openuseragent.util.SharePreferenceUtil;
import com.jianhui_zhu.openuseragent.view.adapter.SearchEngineAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jianhui Zhu on 2016-02-05.
 */
public class SettingView extends AbstractFragment {
    @Bind(R.id.setting_home_page)
    EditText homePage;
    @Bind(R.id.search_engine_spinner)
    Spinner searchEngine;
    SettingSingleton settings;

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
        SearchEngineAdapter adapter=new SearchEngineAdapter(getActivity(),curSearchEngine);
        int position=adapter.getPosition(curSearchEngine);
        searchEngine.setAdapter(adapter);
        searchEngine.setSelection(position);
    }
}
