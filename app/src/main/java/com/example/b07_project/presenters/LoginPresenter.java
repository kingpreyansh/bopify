package com.example.b07_project.presenters;

import android.content.Context;
import android.view.View;

import com.example.b07_project.Contract;
import com.example.b07_project.R;
import com.example.b07_project.layout.LoginActivity;
import com.example.b07_project.models.Customer;
import com.example.b07_project.models.Store;
import com.example.b07_project.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPresenter implements Contract.Presenter {

    private Contract.Model auth;
    private LoginActivity view;

    public LoginPresenter(Contract.Model auth, LoginActivity view) {
        this.auth = auth;
        this.view = view;
    }

    public void successDecider(boolean a, Contract.View v, User user) {
        if(a) {
            view.handleGetUser(user);
        }
        else {
            view.handleFailure();
        }
    }

    @Override
    public void handleFinishLogin(boolean isLoginSuccessful, String errorMessage)  {
        if (isLoginSuccessful) {
            auth.handleInitializeUser(view,this);
        } else {
            view.handleFinishLoading(errorMessage);
        }
    }

    @Override
    public void handleLogin() {
        String email = view.getEmail();
        String password = view.getPassword();

        view.handleStartLoading();
        auth.login(
                email,
                password,
                view,
                this
        );
    }

    public void recieveData(Contract.View view, String email, String pass) {
        auth.login(email, pass, view, this);
    }

}