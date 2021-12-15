
package com.example.b07_project.layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07_project.R;
import com.example.b07_project.models.Customer;
import com.example.b07_project.config.RecyclerViewConfigStore;
import com.example.b07_project.utils.FirebaseUtil;
import com.google.firebase.auth.FirebaseAuth;

public class CustomerViewStores extends AppCompatActivity implements View.OnClickListener {
    private TextView welcome, logoutButton, emailText;
    private Customer customer;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_stores);
        FirebaseUtil.readStores((stores, keys) -> new RecyclerViewConfigStore()
                .setConfig(
                        mRecyclerView,
                        CustomerViewStores.this,
                        stores,
                        keys,
                        customer),
                this);

        welcome = findViewById(R.id.welcomeText);
        emailText = findViewById(R.id.emailTextWelcome);
        logoutButton = findViewById(R.id.logoutTextButton);
        logoutButton.setOnClickListener(this);

        if(getIntent().getExtras() != null) {
            customer = getIntent().getParcelableExtra(getResources().getString(R.string.customers));
        }

        welcome.setText("Welcome " + customer.name );
        emailText.setText("Logged in as " + customer.email);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.logoutTextButton:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(CustomerViewStores.this, LoginActivity.class));
                break;
        }
    }


    @Override
    public void onBackPressed() {
        return;
    }
}