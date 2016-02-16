package com.jianhui_zhu.openuseragent.model.beans;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by jianhuizhu on 2016-02-14.
 */
public class User implements Parcelable {
    String username;
    String avatarUrl;
    List<Bookmark> bookmarkList;
    List<Record> recordList;

    public User() {

    }

    protected User(Parcel in) {
        username = in.readString();
        avatarUrl = in.readString();
        bookmarkList = in.createTypedArrayList(Bookmark.CREATOR);
        recordList = in.createTypedArrayList(Record.CREATOR);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public List<Bookmark> getBookmarkList() {
        return bookmarkList;
    }

    public void setBookmarkList(List<Bookmark> bookmarkList) {
        this.bookmarkList = bookmarkList;
    }

    public List<Record> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
    }


    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(avatarUrl);
        dest.writeTypedList(bookmarkList);
        dest.writeTypedList(recordList);
    }
}
