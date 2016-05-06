package com.jianhui_zhu.openuseragent.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.util.SettingSingleton;
import com.jianhui_zhu.openuseragent.view.adapter.SearchEngineAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Jianhui Zhu on 2016-02-05.
 */
public class SettingView extends Fragment {
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
    }
}
