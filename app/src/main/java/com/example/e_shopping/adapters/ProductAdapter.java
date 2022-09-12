package com.example.e_shopping.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_shopping.R;
import com.example.e_shopping.activities.ProductDetailActivity;
import com.example.e_shopping.databinding.ItemProductBinding;
import com.example.e_shopping.model.Product;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.productViewHolder>{
    Context context;
    ArrayList<Product> products;
    public ProductAdapter(Context context,ArrayList<Product> products){
        this.context=context;
        this.products=products;
    }
    @NonNull
    @Override
    public productViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new productViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull productViewHolder holder, int position) {
        Product product =products.get(position);
        Glide.with(context)
                .load(product.getImage())
                .into(holder.binding.image);
        holder.binding.label.setText(product.getName());
        holder.binding.price.setText("INR "+product.getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("name",product.getName());
                intent.putExtra("image",product.getImage());
                intent.putExtra("id",product.getId());
                intent.putExtra("price",product.getPrice());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class productViewHolder extends RecyclerView.ViewHolder{

        ItemProductBinding binding;

        public productViewHolder(@NonNull View itemView) {
            super(itemView);
            binding= ItemProductBinding.bind(itemView);
        }
    }
}
