package com.codegene.femicodes.cscprojectadmin.fragments;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codegene.femicodes.cscprojectadmin.AddProductActivity;
import com.codegene.femicodes.cscprojectadmin.R;
import com.codegene.femicodes.cscprojectadmin.models.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProductFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    final static String REFERENCE_CHILD = "products";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        //initialize recyclerview and FIrebase objects
        recyclerView = view.findViewById(R.id.product_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(REFERENCE_CHILD);


        FloatingActionButton fab =  view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddProductActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Products");
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Product, ProductViewHolder> FBRA = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(
                Product.class,
                R.layout.item_product,
                ProductViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, Product model, final int position) {
                final String product_key = getRef(position).getKey();
                viewHolder.setProductName(model.getProductName());
               viewHolder.setNafdacNumber(model.getNafdacNumber());
                viewHolder.setImageUrl(getContext(), model.getImageUrl());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(getContext(), product_key, Toast.LENGTH_LONG).show();

                    }
                });
            }
        };
        recyclerView.setAdapter(FBRA);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public ProductViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        void setProductName(String productName) {
            TextView product_name = mView.findViewById(R.id.product_name_tv);
            product_name.setText(productName);
        }

        public void setNafdacNumber(String nafdacNumber) {
            TextView nafdac_number = mView.findViewById(R.id.product_number_tv);
            nafdac_number.setText(nafdacNumber);
        }


        void setImageUrl(Context ctx, String imageUrl) {
            ImageView product_image = mView.findViewById(R.id.product_image_tv);
            Picasso.with(ctx).load(imageUrl).into(product_image);
        }
    }
}
