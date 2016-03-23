package com.jianhui_zhu.openuseragent.util;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianhui_zhu.openuseragent.R;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jianhuizhu on 2016-03-23.
 */
public class WebViewFragment extends Fragment {
    @Bind(R.id.title)
    TextView head;
    @Bind(R.id.snapshot)
    ImageView snapshot;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.item_webview,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = getArguments().getString("title");
        String path = getArguments().getString("path");
        head.setText(title);
        Picasso.with(getActivity())
                .load(path)
                .fit()
                .into(snapshot);

    }

    public static WebViewFragment newInstance(String title,String path){
        Bundle bundle=new Bundle();
        bundle.putString("title",title);
        bundle.putString("path",path);
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

}
