
package com.example.b07_project.layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.b07_project.R;
import com.example.b07_project.config.FragmentAdapter;
import com.example.b07_project.models.Store;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class StoreActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView welcome, logoutButton;
    public Store store;
    public ViewPager2 viewPager2;
    public TabLayout tabs;
    public FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        welcome = findViewById(R.id.welcomeText);
        logoutButton = findViewById(R.id.logoutTextButton);
        logoutButton.setOnClickListener(this);
        tabs = findViewById(R.id.ordersTabs);
        viewPager2 = findViewById(R.id.pager);

        if(getIntent().getExtras() != null) {
            store = getIntent().getParcelableExtra(getResources().getString(R.string.stores));
            welcome.setText(store.name);

            FragmentManager fm = getSupportFragmentManager();
            adapter = new FragmentAdapter(fm, getLifecycle(), store, this);
            viewPager2.setAdapter(adapter);

            tabs.addTab(tabs.newTab().setText(getResources().getString(R.string.view_orders)));
            tabs.addTab(tabs.newTab().setText(getResources().getString(R.string.view_items)));
            tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager2.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {}

                @Override
                public void onTabReselected(TabLayout.Tab tab) {}
            });

            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    tabs.selectTab(tabs.getTabAt(position));
                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.logoutTextButton:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(StoreActivity.this, LoginActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }
}