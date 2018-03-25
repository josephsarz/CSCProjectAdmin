package com.codegene.femicodes.cscprojectadmin.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.codegene.femicodes.cscprojectadmin.R;
import com.codegene.femicodes.cscprojectadmin.utils.CommonUtils;
import com.codegene.femicodes.cscprojectadmin.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Map;
import java.util.Random;

public class AddProductActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 2;
    DatabaseReference mProductsDatabaseReference;
    ImageView mProductImage, mBarcodeImage;
    EditText mProductName, mExpiringDate, mManufacturingDate;
    Random random;
    String productType = "";
    String manufacturerName = "";
    boolean generate = true;
    private StorageReference storage;
    private Uri uri = null;
    private String manufacturer_key;
    private DatabaseReference mManufacturerDatabaseReference;

    public static Intent getStartedIntent(Context context){
        return new Intent(context, AddProductActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        getSupportActionBar().setTitle("Register Product");

        Intent intent = getIntent();
        if (intent != null) {
            manufacturer_key = intent.getStringExtra("ManufacturerID");

        }


        mManufacturerDatabaseReference = FirebaseDatabase.getInstance().getReference().child("manufacturers");
        mProductsDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.REFERENCE_CHILD_PRODUCTS);
        storage = FirebaseStorage.getInstance().getReference();
        mProductImage = findViewById(R.id.product_image);
        mProductName = findViewById(R.id.product_name);
        mManufacturingDate = findViewById(R.id.manufacturing_date);
        mExpiringDate = findViewById(R.id.expiring_date);
        mBarcodeImage = findViewById(R.id.IV_barcode_image);

        getManufacturerInfo();


        mProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
        });

    }

//    private void generateNafdacNumber() {
//        if(generate) {
//
//            random = new Random();
//            int no1 = random.nextInt(9);
//            int no2 = random.nextInt(9);
//            int no3 = random.nextInt(9);
//            int no4 = random.nextInt(9);
//
//            StringBuilder sb = new StringBuilder();
//            sb.append("01-");
//            sb.append(no1);
//            sb.append(no2);
//            sb.append(no3);
//            sb.append(no4);
//            String nafdacNumber = sb.toString();
//            //mNafdacNumber.setText(nafdacNumber);
//
//            //Generate Barcode from the nafdac Number
//            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
//            try {
//                BitMatrix bitMatrix = multiFormatWriter.encode(nafdacNumber, BarcodeFormat.QR_CODE,200,200);
//                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
//                mBarcodeImage.setImageBitmap(bitmap);
//            } catch (WriterException e) {
//                e.printStackTrace();
//            }
//
//            generate = false;
//        }
//    }
//

    private void addProduct() {
        Toast.makeText(AddProductActivity.this, "Posting", Toast.LENGTH_SHORT).show();

        final String productName = mProductName.getText().toString().trim();
        final String manufacturingDate = mManufacturingDate.getText().toString().trim();
        final String expiringDate = mExpiringDate.getText().toString().trim();

        // do a check for empty fields
        if (!TextUtils.isEmpty(productName) && !TextUtils.isEmpty(productType)) {
            StorageReference filepath = storage.child("product_images").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    final DatabaseReference getcode = mProductsDatabaseReference.push();
                    int code = CommonUtils.convertToHash(getcode.getKey());
                    final String nafdacNumber = String.valueOf(code).substring(1);
                    final DatabaseReference newProduct = mProductsDatabaseReference.child(nafdacNumber);
                    Toast.makeText(AddProductActivity.this, nafdacNumber, Toast.LENGTH_SHORT).show();
                    mManufacturerDatabaseReference.child(manufacturer_key).child("products").child(nafdacNumber).setValue(true);

                    newProduct.child("productName").setValue(productName);
                    newProduct.child("productType").setValue(productType);
                    newProduct.child("nafdacNumber").setValue(nafdacNumber);
                    newProduct.child("manufacturerId").setValue(manufacturer_key);
                    newProduct.child("manufacturerName").setValue(manufacturerName);
                    newProduct.child("manufacturingDate").setValue(manufacturingDate);
                    newProduct.child("expiringDate").setValue(expiringDate);

                    assert downloadUrl != null;
                    newProduct.child("imageUrl").setValue(downloadUrl.toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(AddProductActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                }
            });


        }else{
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_LONG).show();
        }
    }

    private void getManufacturerInfo() {
        DatabaseReference mManufacturerDatabase = mManufacturerDatabaseReference.child(manufacturer_key);
        mManufacturerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("name") != null) {
                        manufacturerName = map.get("name").toString();
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            uri = data.getData();
            mProductImage.setImageURI(uri);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_add_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done_product:
                addProduct();
                return true;
        }  return super.onOptionsItemSelected(item);
    }

    public void RadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.drug_type:
                if (checked)
                    Toast.makeText(this, "drug selected", Toast.LENGTH_SHORT).show();
                    productType = "drug";
                    break;
            case R.id.food_type:
                if (checked)
                    Toast.makeText(this, "food selected", Toast.LENGTH_SHORT).show();
                    productType = "food";
                break;
        }
    }
}