package com.chessytrooper.retailbusinessapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chessytrooper.retailbusinessapp.R;
import com.chessytrooper.retailbusinessapp.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private OnAddToCartListener addToCartListener;

    public ProductAdapter(List<Product> productList, OnAddToCartListener addToCartListener) {
        this.productList = productList;
        this.addToCartListener = addToCartListener;
    }

    public interface OnAddToCartListener {
        void onAddToCart(Product product);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.productName.setText(product.getName());
        holder.productDescription.setText(product.getDescription());
        holder.productPrice.setText("Price: $" + product.getNgnPrice());
        holder.productStock.setText("Quantity Left: " + product.getStock());

        int maxQuantity = 50;
        int progress = (product.getStock() * 50) / maxQuantity;
        holder.productProgress.setProgress(progress);

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

        holder.addToCartButton.setOnClickListener(v -> addToCartListener.onAddToCart(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productDescription, productPrice, productStock;
        ImageView productImage;
        ProgressBar productProgress;
        Button addToCartButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);
            productStock = itemView.findViewById(R.id.productStock);
            productImage = itemView.findViewById(R.id.productImage);
            productProgress = itemView.findViewById(R.id.product_progress);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }
}
