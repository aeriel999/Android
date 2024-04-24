package com.example.sim.network;

import com.example.sim.dto.category.CategoryCreateDto;
import com.example.sim.dto.category.CategoryDto;
import com.example.sim.dto.category.CategoryEditDto;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface CategoriesApi {
    @GET("/api/categories")
    public Call<List<CategoryDto>> list();

    @Multipart
    @POST("/api/categories")
    public Call<CategoryDto> create(@PartMap Map<String, RequestBody> params,
                                        @Part MultipartBody.Part image);

    @GET("/api/categories/{categoryId}")
    public Call<CategoryDto> getCategoryInfo(@Path("categoryId") int categoryId);

    @Multipart
    @PUT("/api/categories")
    public Call<CategoryDto> edit(@PartMap Map<String, RequestBody> params,
                                      @Part MultipartBody.Part image);

    @DELETE("/api/categories/{categoryId}")
    public Call<Void> deleteCategory(@Path("categoryId") int categoryId);
}
