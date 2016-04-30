package com.jianhui_zhu.openuseragent.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by jianhuizhu on 2016-03-07.
 */
public class SearchSuggestionAdapter extends SimpleCursorAdapter {

    public SearchSuggestionAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1, null, new String[]{"query"},new int[]{android.R.id.text1} , 0);

    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1,parent,false);
    }

    @Override
    public void bindView(View view,  Context context, final Cursor cursor) {
        TextView tv=(TextView)view.findViewById(android.R.id.text1);
        final int id=cursor.getInt(0);
        final String suggestion= cursor.getString(1);
        final int count=cursor.getInt(2);
        tv.setText(suggestion);
    }


}
