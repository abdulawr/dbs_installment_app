package com.ss_technology.hanguoilproject.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.ss_technology.hanguoilproject.Admin_Activity.Admin_Home;
import com.ss_technology.hanguoilproject.Config.KeepMeLogin;
import com.ss_technology.hanguoilproject.DBS_Shop_Activity.ShopkeeperHome;
import com.ss_technology.hanguoilproject.R;

public class Splash_Screen extends AppCompatActivity {

    KeepMeLogin login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        login=new KeepMeLogin(this);

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (login.checkData() && !login.getData().trim().equals("null")){

                    if(login.getRole().trim().equals("admin")){
                        startActivity(new Intent(getApplicationContext(), Admin_Home.class));
                        finish();
                    }
                    else if(login.getRole().trim().equals("shop")){
                       // for DBS shop user
                        startActivity(new Intent(getApplicationContext(), ShopkeeperHome.class));
                        finish();
                    }
                }
                else{
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
            }
        },1000);
    }
}