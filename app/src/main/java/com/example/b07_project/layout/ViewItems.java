package com.example.b07_project.layout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.b07_project.R;
import com.example.b07_project.config.RecyclerViewItemsOwner;
import com.example.b07_project.models.Store;
import com.example.b07_project.utils.FirebaseUtil;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class ViewItems extends Fragment {
    private Store mStore;
    private RecyclerView mRecyclerView;
    private ExtendedFloatingActionButton addItemBtn;

    public ViewItems() {
        // Required empty public constructor
    }

    public static ViewItems newInstance(Store store, Context c) {
        ViewItems fragment = new ViewItems();
        Bundle args = new Bundle();
        args.putParcelable(c.getResources().getString(R.string.stores), store);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_view_items, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView_items);
        addItemBtn = view.findViewById(R.id.addItemBtn);

        addItemBtn.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), AddItem.class);
            i.putExtra("store", mStore);
            startActivity(i);
        });

        if (getArguments() != null) {
            mStore = getArguments().getParcelable(getResources().getString(R.string.stores));
        }

        FirebaseUtil.readItems((items, keys) -> {
            new RecyclerViewItemsOwner().setConfig(mRecyclerView, getActivity(), items, keys);
        }, mStore.getId(), getActivity());

        return view;
    }


}