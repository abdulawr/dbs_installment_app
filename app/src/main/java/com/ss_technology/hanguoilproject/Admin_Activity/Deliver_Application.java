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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.ss_technology.hanguoilproject.Config.ApiCall;
import com.ss_technology.hanguoilproject.Config.HelperFunctions;
import com.ss_technology.hanguoilproject.Config.KeepMeLogin;
import com.ss_technology.hanguoilproject.Config.Loading_Dai;
import com.ss_technology.hanguoilproject.Config.Messages;
import com.ss_technology.hanguoilproject.Config.VolleyCallback;
import com.ss_technology.hanguoilproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Deliver_Application extends AppCompatActivity {

    Spinner spinner;
    ApiCall apiCall;
    ArrayList<String> investorID,investName;
    LinearLayout spinner_layout;
    KeepMeLogin keepMeLogin;

    final int REQUST_CODE_IMAGE=222;
    Loading_Dai alert;
    ImageView chooseImage;
    Bitmap bitmap = null;
    final  int REQUEST_CODE=111;
    Button submitbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_application);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinner = findViewById(R.id.investList);
        apiCall = new ApiCall(this);
        keepMeLogin = new KeepMeLogin(this);
        alert = new Loading_Dai(this);
        chooseImage = findViewById(R.id.chooseImage);
        investorID = new ArrayList<>();
        investName = new ArrayList<>();
        submitbtn = findViewById(R.id.submitbtn);
        spinner_layout = findViewById(R.id.spinner_layout);
        spinner_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.performClick();
            }
        });

        HashMap<String,String> map = new HashMap<>();
        map.put("adminID", HelperFunctions.AdminID(this));
        map.put("type","getDeliverApplicationInvestorLIst");
        map.put("appID",getIntent().getStringExtra("id"));
        apiCall.Insert(map, Messages.FileName, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray array = new JSONArray(result);
                    for (int i=0; i<array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        investorID.add(object.getString("id"));
                        investName.add(object.getString("name")+"  cnic: "+object.getString("cnic"));
                    }

                    if(!investName.isEmpty()){
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Deliver_Application.this, android.R.layout.simple_list_item_1,investName);
                        spinner.setAdapter(adapter);
                    }
                    else{
                        Toast.makeText(Deliver_Application.this, "No investor found to match this application", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    HelperFunctions.Message(Deliver_Application.this,Messages.JsonMsg);
                }
            }
        });


        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(Deliver_Application.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
                }
                else
                {
                    SelectImage();
                }

            }
        });


    }

    public void SelectImage()
    {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        if(intent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUST_CODE_IMAGE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE && permissions.length >0)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                SelectImage();
            }
            else {
                ActivityCompat.requestPermissions(Deliver_Application.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUST_CODE_IMAGE && resultCode == Activity.RESULT_OK)
        {
            if(data != null)
            {
                Uri uri=data.getData();
                if (uri != null)
                {
                    try {
                        InputStream stream=getApplicationContext().getContentResolver().openInputStream(uri);
                        Bitmap original = BitmapFactory.decodeStream(stream);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        original.compress(Bitmap.CompressFormat.JPEG, 80, out);
                        bitmap = original;
//                        bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
//                        bitmap=Bitmap.createScaledBitmap(bitmap, 250, 250, false);
                        chooseImage.setImageBitmap(original);

                    } catch (Exception e) {
                        Toast.makeText(this, "Some thing went wrong try again.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    // to submit the application form
    public void Submit(View view) {
        if(bitmap != null){
            alert.Show();
            submitbtn.setClickable(false);
            String invID = investorID.get(spinner.getSelectedItemPosition());
            String image = HelperFunctions.BitmapToString(bitmap,80);

            HashMap<String,String> map = new HashMap<>();
            map.put("adminID",HelperFunctions.AdminID(Deliver_Application.this));
            map.put("type","submitDeliverApplicationForom");
            map.put("image",image);
            map.put("investorID",invID);
            map.put("adminType",keepMeLogin.getAdminType());
            map.put("appID",getIntent().getStringExtra("id"));
            apiCall.Insert2(map, Messages.FileName, new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                  try {
                      JSONObject object = new JSONObject(result);
                      if(object.getString("status").trim().equals("1")){
                          startActivity(new Intent(getApplicationContext(),Admin_Home.class));
                          finish();
                      }
                      Toast.makeText(Deliver_Application.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                      alert.Hide();
                      submitbtn.setClickable(true);
                  }
                  catch (Exception e){
                      alert.Hide();
                      submitbtn.setClickable(true);
                      Toast.makeText(Deliver_Application.this, Messages.JsonMsg, Toast.LENGTH_SHORT).show();
                  }
                }
            });

        }
        else{
            Toast.makeText(Deliver_Application.this, "Select deliver image first", Toast.LENGTH_SHORT).show();
        }

    }
}