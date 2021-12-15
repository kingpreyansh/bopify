package com.example.b07_project.config;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07_project.R;
import com.example.b07_project.layout.ViewCart;
import com.example.b07_project.models.Item;
import com.example.b07_project.utils.Format;

import java.util.ArrayList;

public class RecyclerViewConfigCart {
    private Context mContext;
    private CartAdapter mCartAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, ArrayList<Item> items){
        mContext = context;
        mCartAdapter = new CartAdapter(items);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mCartAdapter);
    }

    class ItemView extends RecyclerView.ViewHolder  {
        private TextView mItemName, mItemPrice, mItemBrand, itemQuantity, minusButton, plusButton;

        public ItemView(ViewGroup parent){

            super(LayoutInflater.from(mContext).inflate(R.layout.item_list_cart,parent,false));

            mItemName = (TextView) itemView.findViewById(R.id.cart_title_item2);
            mItemPrice = (TextView) itemView.findViewById(R.id.cart_itemPrice2);
            mItemBrand = (TextView) itemView.findViewById(R.id.cart_itemBrand2);
            minusButton = (TextView) itemView.findViewById(R.id.quanMinus);
            itemQuantity = (TextView) itemView.findViewById(R.id.itemQuantityCart);
            plusButton = (TextView) itemView.findViewById(R.id.quanPlus);
        }
        public void bind(Item item){
            mItemName.setText(item.getName());
            mItemPrice.setText(Format.formatCurrency(item.getPrice()));
            mItemBrand.setText(item.getBrand());
            itemQuantity.setText(item.getQuantity() + "");
            minusButton.setOnClickListener(v -> {
                ViewCart viewCart =(ViewCart) mContext;
                viewCart.minusQuantity(item);
                itemQuantity.setText(item.getQuantity() + "");
            });
            plusButton.setOnClickListener(v -> {
                ViewCart viewCart =(ViewCart) mContext;
                viewCart.addQuantity(item);
                itemQuantity.setText(item.getQuantity() + "");
            });
        }
    }
    class CartAdapter extends RecyclerView.Adapter<ItemView>{
        private ArrayList<Item> mItemList;

        public CartAdapter(ArrayList<Item> mItemList) {
            this.mItemList = mItemList;
        }

        @NonNull
        @Override
        public ItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemView holder, int position) {
            holder.bind(mItemList.get(position));
        }

        @Override
        public int getItemCount() {
            return mItemList.size();
        }
    }
}
