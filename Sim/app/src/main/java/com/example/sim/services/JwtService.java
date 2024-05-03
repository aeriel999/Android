package com.example.sim.services;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class JwtService extends Application {
     private static final String KEY_TOKEN = "jwt_token";

    private static JwtService instance;
    private SharedPreferences sharedPreferences;
    private static Context appContext;

    public void setAppContext(Context appContext) {
        JwtService.appContext = appContext;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        this.setAppContext(getApplicationContext());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static JwtService getInstance() {
        return instance;
    }

    public void saveToken(String token) {
        SharedPreferences prefs;
        SharedPreferences.Editor edit;
        prefs =  instance.getSharedPreferences("jwtStore", MODE_PRIVATE);
        edit=prefs.edit();
        try {
            edit.putString(KEY_TOKEN,token);
            edit.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getToken() {
        SharedPreferences prefs=instance.getSharedPreferences("jwtStore", Context.MODE_PRIVATE);
        String token = prefs.getString(KEY_TOKEN,"");
        return token;
    }

    public void clearToken() {
        SharedPreferences prefs;
        SharedPreferences.Editor edit;
        prefs=instance.getSharedPreferences("jwtStore", Context.MODE_PRIVATE);
        edit=prefs.edit();
        try {
            edit.remove(KEY_TOKEN);
            edit.apply();
            edit.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean isLogin() {
        //return !getToken().isEmpty();
        if(getToken().equals(""))
            return false;
        return true;
    }
}
