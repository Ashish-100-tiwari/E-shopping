package com.example.e_shopping.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_shopping.R;
import com.example.e_shopping.databinding.ItemCartBinding;
import com.example.e_shopping.databinding.QuantityDialogBinding;
import com.example.e_shopping.model.Product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;

public class CartAdapter  extends  RecyclerView.Adapter<CartAdapter.CardViewHolder>{

    Context context;
    ArrayList<Product> products;
    CardListener cardListener;
    Cart cart;
    public interface CardListener{
        public void  onQuantityChanged();
    }

    public CartAdapter(Context context,ArrayList<Product>products,CardListener cardListener){
        this.context=context;
        this.products=products;
        this.cardListener=cardListener;
        cart = TinyCartHelper.getCart();
    }
    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CardViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Product product = products.get(position);
        Glide.with(context)
                .load(product.getImage())
                .into(holder.binding.image);
        holder.binding.name.setText(product.getName());
        holder.binding.price.setText("IND "+product.getPrice());
        holder.binding.quantity.setText(product.getQuantity()+"items");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuantityDialogBinding quantityDialogBinding= QuantityDialogBinding.inflate(LayoutInflater.from(context));
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setView(quantityDialogBinding.getRoot())
                        .create();


                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));

                quantityDialogBinding.productName.setText(product.getName());
                quantityDialogBinding.productStock.setText("stock"+product.getStock());
                quantityDialogBinding.quantity.setText(String.valueOf(product.getQuantity()));

                int stock = product.getStock();


                quantityDialogBinding.plusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int quantity =product.getQuantity();
                        quantity++;
                        if(quantity>product.getStock()){
                            Toast.makeText(context,"Max Stock available: "+product.getStock(),Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            product.setQuantity(quantity);
                            quantityDialogBinding.quantity.setText(String.valueOf(quantity));
                        }
                        notifyDataSetChanged();
                        cart.updateItem(product,product.getQuantity());
                        cardListener.onQuantityChanged();
                    }
                });

                quantityDialogBinding.minusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int quantity =product.getQuantity();

                        if(quantity>=2){
                            quantity--;
                        }
                        product.setQuantity(quantity);
                        quantityDialogBinding.quantity.setText(String.valueOf(quantity));

                        notifyDataSetChanged();
                        cart.updateItem(product,product.getQuantity());
                        cardListener.onQuantityChanged();
                    }
                });
                quantityDialogBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
//                        notifyDataSetChanged();
//                        cart.updateItem(product,product.getQuantity());
//                        cardListener.onQuantityChanged();
                    }
                });

                dialog.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        ItemCartBinding binding;
        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            binding= ItemCartBinding.bind(itemView);
        }
    }
}
