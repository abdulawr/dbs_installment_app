package com.ss_technology.hanguoilproject.Admin_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Helper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.ss_technology.hanguoilproject.Adapter.InvestorUser_Adapter;
import com.ss_technology.hanguoilproject.Config.ApiCall;
import com.ss_technology.hanguoilproject.Config.HelperFunctions;
import com.ss_technology.hanguoilproject.Config.Messages;
import com.ss_technology.hanguoilproject.Config.VolleyCallback;
import com.ss_technology.hanguoilproject.Container.Investor_User_Container;
import com.ss_technology.hanguoilproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class InvestorUser extends AppCompatActivity {

    RecyclerView rec;
    ApiCall apiCall;
    ArrayList<Investor_User_Container> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Investor Users");
        setContentView(R.layout.activity_investor_user);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rec = findViewById(R.id.rec);
        apiCall = new ApiCall(this);
        list = new ArrayList<>();

        HashMap<String,String> map = new HashMap<>();
        map.put("type","getinvestorUser");
        map.put("adminID", HelperFunctions.AdminID(this));
        map.put("ID",getIntent().getStringExtra("id"));
        apiCall.Insert(map, Messages.FileName, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray array = new JSONArray(result);
                    for(int i=0; i<array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        Investor_User_Container con = new Investor_User_Container();
                        con.setId(object.getString("id"));
                        con.setCnic(object.getString("cnic"));
                        con.setMobile(object.getString("mobile"));
                        con.setName(object.getString("name"));
                        con.setAppID(object.getString("appID"));
                        list.add(con);
                    }

                    if(!list.isEmpty()){
                        rec.setHasFixedSize(true);
                        rec.setLayoutManager(new LinearLayoutManager(InvestorUser.this));
                        InvestorUser_Adapter adapter = new InvestorUser_Adapter(InvestorUser.this,list);
                        rec.setAdapter(adapter);
                    }
                    else {
                        Toast.makeText(InvestorUser.this, "Nothing to show!", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    HelperFunctions.Message(InvestorUser.this,Messages.JsonMsg);
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