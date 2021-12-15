package com.example.b07_project;

import android.content.Context;

import com.example.b07_project.models.Auth;
import com.example.b07_project.models.Customer;
import com.example.b07_project.models.Store;
import com.example.b07_project.models.User;

public interface Contract {
    interface Model{
        void login(String email, String password, Contract.View view, Presenter presenter);
        void handleInitializeUser(View view, Presenter presenter);
    }

    interface View {
        void handleFinishLoading(String toastMessage);
        void handleStartLoading();
        void handleGetCustomer(Customer customer);
        void handleGetStore(Store store);
        void handleGetUser(User user);
        void handleFailure();
        void login();
    }

    interface Presenter{
        void handleLogin();
        void handleFinishLogin(boolean isLoginSuccessful, String errorMessage);
    }
}
