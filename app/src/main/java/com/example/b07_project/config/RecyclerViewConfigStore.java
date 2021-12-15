package com.example.b07_project.config;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07_project.R;
import com.example.b07_project.layout.CustomerVisitStore;
import com.example.b07_project.models.Customer;
import com.example.b07_project.models.Store;

import java.util.ArrayList;
public class RecyclerViewConfigStore {
    private Context mContext;
    private StoresAdapter mStoresAdapter;
    private Customer customer;
    public void setConfig(RecyclerView recyclerView, Context context, ArrayList<Store> stores, ArrayList<String> keys, Customer customer){
        mContext = context;
        mStoresAdapter = new StoresAdapter(stores, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mStoresAdapter);
        this.customer = customer;
    }


    class StoreItemView extends RecyclerView.ViewHolder  {
        private Button mName;
        // send the id of the store clicked to CustomerStoreVisitActivity and initiate that activity

        public StoreItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).inflate(R.layout.store_list_item,parent,false));
            mName = (Button) itemView.findViewById(R.id.title_store);
        }
        public void bind(Store store){
            mName.setText(store.getName());
            mName.setOnClickListener(v -> {
                Intent i1 = new Intent(mContext, CustomerVisitStore.class);
                i1.putExtra(mContext.getResources().getString(R.string.stores), (Parcelable) store);
                i1.putExtra(mContext.getResources().getString(R.string.customers), (Parcelable) customer);
                mContext.startActivity(i1);
            });
        }
    }
    class StoresAdapter extends RecyclerView.Adapter<StoreItemView>{
        private ArrayList<Store> mStoreList;
        private ArrayList<String> mKeys;

        public StoresAdapter(ArrayList<Store> mStoreList, ArrayList<String> mKeys) {
            this.mStoreList = mStoreList;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public StoreItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new StoreItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull StoreItemView holder, int position) {
            holder.bind(mStoreList.get(position));

        }

        @Override
        public int getItemCount() {
            return mStoreList.size();
        }
    }
}
