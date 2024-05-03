package com.example.sim;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.sim.category.CategoriesAdapter;

import com.example.sim.category.CategoryCreateActivity;
import com.example.sim.category.CategoryEditActivity;
import com.example.sim.dto.account.LoginDto;
import com.example.sim.dto.account.LoginResponseDto;
import com.example.sim.dto.category.CategoryDto;
import com.example.sim.services.ApplicationNetwork;
import com.example.sim.services.BaseActivity;

import com.example.sim.services.JwtService;
import com.example.sim.utils.CommonUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends BaseActivity{
    RecyclerView rcCategories;
    EditText etUsername;
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if( JwtService.getInstance().isLogin())
        {
            setContentView(R.layout.activity_main);
            rcCategories = findViewById(R.id.rcCategories);
            rcCategories.setHasFixedSize(true);
            rcCategories.setLayoutManager(new GridLayoutManager(this, 1, RecyclerView.VERTICAL, false));

            onLoadData();
        }else{
            setContentView(com.example.sim.R.layout.activity_login);

            etUsername = findViewById(R.id.etUsername);
            etPassword = findViewById(R.id.etPassword);
        }

    }

    private void onClickEditCategory(CategoryDto category) {
        //Toast.makeText(this, category.getName(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MainActivity.this, CategoryEditActivity.class);
        Bundle b = new Bundle();
        b.putInt("id", category.getId());
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    private void onClickDeleteCategory(CategoryDto category) {
      //  Toast.makeText(this, category.getName(), Toast.LENGTH_LONG).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Видалити " + category.getName() + "?")
                .setPositiveButton("Так", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApplicationNetwork.getInstance()
                                .getCategoriesApi()
                                .deleteCategory(category.getId())
                                .enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if(response.isSuccessful()) {
                                            onLoadData();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable throwable) {

                                    }
                                });
                    }
                })
                .setNegativeButton("Ні", null) // No action when user clicks No
                .show();
    }



    private void onLoadData() {
        CommonUtils.showLoading(this);
        ApplicationNetwork
                .getInstance()
                .getCategoriesApi()
                .list()
                .enqueue(new Callback<List<CategoryDto>>() {
                    @Override
                    public void onResponse(Call<List<CategoryDto>> call, Response<List<CategoryDto>> response) {
                        List<CategoryDto> items = response.body();
                        CategoriesAdapter ca = new CategoriesAdapter(items,
                                MainActivity.this::onClickEditCategory,
                                MainActivity.this::onClickDeleteCategory);

                        rcCategories.setAdapter(ca);

                        CommonUtils.hideLoading();
                        Log.d("--list categories--", String.valueOf(items.size()));
                    }
                    @Override
                    public void onFailure(Call<List<CategoryDto>> call, Throwable throwable) {

                    }
                });
    }

    public void onClickLogin(View view) {
        CommonUtils.showLoading(this);
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(etUsername.getText().toString());
        loginDto.setPassword(etPassword.getText().toString());



        ApplicationNetwork.getInstance()
                .getAccountApi()
                .login(loginDto)
                .enqueue(new Callback<LoginResponseDto>() {

                    @Override
                    public void onResponse(Call<LoginResponseDto> call, Response<LoginResponseDto> response) {
                        if (response.isSuccessful()) {
                            LoginResponseDto data = response.body();

                            String token = data.getToken();

                            JwtService service = JwtService.getInstance();
                            service.saveToken(token);

                            Intent intent = new Intent(MainActivity.this, CategoryCreateActivity.class);
                            startActivity(intent);
                            finish();

                            CommonUtils.hideLoading();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponseDto> call, Throwable t) {

                    }
                });
    }


}