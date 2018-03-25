package com.codegene.femicodes.cscprojectadmin.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codegene.femicodes.cscprojectadmin.R;
import com.codegene.femicodes.cscprojectadmin.models.Product;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> itemList;
    private Context context;

    public ProductAdapter(List<Product> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ProductViewHolder rcv = new ProductViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        holder.productName.setText(itemList.get(position).getProductName());
        holder.nafdacNumber.setText(itemList.get(position).getNafdacNumber());
        Picasso.with(context).load(itemList.get(position).getImageUrl()).into(holder.imageUrl);


    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageUrl;
        public TextView productName;
        public TextView nafdacNumber;

        public ProductViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            imageUrl = itemView.findViewById(R.id.product_image_tv);
            productName = itemView.findViewById(R.id.product_name_tv);
            nafdacNumber = itemView.findViewById(R.id.product_number_tv);
        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ProductDetailsActivity.class);
            Bundle b = new Bundle();
            b.putString("productId", nafdacNumber.getText().toString());
            intent.putExtras(b);
            v.getContext().startActivity(intent);
        }
    }


}