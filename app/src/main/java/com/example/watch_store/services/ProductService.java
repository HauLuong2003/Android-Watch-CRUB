package com.example.watch_store.services;

import com.example.watch_store.entities.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductService {

    @GET("products")
    Call<List<Product>> getAll();

    @GET("products/{id}")
     Call<Product> getById(@Path("id") int id);

    @POST("products")
    Call<Void> insertProduct(@Body Product product);

    @DELETE("products/{id}")
    Call<Void> deleteProduct(@Path("id") int id);

    @PUT("products/{id}")
    Call<Void> updateProduct(@Body Product product, @Path("id") int id);
}
