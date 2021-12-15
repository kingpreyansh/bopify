package com.example.b07_project.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.b07_project.R;
import com.example.b07_project.layout.CustomerViewStores;
import com.example.b07_project.layout.LoginActivity;
import com.example.b07_project.layout.RegisterUser;
import com.example.b07_project.layout.StoreActivity;
import com.example.b07_project.models.Customer;
import com.example.b07_project.models.Item;
import com.example.b07_project.models.Order;
import com.example.b07_project.models.Store;
import com.example.b07_project.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseUtil {
    public interface DataStatusItems {
        void DataIsLoaded(ArrayList<Item> items, ArrayList<String> keys);
    }
    public interface DataStatusOrders {
        void DataIsLoaded(ArrayList<Order> orders, ArrayList<String> keys);
    }
    public interface DataStatusOrder {
        void DataIsLoaded(Order order);
    }
    public interface DataStatusStores {
        void DataIsLoaded(ArrayList<Store> stores, ArrayList<String> keys);
    }

    public static void readItems(final DataStatusItems dataStatus, String storeId, Context context){
        ArrayList<Item> items = new ArrayList<Item>();
        FirebaseDatabase fbDb = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = fbDb.getReference(context.getResources().getString(R.string.items));
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                ArrayList<String> keys = new ArrayList<String>();
                for(DataSnapshot keyNode : snapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Item item = keyNode.getValue(Item.class);
                    if(item.getStoreId().equals(storeId))
                        items.add(item);
                }
                dataStatus.DataIsLoaded(items, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    
    public static void readOrders(final DataStatusOrders dataStatus, String storeId, Context context,Boolean showCompleted){
        ArrayList<Order> orders = new ArrayList<Order>();
        FirebaseDatabase fbDb = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = fbDb.getReference(context.getResources().getString(R.string.orders));
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orders.clear();
                ArrayList<String> keys = new ArrayList<String>();
                for(DataSnapshot keyNode : snapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Order order = keyNode.getValue(Order.class);
                    if(order.getStoreId().equals(storeId) && order.getIsCompleted()==showCompleted)
                        orders.add(order);
                }
                dataStatus.DataIsLoaded(orders, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public static void getOrder(final DataStatusOrder dataStatus, String orderId, Context context){
        try {
            DatabaseReference mDb = FirebaseDatabase.getInstance().getReference();
            mDb.child(context.getResources().getString(R.string.orders)).child(orderId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot d = task.getResult();
                    Order order = d.getValue(Order.class);
                    if(order!=null)
                        dataStatus.DataIsLoaded(order );
                } else {
                    Log.w("Exception getting order", "Order task is not successful");
                }
            });
        } catch (Exception e) {
            Log.w("Exception getting order", e.getMessage());
        }

    }
    public static void changeOrder(Order order, Context context){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(context.getResources().getString(R.string.orders)).child(order.getId());
        ref.setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Order updated!", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Order update failed", Toast.LENGTH_SHORT).show();
                });
    }
    
    public static void readStores(final DataStatusStores dataStatus, Context context){
        ArrayList<Store> stores = new ArrayList<Store>();
        FirebaseDatabase fbDb = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = fbDb.getReference(context.getResources().getString(R.string.users));
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stores.clear();
                ArrayList<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : snapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Store store = keyNode.getValue(Store.class);
                    if(store.type.equals(context.getResources().getString(R.string.stores)))
                        stores.add(store);
                }
                dataStatus.DataIsLoaded(stores, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public static void register(String email, String password, String type, String fullName, Context context) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        User user = new User(fullName, email, id, type);
                        FirebaseDatabase.getInstance().getReference(context.getResources().getString(R.string.users))
                                .child(id)
                                .setValue(user).addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()){
                                        Toast.makeText(context, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                        handleAuth(context);
                                    } else {
                                        Toast.makeText(context, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                        Log.w("Register Task Failure", "Failure");
                                        handleErrorAndLogout(context);
                                    }
                                });
                    } else {
                        Toast.makeText(context, "Register Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.w("Register Ref Failure " + task.getException().getMessage(), "Failure");
                        handleLoadingFinished(context);
                    }
                });
    }

    public static void handleErrorAndLogout(Context context) {
        FirebaseAuth.getInstance().signOut();
        handleLoadingFinished(context);
        Toast.makeText(context, "Failed to login! Try again!", Toast.LENGTH_LONG).show();
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    public static void login(String email, String password, Context context) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, (OnCompleteListener<AuthResult>) task -> {
                    if (task.isSuccessful()) {
                        Log.w("signInWithEmail:success", "Success");
                        handleAuth(context);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("signInWithEmail:failure", task.getException());
                        Toast.makeText(context, "Login failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                        handleLoadingFinished(context);
                    }
                });
    }

    private static void handleLoadingFinished(Context context) {
        if (context instanceof LoginActivity) {
            ((LoginActivity) context).handleFinishLoading(null);
        } else  if (context instanceof RegisterUser) {
            ((RegisterUser) context).handleFinishLoading();
        }
    }

    public static void handleAuth(Context context) {
        FirebaseAuth mAuth =  FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        try {
            if (currentUser != null) {
                String userId = currentUser.getUid();
                DatabaseReference mDb = FirebaseDatabase.getInstance().getReference();
                mDb.child(context.getResources().getString(R.string.users)).child(userId).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DataSnapshot d = task.getResult();
                        String type = (String) d.child(context.getResources().getString(R.string.type)).getValue();
                        if (type == null) {
                            handleErrorAndLogout(context);
                        }
                        if (type.equals(context.getResources().getString(R.string.customers))) {
                            Customer customer = new Customer(
                                    (String) d.child("name").getValue(),
                                    currentUser.getEmail(),
                                    currentUser.getUid()
                            );

                            if (customer != null) {
                                Log.w("firebase:success-cust", task.getResult().getValue() + currentUser.getUid());
                                Intent i1 = new Intent(context, CustomerViewStores.class);
                                i1.putExtra(context.getResources().getString(R.string.customers), (Parcelable) customer);
                                context.startActivity(i1);
                            } else {
                                Log.w("Customer is null", "Login");
                                handleErrorAndLogout(context);
                            }
                        } else if (type.equals(context.getResources().getString(R.string.stores))) {
                            Store store = new Store(
                                    (String) d.child("name").getValue(),
                                    currentUser.getEmail(),
                                    currentUser.getUid()
                            );

                            if (store != null) {
                                 Log.w("firebase:success-cust", String.valueOf(task.getResult().getValue()));
                                Intent i1 = new Intent(context, StoreActivity.class);
                                i1.putExtra(context.getResources().getString(R.string.stores), (Parcelable) store);
                                context.startActivity(i1);
                            } else {
                                handleErrorAndLogout(context);
                            }
                        } else {
                            handleErrorAndLogout(context);
                        }
                    } else {
                        handleErrorAndLogout(context);
                    }
                });
            } else {
                handleLoadingFinished(context);
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        } catch (Exception e) {
            Log.w("Exception handling auth", e.getMessage());
            handleErrorAndLogout(context);
        }
    }
}
