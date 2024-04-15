package com.example.sim;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sim.adapter.CategoryAdapter;
import com.example.sim.databinding.ActivityMainBinding;
import com.example.sim.dto.category.CategoryDto;
import com.example.sim.services.ApplicationNetwork;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

//        ImageView ivAvatar = findViewById(R.id.ivAvatar);
//        String url = "https://pd112.itstep.click/images/1.jpg";
//        Glide.with(this)
//                        .load(url)
//                        .apply(new RequestOptions().override(400))
//                        .into(ivAvatar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ApplicationNetwork
                .getInstance()
                .getCategoriesApi()
                .list()
                .enqueue(new Callback<List<CategoryDto>>() {
                    @Override
                    public void onResponse(Call<List<CategoryDto>> call, Response<List<CategoryDto>> response) {
                        List<CategoryDto> items = response.body();
                        Log.d("--list categories--", String.valueOf(items.size()));

                        if (items != null) {
                            adapter = new CategoryAdapter( items); // Pass MainActivity context
                            binding.rcView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CategoryDto>> call, Throwable throwable) {

                    }
                });

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        init();

    }




    private void init() {
        binding.rcView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
        binding.rcView.setAdapter(adapter);
    }

}