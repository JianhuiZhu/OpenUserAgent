package com.jianhui_zhu.openuseragent.view.dialogs;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.view.adapter.WebViewAdapter;
import com.jianhui_zhu.openuseragent.view.interfaces.HomeViewInterface;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jianhuizhu on 2016-03-23.
 */
public class TabStackDialog extends DialogFragment {
    HomeViewInterface viewInterface;
    @Bind(R.id.tab_pager)
    RecyclerView tabs;
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
        adapter = WebViewAdapter.getInstance(viewInterface);
        adapter.setTabStackDialog(this);
        tabs.setItemAnimator(new DefaultItemAnimator());
        tabs.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        tabs.setAdapter(adapter);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        try {
            viewInterface.changeNumTabsIcon(adapter.getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            viewInterface.changeNumTabsIcon(adapter.getItemCount());
            ButterKnife.unbind(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static DialogFragment newInstance(HomeViewInterface viewInterface){
        DialogFragment dialog = new TabStackDialog();
        ((TabStackDialog)dialog).viewInterface = viewInterface;
        return dialog;
    }
}
