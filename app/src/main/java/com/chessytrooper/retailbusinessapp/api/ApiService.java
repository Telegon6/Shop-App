package com.chessytrooper.retailbusinessapp.api;

import com.chessytrooper.retailbusinessapp.model.Product;
import com.chessytrooper.retailbusinessapp.model.ProductsResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("products?organization_id=b966b9228ed5415abd1d95083478a1a8&Appid=KRRL8NMI2J333A6&Apikey=27403586ac284c799d11fc846cad4c8320240705000112334401")
    Call<ProductsResponse> getProducts();
}
