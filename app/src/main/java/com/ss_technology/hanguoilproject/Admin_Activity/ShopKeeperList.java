package com.ss_technology.hanguoilproject.Admin_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.ss_technology.hanguoilproject.Adapter.Shopkeeper_Adapter;
import com.ss_technology.hanguoilproject.Config.ApiCall;
import com.ss_technology.hanguoilproject.Config.HelperFunctions;
import com.ss_technology.hanguoilproject.Config.Messages;
import com.ss_technology.hanguoilproject.Config.VolleyCallback;
import com.ss_technology.hanguoilproject.Container.ShopkeeperList_Container;
import com.ss_technology.hanguoilproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ShopKeeperList extends AppCompatActivity {

    RecyclerView recyclerView;
    ApiCall apiCall;
    ArrayList<ShopkeeperList_Container> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_keeper_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.rec);
        apiCall = new ApiCall(this);
        list = new ArrayList<>();

        HashMap map=new HashMap();
        map.put("adminID", HelperFunctions.AdminID(this));
        map.put("type","getShopkeeperList");
        apiCall.Insert(map, "AdminApi.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONArray array = new JSONArray(result);
                    for (int i=0; i<array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        ShopkeeperList_Container con = new ShopkeeperList_Container();
                        con.setId(object.getString("id"));
                        con.setName(object.getString("name"));
                        con.setMobile(object.getString("mobile"));
                        con.setCnic(object.getString("cnic"));
                        con.setSalary(object.getString("salary"));
                        con.setImage(object.getString("image"));
                        con.setStatus(object.getString("status"));
                        con.setBalance(object.getString("balance"));

                        list.add(con);
                    }
                    if (!list.isEmpty()) {
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ShopKeeperList.this));
                        Shopkeeper_Adapter adapter = new Shopkeeper_Adapter(ShopKeeperList.this, list);
                        recyclerView.setAdapter(adapter);
                    }
                }
                catch (Exception e){
                    HelperFunctions.Message(ShopKeeperList.this, Messages.JsonMsg);
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