package com.codegene.femicodes.cscprojectadmin.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codegene.femicodes.cscprojectadmin.R;
import com.codegene.femicodes.cscprojectadmin.utils.CommonUtils;
import com.codegene.femicodes.cscprojectadmin.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProduceBatch extends AppCompatActivity {

    EditText mProductCount;
    Button mAddBatch;
    String productId;

    DatabaseReference mProductDatabaseReference;
    DatabaseReference mUniqueNumbersDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produce_batch);

        mProductCount = findViewById(R.id.product_count);
        mAddBatch = findViewById(R.id.product_batch_btn);
        mUniqueNumbersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("uniqueNumber");

        Intent intent = getIntent();

        if (intent != null) {

            productId = intent.getStringExtra("productId");
            mProductDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.REFERENCE_CHILD_PRODUCTS).child(productId);
        }

        mAddBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBatch();
            }
        });

    }

    private void addBatch() {
        String count = mProductCount.getText().toString().trim();
        if (!TextUtils.isEmpty(count)) {
            int n = Integer.valueOf(count);
            final DatabaseReference getcode = mProductDatabaseReference.push();
            int code = CommonUtils.convertToHash(getcode.getKey());
            String batchId = String.valueOf(code).substring(1);
            DatabaseReference newBatch = mProductDatabaseReference.child("batch").child(batchId);


            for (int i = 0; i < n; i++) {

                final DatabaseReference getuniquecode = newBatch.push();
                int ucode = CommonUtils.convertToHash(getuniquecode.getKey());
                String uniqueId = String.valueOf(ucode).substring(1);
                DatabaseReference newUnique = newBatch.child(uniqueId);

                newUnique.child("uniqueId").setValue(uniqueId);
                newUnique.child("isUsed").setValue(false);
                newUnique.child("productId").setValue(productId);
                newUnique.child("batchId").setValue(batchId).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "successful product sent", Toast.LENGTH_SHORT).show();
                    }
                });

                DatabaseReference unique = mUniqueNumbersDatabaseReference.child(uniqueId);
                unique.child("uniqueId").setValue(uniqueId);
                unique.child("isUsed").setValue(false);
                unique.child("productId").setValue(productId);
                unique.child("batchId").setValue(batchId).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(ProduceBatch.this, ProductDetailsActivity.class));
                        Toast.makeText(getApplicationContext(), "successful unique sent", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        }

    }
}
