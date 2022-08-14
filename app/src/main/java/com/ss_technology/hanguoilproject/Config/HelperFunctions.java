package com.ss_technology.hanguoilproject.Config;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;


import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HelperFunctions {

    public static String currentDate()
    {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public static String BitmapToString(Bitmap bi,int quality)
    {
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        bi.compress(Bitmap.CompressFormat.JPEG,quality,outputStream);
        byte[] imageByte=outputStream.toByteArray();
        String encodeimage= Base64.encodeToString(imageByte, Base64.DEFAULT);
        return  encodeimage;

    }

    public static String getCurrentTime()
    {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-4:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("KK:mm:ss");
        date.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));
        String localTime = date.format(currentLocalTime);
        return localTime;
    }

    public static  boolean verify(String value){
        return (value.trim().length() > 0 && !value.equals(""));
    }

    public static int getAge(String dobString,String sep){
        int age_year=0,age_month=0,age_day=0;
        String[] dob_arr=dobString.split(sep);
        int dob_year=Integer.parseInt(dob_arr[0]);
        int dob_month=Integer.parseInt(dob_arr[1]);
        int dob_day=Integer.parseInt(dob_arr[2]);

        String[] current_arr=currentDate().split("/");
        int cur_year=Integer.parseInt(current_arr[0]);
        int cur_month=Integer.parseInt(current_arr[1]);
        int cur_day=Integer.parseInt(current_arr[2]);

        // minus dob from the curr date
        if(cur_month > dob_month && cur_day > dob_day){
          age_year=cur_year-dob_year;
          age_month=cur_month-dob_month;
          age_day=cur_day - dob_day;
        }
        else if(cur_day == dob_day && cur_month > dob_month){
          age_day=cur_day-dob_day;
          age_month=cur_month-dob_month;
          age_year=cur_year-dob_year;
        }
        else if(cur_day == dob_day && cur_month < dob_month){
            age_day=cur_day-dob_day;
            cur_month+=12;
            cur_year-=1;
            age_month=cur_month-dob_month;
            age_year=cur_year-dob_year;
        }
        else if(cur_month == dob_month && cur_day < dob_day){
            cur_day+=getMonthlength(cur_month);
            age_day=cur_day-dob_day;
            cur_month-=1;
            if(cur_month > dob_month){
                age_month=cur_month-dob_month;
            }
            else{
                cur_year-=1;
                cur_month+=12;
                age_month=cur_month-dob_month;
            }
            age_year=cur_year-dob_year;

        }
        else if(cur_month == dob_month && cur_day > dob_day){
            age_day=cur_day-dob_day;
            age_month=cur_month-dob_month;
            age_year=cur_year-dob_year;
        }
        else if(cur_month == dob_month && cur_day == dob_day){
            age_year=0;
            age_month=0;
            age_day=0;
        }
        else if(cur_day > dob_day && cur_month < dob_month){
            age_day=cur_day-dob_day;
            age_year=cur_year-dob_year;
            cur_year=cur_year-1;
            cur_month+=12;
            age_month=cur_month-dob_month;
        }
        else if(cur_day < dob_day && cur_month > dob_month){
            cur_day+=getMonthlength(cur_month);
            age_day=cur_day - dob_day;
            cur_month-=1;
            if(cur_month > dob_month){
               age_month=cur_month-dob_month;
            }
            else{
               cur_year-=1;
               cur_month+=12;
               age_month=cur_month-dob_month;
            }
            age_year=cur_year-dob_year;
        }
        else if(cur_month < dob_month && cur_day < dob_day){
           cur_day+=getMonthlength(cur_month);
           age_day=cur_day-dob_day;
           cur_month-=1;
           cur_month+=12;
           age_month=cur_month-dob_month;
           cur_year--;
           age_year=cur_year-dob_year;
        }

        float total= (float) (age_month * 30.417);
        total+=age_day;
        total=Math.round(total);
        int total_age= (int) total;
       return total_age;
    }

    public static void Network(Context context){
        if (!CheckInternetConnection.Connection(context)){
            Toast.makeText(context,"Check you internet connection",Toast.LENGTH_LONG).show();
        }
    }

    public static String AdminID(Context context){
        KeepMeLogin keepMeLogin = new KeepMeLogin(context);
        try{
            JSONObject object = new JSONObject(keepMeLogin.getData());
            return object.getString("id");
        }
        catch (Exception e){
            return "";
        }
    }

    public static void Message(Activity context,String msg){
        Snackbar.make(context.findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT)
               .show();
    }

    public static int getMonthlength(int index){
        ArrayList<Integer> lis=new ArrayList<>();
        lis.add(31);
        lis.add(28);
        lis.add(31);
        lis.add(30);
        lis.add(31);
        lis.add(30);
        lis.add(31);
        lis.add(31);
        lis.add(30);
        lis.add(31);
        lis.add(30);
        lis.add(31);
        return lis.get(index);
    }

}
