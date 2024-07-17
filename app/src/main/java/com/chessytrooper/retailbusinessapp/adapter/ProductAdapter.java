package com.chessytrooper.retailbusinessapp.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.chessytrooper.retailbusinessapp.CartManager;
import com.chessytrooper.retailbusinessapp.ProductDetailActivity;
import com.chessytrooper.retailbusinessapp.R;
import com.chessytrooper.retailbusinessapp.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private Context context;

    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
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

        // Set progress based on available quantity
        int maxQuantity = 50;
        int progress = (product.getStock() * 50) / maxQuantity;
        holder.productProgress.setProgress(progress);

        if (product.getPhotos() != null && !product.getPhotos().isEmpty()) {
            Glide.with(holder.productImage.getContext())
                    .load("https://api.timbu.cloud/images/" + product.getPhotos().get(0).getUrl())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.placeholder);
        }

        updateCartButtonStatus(holder.addToCartButton, product);

        holder.addToCartButton.setOnClickListener(v -> toggleCartStatus(holder.addToCartButton, product));

        holder.itemView.setOnClickListener(v -> openProductDetail(product));
    }

    private void openProductDetail(Product product) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra("product", product);
        context.startActivity(intent);
    }

    private void toggleCartStatus(Button button, Product product) {
        if (CartManager.isProductInCart(product)) {
            CartManager.removeFromCart(context, product);
        } else {
            CartManager.addToCart(context, product);
        }
        updateCartButtonStatus(button, product);
    }

    private void updateCartButtonStatus(Button button, Product product) {
        if (CartManager.isProductInCart(product)) {
            button.setText("Remove from Cart");
        } else {
            button.setText("Add to Cart");
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productDescription, productPrice;
        ImageView productImage;
        Button addToCartButton;
        ProgressBar productProgress;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
            productProgress = itemView.findViewById(R.id.product_progress);
        }
    }
}
