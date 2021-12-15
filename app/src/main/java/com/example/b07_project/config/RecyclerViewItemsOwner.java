package com.example.b07_project.config;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07_project.R;
import com.example.b07_project.layout.AddItem;
import com.example.b07_project.layout.CustomerVisitStore;
import com.example.b07_project.layout.EditItem;
import com.example.b07_project.models.Item;
import com.example.b07_project.utils.Format;

import java.util.ArrayList;

public class RecyclerViewItemsOwner {
    private Context mContext;
    private RecyclerViewItemsOwner.ItemsAdapterOwner mItemsAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, ArrayList<Item> items, ArrayList<String> keys){
        mContext = context;
        mItemsAdapter = new RecyclerViewItemsOwner.ItemsAdapterOwner(items, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mItemsAdapter);
    }

    class ItemViewOwner extends RecyclerView.ViewHolder  {
        private TextView itemName;
        private TextView itemPrice;
        private TextView itemBrand;
        private Button editButton;

        public ItemViewOwner(ViewGroup parent){

            super(LayoutInflater.from(mContext).inflate(R.layout.items_owner,parent,false));

            itemName = (TextView) itemView.findViewById(R.id.itm_name);
            itemPrice = (TextView) itemView.findViewById(R.id.item_price);
            itemBrand = (TextView) itemView.findViewById(R.id.item_brand);
            editButton = (Button) itemView.findViewById(R.id.edit_itm);
        }
        public void bind(Item item){
            itemName.setText(item.getName());
            itemPrice.setText(Format.formatCurrency(item.getPrice()));
            itemBrand.setText(item.getBrand());
            editButton.setText("edit");
            editButton.setOnClickListener(v -> {
                Intent it = new Intent(mContext, EditItem.class);
                it.putExtra("item_id", item.getId());
                it.putExtra("item_name", item.getName());
                it.putExtra("item_brand", item.getBrand());
                it.putExtra("item_price", item.getPrice());
                System.out.println(item.getName());
                mContext.startActivity(it);
            });
        }
    }
    class ItemsAdapterOwner extends RecyclerView.Adapter<RecyclerViewItemsOwner.ItemViewOwner>{
        private ArrayList<Item> item_list;
        private ArrayList<String> mKeys;

        public ItemsAdapterOwner(ArrayList<Item> mItemList, ArrayList<String> mKeys) {
            this.item_list = mItemList;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public ItemViewOwner onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewOwner(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewItemsOwner.ItemViewOwner holder, int position) {
            holder.bind(item_list.get(position));
        }

        @Override
        public int getItemCount() {
            return item_list.size();
        }
    }

}
