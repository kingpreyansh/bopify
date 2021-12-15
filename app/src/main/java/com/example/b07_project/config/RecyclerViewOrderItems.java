package com.example.b07_project.config;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07_project.R;
import com.example.b07_project.models.Item;
import com.example.b07_project.utils.Format;

import java.util.ArrayList;
public class RecyclerViewOrderItems {
    private Context mContext;
    private RecyclerViewOrderItems.OrderItemsAdapterOwner mItemsAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, ArrayList<Item> items){
        mContext = context;
        mItemsAdapter = new RecyclerViewOrderItems.OrderItemsAdapterOwner(items);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mItemsAdapter);
    }

    class ItemViewOwner extends RecyclerView.ViewHolder  {
        private TextView itemName, itemQuantity;
        private TextView itemPrice;
        private TextView itemBrand;

        public ItemViewOwner(ViewGroup parent){

            super(LayoutInflater.from(mContext).inflate(R.layout.orderitem_list_item,parent,false));

            itemName = (TextView) itemView.findViewById(R.id.order_itm_name);
            itemPrice = (TextView) itemView.findViewById(R.id.order_item_price);
            itemBrand = (TextView) itemView.findViewById(R.id.order_item_brand);
            itemQuantity = (TextView) itemView.findViewById(R.id.order_item_quantity);
        }
        public void bind(Item item){
            itemName.setText(item.getName());
            itemQuantity.setText("Quantity: " + item.getQuantity());
            itemPrice.setText(Format.formatCurrency(item.getPrice()));
            itemBrand.setText(item.getBrand());
        }
    }
    class OrderItemsAdapterOwner extends RecyclerView.Adapter<RecyclerViewOrderItems.ItemViewOwner>{
        private ArrayList<Item> item_list;
        public OrderItemsAdapterOwner(ArrayList<Item> mItemList) {
            this.item_list = mItemList;
        }

        @NonNull
        @Override
        public ItemViewOwner onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewOwner(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewOrderItems.ItemViewOwner holder, int position) {
            holder.bind(item_list.get(position));

        }

        @Override
        public int getItemCount() {
            return item_list.size();
        }
    }
}
