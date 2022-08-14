package com.ss_technology.hanguoilproject.Admin_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ss_technology.hanguoilproject.Config.ApiCall;
import com.ss_technology.hanguoilproject.Config.HelperFunctions;
import com.ss_technology.hanguoilproject.Config.KeepMeLogin;
import com.ss_technology.hanguoilproject.Config.Loading_Dai;
import com.ss_technology.hanguoilproject.Config.VolleyCallback;
import com.ss_technology.hanguoilproject.R;

import org.json.JSONObject;

import java.util.HashMap;

public class CompanyInfo extends AppCompatActivity {

    KeepMeLogin keepMeLogin;
    ApiCall apiCall;
    Loading_Dai loading_dai;
    EditText name,mobile,email,address,facebook,whatsapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        HelperFunctions.Network(this);
        apiCall = new ApiCall(this);
        keepMeLogin = new KeepMeLogin(this);
        loading_dai = new Loading_Dai(this);

        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        facebook = findViewById(R.id.facebook);
        whatsapp = findViewById(R.id.whatsapp);

        HashMap<String,String> map = new HashMap<>();
        map.put("adminID",HelperFunctions.AdminID(this));
        map.put("type","getCompanyInfo");
        apiCall.Insert(map, "AdminApi.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if(object.getString("status").trim().equals("1")){
                       JSONObject data = object.getJSONObject("data");

                       name.setText(data.getString("name"));
                        mobile.setText(data.getString("mobile"));
                        email.setText(data.getString("email"));
                        address.setText(data.getString("address"));
                        facebook.setText(data.getString("facebook"));
                        whatsapp.setText(data.getString("whatsapp"));
                    }
                    else{
                        HelperFunctions.Message(CompanyInfo.this,object.getString("message"));
                    }
                }
                catch (Exception e){

                    HelperFunctions.Message(CompanyInfo.this,"Error occurred in json parsing");
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    public void Update(View view) {

        String dname=name.getText().toString(), dmobile = mobile.getText().toString(),
                demail = email.getText().toString(),daddress = address.getText().toString(),
                dfacebook = facebook.getText().toString(), dwhatapp = whatsapp.getText().toString();

        if(HelperFunctions.verify(dname) && HelperFunctions.verify(dmobile) && HelperFunctions.verify(demail) &&
                HelperFunctions.verify(daddress) && HelperFunctions.verify(dfacebook)
                && HelperFunctions.verify(dwhatapp)){
            HashMap<String,String> map = new HashMap<>();
            map.put("name",dname);
            map.put("mobile",dmobile);
            map.put("email",demail);
            map.put("address",daddress);
            map.put("facebook",dfacebook);
            map.put("whatapp",dwhatapp);
            map.put("type","updateCompanyInfo");
            map.put("adminID",HelperFunctions.AdminID(CompanyInfo.this));
            apiCall.Insert(map, "AdminApi.php", new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    try {
                       JSONObject object = new JSONObject(result);
                        Toast.makeText(CompanyInfo.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e){
                        HelperFunctions.Message(CompanyInfo.this,"Error occured in json parsing");
                    }
                }
            });
        }
        else {
            HelperFunctions.Message(CompanyInfo.this,"Fill the form correctly");
        }

    }
}
