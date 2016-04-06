package com.jianhui_zhu.openuseragent.view;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.beans.DownloadedFile;
import com.jianhui_zhu.openuseragent.util.AbstractFragment;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.view.adapter.DownloadAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jianhui Zhu on 2016-02-05.
 */
public class DownloadView extends AbstractFragment{
    @OnClick(R.id.download_go_back)
    public void click(){
        FragmenUtil.backToPreviousFragment(getActivity(),this);
    }
    @Bind(R.id.download_list)
    RecyclerView downloadList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_download,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<DownloadedFile> files = new ArrayList<>();
        DownloadedFile file1 = new DownloadedFile();
        file1.setName("windows.jpg");
        file1.setPath("https://www.google.com/download/blablabla...");
        file1.setName("jpg");
        DownloadedFile file2 = new DownloadedFile();
        file2.setName("Concordia.jpg");
        file2.setPath("https://www.google.com/download/blablabla...");
        file2.setName("jpg");
        files.add(file1);
        files.add(file2);
        downloadList.setLayoutManager(new LinearLayoutManager(getActivity()));
        downloadList.setItemAnimator(new DefaultItemAnimator());
        downloadList.setHasFixedSize(true);
        DownloadAdapter adapter = new DownloadAdapter();
        adapter.setDownloadedFiles(files);
        downloadList.setAdapter(adapter);
    }
}
