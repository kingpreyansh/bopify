package com.example.b07_project.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class Order implements Parcelable{
    private double total;
    private ArrayList<Item> items = new ArrayList<Item>();
    private String createdAt;
    private boolean isCompleted;
    private String id;
    private String storeId;
    private String customerId;
    private String customerName;


    public double getTotal() { return total; }
    public ArrayList<Item> getItems() { return items; }
    public String getCreatedAt() { return createdAt; }
    public boolean getIsCompleted() { return isCompleted; }
    public String getId() { return id; }
    public String getStoreId() { return storeId; }
    public String getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }

    public Order(){}

    //This code is so that we can parse orders (EVERYTHING BUT THE ITEMS)
    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected Order(Parcel in) {
        total = in.readDouble();
        createdAt = in.readString();
        isCompleted = in.readBoolean();
        id = in.readString();
        storeId = in.readString();
        customerId = in.readString();
        customerName = in.readString();

    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(total);
        dest.writeString(createdAt);
        dest.writeBoolean(isCompleted);
        dest.writeString(id);
        dest.writeString(storeId);
        dest.writeString(customerId);
        dest.writeString(customerName);
    }

    public Order(
            double total,
            ArrayList<Item> items,
            String customerId,
            String storeId,
            String createdAt,
            boolean isCompleted,
            String id,
            String customerName
    ) {
        this.total = total;
        this.items = items;
        this.customerId = customerId;
        this.storeId = storeId;
        this.createdAt = createdAt;
        this.isCompleted = isCompleted;
        this.id = id;
        this.customerName = customerName;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public void setId(String id) {
        this.id = id;
    }
}
