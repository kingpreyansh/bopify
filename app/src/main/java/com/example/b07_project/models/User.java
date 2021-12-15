package com.example.b07_project.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    public String name, email, id, type;

    public User(){

    }
    public User(String name, String email, String id, String type){
        this.name = name;
        this.email = email;
        this.id = id;
        this.type = type;
    }

    protected User(Parcel in) {
        name = in.readString();
        email = in.readString();
        id = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(id);
    }
}
