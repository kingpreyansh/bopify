package com.example.b07_project.layout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.b07_project.R;
import com.example.b07_project.models.Item;
import com.example.b07_project.models.Store;
import com.example.b07_project.utils.Format;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddItem extends AppCompatActivity {

    String curr_sid;
    Store curr_store;
    Double price;
    public FirebaseDatabase mDatabase;
    String id;
    DatabaseReference ref, ref_n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        mDatabase = com.google.firebase.database.FirebaseDatabase.getInstance();
        if(getIntent().getExtras() != null) {
           curr_store = getIntent().getParcelableExtra("store");
        }
        curr_sid = curr_store.id;
    }

    public void goBack(View v){
        this.onBackPressed();
    }

    public void addItem(View view){
        add_in();
    }

    private void add_in(){
        EditText nameEdit = findViewById(R.id.editTextPersonName);
        EditText priceEdit = findViewById(R.id.editTextPrice);
        EditText brandEdit = findViewById(R.id.editTextBrand);

        String name = nameEdit.getText().toString();
        if (priceEdit.getText().toString().isEmpty()){
            priceEdit.setError("Price Cannot be Empty");
            priceEdit.requestFocus();
            return;
        } else {
            price = Format.bankersRound(Double.parseDouble(priceEdit.getText().toString()));
        }
        String brand = brandEdit.getText().toString();



        if (name.isEmpty()){
            nameEdit.setError("Name cannot be empty");
            nameEdit.requestFocus();
            return;
        }

        if (brand.isEmpty()){
            brandEdit.setError("Brand cannot be empty");
            brandEdit.requestFocus();
            return;
        }

       ref = mDatabase.getReference().child("Items");
       ref_n = ref.push();
       id = ref_n.getKey();
       Item it = new Item(price, brand, name, id, curr_sid, 1);
       ref_n.setValue(it);
       Toast.makeText(AddItem.this, "Item Added to Store!", Toast.LENGTH_LONG).show();


       nameEdit.setText("");
       priceEdit.setText("");
       brandEdit.setText("");
    }



}
