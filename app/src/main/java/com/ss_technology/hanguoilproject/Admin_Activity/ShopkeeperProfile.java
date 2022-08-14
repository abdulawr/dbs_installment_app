package com.ss_technology.hanguoilproject.Admin_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.ss_technology.hanguoilproject.Adapter.PendingPaymentAdapter;
import com.ss_technology.hanguoilproject.Config.ApiCall;
import com.ss_technology.hanguoilproject.Config.HelperFunctions;
import com.ss_technology.hanguoilproject.Config.Messages;
import com.ss_technology.hanguoilproject.Config.VolleyCallback;
import com.ss_technology.hanguoilproject.Container.PaymentContainer;
import com.ss_technology.hanguoilproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ShopkeeperProfile extends AppCompatActivity {

    ApiCall apiCall;
    RecyclerView rec;
    ArrayList<PaymentContainer> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(getIntent().getStringExtra("name"));
        setContentView(R.layout.activity_shopkeeper_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        apiCall = new ApiCall(this);
        rec = findViewById(R.id.rec);
        list = new ArrayList<>();

        HashMap<String,String> map = new HashMap<>();
        map.put("type","getAccessoryPendingpayment");
        map.put("adminID", HelperFunctions.AdminID(this));
        map.put("ID",getIntent().getStringExtra("ID"));
        apiCall.Insert(map, "AdminApi.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONArray array = new JSONArray(result);
                    for(int i=0; i<array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        PaymentContainer con = new PaymentContainer();
                        con.setId(object.getString("id"));
                        con.setAmount(object.getString("amount"));
                        con.setDate(object.getString("date"));
                        con.setAppID(object.getString("accessID"));
                        con.setAdminIDS(object.getString("sellID"));
                        list.add(con);
                    }

                    if(!list.isEmpty()){
                        PendingPaymentAdapter adapter = new PendingPaymentAdapter(ShopkeeperProfile.this,list,2);
                        rec.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ShopkeeperProfile.this);
                        rec.setLayoutManager(layoutManager);
                        rec.setAdapter(adapter);
                    }
                    else{
                        Toast.makeText(ShopkeeperProfile.this, "Nothing to show", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    HelperFunctions.Message(ShopkeeperProfile.this, Messages.JsonMsg);
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
}