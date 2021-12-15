package com.example.b07_project.layout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.b07_project.R;
import com.example.b07_project.utils.FirebaseUtil;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUtil.handleAuth(this);
    }
}