package com.codegene.femicodes.cscprojectadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AddProductActivity extends AppCompatActivity {

    DatabaseReference mProductsDatabaseReference;
    private StorageReference storage;
    private static final int GALLERY_REQUEST_CODE = 2;
    private Uri uri = null;

    ImageView mProductImage, mBarcodeImage;
    EditText mProductName, mNafdacNumber, mManufacturerName, mExpiringDate, mManufacturingDate;
    Button mGenerateNafdacNumber;
    Random random;

    String productType = "";
    boolean generate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        getSupportActionBar().setTitle("Add Product");

        mProductsDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.REFERENCE_CHILD_PRODUCTS);
        storage = FirebaseStorage.getInstance().getReference();
        mProductImage = findViewById(R.id.product_image);
        mProductName = findViewById(R.id.product_name);
        mNafdacNumber = findViewById(R.id.nafdac_number);
        mManufacturerName = findViewById(R.id.manufacturer_name);
        mManufacturingDate = findViewById(R.id.manufacturing_date);
        mExpiringDate = findViewById(R.id.expiring_date);
        mBarcodeImage = findViewById(R.id.IV_barcode_image);
        mGenerateNafdacNumber = findViewById(R.id.BTN_generate_nafdac_number);

        mGenerateNafdacNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                generateNafdacNumber();

            }
        });


        mProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
        });

    }

    private void generateNafdacNumber() {
        if(generate) {

            random = new Random();
            int no1 = random.nextInt(9);
            int no2 = random.nextInt(9);
            int no3 = random.nextInt(9);
            int no4 = random.nextInt(9);

            StringBuilder sb = new StringBuilder();
            sb.append("01-");
            sb.append(no1);
            sb.append(no2);
            sb.append(no3);
            sb.append(no4);
            String nafdacNumber = sb.toString();
            mNafdacNumber.setText(nafdacNumber);

            //Generate Barcode from the nafdac Number
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(nafdacNumber, BarcodeFormat.QR_CODE,200,200);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                mBarcodeImage.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }

            generate = false;
        }
    }


    private void addProduct() {
        Toast.makeText(AddProductActivity.this, "Posting", Toast.LENGTH_SHORT).show();

        final String productName = mProductName.getText().toString().trim();
        final String nafdacNumber = mNafdacNumber.getText().toString().trim();
        final String manufacturerName = mManufacturerName.getText().toString().trim();
        final String batchNumber = productName+" "+nafdacNumber;
        final String manufacturingDate = mManufacturingDate.getText().toString().trim();
        final String expiringDate = mExpiringDate.getText().toString().trim();

        // do a check for empty fields
        if (!TextUtils.isEmpty(productName) && !TextUtils.isEmpty(productType) && !TextUtils.isEmpty(nafdacNumber)) {
            StorageReference filepath = storage.child("product_images").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    @SuppressWarnings("VisibleForTests")
                    //getting the post image download url
                    final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    //Toast.makeText(getApplicationContext(), "Succesfully Uploaded", Toast.LENGTH_SHORT).show();
                    final DatabaseReference newProduct = mProductsDatabaseReference.push();
                    //adding post contents to database reference

                    newProduct.child("productName").setValue(productName);
                    newProduct.child("productType").setValue(productType);
                    newProduct.child("nafdacNumber").setValue(nafdacNumber);
                    newProduct.child("manufacturerName").setValue(manufacturerName);
                    newProduct.child("batchNumber").setValue(batchNumber);
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


    @Override
// image from gallery result
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