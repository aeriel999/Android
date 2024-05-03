package com.example.sim.services;

import com.example.sim.constants.Urls;
import com.example.sim.network.AccountApi;
import com.example.sim.network.CategoriesApi;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationNetwork {
    private static ApplicationNetwork instance;

    private Retrofit retrofit;

    public ApplicationNetwork() {
        HttpLoggingInterceptor interceptor =  new HttpLoggingInterceptor();
        interceptor.setLevel( HttpLoggingInterceptor.Level.BODY );

        OkHttpClient.Builder client =  new  OkHttpClient.Builder ()
                .addInterceptor(interceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        // Get the token from JwtServiceImp or JwtService, whichever you are using
                        String token = JwtService.getInstance().getToken(); // Example

                        // Add the token to the request header
                        if (token != null && !token.isEmpty()) {
                            Request modifiedRequest = originalRequest.newBuilder()
                                    .header("Authorization", "Bearer " + token)
                                    .build();
                            return chain.proceed(modifiedRequest);
                        } else {
                            return chain.proceed(originalRequest);
                        }
                    }
                });

        retrofit = new Retrofit.Builder()
                .baseUrl(Urls.BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public static ApplicationNetwork getInstance() {
        if(instance==null)
            instance = new ApplicationNetwork();
        return instance;
    }

    public CategoriesApi getCategoriesApi() {
        return retrofit.create(CategoriesApi.class);
    }

    public AccountApi getAccountApi() {
        return retrofit.create(AccountApi.class);
    }

}
