package com.ss_technology.hanguoilproject.Admin_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ss_technology.hanguoilproject.Config.BaseURL;
import com.ss_technology.hanguoilproject.Container.Customer_Container;
import com.ss_technology.hanguoilproject.R;

public class CustomerProfile extends AppCompatActivity {

    ImageView profile;
    TextView name,id,cnic,mobile,address;
    Customer_Container data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Customer Profile");
        setContentView(R.layout.activity_customer_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        profile = findViewById(R.id.profile);
        name = findViewById(R.id.name);
        id = findViewById(R.id.id);
        cnic = findViewById(R.id.cnic);
        mobile = findViewById(R.id.mobile);
        address = findViewById(R.id.address);


         data = (Customer_Container) getIntent().getSerializableExtra("ob");
        Picasso.get().load(BaseURL.ImagePath("customer")+data.getImage()).into(profile);
        name.setText(data.getName());
        id.setText(data.getId()+"   (id)");
        mobile.setText(data.getMobile()+"   (mobile)");
        cnic.setText(data.getCnic()+"   (cnic)");
        address.setText(data.getAddress());

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    public void View_Application(View view) {
        Intent intent = new Intent(getApplicationContext(),ApplicationList.class);
        intent.putExtra("cusID",data.getId());
        startActivity(intent);
    }

    public void Create_Application(View view) {
        Intent intent = new Intent(getApplicationContext(),CreateApplication.class);
        intent.putExtra("ob",data);
        startActivity(intent);
    }
}