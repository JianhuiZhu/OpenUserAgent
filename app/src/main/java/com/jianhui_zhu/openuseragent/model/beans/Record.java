package com.jianhui_zhu.openuseragent.model.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Jianhui Zhu on 2016-02-06.
 */
public class Record implements Parcelable{
    String rID;
    String url;
    long timestamp;


    protected Record(Parcel in) {
        rID = in.readString();
        url = in.readString();
        timestamp = in.readLong();
    }

    public static final Creator<Record> CREATOR = new Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel in) {
            return new Record(in);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };

    public String getrID() {
        return rID;
    }

    public void setrID(String rID) {
        this.rID = rID;
    }

    public Record() {

    }
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rID);
        dest.writeString(url);
        dest.writeLong(timestamp);
    }
}
