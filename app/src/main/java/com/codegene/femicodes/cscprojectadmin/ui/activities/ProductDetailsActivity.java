package com.codegene.femicodes.cscprojectadmin.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codegene.femicodes.cscprojectadmin.R;
import com.codegene.femicodes.cscprojectadmin.models.Product;
import com.codegene.femicodes.cscprojectadmin.utils.Constants;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class ProductDetailsActivity extends AppCompatActivity {

    String productId;
    TextView mProductName;
    TextView mNafdacNumber;
    TextView mProductType;
    TextView mManufacturingDate;
    TextView mExpiringDate;
    ImageView mDrugHeader;
    TextView mManufacturerName;
    private DatabaseReference mProductDatabase;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent != null) {
            productId = intent.getStringExtra("productId");

        }


        recyclerView = findViewById(R.id.batchProductRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        mProductDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.REFERENCE_CHILD_PRODUCTS).child(productId);
        //initializing views
        mProductName = findViewById(R.id.product_name);
        mManufacturerName = findViewById(R.id.product_manufacturer_name);
        mProductType = findViewById(R.id.product_type);
        mNafdacNumber = findViewById(R.id.nafdac_number);
        mDrugHeader = findViewById(R.id.drug_header_image);
        mManufacturingDate = findViewById(R.id.manufacturing_date);
        mExpiringDate = findViewById(R.id.expiring_date);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailsActivity.this, ProduceBatch.class);
                intent.putExtra("productId", productId);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getProductInfo();
    }

    private void getProductInfo() {
        mProductDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("imageUrl") != null) {
                        Picasso.with(getApplication()).load(map.get("imageUrl").toString()).into(mDrugHeader);
                    }
                    if (map.get("productName") != null) {
                        mProductName.setText(map.get("productName").toString());
                        getSupportActionBar().setTitle(map.get("productName").toString());
                    }
                    if (map.get("nafdacNumber") != null) {
                        mNafdacNumber.setText(map.get("nafdacNumber").toString());
                    }
                    if (map.get("productType") != null) {
                        mProductType.setText(map.get("productType").toString());
                    }
                    if (map.get("manufacturerName") != null) {
                        mManufacturerName.setText(map.get("manufacturerName").toString());
                    }
                    if (map.get("manufacturingDate") != null) {
                        mManufacturingDate.setText(map.get("manufacturingDate").toString());
                    }
                    if (map.get("expiringDate") != null) {
                        mExpiringDate.setText(map.get("expiringDate").toString());
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        productId = getIntent().getStringExtra("productId");
        DatabaseReference mBatchDatabaseReference = mProductDatabase.child(productId).child("batch");
        FirebaseRecyclerAdapter<Product, ProductViewHolder> FBRA = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(
                Product.class,
                R.layout.item_product,
                ProductViewHolder.class,
                mBatchDatabaseReference
        ) {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, Product model, final int position) {
                final String product_key = getRef(position).getKey();
                Toast.makeText(ProductDetailsActivity.this, product_key, Toast.LENGTH_SHORT).show();
                viewHolder.setProductName(product_key);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        };
        recyclerView.setAdapter(FBRA);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public ProductViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        void setProductName(String productName) {
            TextView product_name = mView.findViewById(R.id.product_name_tv);
            product_name.setText(productName);
        }

    }

}
