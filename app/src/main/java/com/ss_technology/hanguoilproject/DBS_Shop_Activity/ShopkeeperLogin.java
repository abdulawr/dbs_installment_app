package com.ss_technology.hanguoilproject.DBS_Shop_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.ss_technology.hanguoilproject.Config.ApiCall;
import com.ss_technology.hanguoilproject.Config.HelperFunctions;
import com.ss_technology.hanguoilproject.Config.KeepMeLogin;
import com.ss_technology.hanguoilproject.Config.Loading_Dai;
import com.ss_technology.hanguoilproject.Config.VolleyCallback;
import com.ss_technology.hanguoilproject.R;

import org.json.JSONObject;

import java.util.HashMap;

public class ShopkeeperLogin extends AppCompatActivity {

    ApiCall apiCall;
    KeepMeLogin keepMeLogin;
    Loading_Dai loading_dai;
    TextInputEditText mobile,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeeper_login);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        apiCall= new ApiCall(this);
        keepMeLogin = new KeepMeLogin(this);
        loading_dai = new Loading_Dai(this);
        mobile = findViewById(R.id.mobile);
        password = findViewById(R.id.pass);


    }

    public void Login(View view) {
        if(HelperFunctions.verify(mobile.getText().toString()) && HelperFunctions.verify(password.getText().toString())){
            loading_dai.Show();
            String mob = mobile.getText().toString(), pass = password.getText().toString();
            HashMap<String,String> map = new HashMap<>();
            map.put("mobile",mob);
            map.put("pass",pass);
            apiCall.Insert(map, "shopkeeperLogin.php", new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject object=new JSONObject(result);
                        if(object.getString("status").trim().equals("1")){
                            keepMeLogin.setData(object.getString("data"),"null","shop");
                            startActivity(new Intent(getApplicationContext(),ShopkeeperHome.class));
                            finish();
                        }
                        else{
                            Toast.makeText(ShopkeeperLogin.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e){
                        Toast.makeText(ShopkeeperLogin.this,"Something went wrong try again",Toast.LENGTH_LONG).show();
                    }
                    loading_dai.Hide();
                }
            });
        }
        else{
            loading_dai.Hide();
            password.getText().clear();
            Toast.makeText(this, "Input fields should not be empty!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return true;
    }
}