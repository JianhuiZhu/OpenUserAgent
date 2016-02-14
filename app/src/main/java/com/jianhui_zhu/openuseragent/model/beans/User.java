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
    String uID;
    String username;
    String email;
    Bitmap avatar;
    List<Bookmark> bookmarkList;
    List<Record> recordList;


    protected User(Parcel in) {
        uID = in.readString();
        username = in.readString();
        email = in.readString();
        avatar = in.readParcelable(Bitmap.class.getClassLoader());
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

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uID);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeParcelable(avatar, flags);
        dest.writeTypedList(bookmarkList);
        dest.writeTypedList(recordList);
    }
}
