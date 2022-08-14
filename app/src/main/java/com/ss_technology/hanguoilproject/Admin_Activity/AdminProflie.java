package com.ss_technology.hanguoilproject.Admin_Activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ss_technology.hanguoilproject.R;
import com.ss_technology.hanguoilproject.databinding.ActivityAdminProflieBinding;

public class AdminProflie extends AppCompatActivity {

    private ActivityAdminProflieBinding binding;
    public String ID = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle(getIntent().getStringExtra("name"));
        super.onCreate(savedInstanceState);

        ActionBar ab = ((AppCompatActivity) this).getSupportActionBar();
        if(ab != null){
            ab.setDisplayHomeAsUpEnabled(true);
        }

        ID = getIntent().getStringExtra("ID");
        binding = ActivityAdminProflieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_admin_proflie);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}