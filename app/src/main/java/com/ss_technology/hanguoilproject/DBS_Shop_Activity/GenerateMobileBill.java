package com.ss_technology.hanguoilproject.DBS_Shop_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ss_technology.hanguoilproject.Config.ApiCall;
import com.ss_technology.hanguoilproject.Config.HelperFunctions;
import com.ss_technology.hanguoilproject.Config.VolleyCallback;
import com.ss_technology.hanguoilproject.R;

import org.json.JSONObject;

import java.util.HashMap;

public class GenerateMobileBill extends AppCompatActivity {

    TextView ID,fringerprint,backCamera,ram,sim,frontCamera,memory,network,company,price;
    ApiCall apiCall;
    CardView cardView;
    EditText stockID,cname,cmobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_mobile_bill);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ID = findViewById(R.id.ID);
        cname = findViewById(R.id.cname);
        cmobile = findViewById(R.id.cmobile);
        cardView = findViewById(R.id.cardView);
        stockID = findViewById(R.id.stockID);
        fringerprint = findViewById(R.id.fringerprint);
        backCamera = findViewById(R.id.backCamera);
        ram = findViewById(R.id.ram);
        sim = findViewById(R.id.sim);
        frontCamera = findViewById(R.id.frtCamera);
        memory = findViewById(R.id.MemoryID);
        network = findViewById(R.id.network);
        company = findViewById(R.id.cmpID);
        price = findViewById(R.id.price);
        apiCall = new ApiCall(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    // when search button click
    public void Search(View view) {
        cardView.setVisibility(View.GONE);
        String stID = stockID.getText().toString();
        if(!TextUtils.isEmpty(stID)){
            HashMap<String,String> map = new HashMap<>();
            map.put("ID",stID);
            apiCall.Insert(map, "getStockData.php", new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                   try {
                       JSONObject data = new JSONObject(result);
                       JSONObject ob = data.getJSONObject("data");
                       if(data.getString("status").trim().equals("1")){
                         cardView.setVisibility(View.VISIBLE);
                         stockID.getText().clear();
                         ID.setText(ob.getString("id"));
                         String ff = (ob.getString("fringerprint").trim().equals("0")) ? "Yes" : "No";
                         fringerprint.setText(ff);
                         backCamera.setText(ob.getString("back_camera")+ " MP");
                         ram.setText(ob.getString("ram")+" GB");
                         sim.setText(ob.getString("sim"));
                         frontCamera.setText(ob.getString("font_camera")+" MP");
                         memory.setText(ob.getString("memory")+" GB");
                         network.setText(ob.getString("network"));
                         company.setText(ob.getString("date").toUpperCase());
                         price.setText(ob.getString("selling_price"));
                       }
                       else{
                           Toast.makeText(GenerateMobileBill.this, data.getString("message"), Toast.LENGTH_SHORT).show();
                       }
                   }
                   catch (Exception e){
                       Log.e("Basit",e.getMessage());
                       Toast.makeText(GenerateMobileBill.this, "Error occured while processing json data", Toast.LENGTH_SHORT).show();
                   }
                }
            });
        }
        else{
            stockID.setError("Stock id is required");
            Toast.makeText(this, "Enter stock id first....", Toast.LENGTH_SHORT).show();
        }
    }

    public void SubmitRequest(View view) {
        String cn = cname.getText().toString(), cm = cmobile.getText().toString(),id = ID.getText().toString();
        if(HelperFunctions.verify(cn) && HelperFunctions.verify(cm) && HelperFunctions.verify(id)){
          HashMap<String,String> map = new HashMap<>();
          map.put("name",cn);
          map.put("mobile",cm);
          map.put("id",id);
          map.put("submitRequest","submitRequest");

          apiCall.Insert(map, "getStockData.php", new VolleyCallback() {
              @Override
              public void onSuccess(String result) {
                  try {
                      JSONObject object=new JSONObject(result);
                      if(object.getString("status").trim().equals("1")){
                          Toast.makeText(GenerateMobileBill.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                          startActivity(new Intent(getApplicationContext(),ShopkeeperHome.class));
                          finish();
                      }
                      else {
                          Toast.makeText(GenerateMobileBill.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                      }
                  }
                  catch (Exception e){
                      Toast.makeText(GenerateMobileBill.this, "Error occured in json parsing", Toast.LENGTH_SHORT).show();
                  }
              }
          });
        }
        else{
            cname.setError("Required");
            cmobile.setError("Required");
            Toast.makeText(GenerateMobileBill.this, "Enter customer name and mobile number", Toast.LENGTH_SHORT).show();
        }
    }
}