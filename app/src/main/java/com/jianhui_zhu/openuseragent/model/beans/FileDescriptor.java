package com.jianhui_zhu.openuseragent.model.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jianhuizhu on 2016-04-06.
 */
public class FileDescriptor{
    String name;
    String path;
    String fileType;
    String createDate;
    String permission;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    long size;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }


    public FileDescriptor(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

}
