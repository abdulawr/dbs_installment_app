package com.ss_technology.hanguoilproject.Admin_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;
import com.ss_technology.hanguoilproject.Config.ApiCall;
import com.ss_technology.hanguoilproject.Config.BaseURL;
import com.ss_technology.hanguoilproject.Config.HelperFunctions;
import com.ss_technology.hanguoilproject.Config.KeepMeLogin;
import com.ss_technology.hanguoilproject.Config.Messages;
import com.ss_technology.hanguoilproject.Config.VolleyCallback;
import com.ss_technology.hanguoilproject.Container.Customer_Container;
import com.ss_technology.hanguoilproject.R;

import org.json.JSONObject;

import java.util.HashMap;

public class InvestorProfile extends AppCompatActivity {


    ImageView profile;
    TextView name,id,cnic,mobile,address,balance;
    Customer_Container data;
    ApiCall apiCall;
    KeepMeLogin keepMeLogin;
    Button pendingBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Investor Profile");
        setContentView(R.layout.activity_investor_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        profile = findViewById(R.id.profile);
        name = findViewById(R.id.name);
        id = findViewById(R.id.id);
        cnic = findViewById(R.id.cnic);
        mobile = findViewById(R.id.mobile);
        address = findViewById(R.id.address);
        pendingBalance = findViewById(R.id.pendingBalance);
        balance = findViewById(R.id.balance);
        apiCall = new ApiCall(this);
        keepMeLogin = new KeepMeLogin(this);

        data = (Customer_Container) getIntent().getSerializableExtra("ob");
        Picasso.get().load(BaseURL.ImagePath("investor")+data.getImage()).into(profile);
        name.setText(data.getName());
        id.setText(data.getId()+"   (id)");
        mobile.setText(data.getMobile()+"   (mobile)");
        cnic.setText(data.getCnic()+"   (cnic)");
        address.setText(data.getAddress());

        HashMap<String,String> map = new HashMap<>();
        map.put("type","getInvestorBalance");
        map.put("adminID", HelperFunctions.AdminID(this));
        map.put("invID",data.getId());

        if (keepMeLogin.getAdminType().trim().equals("2")){
            pendingBalance.setVisibility(View.GONE);
        }

        apiCall.Insert(map, Messages.FileName, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
             try {
                 JSONObject object = new JSONObject(result);
                 if(object.getString("status").trim().equals("1")){
                     balance.setText(object.getString("balance")+" PKR");
                 }
                 else{
                     Toast.makeText(InvestorProfile.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                 }
             }
             catch (Exception e){
                 HelperFunctions.Message(InvestorProfile.this,Messages.JsonMsg);
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

    public void View_Application(View view) {
        Intent intent = new Intent(getApplicationContext(),ApplicationList.class);
        intent.putExtra("invID",data.getId());
        startActivity(intent);
    }

    public void Add_Balance(View view) {
        showBalanceBottomSheet("0");
    }

    public void Sub_Balance(View view) {
        showBalanceBottomSheet("1");
    }

    void showBalanceBottomSheet(String type){
        // 0 => add balance & 1 => sub balance

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(InvestorProfile.this,R.style.DialogStyle);
        bottomSheetDialog.setContentView(R.layout.rechare_investor_account);
        bottomSheetDialog.setCancelable(false);

        TextView title = bottomSheetDialog.findViewById(R.id.title);
        if(type.trim().equals("0")){
           title.setText("Add Balance");
        }
        else{
            title.setText("Sub Balance");
        }


        Button closebtn = bottomSheetDialog.findViewById(R.id.closebtn);
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        TextView amounts = bottomSheetDialog.findViewById(R.id.amounts);
        TextView des = bottomSheetDialog.findViewById(R.id.des);

        Button submitTransaction = bottomSheetDialog.findViewById(R.id.submitTransaction);
        submitTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amt = amounts.getText().toString();
                String des_val = des.getText().toString();

                if(HelperFunctions.verify(amt) && HelperFunctions.verify(des_val)){
                   HashMap<String,String> map = new HashMap<>();
                   map.put("type","insert_amount_into_investorProfile");
                   map.put("adminID",HelperFunctions.AdminID(InvestorProfile.this));
                   map.put("adminType",keepMeLogin.getAdminType());
                   map.put("tran_type",type);
                   map.put("amount",amt);
                   map.put("des",des_val);
                   map.put("ID",data.getId());
                   apiCall.Insert(map, Messages.FileName, new VolleyCallback() {
                       @Override
                       public void onSuccess(String result) {
                        try {
                            JSONObject object = new JSONObject(result);
                            if(object.getString("status").trim().equals("1")){
                                Toast.makeText(InvestorProfile.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                InvestorProfile.super.recreate();
                            }
                            else{
                                Toast.makeText(InvestorProfile.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e){
                            Toast.makeText(InvestorProfile.this, Messages.JsonMsg, Toast.LENGTH_SHORT).show();
                        }
                       }
                   });
                }
                else{
                    Toast.makeText(InvestorProfile.this, "Enter values correclty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomSheetDialog.show();

    }

    public void Investor_User(View view) {
        Intent intent = new Intent(getApplicationContext(),InvestorUser.class);
        intent.putExtra("id",data.getId());
        startActivity(intent);
    }

    public void Investor_Pending_Balance(View view) {
        Intent intent = new Intent(getApplicationContext(),InvestorPending_Balnace.class);
        intent.putExtra("id",data.getId());
        startActivity(intent);
    }
}