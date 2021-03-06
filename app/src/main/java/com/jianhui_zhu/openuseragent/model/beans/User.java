package com.jianhui_zhu.openuseragent.model.beans;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.jianhui_zhu.openuseragent.util.MapUtil;

import java.util.Map;

/**
 * Created by jianhuizhu on 2016-02-14.
 */
public class User implements Parcelable {
    String uID;
    String username;
    String avatarUrl;
    String homepage;
    Map<String, Bookmark> bookmarks;
    Map<String, History> histories;

    public User() {

    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    protected User(Parcel in) {
        uID = in.readString();
        username = in.readString();
        avatarUrl = in.readString();
        homepage=in.readString();
        Bundle bundle = in.readBundle(getClass().getClassLoader());
        bookmarks = MapUtil.fromBundle(bundle.getBundle("bookmarks"), Bookmark.class);
        histories = MapUtil.fromBundle(bundle.getBundle("histories"), History.class);
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

    public Map<String, History> getHistories() {
        return histories;
    }

    public void setHistories(Map<String, History> histories) {
        this.histories = histories;
    }

    public Map<String, Bookmark> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(Map<String, Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
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
        dest.writeString(uID);
        dest.writeString(username);
        dest.writeString(avatarUrl);
        dest.writeString(homepage);
        Bundle bundle = new Bundle();
        bundle.putBundle("bookmarks", MapUtil.toBundle(bookmarks));
        bundle.putBundle("histories", MapUtil.toBundle(histories));
        dest.writeBundle(bundle);

    }
}
