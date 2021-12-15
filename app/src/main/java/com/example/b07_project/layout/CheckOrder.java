package com.example.b07_project.layout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.b07_project.R;

import com.example.b07_project.config.RecyclerViewOrderItems;
import com.example.b07_project.models.Order;
import com.example.b07_project.utils.FirebaseUtil;

public class CheckOrder extends AppCompatActivity implements View.OnClickListener {
    private Order order;
    private RecyclerView mRecyclerView;
    private Button completeOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_order);

        if(getIntent().getExtras() != null) {
            order = getIntent().getParcelableExtra(getResources().getString(R.string.orders));
            completeOrder = (Button) findViewById(R.id.complete_order);
            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_orderitem);
            if(order.getIsCompleted())
                completeOrder.setText("Go back");
            FirebaseUtil.getOrder((order) -> {
                new RecyclerViewOrderItems().setConfig(mRecyclerView, CheckOrder.this, order.getItems());
                completeOrder.setOnClickListener(v -> {
                    if (!order.getIsCompleted()) {
                        order.setIsCompleted(true);
                        FirebaseUtil.changeOrder(order, CheckOrder.this);
                    }
                    onBackPressed();
                });
            },order.getId(),CheckOrder.this);
        }
    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.ownor_back:
                onBackPressed();
        }

    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}