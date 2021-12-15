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
import com.example.b07_project.config.RecyclerViewConfigItems;
import com.example.b07_project.models.Customer;
import com.example.b07_project.models.Item;
import com.example.b07_project.models.Order;
import com.example.b07_project.models.Store;
import com.example.b07_project.utils.FirebaseUtil;
import com.example.b07_project.utils.Format;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomerVisitStore extends AppCompatActivity implements View.OnClickListener{
    private Store store;
    private Order order;
    private TextView storeName;
    private TextView totalPrice;
    private TextView empty_store;
    private Button viewCart;
    private RecyclerView mRecyclerView;
    private ArrayList<Item> cart = new ArrayList<Item>();
    private Customer customer;
    public String orderId;
    double tempTotalPrice=0;
    DatabaseReference ref;
    DatabaseReference ref_n;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_store_visit);

        viewCart = findViewById(R.id.viewCart);

        totalPrice = findViewById(R.id.cart_total);

        empty_store = findViewById(R.id.empty_store);
        empty_store.setVisibility(View.GONE);



        if(getIntent().getExtras() != null) {
            store = getIntent().getParcelableExtra(getResources().getString(R.string.stores));
            customer = getIntent().getParcelableExtra(getResources().getString(R.string.customers));
            if(getIntent().getParcelableExtra(getResources().getString(R.string.orders))!=null) {
                //passes in order so that the total price can remain the same from ViewCart
                order = getIntent().getParcelableExtra(getResources().getString(R.string.orders));
                tempTotalPrice = order.getTotal();
            }
            if(getIntent().getParcelableArrayListExtra(getResources().getString(R.string.items))!=null){
                cart = getIntent().getParcelableArrayListExtra(getResources().getString(R.string.items));
            }
        }
        totalPrice.setText("Your total price is: " + Format.formatCurrency(tempTotalPrice));
        storeName = findViewById(R.id.storeName);
        storeName.setText("Items of " + store.name);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_items);
        FirebaseUtil.readItems((items, keys) -> {
            new RecyclerViewConfigItems().setConfig(mRecyclerView, CustomerVisitStore.this, items, keys);
            if (items.isEmpty()){
                empty_store.setVisibility(View.VISIBLE);
                empty_store.setText("This store has no items yet");
            }
        }, store.getId(), this);
        ref = FirebaseDatabase.getInstance().getReference().child(getResources().getString(R.string.orders));
        removeEmptyItems(cart);
        viewCart.setOnClickListener(v -> goViewCart());
    }



    private void goViewCart() {
        if(cart.size() == 0) {
            Toast.makeText(CustomerVisitStore.this, "Cart is empty", Toast.LENGTH_SHORT).show();
        }
        else{
            ref_n = ref.push();
            orderId = ref_n.getKey();
            Date str = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
            String date = formatter.format(str);
            Order order = new Order(
                    tempTotalPrice,
                    cart,
                    customer.id,
                    store.getId(),
                    date,
                    false,
                    orderId,
                    customer.name);
            Intent i = new Intent(CustomerVisitStore.this, ViewCart.class);
            i.putExtra(getResources().getString(R.string.customers), (Parcelable) customer);
            i.putExtra(getResources().getString(R.string.stores), (Parcelable) store);
            i.putExtra(getResources().getString(R.string.orders), (Parcelable) order);
            i.putParcelableArrayListExtra(getResources().getString(R.string.items), (ArrayList<? extends Parcelable>) cart);
            startActivity(i);
        }
    }

    public void addToCart(Item item){
        cart = Item.incrementItem(item, cart);
        tempTotalPrice += item.getPrice();
        tempTotalPrice = Format.bankersRound(tempTotalPrice);
        totalPrice.setText("Your total price is:" + Format.formatCurrency(tempTotalPrice));
        return;
    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(CustomerVisitStore.this, CustomerViewStores.class);
        i.putExtra(getResources().getString(R.string.customers), (Parcelable) customer);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.back_csv:
                onBackPressed();
        }
    }
    private void removeEmptyItems(ArrayList<Item> items){
        for(int i = 0;i<items.size();i++){
            if(items.get(i).getQuantity()==0)
                items.remove(i);
        }
    }
}