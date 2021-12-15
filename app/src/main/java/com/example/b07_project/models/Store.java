package com.example.b07_project.models;

import android.os.Parcel;

public class Store extends User {
    public Store(){

    }
    public Store(String name, String email, String id) {
        super(name, email, id, "Stores");
    }

    public static final Creator<Store> CREATOR = new Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel in) {
            return new Store(in);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }


    protected Store(Parcel in) {
        super(in);
    }

    public String getName(){
        return name;
    }
    public String getId(){
        return id;
    }
}