package com.chessytrooper.retailbusinessapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.chessytrooper.retailbusinessapp.api.ApiService;
import com.chessytrooper.retailbusinessapp.api.RetrofitClient;
import com.chessytrooper.retailbusinessapp.model.Product;
import com.chessytrooper.retailbusinessapp.model.ProductsResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {
    private ApiService apiService;

    public ProductRepository() {
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
    }

    public LiveData<List<Product>> getProducts() {
        MutableLiveData<List<Product>> data = new MutableLiveData<>();
        apiService.getProducts().enqueue(new Callback<ProductsResponse>() {
            @Override
            public void onResponse(Call<ProductsResponse> call, Response<ProductsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> productList = response.body().getItems();
                    data.setValue(productList);
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ProductsResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
