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

public class Profile extends AppCompatActivity {

    EditText name,mobile,email,cnic,address,password;
    KeepMeLogin keepMeLogin;
    ApiCall apiCall;
    Loading_Dai loading_dai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        HelperFunctions.Network(this);
        apiCall = new ApiCall(this);
        keepMeLogin = new KeepMeLogin(this);
        loading_dai = new Loading_Dai(this);

        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        email = findViewById(R.id.email);
        cnic = findViewById(R.id.cnic);
        address = findViewById(R.id.address);
        password =findViewById(R.id.password);

        HashMap<String,String> map= new HashMap<>();
        map.put("adminID",HelperFunctions.AdminID(this));
        map.put("type","getAdminProfile");
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
                        cnic.setText(data.getString("cnic"));
                        address.setText(data.getString("address"));
                        password.setText(data.getString("pass"));
                    }
                    else{
                        HelperFunctions.Message(Profile.this,object.getString("message"));
                    }
                }
                catch (Exception e){
                    Toast.makeText(Profile.this, "Error occured in json parsing", Toast.LENGTH_SHORT).show();
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
       String dname = name.getText().toString(), demail = email.getText().toString(),
               daddress = address.getText().toString(), dpass = password.getText().toString(),
               dcnic = cnic.getText().toString(),dmobile = mobile.getText().toString();

       if(HelperFunctions.verify(dname) && HelperFunctions.verify(demail) && HelperFunctions.verify(daddress)
       && HelperFunctions.verify(dpass) && HelperFunctions.verify(dcnic) && HelperFunctions.verify(dmobile)){
           HashMap<String,String> map = new HashMap<>();
           map.put("type","updateAdminProfile");
           map.put("adminID",HelperFunctions.AdminID(Profile.this));
           map.put("name",dname);
           map.put("email",demail);
           map.put("mobile",dmobile);
           map.put("cnic",dcnic);
           map.put("address",daddress);
           map.put("password",dpass);

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
                           cnic.setText(data.getString("cnic"));
                           address.setText(data.getString("address"));
                           password.setText(data.getString("pass"));

                           HelperFunctions.Message(Profile.this,object.getString("message"));
                       }
                       else{
                           HelperFunctions.Message(Profile.this,object.getString("message"));
                       }
                   }
                   catch (Exception e){
                       Toast.makeText(Profile.this, "Error occured in json parsing", Toast.LENGTH_SHORT).show();
                   }

               }
           });
       }
       else{
           HelperFunctions.Message(Profile.this,"Empty values are not accepted!");
       }

    }
}