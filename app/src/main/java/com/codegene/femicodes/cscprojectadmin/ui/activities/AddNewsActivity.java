package com.codegene.femicodes.cscprojectadmin.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.codegene.femicodes.cscprojectadmin.R;
import com.codegene.femicodes.cscprojectadmin.models.Post;
import com.codegene.femicodes.cscprojectadmin.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNewsActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 5;
    private ImageButton imageBtn;
    private Uri uri = null;
    private EditText textTitle;
    private EditText textContent;
    private Button postBtn;
    private StorageReference storage;
    private DatabaseReference databaseRef;

    public static Intent getStartedIntent(Context context){
        return new Intent(context, AddNewsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        getSupportActionBar().setTitle("Add News");

        // initializing objects
        postBtn = findViewById(R.id.postBtn);
        textContent = findViewById(R.id.textDesc);
        textTitle = findViewById(R.id.textTitle);
        storage = FirebaseStorage.getInstance().getReference();
        databaseRef = FirebaseDatabase.getInstance().getReference().child(Constants.REFERENCE_CHILD_NEWS);
        imageBtn = findViewById(R.id.imageBtn);
        //picking image from gallery
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
        });
        // posting to Firebase
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddNewsActivity.this, "POSTING...", Toast.LENGTH_LONG).show();
                final String PostTitle = textTitle.getText().toString().trim();
                final String PostContent = textContent.getText().toString().trim();
                // do a check for empty fields
                if (!TextUtils.isEmpty(PostContent) && !TextUtils.isEmpty(PostTitle)){
                    StorageReference filepath = storage.child("post_images").child(uri.getLastPathSegment());
                    filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            @SuppressWarnings("VisibleForTests")
                            //getting the post image download url
                            final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Toast.makeText(getApplicationContext(), "Succesfully Uploaded", Toast.LENGTH_SHORT).show();
                            final DatabaseReference newPost = databaseRef.push();

                            //adding post contents to database reference
                            final String PostImage = String.valueOf(downloadUrl);
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd ");
                            String PostDate = mdformat.format(calendar.getTime());
                            String AuthorId = "";


                            Post post = new Post(PostTitle, PostContent, PostImage, PostDate ,AuthorId);

                                    newPost.setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()){
                                                        Intent intent = new Intent(AddNewsActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }
                                            });

                        }
                    });

                }
            }
        });

    }

    @Override
    // image from gallery result
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK){
            uri = data.getData();
            imageBtn.setImageURI(uri);
        }
    }
}
