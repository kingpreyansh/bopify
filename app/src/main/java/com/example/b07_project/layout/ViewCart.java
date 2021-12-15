package com.example.b07_project.layout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b07_project.R;
import com.example.b07_project.config.RecyclerViewConfigCart;
import com.example.b07_project.models.Customer;
import com.example.b07_project.models.Item;
import com.example.b07_project.models.Order;
import com.example.b07_project.models.Store;
import com.example.b07_project.utils.Format;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewCart extends AppCompatActivity implements View.OnClickListener {
    private Order order;
    private Customer customer;
    private Store store;
    private TextView storeName;
    private TextView totalPrice;
    private Button checkout;
    private RecyclerView mRecyclerView;
    private ArrayList<Item> cart = new ArrayList<Item>();
    public String orderId;
    Date str = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
    DatabaseReference ref;
    DatabaseReference ref_n;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        checkout = findViewById(R.id.checkout_items);
        totalPrice = findViewById(R.id.cart_total);

        if(getIntent().getExtras() != null) {
            order = getIntent().getParcelableExtra(getResources().getString(R.string.orders));
            customer = getIntent().getParcelableExtra(getResources().getString(R.string.customers));
            cart = getIntent().getParcelableArrayListExtra(getResources().getString(R.string.items));
            store = getIntent().getParcelableExtra(getResources().getString(R.string.stores));
            totalPrice.setText("Your total price is: " + Format.formatCurrency(order.getTotal()));
        }
        storeName = findViewById(R.id.storeName);
        storeName.setText("CHECKOUT");

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_items);
        new RecyclerViewConfigCart().setConfig(
                mRecyclerView,
                ViewCart.this,
                cart
        );
        ref = FirebaseDatabase.getInstance().getReference().child(getResources().getString(R.string.orders));
        checkout.setOnClickListener(v ->{
            removeEmptyItems(cart);
            if(cart.size()==0){
                Toast.makeText(ViewCart.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
            else
                checkout();
        });
    }
    public void minusQuantity(Item item) {
        int index = Item.getIndexById(item.getId(), cart);
        if (index > -1) {
            Item decr = cart.get(index);
            boolean didDec = decr.decrementQuantity();
            if (didDec) {
                order.setTotal(order.getTotal() - item.getPrice());

                if (item.getQuantity() == 0) {
                    //cart.remove(index);
                } else {
                    cart.set(index, decr);
                }
            }
        }
        totalPrice.setText("Your total price is: " + Format.formatCurrency(order.getTotal()));
    }
    public void addQuantity(Item item) {
        cart = Item.incrementItem(item, cart);
        order.setTotal(order.getTotal() + item.getPrice());
        totalPrice.setText("Your total price is: " + Format.formatCurrency(order.getTotal()));
    }
    private void checkout() {
        removeEmptyItems(cart);
        ref_n = ref.push();
        orderId = ref_n.getKey();
        order.setItems(cart);
        order.setId(orderId);
        ref_n.setValue(order);
        Toast.makeText(ViewCart.this, "Order sent successfully!", Toast.LENGTH_SHORT).show();
        cart.clear();
        Intent i1 = new Intent(ViewCart.this, CustomerViewStores.class);
        i1.putExtra(getResources().getString(R.string.customers), (Parcelable) customer);
        startActivity(i1);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.back_cart:
                onBackPressed();
        }
    }



    @Override
    public void onBackPressed()
    {
        removeEmptyItems(cart);
        Intent i = new Intent(ViewCart.this, CustomerVisitStore.class);
        i.putExtra(getResources().getString(R.string.customers), (Parcelable) customer);
        i.putExtra(getResources().getString(R.string.stores), (Parcelable) store);
        i.putExtra(getResources().getString(R.string.orders), (Parcelable) order);
        i.putParcelableArrayListExtra(getResources().getString(R.string.items), (ArrayList<? extends Parcelable>) cart);
        startActivity(i);
    }
    private void removeEmptyItems(ArrayList<Item> items){
        for(int i = 0;i<items.size();i++){
            if(items.get(i).getQuantity()==0)
                items.remove(i);
        }
    }
}