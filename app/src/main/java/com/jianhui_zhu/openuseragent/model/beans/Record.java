package com.jianhui_zhu.openuseragent.model.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.jianhui_zhu.openuseragent.util.TimeStamp;

import java.sql.Timestamp;

/**
 * Created by Jianhui Zhu on 2016-02-06.
 */
public class Record implements Parcelable {
    String url;
    TimeStamp timestamp;

    public Record() {
    }
    public Record(String url) {
        this.url = url;
    }

    protected Record(Parcel in) {
        url = in.readString();
        timestamp = in.readParcelable(TimeStamp.class.getClassLoader());
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TimeStamp getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Record{" +
                "url='" + url + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeParcelable(timestamp, flags);
    }
}
