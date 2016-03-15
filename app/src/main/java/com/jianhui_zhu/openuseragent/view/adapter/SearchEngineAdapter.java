package com.jianhui_zhu.openuseragent.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.util.SettingSingleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianhuizhu on 2016-03-13.
 */
public class SearchEngineAdapter extends ArrayAdapter<CharSequence> {
    private Context context;
    public List<CharSequence> getSearchEngines() {
        return searchEngines;
    }

    List<CharSequence> searchEngines=new ArrayList<>();
    CharSequence currentSearchEngine;
    public SearchEngineAdapter(Context context,CharSequence searchEngine) {
        super(context, R.layout.item_spinner);
        this.context=context;
        currentSearchEngine=searchEngine;
        searchEngines.add("Google");
        searchEngines.add("Baidu");
        searchEngines.add("Bing");
        this.addAll(searchEngines);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_spinner,parent,false);
        }
        final TextView tv=(TextView)convertView.findViewById(R.id.spinner_item);
        tv.setText(searchEngines.get(position));
        return convertView;
    }

    @Override
    public int getCount() {
        return searchEngines == null ?0 : searchEngines.size();
    }
}
