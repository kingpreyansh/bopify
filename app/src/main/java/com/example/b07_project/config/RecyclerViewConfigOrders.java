package com.example.b07_project.config;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07_project.R;
import com.example.b07_project.layout.CheckOrder;
import com.example.b07_project.models.Order;
import com.example.b07_project.utils.Format;

import java.util.ArrayList;

public class RecyclerViewConfigOrders extends AppCompatActivity {
    private Context mContext;
    private OrdersAdapter mOrdersAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, ArrayList<Order> orders, ArrayList<String> keys){
        mContext = context;
        mOrdersAdapter = new OrdersAdapter(orders, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mOrdersAdapter);
    }

    class OrderItemView extends RecyclerView.ViewHolder  {
        private TextView mName;
        private TextView mPrice;
        private TextView mDate;
        private TextView mTotalItems;
        private Button mCheckOrder;

        public OrderItemView(ViewGroup parent){

            super(LayoutInflater.from(mContext).inflate(R.layout.order_list_item,parent,false));
            mCheckOrder = (Button) itemView.findViewById(R.id.check_order);
            mName = (TextView) itemView.findViewById(R.id.title_order);
            mPrice = (TextView) itemView.findViewById(R.id.total_order_price);
            mDate = (TextView) itemView.findViewById(R.id.date_created);
            mTotalItems = (TextView) itemView.findViewById(R.id.total_items);
        }
        public void bind(Order order){
            mName.setText(order.getCustomerName());
            mPrice.setText("Total Price: "+ Format.formatCurrency(order.getTotal()));
            mDate.setText("Created on: "+order.getCreatedAt());
            mTotalItems.setText("Total number of items: "+order.getItems().size());
            mCheckOrder.setOnClickListener(v -> {
                Intent i1 = new Intent(mContext, CheckOrder.class);
                i1.putExtra(mContext.getResources().getString(R.string.orders), (Parcelable) order);
                mContext.startActivity(i1);
            });
        }
    }
    class OrdersAdapter extends RecyclerView.Adapter<OrderItemView>{
        private ArrayList<Order> mOrderList;
        private ArrayList<String> mKeys;

        public OrdersAdapter(ArrayList<Order> mOrderList, ArrayList<String> mKeys) {
            this.mOrderList = mOrderList;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public OrderItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new OrderItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderItemView holder, int position) {
            holder.bind(mOrderList.get(position));

        }

        @Override
        public int getItemCount() {
            return mOrderList.size();
        }
    }


}
