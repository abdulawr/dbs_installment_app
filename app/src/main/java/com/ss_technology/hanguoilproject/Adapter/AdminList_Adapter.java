package com.ss_technology.hanguoilproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ss_technology.hanguoilproject.Admin_Activity.AdminProflie;
import com.ss_technology.hanguoilproject.Config.ApiCall;
import com.ss_technology.hanguoilproject.Config.BaseURL;
import com.ss_technology.hanguoilproject.Config.HelperFunctions;
import com.ss_technology.hanguoilproject.Config.Messages;
import com.ss_technology.hanguoilproject.Config.VolleyCallback;
import com.ss_technology.hanguoilproject.Container.AdminListContainter;
import com.ss_technology.hanguoilproject.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminList_Adapter extends RecyclerView.Adapter<AdminList_Adapter.View_Holder> implements Filterable {

    Context context;
    List<AdminListContainter> list;
    List<AdminListContainter> exampleListFull;
    ApiCall apiCall;

    public AdminList_Adapter(Context context, List<AdminListContainter> list) {
        this.context = context;
        this.list = list;
        exampleListFull = new ArrayList<>(list);
        apiCall = new ApiCall(context);
    }

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new View_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_list_adapter_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {
       AdminListContainter data = list.get(position);
       holder.name.setText(data.getName());
       holder.mobile.setText(data.getMobile());
       holder.email.setText(data.getEmail());

       if(data.getType().trim().equals("1")){
           holder.type.setText("Super Admin");
       }
       else{
           holder.type.setText("Sub Admin");
       }

        if(data.getStatus().trim().equals("0")){
            holder.status.setText("Allowed");
        }
        else{
            holder.status.setText("Blocked");
        }

        if(!data.getBalance().trim().equals("null"))
            holder.balance.setText(data.getBalance());
        else
            holder.balance.setText("0");

        if(!data.getAccessBalance().trim().equals("null"))
        holder.access_balance.setText(data.getAccessBalance());
        else
            holder.access_balance.setText("0");
        Picasso.get().load(BaseURL.ImagePath("admin")+data.getImage()).into(holder.profile);

        if(data.getStatus().trim().equals("0")){
            // allowed
            holder.changeAccount.setText("Block it");
        }
        else{
            // blocked
            holder.changeAccount.setText("Allowed it");
        }

        holder.changeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> map = new HashMap<>();
                map.put("type","ChangeAdminAccountStatus");
                map.put("adminID", HelperFunctions.AdminID(context));
                if(holder.changeAccount.getText().toString().trim().equals("Block it")) {
                 map.put("status","1");
                }
                else{
                    map.put("status","0");
                }

                apiCall.Insert(map, "AdminApi.php", new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                     try{
                         JSONObject object = new JSONObject(result);
                         if(object.getString("status").trim().equals("1")){
                             if(object.getString("st").trim().equals("0")){
                                 // allowed
                                 holder.changeAccount.setText("Block it");
                             }
                             else{
                                 // blocked
                                 holder.changeAccount.setText("Allowed it");
                             }
                         }
                         else{
                             Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                         }
                     }
                     catch (Exception e){
                         Toast.makeText(context, Messages.JsonMsg, Toast.LENGTH_SHORT).show();
                     }
                    }
                });
            }
        });

        holder.profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, AdminProflie.class);
                intent.putExtra("ID",data.getId());
                intent.putExtra("name",data.getName());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class View_Holder extends RecyclerView.ViewHolder {

        ImageView profile;
        TextView name,mobile,email,type,status,balance,access_balance;
        Button changeAccount,profile_btn;


        public View_Holder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            changeAccount = itemView.findViewById(R.id.changeAccount);
            mobile = itemView.findViewById(R.id.mobile);
            profile_btn = itemView.findViewById(R.id.profile_btn);
            email = itemView.findViewById(R.id.email);
            type = itemView.findViewById(R.id.type);
            status = itemView.findViewById(R.id.status);
            balance = itemView.findViewById(R.id.balance);
            access_balance = itemView.findViewById(R.id.access_balance);
            profile = itemView.findViewById(R.id.profile);

        }
    }


    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<AdminListContainter> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (AdminListContainter item : exampleListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern) ||
                            item.getMobile().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}
