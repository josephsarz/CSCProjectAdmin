package com.codegene.femicodes.cscprojectadmin.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codegene.femicodes.cscprojectadmin.R;
import com.codegene.femicodes.cscprojectadmin.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class NewsDetailsActivity extends AppCompatActivity {

    String postId;
    ImageView mHeader;
    TextView mContent;
    private DatabaseReference mNewsDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mHeader = findViewById(R.id.news_header_image);
        mContent = findViewById(R.id.news_detail_content);

        Intent intent = getIntent();
        if (intent != null) {
            postId = intent.getStringExtra("postId");
            mNewsDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.REFERENCE_CHILD_NEWS).child(postId);
            getNewsInfo();
        }

    }

    private void getNewsInfo() {
        mNewsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("title") != null) {
                        getSupportActionBar().setTitle(map.get("title").toString());
                    }
                    if (map.get("imageUrl") != null) {
                        Picasso.with(getApplication()).load(map.get("imageUrl").toString()).into(mHeader);
                    }
                    if (map.get("content") != null) {
                        mContent.setText(map.get("content").toString());
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
