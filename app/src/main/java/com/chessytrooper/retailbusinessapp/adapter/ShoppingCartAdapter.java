package com.chessytrooper.retailbusinessapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chessytrooper.retailbusinessapp.R;
import com.chessytrooper.retailbusinessapp.model.Product;

import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {
    private List<Product> shoppingCart;
    private OnItemRemovedListener itemRemovedListener;
    private OnQuantityChangedListener quantityChangedListener;

    public interface OnItemRemovedListener {
        void onItemRemoved(int position);
    }

    public interface OnQuantityChangedListener {
        void onQuantityChanged();
    }

    public ShoppingCartAdapter(List<Product> shoppingCart, OnItemRemovedListener listener, OnQuantityChangedListener quantityListener) {
        this.shoppingCart = shoppingCart;
        this.itemRemovedListener = listener;
        this.quantityChangedListener = quantityListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shopping_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = shoppingCart.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText("Price: $" + product.getNgnPrice());
        holder.productQuantity.setText("" + product.getQuantity());

        if (product.getPhotos() != null && !product.getPhotos().isEmpty()) {
            Glide.with(holder.productImage.getContext())
                    .load("https://api.timbu.cloud/images/" + product.getPhotos().get(0).getUrl())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.placeholder);
        }

        holder.removeProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemRemovedListener != null) {
                    itemRemovedListener.onItemRemoved(holder.getAdapterPosition());
                }
            }
        });

        holder.increaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = product.getQuantity() + 1;
                product.setQuantity(newQuantity);
                holder.productQuantity.setText("" + newQuantity);
                if (quantityChangedListener != null) {
                    quantityChangedListener.onQuantityChanged();
                }
            }
        });

        holder.decreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product.getQuantity() > 1) {
                    int newQuantity = product.getQuantity() - 1;
                    product.setQuantity(newQuantity);
                    holder.productQuantity.setText("" + newQuantity);
                    if (quantityChangedListener != null) {
                        quantityChangedListener.onQuantityChanged();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingCart.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, productPrice, productQuantity, increaseQuantityButton,
                decreaseQuantityButton;
        ImageView productImage;
        ImageView removeProduct;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productImage = itemView.findViewById(R.id.productImage);
            removeProduct = itemView.findViewById(R.id.removeProduct);
            increaseQuantityButton = itemView.findViewById(R.id.increaseQuantityButton);
            decreaseQuantityButton = itemView.findViewById(R.id.decreaseQuantityButton);
        }
    }
}
