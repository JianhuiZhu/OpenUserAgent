package com.jianhui_zhu.openuseragent.model.beans;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;

import java.sql.Timestamp;

/**
 * Created by Jianhui Zhu on 2016-02-06.
 */
public class Bookmark implements Parcelable {

    String url;
    String name;

    public Bookmark(String url, String name) {
        this.url = url;
        this.name = name;
    }

    protected Bookmark(Parcel in) {
        url = in.readString();
        name = in.readString();
    }

    public static final Creator<Bookmark> CREATOR = new Creator<Bookmark>() {
        @Override
        public Bookmark createFromParcel(Parcel in) {
            return new Bookmark(in);
        }

        @Override
        public Bookmark[] newArray(int size) {
            return new Bookmark[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(name);
    }
}
