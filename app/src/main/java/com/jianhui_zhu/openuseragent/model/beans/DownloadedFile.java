package com.jianhui_zhu.openuseragent.model.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jianhuizhu on 2016-04-06.
 */
public class DownloadedFile implements Parcelable {
    String name;
    String path;
    String fileType;
    public DownloadedFile(){

    }
    protected DownloadedFile(Parcel in) {
        name = in.readString();
        path = in.readString();
        fileType = in.readString();
    }

    public static final Creator<DownloadedFile> CREATOR = new Creator<DownloadedFile>() {
        @Override
        public DownloadedFile createFromParcel(Parcel in) {
            return new DownloadedFile(in);
        }

        @Override
        public DownloadedFile[] newArray(int size) {
            return new DownloadedFile[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(path);
        dest.writeString(fileType);
    }
}
