package com.codegene.femicodes.cscprojectadmin.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codegene.femicodes.cscprojectadmin.R;
import com.codegene.femicodes.cscprojectadmin.ui.fragments.ManufacturerFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditManufacturerActivity extends AppCompatActivity {

    String post_key = null;
    private EditText mName;
    private DatabaseReference mDatabase;
    private Button deleteBtn, updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_manufacturer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mName = findViewById(R.id.man_name);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("manufacturers");
        post_key = getIntent().getExtras().getString("PostID");
        deleteBtn = findViewById(R.id.deleteBtn);
        updateBtn = findViewById(R.id.updateBtn);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.child(post_key).removeValue();

                Intent mainintent = new Intent(EditManufacturerActivity.this, ManufacturerFragment.class);
                startActivity(mainintent);
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mName.getText().toString().trim();
                mDatabase.child(post_key).child("name").setValue(name);
                Intent mainintent = new Intent(EditManufacturerActivity.this, ManufacturerFragment.class);
                startActivity(mainintent);
            }
        });


        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("name").getValue();
                mName.setText(name);
                Toast.makeText(EditManufacturerActivity.this, name, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


}

