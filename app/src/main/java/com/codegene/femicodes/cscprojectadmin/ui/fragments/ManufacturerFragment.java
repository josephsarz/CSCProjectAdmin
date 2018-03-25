package com.codegene.femicodes.cscprojectadmin.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codegene.femicodes.cscprojectadmin.R;
import com.codegene.femicodes.cscprojectadmin.models.Manufacturer;
import com.codegene.femicodes.cscprojectadmin.ui.activities.AddManufacturer;
import com.codegene.femicodes.cscprojectadmin.ui.activities.ManufacturerDetailsActivity;
import com.codegene.femicodes.cscprojectadmin.utils.Constants;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ManufacturerFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_manufacturer, container, false);

        //initialize recyclerview and FIrebase objects
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.REFERENCE_CHILD_MANUFACTURER);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddManufacturer.getStartedIntent(getContext());
                startActivity(intent);
                //startActivity(new Intent(getActivity(), AddManufacturer.class));
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Manufacturers");
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Manufacturer, BlogzoneViewHolder> FBRA = new FirebaseRecyclerAdapter<Manufacturer, BlogzoneViewHolder>(
                Manufacturer.class,
                R.layout.item_manufacturer,
                BlogzoneViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(BlogzoneViewHolder viewHolder, Manufacturer model, int position) {
                final String manufacturer_key = getRef(position).getKey();

                viewHolder.setName(model.getName());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent singleActivity = new Intent(getContext(), ManufacturerDetailsActivity.class);
                        singleActivity.putExtra("ManufacturerID", manufacturer_key);
                        startActivity(singleActivity);
                        //Toast.makeText(getContext(), post_key, Toast.LENGTH_LONG).show();
                    }
                });
            }
        };
        recyclerView.setAdapter(FBRA);
    }

    public static class BlogzoneViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public BlogzoneViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name) {
            TextView postUserName = mView.findViewById(R.id.name);
            postUserName.setText(name);
        }
    }

}
