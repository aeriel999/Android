package com.example.sim.network;

import com.example.sim.dto.category.CategoryDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoriesApi {
    @GET("/api/categories")
    public Call<List<CategoryDto>> list();
}
