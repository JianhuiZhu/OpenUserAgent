package com.jianhui_zhu.openuseragent.util;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

/**
 * Created by jianhuizhu on 2016-02-14.
 */
public class TimeStamp extends Timestamp implements Parcelable {
    public TimeStamp(long theTime) {
        super(theTime);
    }

    public static Creator<TimeStamp> getCREATOR() {
        return CREATOR;
    }

    protected TimeStamp(Parcel in) {
        super(in.readLong());
    }

    public static final Creator<TimeStamp> CREATOR = new Creator<TimeStamp>() {
        @Override
        public TimeStamp createFromParcel(Parcel in) {
            return new TimeStamp(in);
        }

        @Override
        public TimeStamp[] newArray(int size) {
            return new TimeStamp[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.getTime());
    }
}
