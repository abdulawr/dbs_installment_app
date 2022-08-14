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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.ss_technology.hanguoilproject.Config.ApiCall;
import com.ss_technology.hanguoilproject.Config.HelperFunctions;
import com.ss_technology.hanguoilproject.Config.Loading_Dai;
import com.ss_technology.hanguoilproject.Config.VolleyCallback;
import com.ss_technology.hanguoilproject.R;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

public class AddInvestor extends AppCompatActivity {

    TextInputEditText name,mobile,cnic,address;
    ImageView chooseImage;
    Bitmap bitmap = null;
    final  int REQUEST_CODE=111;
    Button btnClick;
    final int REQUST_CODE_IMAGE=222;
    Loading_Dai alert;
    ApiCall apiCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_investor);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        chooseImage = findViewById(R.id.chooseImage);
        name = findViewById(R.id.name);
        alert = new Loading_Dai(this);
        apiCall = new ApiCall(this);
        btnClick = findViewById(R.id.btnClick);
        mobile = findViewById(R.id.mobile);
        cnic = findViewById(R.id.cnic);
        address = findViewById(R.id.address);

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(AddInvestor.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
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
                ActivityCompat.requestPermissions(AddInvestor.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
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
                        original.compress(Bitmap.CompressFormat.PNG, 80, out);
                        bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                        bitmap=Bitmap.createScaledBitmap(bitmap, 250, 250, false);
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

    public void Submit(View view) {
        btnClick.setEnabled(false);
        alert.Show();

        String dname = name.getText().toString(),
                dmobile = mobile.getText().toString(),
                dcnic = cnic.getText().toString(),
                daddress = address.getText().toString();

        if(HelperFunctions.verify(dname) && HelperFunctions.verify(dmobile)
                && HelperFunctions.verify(dcnic) && HelperFunctions.verify(daddress)){

            if(bitmap != null){
                String image = HelperFunctions.BitmapToString(bitmap,90);
                HashMap<String,String> map = new HashMap<>();
                map.put("type","AddnewInvest");
                map.put("adminID",HelperFunctions.AdminID(AddInvestor.this));
                map.put("name",dname);
                map.put("mobile",dmobile);
                map.put("cnic",dcnic);
                map.put("address",daddress);
                map.put("image",image);

                apiCall.Insert2(map, "AdminApi.php", new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {

                        try {
                            JSONObject object = new JSONObject(result);
                            if(object.getString("status").trim().equals("1")){
                                name.getText().clear();
                                address.getText().clear();
                                mobile.getText().clear();
                                cnic.getText().clear();
                                chooseImage.setImageResource(R.drawable.chooseimage);
                                bitmap = null;
                                HelperFunctions.Message(AddInvestor.this,object.getString("message"));
                            }
                            else{
                                HelperFunctions.Message(AddInvestor.this,object.getString("message"));
                            }
                        }
                        catch (Exception e){
                            Toast.makeText(AddInvestor.this, "Error occurred in json parsing!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else{
                HelperFunctions.Message(AddInvestor.this,"Select image first...");
            }

        }
        else{
            HelperFunctions.Message(AddInvestor.this,"Fill all the details....");
        }
        alert.Hide();
        btnClick.setEnabled(true);
    }
}