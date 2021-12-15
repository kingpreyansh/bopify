package com.example.b07_project.models;

import android.os.Parcel;

public class Customer extends User {
    public Customer(String name, String email, String id) {
        super(name, email, id, "Customers");
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected Customer(Parcel in) {
        super(in);
    }
}
