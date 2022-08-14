package com.ss_technology.hanguoilproject.Config;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class KeepMeLogin {

    Context context;
    private SharedPreferences mPreferences;
    private String sharedPrefFileName = "urData";

    public KeepMeLogin(Context context) {
        this.context = context;
        mPreferences=context.getSharedPreferences(sharedPrefFileName,MODE_PRIVATE);
    }

    public void setData(String obj,String admin_type,String role)
    {
        //role admin or shop user
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString("obj",obj);
        //  1 for super admin and 2 for sub admin
        preferencesEditor.putString("admin_type",admin_type);
        preferencesEditor.putString("role",role);
        preferencesEditor.apply();
    }
   // role = 'admin' && 'shop'
    public String getData()
    {
     return mPreferences.getString("obj","null");
    }
    public String getAdminType()
    {
        return mPreferences.getString("admin_type","null");
    }
    public String getRole()
    {
        return mPreferences.getString("role","null");
    }

    public Boolean checkData()
    {
        if(mPreferences.contains("obj"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void Clear()
    {
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.clear().commit();
    }
}
