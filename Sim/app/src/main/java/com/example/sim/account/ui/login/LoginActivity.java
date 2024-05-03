package com.example.sim.account.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.example.sim.MainActivity;
import com.example.sim.dto.account.LoginDto;
import com.example.sim.dto.account.LoginResponseDto;
import com.example.sim.services.ApplicationNetwork;
import com.example.sim.services.BaseActivity;
import com.example.sim.R;
import com.example.sim.services.JwtService;
import com.example.sim.utils.CommonUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    EditText etUsername;
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.sim.R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
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

                           JwtService.getInstance().saveToken(data.getToken());

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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