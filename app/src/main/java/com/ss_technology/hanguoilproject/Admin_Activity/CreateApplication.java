package com.ss_technology.hanguoilproject.Admin_Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.ss_technology.hanguoilproject.Config.ApiCall;
import com.ss_technology.hanguoilproject.Config.BaseURL;
import com.ss_technology.hanguoilproject.Config.HelperFunctions;
import com.ss_technology.hanguoilproject.Config.Loading_Dai;
import com.ss_technology.hanguoilproject.Config.Messages;
import com.ss_technology.hanguoilproject.Config.VolleyCallback;
import com.ss_technology.hanguoilproject.Container.Customer_Container;
import com.ss_technology.hanguoilproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

public class CreateApplication extends AppCompatActivity {

    ImageView profile;
    TextView name,id,cnic,mobile;
    Customer_Container data;
    ApiCall apiCall;
    TextInputEditText ph1,ph2,ph3,ph4;
    Button submitApplication;
    Spinner companySpinner,itemTypeSpinner;
    Loading_Dai loading_dai;

    ArrayList<String> cmpName,cmpID,itemName,itemID;

    TextInputEditText itemPrice,percentage,total_price,installmentMonths,monthly_payments,advance_payment;
    TextInputEditText age,monthly_income,business_type,business_address;
    TextInputEditText item_name,model_no,ref_by,item_des;
    TextInputEditText p_name,p_mobile,p_cnic,p_fname,p_business_address,p_business_type,p_address;

    ImageView p_personal_image,p_cnic_image;
    Bitmap pp_bitmap = null,pc_bitmap = null;
    final int PP_CODE=111;
    final int PC_CODE=222;
    final  int REQUEST_CODE=111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_application);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        apiCall = new ApiCall(this);
        loading_dai = new Loading_Dai(this);
        profile = findViewById(R.id.profile);
        name = findViewById(R.id.name);
        id = findViewById(R.id.id);
        cnic = findViewById(R.id.cnic);
        mobile = findViewById(R.id.mobile);
        cmpName = new ArrayList<>();
        cmpID = new ArrayList<>();
        itemName = new ArrayList<>();
        itemID = new ArrayList<>();

        data = (Customer_Container) getIntent().getSerializableExtra("ob");
        Picasso.get().load(BaseURL.ImagePath("customer")+data.getImage()).into(profile);
        name.setText(data.getName());
        id.setText(data.getId()+"   (id)");
        mobile.setText(data.getMobile()+"   (mobile)");
        cnic.setText(data.getCnic()+"   (cnic)");


            submitApplication = findViewById(R.id.submitApplication);
            ph1 = findViewById(R.id.ph1);
            ph2 = findViewById(R.id.ph2);
            ph3 = findViewById(R.id.ph3);
            ph4 = findViewById(R.id.ph4);

            companySpinner = findViewById(R.id.companySpinner);
            itemTypeSpinner = findViewById(R.id.itemTypeSpinner);

            HashMap<String,String> map = new HashMap<>();
            map.put("type","getApplicationSpinnerData");
            map.put("adminID",HelperFunctions.AdminID(this));
            apiCall.Insert(map, Messages.FileName, new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                   try {
                       JSONObject object = new JSONObject(result);
                       JSONArray company = object.getJSONArray("company");
                       JSONArray items = object.getJSONArray("items");

                       for (int i=0; i<company.length(); i++){
                           JSONObject single = company.getJSONObject(i);
                           cmpID.add(single.getString("id"));
                           cmpName.add(single.getString("name"));
                       }

                       for (int i=0; i<items.length(); i++){
                           JSONObject single = items.getJSONObject(i);
                           itemName.add(single.getString("name"));
                           itemID.add(single.getString("id"));
                       }

                       ArrayAdapter<String> cmpAdapter = new ArrayAdapter<>(CreateApplication.this, android.R.layout.simple_list_item_1,cmpName);
                       ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(CreateApplication.this, android.R.layout.simple_list_item_1,itemName);
                       companySpinner.setAdapter(cmpAdapter);
                       itemTypeSpinner.setAdapter(itemAdapter);
                   }
                   catch (Exception e){
                       Toast.makeText(CreateApplication.this, Messages.JsonMsg, Toast.LENGTH_SHORT).show();
                       finish();
                   }
                }
            });


        itemPrice = findViewById(R.id.itemPrice);
        percentage = findViewById(R.id.percentage);
        total_price = findViewById(R.id.total_price);
        installmentMonths = findViewById(R.id.installmentMonths);
        monthly_payments = findViewById(R.id.monthly_payments);
        advance_payment = findViewById(R.id.advance_payment);
        item_name = findViewById(R.id.item_name);
        model_no = findViewById(R.id.model_no);
        ref_by = findViewById(R.id.ref_by);
        item_des = findViewById(R.id.item_des);

        age = findViewById(R.id.age);
        monthly_income = findViewById(R.id.monthly_income);
        business_type = findViewById(R.id.business_type);
        business_address = findViewById(R.id.business_address);

        p_name  = findViewById(R.id.p_name);
        p_mobile = findViewById(R.id.p_mobile);
        p_cnic = findViewById(R.id.p_cnic);
        p_fname = findViewById(R.id.p_fname);
        p_business_address = findViewById(R.id.p_business_address);
        p_business_type = findViewById(R.id.p_business_type);
        p_address = findViewById(R.id.p_address);

        p_personal_image  = findViewById(R.id.p_personal_image);
        p_cnic_image  = findViewById(R.id.p_cnic_image);

        p_personal_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(CreateApplication.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
                }
                else
                {
                    SelectImage(PP_CODE);
                }
            }
        });

        p_cnic_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(CreateApplication.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
                }
                else
                {
                    SelectImage(PC_CODE);
                }
            }
        });

        itemPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               // text here
                    int per = (HelperFunctions.verify(percentage.getText().toString())) ? Integer.parseInt(percentage.getText().toString()) : 0;
                    int item_price = (HelperFunctions.verify(charSequence.toString())) ? Integer.parseInt(charSequence.toString()) : 0;
                    if(per > 0){
                        int result = (int) Math.ceil(item_price / 100);
                        result =  result * per;
                        result  = (int) Math.ceil(result);
                        advance_payment.setText(String.valueOf(result));
                        result += item_price;
                        total_price.setText(String.valueOf(result));
                    }
                    else{
                        total_price.setText(String.valueOf(item_price));
                    }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        percentage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // text here

                    int per = (HelperFunctions.verify(charSequence.toString())) ? Integer.parseInt(charSequence.toString()) : 0;
                    int item_price = (HelperFunctions.verify(itemPrice.getText().toString())) ? Integer.parseInt(itemPrice.getText().toString()) : 0;

                    if(item_price > 0){
                        int result = (int) Math.ceil(item_price / 100);
                        result =  result * per;
                        result  = (int) Math.ceil(result);
                        advance_payment.setText(String.valueOf(result));
                        result += item_price;
                        total_price.setText(String.valueOf(result));
                    }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        installmentMonths.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // text here
                if(HelperFunctions.verify(charSequence.toString())){
                    int install = Integer.parseInt(charSequence.toString());

                    if(install <= 24 || install >= 1){
                        int tot_price = Integer.parseInt(total_price.getText().toString());
                        int advance_pay = Integer.parseInt(advance_payment.getText().toString());
                        int month = (int) Math.ceil(tot_price - advance_pay);
                        month = (int) Math.ceil(month / install);
                        monthly_payments.setText(String.valueOf(month));
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        // submit application
        submitApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 submitApplication.setClickable(false);
                if(HelperFunctions.verify(ph1.getText().toString())){

                    JSONArray phones = new JSONArray();
                    phones.put(ph1.getText().toString());
                    phones.put(ph2.getText().toString());
                    phones.put(ph3.getText().toString());
                    phones.put(ph4.getText().toString());

                    String age_value = age.getText().toString();
                    String monthly_income_value = monthly_income.getText().toString();
                    String business_type_value = business_type.getText().toString();
                    String business_address_value = business_address.getText().toString();

                    String cusID = data.getId();
                    String companyID = cmpID.get(companySpinner.getSelectedItemPosition());
                    String itemIDs = itemID.get(itemTypeSpinner.getSelectedItemPosition());
                    String itemPrice_value = itemPrice.getText().toString();
                    String percentage_value = percentage.getText().toString();
                    String total_price_value = total_price.getText().toString();
                    String installmentMonths_value = installmentMonths.getText().toString();
                    String monthly_payments_value = monthly_payments.getText().toString();
                    String advance_payment_value = advance_payment.getText().toString();
                    String item_name_value = item_name.getText().toString();
                    String model_no_value = model_no.getText().toString();
                    String ref_by_value = ref_by.getText().toString();
                    String item_des_value = item_des.getText().toString();

                    String p_name_value = p_name.getText().toString();
                    String p_mobile_value = p_mobile.getText().toString();
                    String p_cnic_value = p_cnic.getText().toString();
                    String p_fname_value = p_fname.getText().toString();
                    String  p_business_address_value = p_business_address.getText().toString();
                    String p_business_type_value = p_business_type.getText().toString();
                    String p_address_value = p_address.getText().toString();

                    if(HelperFunctions.verify(age_value) && HelperFunctions.verify(monthly_income_value) && HelperFunctions.verify(business_type_value)
                            && HelperFunctions.verify(business_address_value) && HelperFunctions.verify(itemPrice_value) && HelperFunctions.verify(percentage_value)
                            && HelperFunctions.verify(total_price_value) && HelperFunctions.verify(installmentMonths_value) && HelperFunctions.verify(monthly_payments_value)
                            && HelperFunctions.verify(advance_payment_value) && HelperFunctions.verify(item_name_value) && HelperFunctions.verify(item_des_value)) {

                        String pp_image_value = "";
                        String pc_image_value = "";

                        if (pc_bitmap != null) {
                            pc_image_value = HelperFunctions.BitmapToString(pc_bitmap, 80);
                        }
                        if (pp_bitmap != null) {
                            pp_image_value = HelperFunctions.BitmapToString(pp_bitmap, 80);
                        }

                        JSONObject proof_obj = new JSONObject();
                        try {
                            proof_obj.put("name", p_name_value);
                            proof_obj.put("mobile", p_mobile_value);
                            proof_obj.put("cnic", p_cnic_value);
                            proof_obj.put("fname", p_fname_value);
                            proof_obj.put("business_address", p_business_address_value);
                            proof_obj.put("business_type", p_business_type_value);
                            proof_obj.put("address", p_address_value);
                            proof_obj.put("personal_image", pp_image_value);
                            proof_obj.put("cnic_image", pc_image_value);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        HashMap<String, String> map = new HashMap<>();
                        map.put("type", "CreateApplication");
                        map.put("adminID", HelperFunctions.AdminID(CreateApplication.this));
                        map.put("phones", phones.toString());
                        map.put("monthly_income", monthly_income_value);
                        map.put("business_type", business_type_value);
                        map.put("bus_address", business_address_value);
                        map.put("product_name", item_name_value);
                        map.put("company_name_id", companyID);
                        map.put("model_number", model_no_value);
                        map.put("item_type", itemIDs);
                        map.put("total_price", total_price_value);
                        map.put("age", age_value);
                        map.put("percentage_on_item", percentage_value);
                        map.put("orginal_price", itemPrice_value);
                        map.put("advance_payment", advance_payment_value);
                        map.put("monthly_payment", monthly_payments_value);
                        map.put("install_months", installmentMonths_value);
                        map.put("ref_by", ref_by_value);
                        map.put("item_desp", item_des_value);
                        map.put("cusID", cusID);
                        map.put("proof_person", proof_obj.toString());

                        apiCall.Insert(map, Messages.FileName, new VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                try {
                                    JSONObject object = new JSONObject(result);
                                    if (object.getString("status").trim().equals("1")) {
                                        startActivity(new Intent(getApplicationContext(), Admin_Home.class));
                                        finish();
                                        Toast.makeText(CreateApplication.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(CreateApplication.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(CreateApplication.this, Messages.JsonMsg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(CreateApplication.this, "Fill the form correctly", Toast.LENGTH_SHORT).show();
                    }

                }
                else{

                    Toast.makeText(CreateApplication.this, "Please provide at least one phone number!", Toast.LENGTH_SHORT).show();
                }
                submitApplication.setClickable(true);
            }
        });


    }

    public void SelectImage(int code)
    {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        if(intent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), code);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE && permissions.length >0)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //SelectImage();
            }
            else {
                ActivityCompat.requestPermissions(CreateApplication.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if((requestCode == PP_CODE || requestCode == PC_CODE)  && resultCode == Activity.RESULT_OK)
        {
            if(data != null)
            {
                Uri uri=data.getData();
                if (uri != null)
                {
                    try {

                        if(requestCode == PC_CODE){
                            InputStream stream=getApplicationContext().getContentResolver().openInputStream(uri);
                            pc_bitmap = BitmapFactory.decodeStream(stream);
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            pc_bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                            p_cnic_image.setImageBitmap(pc_bitmap);
                        }
                        else{
                            InputStream stream=getApplicationContext().getContentResolver().openInputStream(uri);
                            pp_bitmap = BitmapFactory.decodeStream(stream);
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            pp_bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                            p_personal_image.setImageBitmap(pp_bitmap);
                        }


                    } catch (Exception e) {
                        Toast.makeText(this, "Some thing went wrong try again.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }

    }

}