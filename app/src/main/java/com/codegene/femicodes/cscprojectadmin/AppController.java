package com.codegene.femicodes.cscprojectadmin;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by femicodes on 2/15/2018.
 */

public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference();
        Ref.keepSynced(true);


    }
}
