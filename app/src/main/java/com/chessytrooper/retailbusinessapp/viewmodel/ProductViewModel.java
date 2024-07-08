package com.chessytrooper.retailbusinessapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.chessytrooper.retailbusinessapp.model.Product;
import com.chessytrooper.retailbusinessapp.repository.ProductRepository;
import java.util.List;

public class ProductViewModel extends ViewModel {
    private ProductRepository repository;
    private LiveData<List<Product>> products;

    public ProductViewModel() {
        repository = new ProductRepository();
        products = repository.getProducts();
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }
}
