package com.ss_technology.hanguoilproject.DBS_Shop_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ss_technology.hanguoilproject.Activity.MainActivity;
import com.ss_technology.hanguoilproject.Config.ApiCall;
import com.ss_technology.hanguoilproject.Config.CheckInternetConnection;
import com.ss_technology.hanguoilproject.Config.HelperFunctions;
import com.ss_technology.hanguoilproject.Config.KeepMeLogin;
import com.ss_technology.hanguoilproject.Config.VolleyCallback;
import com.ss_technology.hanguoilproject.R;

import org.json.JSONObject;

import java.util.HashMap;

public class ShopkeeperHome extends AppCompatActivity {

    KeepMeLogin keepMeLogin;
    TextView pendingBalance;
    ApiCall apiCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeeper_home);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        keepMeLogin = new KeepMeLogin(this);
        pendingBalance = findViewById(R.id.pendingBalance);
        apiCall = new ApiCall(this);

        if(CheckInternetConnection.Connection(this)){
            try {
                JSONObject object = new JSONObject(keepMeLogin.getData());
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("ID",object.getString("id"));
                hashMap.put("getshopkeeperbalance","getshopkeeperbalance");

                apiCall.Insert(hashMap, "Shopkeeper.php", new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                      pendingBalance.setText("Pending balance: "+result);
                    }
                });
            }
            catch (Exception e){
                Toast.makeText(this, "Error occured in json parsin", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin__home,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        else if(item.getItemId() == R.id.signout){
            keepMeLogin.Clear();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        return true;
    }

    public void Generate_Mobile_Bill(View view) {
        startActivity(new Intent(getApplicationContext(),GenerateMobileBill.class));
    }

    public void Generate_Accessory_Bill(View view) {
        startActivity(new Intent(getApplicationContext(),Gen_Accessory_Bill.class));
    }
}