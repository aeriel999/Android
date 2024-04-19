package com.example.sim;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sim.category.CategoriesAdapter;

import com.example.sim.dto.category.CategoryDto;
import com.example.sim.services.ApplicationNetwork;
import com.example.sim.services.BaseActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {
    RecyclerView rcCategories;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ImageView ivAvatar = findViewById(R.id.ivAvatar);
//        String url = "https://content1.rozetka.com.ua/goods/images/big/343033787.jpg";
//        String url = "http://10.0.2.2:5101/images/1.jpg";
//        String url = "https://pd112.itstep.click/images/1.jpg";
//        Glide.with(this)
//                .load(url)
//                .apply(new RequestOptions().override(400))
//                .into(ivAvatar);

        rcCategories = findViewById(R.id.rcCategories);
        rcCategories.setHasFixedSize(true);
        rcCategories.setLayoutManager(new GridLayoutManager(this, 1, RecyclerView.VERTICAL, false));

        ApplicationNetwork
                .getInstance()
                .getCategoriesApi()
                .list()
                .enqueue(new Callback<List<CategoryDto>>() {
                    @Override
                    public void onResponse(Call<List<CategoryDto>> call, Response<List<CategoryDto>> response) {
                        List<CategoryDto> items = response.body();
                        CategoriesAdapter ca = new CategoriesAdapter(items);
                        rcCategories.setAdapter(ca);
                        //Log.d("--list categories--", String.valueOf(items.size()));
                    }

                    @Override
                    public void onFailure(Call<List<CategoryDto>> call, Throwable throwable) {

                    }
                });

    }

}