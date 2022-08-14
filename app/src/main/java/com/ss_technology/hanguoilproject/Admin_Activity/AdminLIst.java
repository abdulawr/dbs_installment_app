package com.ss_technology.hanguoilproject.Admin_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.ss_technology.hanguoilproject.Adapter.AdminList_Adapter;
import com.ss_technology.hanguoilproject.Config.ApiCall;
import com.ss_technology.hanguoilproject.Config.HelperFunctions;
import com.ss_technology.hanguoilproject.Config.VolleyCallback;
import com.ss_technology.hanguoilproject.Container.AdminListContainter;
import com.ss_technology.hanguoilproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminLIst extends AppCompatActivity {

    EditText searchEd;
    RecyclerView rec;
    ApiCall apiCall;
    ArrayList<AdminListContainter> list;
    AdminList_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_list);

        searchEd = findViewById(R.id.searchEd);
        rec = findViewById(R.id.rec);
        apiCall = new ApiCall(this);
        list = new ArrayList<>();

        HashMap<String,String> map = new HashMap<>();
        map.put("type","getAdminList");
        map.put("adminID", HelperFunctions.AdminID(this));
        apiCall.Insert(map, "AdminApi.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray array = new JSONArray(result);
                    for (int i= 0; i<array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        if(!object.getString("type").trim().equals("1")) {
                            AdminListContainter con = new AdminListContainter();
                            con.setName(object.getString("name"));
                            con.setId(object.getString("id"));
                            con.setMobile(object.getString("mobile"));
                            con.setEmail(object.getString("email"));
                            con.setType(object.getString("type"));
                            con.setStatus(object.getString("account_status"));
                            con.setBalance(object.getString("amount"));
                            con.setAccessBalance(object.getString("accessBalance"));
                            con.setImage(object.getString("image"));
                            list.add(con);
                        }
                    }

                    if(!list.isEmpty()){
                        adapter = new AdminList_Adapter(AdminLIst.this,list);
                        rec.setLayoutManager(new LinearLayoutManager(AdminLIst.this));
                        rec.setItemAnimator(new DefaultItemAnimator());
                        rec.setAdapter(adapter);
                    }
                }
                catch (Exception e){
                    Toast.makeText(AdminLIst.this, "Error occurred in json parsingQ", Toast.LENGTH_SHORT).show();
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
}