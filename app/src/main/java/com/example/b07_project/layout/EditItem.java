package com.example.b07_project.layout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.b07_project.R;
import com.example.b07_project.models.Item;
import com.example.b07_project.utils.Format;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditItem extends AppCompatActivity {

    String curr_id, curr_name, curr_brand;
    Double curr_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            curr_id = extras.getString("item_id");
            curr_name= extras.getString("item_name");
            curr_price= extras.getDouble("item_price");
            curr_brand= extras.getString("item_brand");

        }

        EditText name1 = findViewById(R.id.updateName);
        EditText price1 = findViewById(R.id.updatePrice);
        EditText brand1 = findViewById(R.id.updateBrand);

        name1.setText(curr_name);
        System.out.println(curr_name);
        System.out.println(curr_id);
        price1.setText(String.valueOf(curr_price));
        brand1.setText(curr_brand);

    }

    public void goBack(View v){
        this.onBackPressed();
    }

    public void updateItem(View v){

        EditText name = findViewById(R.id.updateName);
        EditText price = findViewById(R.id.updatePrice);
        EditText brand = findViewById(R.id.updateBrand);

        if (price.getText().toString().isEmpty()){
            price.setError("Price Cannot be Empty");
            price.requestFocus();
            return;
        }


        String name_string = name.getText().toString();
        if (name_string.isEmpty()){
            name.setError("Name cannot be empty");
            name.requestFocus();
            return;
        }

        String brand_string = brand.getText().toString();
        if (brand_string.isEmpty()){
            brand.setError("Brand cannot be empty");
            brand.requestFocus();
            return;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Items");

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    Item item = keyNode.getValue(Item.class);
                    if(item.getId().equals(curr_id)){

                        ref.child(curr_id).child("name").setValue(name.getText().toString());
                        ref.child(curr_id).child("price").setValue(Format.bankersRound(Double.parseDouble(price.getText().toString())));
                        ref.child(curr_id).child("brand").setValue(brand.getText().toString());


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        Toast.makeText(EditItem.this, "Item Updated!", Toast.LENGTH_LONG).show();
    }




}