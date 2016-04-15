package com.jianhui_zhu.openuseragent.view.interfaces;

import com.jianhui_zhu.openuseragent.model.beans.FileDescriptor;

import java.util.List;

/**
 * Created by jianhuizhu on 2016-04-09.
 */
public interface DownloadViewInterface {
    void showTag(String s);
    void refreshWithfileList(List<FileDescriptor> files);
}
