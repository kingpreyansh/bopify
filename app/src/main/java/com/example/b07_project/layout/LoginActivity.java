package com.example.b07_project.layout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.b07_project.Contract;
import com.example.b07_project.R;
import com.example.b07_project.models.Auth;
import com.example.b07_project.models.Customer;
import com.example.b07_project.models.Store;
import com.example.b07_project.models.User;
import com.example.b07_project.presenters.LoginPresenter;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Contract.View {
    private TextView register;
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private ProgressBar progressBar;

    private Contract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = findViewById(R.id.register);
        register.setOnClickListener(this);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        passwordEditText = findViewById(R.id.loginPassword);
        emailEditText = findViewById(R.id.loginEmail);
        progressBar = findViewById(R.id.loginProgessBar);

        progressBar.setVisibility(View.INVISIBLE);
        passwordEditText.setEnabled(true);
        emailEditText.setEnabled(true);
        loginButton.setEnabled(true);

        presenter = new LoginPresenter(new Auth(), this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.loginButton:
                login();
                break;
        }
    }

    @Override
    public void handleFailure() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Failed to login! Try again!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleGetCustomer(Customer customer) {
        Log.w("firebase:success-cust", String.valueOf(customer));
        Intent i1 = new Intent(this, CustomerViewStores.class);
        i1.putExtra(getResources().getString(R.string.customers), (Parcelable) customer);
        startActivity(i1);
    }

    @Override
    public void handleGetStore(Store store) {
        Log.w("firebase:success-cust", String.valueOf(store));
        Intent i1 = new Intent(this, StoreActivity.class);
        i1.putExtra(getResources().getString(R.string.stores), (Parcelable) store);
        startActivity(i1);
    }

    @Override
    public void handleGetUser(User user) {
        if (user == null) {
            handleFailure();
            handleFinishLoading(null);
        }

        if (user.type.equals(getResources().getString(R.string.customers))) {
            handleGetCustomer((Customer) user);
            handleFinishLoading(null);
        } else if (user.type.equals(getResources().getString(R.string.stores))) {
            handleGetStore((Store) user);
            handleFinishLoading(null);
        } else {
            handleFailure();
            handleFinishLoading(null);
        }
    }


    @Override
    public void handleFinishLoading(String toastMessage) {
        progressBar.setVisibility(View.INVISIBLE);
        loginButton.setEnabled(true);
        emailEditText.setEnabled(true);
        passwordEditText.setEnabled(true);

        if (toastMessage != null)
            Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleStartLoading() {
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);
        emailEditText.setEnabled(false);
        passwordEditText.setEnabled(false);
    }

    public String getEmail(){
        String email = emailEditText.getText().toString().trim();
        return email;
    }

    public String getPassword(){
        String password  = passwordEditText.getText().toString().trim();
        return password;
    }

    @Override
    public void onBackPressed() {
        return;
    }

    public void login() {
        String email = emailEditText.getText().toString().trim();
        String password  = passwordEditText.getText().toString().trim();

        if(email.isEmpty()) {
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Please provide valid email!");
            emailEditText.requestFocus();
            return;
        }

        if(password.isEmpty()){
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }
        presenter.handleLogin();
    }

}