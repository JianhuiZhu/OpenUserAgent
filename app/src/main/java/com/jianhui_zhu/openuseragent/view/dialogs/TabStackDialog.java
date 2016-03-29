package com.jianhui_zhu.openuseragent.view.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.StackView;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.util.AbstractDialogFragment;
import com.jianhui_zhu.openuseragent.util.activity.MainActivity;
import com.jianhui_zhu.openuseragent.view.adapter.WebViewAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jianhuizhu on 2016-03-23.
 */
public class TabStackDialog extends AbstractDialogFragment {
    @Bind(R.id.tab_pager)
    StackView stackView;
    WebViewAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_tabstack,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = WebViewAdapter.getInstance(getActivity());
        stackView.setAdapter(adapter);
    }
}
