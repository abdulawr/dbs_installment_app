package com.ss_technology.hanguoilproject.Admin_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.ss_technology.hanguoilproject.Activity.MainActivity;
import com.ss_technology.hanguoilproject.Config.ApiCall;
import com.ss_technology.hanguoilproject.Config.BaseURL;
import com.ss_technology.hanguoilproject.Config.HelperFunctions;
import com.ss_technology.hanguoilproject.Config.KeepMeLogin;
import com.ss_technology.hanguoilproject.Config.VolleyCallback;
import com.ss_technology.hanguoilproject.R;
import com.ss_technology.hanguoilproject.databinding.ActivityAdminHomeBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;

public class Admin_Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityAdminHomeBinding binding;
    DrawerLayout drawer;
    NavigationView navigationView;
    KeepMeLogin keepMeLogin;
    ApiCall apiCall;
    TextView name,email;
    ImageView profile;
    TextView cname,mobile,cemail,facebook,whatsapp,address;
    TextView dbs_shop_balance,dbs_stock;
    TextView cmp_balance,cmp_pending_balance,cmp_investor_pending,cmp_customer_pending,cmp_available;

    TextView balancetxt;
    CardView sub_balance_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarAdminHome.toolbar);
        apiCall = new ApiCall(this);
        keepMeLogin=new KeepMeLogin(this);
        cname = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        cemail  = findViewById(R.id.email);
        facebook = findViewById(R.id.facebook);
        whatsapp = findViewById(R.id.whatsapp);
        address = findViewById(R.id.address);

        balancetxt = findViewById(R.id.balancetxt);
        sub_balance_card = findViewById(R.id.sub_balance_card);
        if(keepMeLogin.getAdminType().equals("2")){
            sub_balance_card.setVisibility(View.VISIBLE);
        }



        dbs_shop_balance = findViewById(R.id.dbs_shop_balance);
        dbs_stock = findViewById(R.id.dbs_stock);

        cmp_balance = findViewById(R.id.cmp_balance);
        cmp_pending_balance = findViewById(R.id.cmp_pending_balance);
        cmp_investor_pending = findViewById(R.id.cmp_investor_pending);
        cmp_customer_pending = findViewById(R.id.cmp_customer_pending);
        cmp_available = findViewById(R.id.cmp_available);

        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, binding.appBarAdminHome.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();
        drawer.addDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);
        name = headerView.findViewById(R.id.name);
        email = headerView.findViewById(R.id.email);
        profile = headerView.findViewById(R.id.profile_image);

        HelperFunctions.Network(this);

        try {
            JSONObject object = new JSONObject(keepMeLogin.getData());
            name.setText(object.getString("name"));
            email.setText(object.getString("email"));
            Picasso.get().load(BaseURL.ImagePath("admin")+object.getString("image")).into(profile);
        }
        catch (Exception e){

            Toast.makeText(this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
        }


        HashMap<String,String> map = new HashMap<>();
        map.put("type","getHomeScreenData");
        map.put("adminID",HelperFunctions.AdminID(this));
        apiCall.Insert(map, "AdminApi.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    JSONObject companyinfo = object.getJSONObject("companyinfo");
                    cname.setText(companyinfo.getString("name"));
                    cemail.setText(companyinfo.getString("email"));
                    mobile.setText(companyinfo.getString("mobile"));
                    facebook.setText(companyinfo.getString("facebook"));
                    whatsapp.setText(companyinfo.getString("whatsapp"));
                    address.setText(companyinfo.getString("address"));

                    dbs_shop_balance.setText(object.getString("dbs_shop_balance"));
                    dbs_stock.setText(object.getString("dbs_shop_stock"));

                    cmp_balance.setText(object.getString("dbs_cmp_balance"));
                    cmp_pending_balance.setText(object.getString("dbs_cmp_pending"));
                    cmp_investor_pending.setText(object.getString("investor_pending"));
                    cmp_customer_pending.setText(object.getString("customer_pending"));
                    cmp_available.setText(object.getString("avail_balance"));

                    balancetxt.setText(object.getString("adminBalance"));

                }
                catch (Exception e){
                    Toast.makeText(Admin_Home.this, "Error occured in json parsing", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // ********************** Only for super admin *************************
         if (keepMeLogin.getAdminType().trim().equals("2")){
              navigationView.getMenu().findItem(R.id.companyInfo).setEnabled(false);
             navigationView.getMenu().findItem(R.id.adminList).setEnabled(false);
             navigationView.getMenu().findItem(R.id.shopkeeperlist).setEnabled(false);
         }

        // ********************** Only for super admin *************************



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin__home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.signout){
          keepMeLogin.Clear();
          startActivity(new Intent(getApplicationContext(), MainActivity.class));
          finish();
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.profile){
          startActivity(new Intent(getApplicationContext(),Profile.class));
        }
        else if(id == R.id.companyInfo){
            startActivity(new Intent(getApplicationContext(),CompanyInfo.class));
        }
        else if(id == R.id.addCustomer){
            startActivity(new Intent(getApplicationContext(),AddCustomer.class));
        }
        else if(id == R.id.addInvestor){
            startActivity(new Intent(getApplicationContext(),AddInvestor.class));
        }
        else if(id == R.id.adminList){
            startActivity(new Intent(getApplicationContext(),AdminLIst.class));
        }
        else if(id == R.id.shopkeeperlist){
            startActivity(new Intent(getApplicationContext(),ShopKeeperList.class));
        }
        else if(id == R.id.pendingApp){
            Intent intent = new Intent(Admin_Home.this,ApplicationList.class);
            intent.putExtra("type","0");
            startActivity(intent);
        }
        else if(id == R.id.acceptedApp){
            Intent intent = new Intent(Admin_Home.this,ApplicationList.class);
            intent.putExtra("type","1");
            startActivity(intent);
        }
        else if(id == R.id.activeApp){
            Intent intent = new Intent(Admin_Home.this,ApplicationList.class);
            intent.putExtra("type","3");
            startActivity(intent);
        }
        else if(id == R.id.completeApp){
            Intent intent = new Intent(Admin_Home.this,ApplicationList.class);
            intent.putExtra("type","4");
            startActivity(intent);
        }
        else if(id == R.id.rejectApp){
            Intent intent = new Intent(Admin_Home.this,ApplicationList.class);
            intent.putExtra("type","5");
            startActivity(intent);
        }
        else if(id == R.id.customer){
            Intent intent = new Intent(Admin_Home.this,Customer_and_Investor_List.class);
            intent.putExtra("type","1");
            startActivity(intent);
        }
        else if(id == R.id.investor){
            Intent intent = new Intent(Admin_Home.this,Customer_and_Investor_List.class);
            intent.putExtra("type","2");
            startActivity(intent);
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}