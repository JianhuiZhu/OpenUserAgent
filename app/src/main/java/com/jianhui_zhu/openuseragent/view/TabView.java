package com.jianhui_zhu.openuseragent.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.beans.WebViewInfoHolder;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jianhuizhu on 2016-03-29.
 */
public class TabView extends Fragment {
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.snapshot)
    ImageView snapshot;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.item_webview,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String title = getArguments().getString("title");
        Bitmap snapshot = getArguments().getParcelable("snapshot");
        this.title.setText(title);
        this.snapshot.setImageBitmap(snapshot);
    }

    public static Fragment newInstance(WebViewInfoHolder webViewInfoHolder){
        TabView tabView = new TabView();
        Bundle bundle = new Bundle();
        bundle.putString("title",webViewInfoHolder.getTitle());
        bundle.putParcelable("snapshot",webViewInfoHolder.getBitmap());
        tabView.setArguments(bundle);
        return  tabView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
