package com.example.b07_project.layout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.b07_project.R;
import com.example.b07_project.utils.FirebaseUtil;
import com.google.android.material.tabs.TabLayout;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    private TextView banner, registerUser;
    private EditText editTextFullName, editTextEmail, editTextPassword;
    private String type;
    private ProgressBar progressBar;
    private TabLayout typeTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        type = getResources().getString(R.string.customers);
        typeTabs = findViewById(R.id.typeTabs);
        typeTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText() == getResources().getString((R.string.customer))) {
                    type = getResources().getString(R.string.customers);
                } else if (tab.getText() == getResources().getString((R.string.store))) {
                    type = getResources().getString(R.string.stores);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        registerUser = findViewById(R.id.registerStore);
        registerUser.setOnClickListener(this);

        editTextFullName = findViewById(R.id.name);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.registerProgressBar);

        progressBar.setVisibility(View.INVISIBLE);
        editTextPassword.setEnabled(true);
        editTextEmail.setEnabled(true);
        editTextFullName.setEnabled(true);
        registerUser.setEnabled(true);
    }


    public void handleFinishLoading() {
        progressBar.setVisibility(View.INVISIBLE);
        editTextPassword.setEnabled(true);
        editTextEmail.setEnabled(true);
        editTextFullName.setEnabled(true);
        registerUser.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterUser.this, LoginActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.registerStore:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();

        if(fullName.isEmpty()){
            editTextFullName.setError("Full name is required!");
            editTextFullName.requestFocus();
            return;
        }

        if(email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email!");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            editTextPassword.setError("Min password length should be 6 characters!");
            editTextPassword.requestFocus();
            return;
        }

        FirebaseUtil.register(email, password, this.type, fullName, this);
        progressBar.setVisibility(View.VISIBLE);
        editTextPassword.setEnabled(false);
        editTextEmail.setEnabled(false);
        editTextFullName.setEnabled(false);
        registerUser.setEnabled(false);
    }
}