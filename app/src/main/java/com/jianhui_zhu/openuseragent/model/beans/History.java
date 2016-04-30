package com.jianhui_zhu.openuseragent.model.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jianhui Zhu on 2016-02-06.
 */
public class History implements Parcelable{
    String rID;
    String name;
    String url;
    long timestamp;
    int visitCount;

    protected History(Parcel in) {
        rID = in.readString();
        name = in.readString();
        url = in.readString();
        timestamp = in.readLong();
        visitCount = in.readInt();
    }

    public static final Creator<History> CREATOR = new Creator<History>() {
        @Override
        public History createFromParcel(Parcel in) {
            return new History(in);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }



    public String getrID() {
        return rID;
    }

    public void setrID(String rID) {
        this.rID = rID;
    }

    public History() {

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
        dest.writeString(name);
        dest.writeString(url);
        dest.writeLong(timestamp);
        dest.writeInt(visitCount);
    }
}
