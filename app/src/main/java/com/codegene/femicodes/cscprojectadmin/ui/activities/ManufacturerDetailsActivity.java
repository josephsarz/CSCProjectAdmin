package com.codegene.femicodes.cscprojectadmin.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.codegene.femicodes.cscprojectadmin.R;
import com.codegene.femicodes.cscprojectadmin.models.Product;
import com.codegene.femicodes.cscprojectadmin.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManufacturerDetailsActivity extends AppCompatActivity {

    String manufacturer_key;
    private RecyclerView mProductRecyclerView;
    private RecyclerView.Adapter mProductAdapter;
    private RecyclerView.LayoutManager mProductLayoutManager;
    private DatabaseReference mDatabase;
    private ArrayList resultsProductArrayList = new ArrayList<Product>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufacturer_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialize recyclerview and FIrebase objects
        mProductRecyclerView = findViewById(R.id.productListRecyclerView);
        mProductRecyclerView.setNestedScrollingEnabled(false);
        mProductRecyclerView.setHasFixedSize(true);
        mProductLayoutManager = new LinearLayoutManager(ManufacturerDetailsActivity.this);
        mProductRecyclerView.setLayoutManager(mProductLayoutManager);
        mProductAdapter = new ProductAdapter(getDataSetHistory(), ManufacturerDetailsActivity.this);
        mProductRecyclerView.setAdapter(mProductAdapter);


        Intent intent = getIntent();

        if (intent != null) {
            manufacturer_key = intent.getStringExtra("ManufacturerID");
        }

        getManufacturerProductsIds();


        FloatingActionButton fab = findViewById(R.id.addProductFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent singleActivity = new Intent(getApplicationContext(), AddProductActivity.class);
                singleActivity.putExtra("ManufacturerID", manufacturer_key);
                startActivity(singleActivity);

            }
        });

    }

    private void getManufacturerProductsIds() {
        DatabaseReference mManufacturerDatabaseReference = FirebaseDatabase.getInstance().getReference().child("manufacturers").child(manufacturer_key).child("products");
        mManufacturerDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot product : dataSnapshot.getChildren()) {
                        FetchProductInformation(product.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void FetchProductInformation(String rideKey) {

        DatabaseReference mProductsDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.REFERENCE_CHILD_PRODUCTS).child(rideKey);
        mProductsDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String productId = dataSnapshot.getKey();
                    String productName = "";
                    String imageUrl = "";
                    String nafdacNumber = "";

                    if (dataSnapshot.child("productName").getValue() != null) {
                        productName = String.valueOf(dataSnapshot.child("productName").getValue().toString());
                    }

                    if (dataSnapshot.child("imageUrl").getValue() != null) {
                        imageUrl = String.valueOf(dataSnapshot.child("imageUrl").getValue().toString());
                    }

                    if (dataSnapshot.child("nafdacNumber").getValue() != null) {
                        nafdacNumber = String.valueOf(dataSnapshot.child("nafdacNumber").getValue().toString());
                    }


                    Product obj = new Product(productId, productName, imageUrl, nafdacNumber);
                    resultsProductArrayList.add(obj);
                    mProductAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private ArrayList<Product> getDataSetHistory() {
        return resultsProductArrayList;
    }

}
