package com.ss_technology.hanguoilproject.DBS_Shop_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ss_technology.hanguoilproject.Config.ApiCall;
import com.ss_technology.hanguoilproject.Config.HelperFunctions;
import com.ss_technology.hanguoilproject.Config.KeepMeLogin;
import com.ss_technology.hanguoilproject.Config.VolleyCallback;
import com.ss_technology.hanguoilproject.R;

import org.json.JSONObject;

import java.util.HashMap;

public class Gen_Accessory_Bill extends AppCompatActivity {

    TextView ID,name,quantity;
    EditText price,stockID;
    CardView cardView;
    ApiCall apiCall;
    KeepMeLogin keepMeLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_accessory_bill);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ID = findViewById(R.id.ID);
        keepMeLogin = new KeepMeLogin(this);
        name = findViewById(R.id.name);
        quantity = findViewById(R.id.quantity);
        price = findViewById(R.id.price);
        cardView = findViewById(R.id.cardView);
        stockID = findViewById(R.id.stockID);
        apiCall = new ApiCall(this);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    public void Search(View view) {
        cardView.setVisibility(View.GONE);
     String sst = stockID.getText().toString();
     if(HelperFunctions.verify(sst)){
         HashMap<String,String> map = new HashMap<>();
         map.put("ID",sst);
         map.put("getAccessordatabyID","getAccessordatabyID");
         apiCall.Insert(map, "Shopkeeper.php", new VolleyCallback() {
             @Override
             public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if(object.getString("status").trim().equals("1")){
                        cardView.setVisibility(View.VISIBLE);
                        stockID.getText().clear();
                      JSONObject ob = object.getJSONObject("data");
                      ID.setText(ob.getString("id"));
                      name.setText(ob.getString("name"));
                      quantity.setText(ob.getString("quantity"));
                      price.setText(ob.getString("selling"));
                    }
                    else{
                        Toast.makeText(Gen_Accessory_Bill.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Toast.makeText(Gen_Accessory_Bill.this, "Error occured in json parsing", Toast.LENGTH_SHORT).show();
                }
             }
         });
     }
     else{
         stockID.setError("Required");
         Toast.makeText(this, "Enter stock id first...", Toast.LENGTH_SHORT).show();
     }

    }

    public void SubmitRequest(View view) {
        String pp = price.getText().toString(), id = ID.getText().toString();
        if(HelperFunctions.verify(pp) && HelperFunctions.verify(id)){

            try {
               JSONObject object = new JSONObject(keepMeLogin.getData());
                HashMap<String,String> map = new HashMap<>();
                map.put("price",pp);
                map.put("stockID",id);
                map.put("userID",object.getString("id"));
                map.put("generateAccessoryBill","generateAccessoryBill");
                apiCall.Insert(map, "Shopkeeper.php", new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject ob= new JSONObject(result);
                            if(ob.getString("status").trim().equals("1")){
                                Toast.makeText(Gen_Accessory_Bill.this, ob.getString("message"), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Gen_Accessory_Bill.this,ShopkeeperHome.class));
                                finish();
                            }
                            else{
                                Toast.makeText(Gen_Accessory_Bill.this, ob.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            Toast.makeText(Gen_Accessory_Bill.this, "Error occurred in json parsing", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            catch (Exception e){
                Toast.makeText(Gen_Accessory_Bill.this, "Error occurred while getting shopkeeper info", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(Gen_Accessory_Bill.this, "Price should not be empty!", Toast.LENGTH_SHORT).show();
        }
    }
}