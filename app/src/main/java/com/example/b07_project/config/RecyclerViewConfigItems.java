package com.example.b07_project.config;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.b07_project.R;
import com.example.b07_project.layout.CustomerVisitStore;
import com.example.b07_project.models.Item;
import com.example.b07_project.utils.Format;

import java.util.ArrayList;
//this is about the recycler
public class RecyclerViewConfigItems {
    private Context mContext;
    private ItemsAdapter mItemsAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, ArrayList<Item> items, ArrayList<String> keys){
        mContext = context;
        mItemsAdapter = new ItemsAdapter(items, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mItemsAdapter);
    }

    class ItemView extends RecyclerView.ViewHolder  {
        private TextView mItemName;
        private TextView mItemPrice;
        private TextView mItemBrand;
        private Button mBuyButton;

        public ItemView(ViewGroup parent){

            super(LayoutInflater.from(mContext).inflate(R.layout.item_list_customer,parent,false));

            mItemName = (TextView) itemView.findViewById(R.id.title_item2);
            mItemPrice = (TextView) itemView.findViewById(R.id.itemPrice2);
            mItemBrand = (TextView) itemView.findViewById(R.id.itemBrand2);
            mBuyButton = (Button) itemView.findViewById(R.id.buy_button2);
        }
        public void bind(Item item){
            mItemName.setText(item.getName());
            mItemPrice.setText(Format.formatCurrency(item.getPrice()));
            mItemBrand.setText(item.getBrand());
            mBuyButton.setText("Add");
            mBuyButton.setOnClickListener(v -> {
                CustomerVisitStore CSVA =(CustomerVisitStore)mContext;
                CSVA.addToCart(item);
                Toast.makeText(CSVA, item.getName() + " added to cart", Toast.LENGTH_SHORT);
            });
        }
    }
    class ItemsAdapter extends RecyclerView.Adapter<ItemView>{
        private ArrayList<Item> mItemList;
        private ArrayList<String> mKeys;

        public ItemsAdapter(ArrayList<Item> mItemList, ArrayList<String> mKeys) {
            this.mItemList = mItemList;
            this.mKeys = mKeys;
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
