package com.example.b07_project.models;

import android.app.Activity;
import android.content.Context;

import com.example.b07_project.Contract;
import com.example.b07_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Auth implements Contract.Model {
    public Auth() { }

    public interface GetUserCallback{
        void getUser(User user);
    }

    public void login(String email, String password, Contract.View view, Contract.Presenter presenter) {
        FirebaseAuth mAuth =  FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) view,
                        task -> presenter.handleFinishLogin(
                                task.isSuccessful(),
                                task.isSuccessful() ? null : "Login failed: " + task.getException().getMessage()
                        )
                );
    }

    public void handleInitializeUser(Contract.View view, Contract.Presenter presenter) {
        FirebaseAuth mAuth =  FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        DatabaseReference mDb = FirebaseDatabase.getInstance().getReference();
        mDb.child(((Context) view).getResources().getString(R.string.users)).child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot d = task.getResult();
                String type = (String) d.child(((Context) view).getResources().getString(R.string.type)).getValue();
                if (type == null) {
                    view.handleGetUser(null);
                }
                if (type.equals(((Context) view).getResources().getString(R.string.customers))) {
                    Customer customer = new Customer(
                            (String) d.child("name").getValue(),
                            currentUser.getEmail(),
                            currentUser.getUid()
                    );
                    view.handleGetUser(customer);
                } else if (type.equals(((Context) view).getResources().getString(R.string.stores))) {
                    Store store = new Store(
                            (String) d.child("name").getValue(),
                            currentUser.getEmail(),
                            currentUser.getUid()
                    );
                    view.handleGetUser(store);
                } else {
                    view.handleGetUser(null);
                }
            } else {
                view.handleGetUser(null);
            }
        });
    }
}
