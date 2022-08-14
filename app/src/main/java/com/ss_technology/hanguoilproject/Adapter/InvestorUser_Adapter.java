package com.ss_technology.hanguoilproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ss_technology.hanguoilproject.Admin_Activity.CustomerProfile;
import com.ss_technology.hanguoilproject.Admin_Activity.View_Application;
import com.ss_technology.hanguoilproject.Container.Investor_User_Container;
import com.ss_technology.hanguoilproject.R;

import java.util.List;

public class InvestorUser_Adapter extends RecyclerView.Adapter<InvestorUser_Adapter.View_Holder>{

    Context context;
    List<Investor_User_Container> list;

    public InvestorUser_Adapter(Context context, List<Investor_User_Container> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InvestorUser_Adapter.View_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.investor_user_adapter_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {

      Investor_User_Container data = list.get(position);
      holder.ID.setText(data.getId());
      holder.name.setText(data.getName());
      holder.mobile.setText(data.getMobile());
      holder.cnic.setText(data.getCnic());
      holder.appID.setText(data.getAppID());


        holder.view_application.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, View_Application.class);
                intent.putExtra("id",data.getAppID());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class View_Holder extends RecyclerView.ViewHolder {

        ImageView view_application;
        TextView ID,name,mobile,cnic,appID;


        public View_Holder(@NonNull View itemView) {
            super(itemView);

            view_application = itemView.findViewById(R.id.view_application);
            ID = itemView.findViewById(R.id.ID);
            name = itemView.findViewById(R.id.name);
            mobile = itemView.findViewById(R.id.mobile);
            cnic = itemView.findViewById(R.id.cnic);
            appID = itemView.findViewById(R.id.appID);
        }
    }
}
