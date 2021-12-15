package com.example.b07_project.config;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.b07_project.layout.ViewItems;
import com.example.b07_project.layout.ViewOrders;
import com.example.b07_project.models.Store;

public class FragmentAdapter extends FragmentStateAdapter {
    Store mStore;
    Context context;
    public FragmentAdapter(
            @NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, Store store, Context c
    ) {
        super(fragmentManager, lifecycle);
        mStore = store;
        context = c;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return ViewItems.newInstance(mStore, context);
        }

        return ViewOrders.newInstance(mStore, context);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
