package com.jianhui_zhu.openuseragent.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.presenter.HomePresenter;

import java.util.List;

/**
 * Created by jianhuizhu on 2016-03-07.
 */
public class SearchSuggestionAdapter extends CursorAdapter {
    HomePresenter homePresenter;



    public SearchSuggestionAdapter(Context context, Cursor c,HomePresenter homePresenter) {
        super(context, c, 0);
        this.homePresenter=homePresenter;
    }

    public List<Cursor> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Cursor> suggestions) {
        this.suggestions = suggestions;
    }

    List<Cursor> suggestions;


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1,parent,false);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        TextView tv=(TextView)view.findViewById(android.R.id.text1);
        tv.setText(cursor.getString(0));
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homePresenter.incrementRecordLocally(cursor);
            }
        });
    }
}
