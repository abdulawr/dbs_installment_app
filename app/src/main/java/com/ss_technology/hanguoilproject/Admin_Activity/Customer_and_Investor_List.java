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
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ss_technology.hanguoilproject.Adapter.Customer_Adapter;
import com.ss_technology.hanguoilproject.Config.ApiCall;
import com.ss_technology.hanguoilproject.Config.HelperFunctions;
import com.ss_technology.hanguoilproject.Config.Messages;
import com.ss_technology.hanguoilproject.Config.VolleyCallback;
import com.ss_technology.hanguoilproject.Container.Customer_Container;
import com.ss_technology.hanguoilproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Customer_and_Investor_List extends AppCompatActivity {

    RecyclerView rec;
    ApiCall apiCall;
    TextView title;
    ArrayList<Customer_Container> list;
    Customer_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_customer_and_investor_list);

        // type 1 = > customer & 2 => investor

        rec = findViewById(R.id.rec);
        apiCall = new ApiCall(this);
        title = findViewById(R.id.title);
        list = new ArrayList<>();

        if(getIntent().getStringExtra("type").trim().equals("1")){
            title.setText("Customer List");
        }
        else{
            title.setText("Investor List");
        }

        HashMap<String,String> map = new HashMap<>();
        map.put("type","getCustomer_and_Investor_List");
        map.put("adminID", HelperFunctions.AdminID(this));
        map.put("status",getIntent().getStringExtra("type"));
        apiCall.Insert(map, Messages.FileName, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray array = new JSONArray(result);
                    for (int i =0; i<array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        Customer_Container con = new Customer_Container();
                        con.setType(getIntent().getStringExtra("type"));
                        con.setId(object.getString("id"));
                        con.setAddress(object.getString("address"));
                        con.setName(object.getString("name"));
                        con.setImage(object.getString("image"));
                        con.setMobile(object.getString("mobile"));
                        con.setCnic(object.getString("cnic"));
                        list.add(con);
                    }

                    if(!list.isEmpty()){
                      rec.setHasFixedSize(true);
                      rec.setLayoutManager(new LinearLayoutManager(Customer_and_Investor_List.this));
                      adapter = new Customer_Adapter(Customer_and_Investor_List.this,list);
                      rec.setAdapter(adapter);
                    }
                    else{
                        Toast.makeText(Customer_and_Investor_List.this, "Nothing to show", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Toast.makeText(Customer_and_Investor_List.this, Messages.JsonMsg, Toast.LENGTH_SHORT).show();
                }

            }
        });

        EditText  searchEd = findViewById(R.id.searchEd);
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

}