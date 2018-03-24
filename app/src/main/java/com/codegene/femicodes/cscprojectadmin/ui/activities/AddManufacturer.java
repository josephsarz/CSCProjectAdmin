package com.codegene.femicodes.cscprojectadmin.ui.activities;

import android.content.Context;
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
import com.codegene.femicodes.cscprojectadmin.models.Manufacturer;
import com.codegene.femicodes.cscprojectadmin.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddManufacturer extends AppCompatActivity {

    FirebaseDatabase mReportDatabase;
    DatabaseReference mManufacturerDatabaseReference;

     EditText mName;
    EditText mAddress;
    EditText mCountry;
    EditText mEmail;
    EditText mPhone;
    EditText mWebsite;
    private Button mAddButton;

    public static Intent getStartedIntent(Context context){
        return new Intent(context, AddManufacturer.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manu);

        getSupportActionBar().setTitle("Add Manufacturer");

        mReportDatabase = FirebaseDatabase.getInstance();
        mManufacturerDatabaseReference = mReportDatabase.getReference(Constants.REFERENCE_CHILD_MANUFACTURER);

        mName = findViewById(R.id.mname);
        mAddress = findViewById(R.id.maddress);
        mCountry = findViewById(R.id.mcountry);
        mEmail = findViewById(R.id.memail);
        mPhone = findViewById(R.id.mphone);
        mWebsite = findViewById(R.id.mwebsite);
        mAddButton = findViewById(R.id.add_product);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addManufacturer();

            }
        });

    }

    private void addManufacturer() {
        Toast.makeText(AddManufacturer.this, "POSTING...", Toast.LENGTH_LONG).show();
        final String name = String.valueOf(mName.getText()).trim();
        final String address = String.valueOf(mAddress.getText()).trim();
        final String country = String.valueOf(mCountry.getText()).trim();
        final String email = String.valueOf(mEmail.getText()).trim();
        final String phone = String.valueOf(mPhone.getText()).trim();
        final String website = String.valueOf(mWebsite.getText()).trim();

        final DatabaseReference newManufacturer = mManufacturerDatabaseReference.push();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone)) {
            Manufacturer manufacturer = new Manufacturer(name, address, country, email, phone, website);

            newManufacturer.setValue(manufacturer).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(AddManufacturer.this, " Successful ", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AddManufacturer.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }else{
            Toast.makeText(getApplicationContext(), "fields cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }
}
