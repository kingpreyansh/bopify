package com.example.b07_project.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Item implements Parcelable {
    private double price;
    private String brand;
    private String name;
    private String id;
    private String storeId;
    private int quantity;
    public Item(){}
    public Item(double price, String brand, String name, String id, String storeId, int quantity) {
        this.price = price;
        this.brand = brand;
        this.name = name;
        this.id = id;
        this.storeId = storeId;
        this.quantity = quantity;
    }

    protected Item(Parcel in) {
        price = in.readDouble();
        brand = in.readString();
        name = in.readString();
        id = in.readString();
        storeId = in.readString();
        quantity = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getId(){
        return this.id;
    }


    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public double getPrice(){
        return this.price;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public String getBrand(){
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getStoreId(){
        return this.storeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void incrementQuantity() {
        quantity++;
    }

    public boolean decrementQuantity() {
        if (quantity > 0) {
            quantity--;
            return true;
        }
        return false;
    }

    public static int getIndexById(String id, ArrayList<Item> items) {
        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getId().equals(id)) {
                    return i;
                }
            }
        }

        return -1;
    }

    public static ArrayList<Item> incrementItem(Item item, ArrayList<Item> items) {
        int index = Item.getIndexById(item.getId(), items);

        if (index > -1) {
            Item incr = items.get(index);
            incr.incrementQuantity();
            items.set(index, incr);
        } else {
            item.quantity = 1;
            items.add(item);
        }

        return items;
    };

    public static ArrayList<Item> decrementItem(Item item, ArrayList<Item> items) {
        int index = Item.getIndexById(item.getId(), items);

        if (index > -1) {
            Item incr = items.get(index);
            incr.decrementQuantity();
            items.set(index, incr);
        }

        return items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(price);
        dest.writeString(brand);
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(storeId);
        dest.writeInt(quantity);
    }
}