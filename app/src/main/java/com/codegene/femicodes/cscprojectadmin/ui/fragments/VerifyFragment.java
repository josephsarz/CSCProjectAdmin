package com.codegene.femicodes.cscprojectadmin.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codegene.femicodes.cscprojectadmin.R;
import com.codegene.femicodes.cscprojectadmin.ui.activities.ProductDetailsActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VerifyFragment extends Fragment {

    EditText mUniqueNumberET;
    Button mVerifyBTN;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verify, container, false);

        mUniqueNumberET = view.findViewById(R.id.uniqueNumberET);
        mVerifyBTN = view.findViewById(R.id.verifyProductBTN);

        mVerifyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = mUniqueNumberET.getText().toString().trim();
                if (!TextUtils.isEmpty(id)) {
                    checkForId(id);
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Verify");
    }

    private void checkForId(final String value) {
        Toast.makeText(getActivity(), "checking", Toast.LENGTH_SHORT).show();
        DatabaseReference mUniqueDatabaseReference = FirebaseDatabase.getInstance().getReference().child("uniqueNumber").child(value);
        mUniqueDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(getContext(), "exists " + value, Toast.LENGTH_SHORT).show();
                    if (dataSnapshot.child("productId").getValue() != null) {
                        String productId = String.valueOf(dataSnapshot.child("productId").getValue().toString());
                        Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                        intent.putExtra("productId", productId);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getActivity(), "doesnt exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


}
