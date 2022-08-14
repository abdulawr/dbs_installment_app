package com.ss_technology.hanguoilproject.Admin_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.ss_technology.hanguoilproject.Adapter.Pending_Balance_Adapter;
import com.ss_technology.hanguoilproject.Config.ApiCall;
import com.ss_technology.hanguoilproject.Config.HelperFunctions;
import com.ss_technology.hanguoilproject.Config.Messages;
import com.ss_technology.hanguoilproject.Config.VolleyCallback;
import com.ss_technology.hanguoilproject.Container.PendingBalance_Container;
import com.ss_technology.hanguoilproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InvestorPending_Balnace extends AppCompatActivity {

    ApiCall apiCall;
    RecyclerView rec;
    List<PendingBalance_Container> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Pending Balance");
        setContentView(R.layout.activity_investor_pending_balnace);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        apiCall = new ApiCall(this);
        rec = findViewById(R.id.rec);
        list = new ArrayList<>();

        HashMap<String,String> map = new HashMap<>();
        map.put("type","getInvestorPendingBalance");
        map.put("adminID", HelperFunctions.AdminID(this));
        map.put("ID",getIntent().getStringExtra("id"));
        apiCall.Insert(map, Messages.FileName, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONArray array = new JSONArray(result);
                    for(int i=0; i<array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        PendingBalance_Container con = new PendingBalance_Container();
                        con.setId(object.getString("id"));
                        con.setInvest_amount(object.getString("invest_amount"));
                        con.setInvesID(object.getString("investorID"));
                        con.setAppID(object.getString("appID"));
                        con.setCusID(object.getString("cusID"));
                        con.setDate(object.getString("date"));
                        con.setProfit(object.getString("profit"));
                        con.setTotal_amount(object.getString("total_amount"));
                        con.setPaid(object.getString("paid"));
                        con.setPayable(object.getString("payable"));
                        list.add(con);
                    }

                    if(!list.isEmpty()){
                        rec.setHasFixedSize(true);
                        rec.setLayoutManager(new LinearLayoutManager(InvestorPending_Balnace.this));
                        Pending_Balance_Adapter adapter = new Pending_Balance_Adapter(InvestorPending_Balnace.this, list, new Pending_Balance_Adapter.OnClist_Listner() {
                            @Override
                            public void values(String id, String invesID,String pay) {

                                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(InvestorPending_Balnace.this,R.style.DialogStyle);
                                bottomSheetDialog.setContentView(R.layout.add_pending_balance_view_bottomsheet);
                                bottomSheetDialog.setCancelable(false);

                                Button closebtn = bottomSheetDialog.findViewById(R.id.closebtn);
                                closebtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        bottomSheetDialog.dismiss();
                                    }
                                });

                                TextInputEditText amounts = bottomSheetDialog.findViewById(R.id.amounts);
                                Button submitTransaction = bottomSheetDialog.findViewById(R.id.submitTransaction);
                                submitTransaction.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (HelperFunctions.verify(amounts.getText().toString())) {
                                            HashMap<String, String> map = new HashMap<>();
                                            map.put("type", "submitPendingBalance");
                                            map.put("adminID", HelperFunctions.AdminID(InvestorPending_Balnace.this));
                                            map.put("id", id);
                                            map.put("investID", invesID);
                                            map.put("amount", amounts.getText().toString());
                                            map.put("pay", pay);
                                            apiCall.Insert(map, Messages.FileName, new VolleyCallback() {
                                                @Override
                                                public void onSuccess(String result) {
                                                    try {
                                                        JSONObject object = new JSONObject(result);
                                                        if (object.getString("status").trim().equals("1")) {
                                                            Toast.makeText(InvestorPending_Balnace.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                                            bottomSheetDialog.dismiss();
                                                           finish();
                                                        } else {
                                                            Toast.makeText(InvestorPending_Balnace.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                                            bottomSheetDialog.dismiss();
                                                        }
                                                    } catch (Exception e) {
                                                        Toast.makeText(InvestorPending_Balnace.this, Messages.JsonMsg, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                        else{
                                            Toast.makeText(InvestorPending_Balnace.this, "Enter amount first", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                                bottomSheetDialog.show();

                            }
                        });

                        rec.setAdapter(adapter);
                    }
                    else {
                        HelperFunctions.Message(InvestorPending_Balnace.this,"Nothing to show");
                    }
                }
                catch (Exception e){
                    HelperFunctions.Message(InvestorPending_Balnace.this,Messages.JsonMsg);
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