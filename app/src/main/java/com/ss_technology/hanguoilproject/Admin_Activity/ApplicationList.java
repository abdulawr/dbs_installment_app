package com.ss_technology.hanguoilproject.Admin_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.ss_technology.hanguoilproject.Adapter.ApplicationAdapter;
import com.ss_technology.hanguoilproject.Config.ApiCall;
import com.ss_technology.hanguoilproject.Config.HelperFunctions;
import com.ss_technology.hanguoilproject.Config.Messages;
import com.ss_technology.hanguoilproject.Config.VolleyCallback;
import com.ss_technology.hanguoilproject.Container.Application_Container;
import com.ss_technology.hanguoilproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ApplicationList extends AppCompatActivity {

    ApiCall apiCall;
    ArrayList<Application_Container> list;
    EditText searchEd;
    RecyclerView rec;
    ApplicationAdapter adapter;
    String message = "Applications";
    String type = "";
    HashMap<String,String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent().hasExtra("type")) {
            type = getIntent().getStringExtra("type");
            if (type.trim().equals("0")) {
                message = "Pending Applications";
            } else if (type.trim().equals("1")) {
                message = "Accepted Applications";
            } else if (type.trim().equals("3")) {
                message = "Active Applications";
            } else if (type.trim().equals("4")) {
                message = "Completed Applications";
            } else if (type.trim().equals("5")) {
                message = "Rejected Applications";
            }
        }
        else{
            message = "Applications";
        }

        getSupportActionBar().setTitle(message);
        setContentView(R.layout.activity_application_list);
        map = new HashMap<>();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list = new ArrayList<>();
        searchEd = findViewById(R.id.searchEd);
        rec = findViewById(R.id.rec);
        apiCall = new ApiCall(this);
        map.put("type","getApplicationList");
        map.put("adminID", HelperFunctions.AdminID(this));

        if(getIntent().hasExtra("type"))
        {
            map.put("status",type);
        }
        else if(getIntent().hasExtra("cusID")){
            map.put("searchQuery","application.cusID = "+getIntent().getStringExtra("cusID"));
        }
        else if(getIntent().hasExtra("invID")){
            map.put("searchQuery","application.investorID  = "+getIntent().getStringExtra("invID"));
        }

        apiCall.Insert(map, "AdminApi.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
               try {
                   JSONArray array = new JSONArray(result);
                   if(array.length() > 0){
                       for (int i=0; i<array.length(); i++){
                           JSONObject object = array.getJSONObject(i);
                           Application_Container con = new Application_Container();
                           con.setCusID(object.getString("cusID"));
                           con.setCus_Name(object.getString("name"));
                           con.setCus_Mobile(object.getString("mobile"));
                           con.setCompany_Name(object.getString("comp"));
                           con.setItem_name(object.getString("item"));
                           con.setId(object.getString("id"));
                           con.setAge(object.getString("age"));
                           con.setMonthly_income(object.getString("monthly_income"));
                           con.setBusiness_type(object.getString("business_type"));
                           con.setBusiness_address(object.getString("business_address"));
                           con.setDate(object.getString("date"));
                           con.setModel_no(object.getString("model_no"));
                           con.setCompanyID(object.getString("companyID"));
                           con.setProductname(object.getString("product_name"));
                           con.setItem_typeID(object.getString("item_type_id"));
                           con.setProduct_orginal_price(object.getString("product_orginal_price"));
                           con.setPercentage_on_prod(object.getString("percentage_on_prod"));
                           con.setTotal_price(object.getString("total_price"));
                           con.setInstallment_months(object.getString("installment_months"));
                           con.setMonthly_payment(object.getString("monthly_payment"));
                           con.setAdvance_payment(object.getString("advance_payment"));
                           con.setStatus(object.getString("status"));
                           con.setRef_by(object.getString("ref_by"));
                           con.setDelivery_image(object.getString("delivery_image"));
                           con.setInvestorID(object.getString("investorID"));
                           con.setDiscount_amount(object.getString("discount_amount"));
                           con.setItem_des(object.getString("item_des"));
                           con.setRej_des(object.getString("rej_des"));
                           con.setAdvance_payment_status(object.getString("advance_payment_status"));
                           con.setAdvance_payment_paid(object.getString("advance_payment_paid"));
                           con.setActive_date(object.getString("active_date"));
                           list.add(con);

                       }

                       if (!list.isEmpty()){
                           rec.setHasFixedSize(true);
                           rec.setLayoutManager(new LinearLayoutManager(ApplicationList.this));
                           adapter = new ApplicationAdapter(ApplicationList.this,list);
                           rec.setAdapter(adapter);
                       }

                   }
                   else{
                       HelperFunctions.Message(ApplicationList.this,"Nothing to show");
                   }
               }
               catch (Exception e){
                   Toast.makeText(ApplicationList.this, Messages.JsonMsg, Toast.LENGTH_SHORT).show();
               }
            }
        });

        searchEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }
}