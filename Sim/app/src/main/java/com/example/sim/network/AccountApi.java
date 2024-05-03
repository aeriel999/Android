package com.example.sim.network;

import com.example.sim.dto.account.LoginDto;
import com.example.sim.dto.account.LoginResponseDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AccountApi {
    @POST("/api/account/login")
    public Call<LoginResponseDto> login(@Body LoginDto model) ;
}
