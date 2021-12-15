package com.example.b07_project.layout;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.b07_project.R;
import com.example.b07_project.config.RecyclerViewConfigOrders;
import com.example.b07_project.models.Store;
import com.example.b07_project.utils.FirebaseUtil;


public class ViewOrders extends Fragment {
    private Store mStore;
    private RecyclerView mRecyclerView;
    private ToggleButton mCompletedOrdersCheck ;

    public ViewOrders() {
    }

    public static ViewOrders newInstance(Store store, Context c) {
        ViewOrders fragment = new ViewOrders();
        Bundle args = new Bundle();
        args.putParcelable(c.getResources().getString(R.string.stores), store);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_view_orders, container, false);
        mCompletedOrdersCheck = (ToggleButton) view.findViewById(R.id.toggle_button);
        mRecyclerView = view.findViewById(R.id.recyclerView_items);

        mCompletedOrdersCheck = (ToggleButton) view.findViewById(R.id.toggle_button);
        mCompletedOrdersCheck.setTextOn("Completed orders");
        mCompletedOrdersCheck.setTextOff("Incomplete orders");
        mCompletedOrdersCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                runFirebase(isChecked);
            }
        });

        if (getArguments() != null) {
            mStore = getArguments().getParcelable(getResources().getString(R.string.stores));
        }
        runFirebase(false); // required for startup
        return view;
    }
    public void runFirebase(boolean showCompleted){
        FirebaseUtil.readOrders((orders, keys) -> {
            new RecyclerViewConfigOrders().setConfig(mRecyclerView, getActivity(), orders, keys);
        },  mStore.getId(),getActivity(),showCompleted);
    }
}