package com.ss_technology.hanguoilproject.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ss_technology.hanguoilproject.Admin_Activity.ShopkeeperProfile;
import com.ss_technology.hanguoilproject.Config.ApiCall;
import com.ss_technology.hanguoilproject.Config.BaseURL;
import com.ss_technology.hanguoilproject.Config.HelperFunctions;
import com.ss_technology.hanguoilproject.Config.Messages;
import com.ss_technology.hanguoilproject.Config.VolleyCallback;
import com.ss_technology.hanguoilproject.Container.ShopkeeperList_Container;
import com.ss_technology.hanguoilproject.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class Shopkeeper_Adapter extends RecyclerView.Adapter<Shopkeeper_Adapter.View_Holder>{

    Activity context;
    List<ShopkeeperList_Container> list;

    public Shopkeeper_Adapter(Activity context, List<ShopkeeperList_Container> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new View_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shopkeeper_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {
      ShopkeeperList_Container data = list.get(position);
        Picasso.get().load(BaseURL.ImagePath("shopkeeper")+data.getImage()).into(holder.imageView);
       holder.id.setText(data.getId());
       holder.name.setText(data.getName());
       holder.cnic.setText(data.getCnic());
       holder.salary.setText(data.getSalary());
       holder.mobile.setText(data.getMobile());

       if(data.getStatus().trim().equals("0")){
           holder.status.setText("Active");
           holder.status_btn.setText("Block it");
       }
       else{
           holder.status.setText("Blocked");
           holder.status_btn.setText("Allow it");
       }


       holder.status_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               HashMap<String,String> map =new HashMap<>();
               map.put("adminID", HelperFunctions.AdminID(context));
               map.put("ID",data.getId());
               if(holder.status_btn.getText().toString().trim().equals("Block it")) {
                   map.put("status","1");
               }
               else{
                   map.put("status","0");
               }
               map.put("type","changeShopkeeperAccountStatus");
               ApiCall apiCall= new ApiCall(context);
               apiCall.Insert(map, "AdminApi.php", new VolleyCallback() {
                   @Override
                   public void onSuccess(String result) {

                       try{
                           JSONObject object = new JSONObject(result);
                           if(object.getString("status").trim().equals("1")){

                               if(object.getString("st").trim().equals("0")){
                                   holder.status_btn.setText("Block it");
                               }
                               else{
                                   holder.status_btn.setText("Allow it");
                               }
                           }
                           Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                       }
                       catch (Exception e){
                           HelperFunctions.Message(context, Messages.JsonMsg);
                       }
                   }
               });

           }
       });

       holder.profile_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(context, ShopkeeperProfile.class);
               intent.putExtra("ID",data.getId());
               intent.putExtra("name",data.getName());
               context.startActivity(intent);

           }
       });

       holder.balance.setText(data.getBalance());

       if(Integer.parseInt(data.getBalance()) > 0){
           holder.balance.setBackgroundColor(Color.parseColor("#FFEB3B"));
       }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class View_Holder extends RecyclerView.ViewHolder {

        ImageView imageView;
        Button status_btn,profile_btn;
        TextView id,name,mobile,cnic,salary,status,balance;

        public View_Holder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            status_btn = itemView.findViewById(R.id.status_btn);
            profile_btn = itemView.findViewById(R.id.profile_btn);
            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.name);
            mobile = itemView.findViewById(R.id.mobile);
            cnic = itemView.findViewById(R.id.cnic);
            salary = itemView.findViewById(R.id.salary);
            status = itemView.findViewById(R.id.status);
            balance = itemView.findViewById(R.id.balance);



        }
    }
}
