package com.ss_technology.hanguoilproject.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ss_technology.hanguoilproject.Config.ApiCall;
import com.ss_technology.hanguoilproject.Config.HelperFunctions;
import com.ss_technology.hanguoilproject.Config.Messages;
import com.ss_technology.hanguoilproject.Config.VolleyCallback;
import com.ss_technology.hanguoilproject.Container.PaymentContainer;
import com.ss_technology.hanguoilproject.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class PendingPaymentAdapter extends RecyclerView.Adapter<PendingPaymentAdapter.View_Holder> {

    Activity context;
    List<PaymentContainer> list;
    int type;
    String types = "";

    public PendingPaymentAdapter(Activity context, List<PaymentContainer> list,int type) {
        this.context = context;
        this.list = list;
        this.type = type;

        // type = 1 (for normal transaction) && type = 0 (for accessories transaction)
    }

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new View_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_payment_adapter_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {
     PaymentContainer data = list.get(position);
     int pos = position;

     if(type == 1) {
         // ***************** Main pending amount list for admin *******************
         holder.exp_row.setVisibility(View.VISIBLE);
         holder.trans_row.setVisibility(View.VISIBLE);
         holder.type_row.setVisibility(View.VISIBLE);
         holder.amount.setText(data.getAmount());
         holder.date.setText(data.getDate());

         if (data.getType().trim().equals("investor")) {
             holder.type.setText("Investor");
         } else if (data.getType().trim().equals("customer")) {
             holder.type.setText("Customer");
         } else if (data.getType().trim().equals("dbs_shop")) {
             holder.type.setText("DBS Shop");
         } else {
             holder.type.setText("Expence");
         }


         if (data.getTran_type().trim().equals("0")) {
             holder.transaction_type.setText("Added investor");
         } else if (data.getTran_type().trim().equals("1")) {
             holder.transaction_type.setText("Sub investor");
         } else if (data.getTran_type().trim().equals("2")) {
             holder.transaction_type.setText("Application installment");
         } else if (data.getTran_type().trim().equals("4")) {
             holder.transaction_type.setText("DBS shop purchase");
         } else {
             holder.transaction_type.setText("Company cost");
         }

         if (!data.getAppID().trim().equals("null") && !TextUtils.isEmpty(data.getAppID().trim())) {
             holder.appID.setText(data.getAppID());
         }

         if (data.getExpence_type().trim().equals("0")) {
             holder.expence_type.setText("Company Exp");
         } else {
             holder.expence_type.setText("DBS Shop Exp");
         }

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             holder.received.setTooltipText("Received Payment");
         }

         holder.received.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 HashMap<String, String> map = new HashMap<>();
                 map.put("type", "changePendingTransactionType");
                 map.put("adminID", HelperFunctions.AdminID(context));
                 map.put("ID", data.getId());
                 map.put("ADMID", data.getAdminIDS());
                 ApiCall apiCall = new ApiCall(context);
                 apiCall.Insert(map, "AdminApi.php", new VolleyCallback() {
                     @Override
                     public void onSuccess(String result) {

                         try {
                             JSONObject ob = new JSONObject(result);
                             if (ob.getString("status").trim().equals("1")) {
                                 list.remove(pos);
                                 notifyDataSetChanged();
                             }
                             Toast.makeText(context, ob.getString("message"), Toast.LENGTH_SHORT).show();
                         } catch (Exception e) {
                             HelperFunctions.Message(context, Messages.JsonMsg);
                         }
                     }
                 });
             }
         });
     }
     else
     {
         // ################# Accessory Payment ######################
         holder.appID_title.setText("Access ID");
         holder.amount.setText(data.getAmount());
         holder.date.setText(data.getDate());
         holder.appID.setText(data.getAppID());

         if(type == 2){
             types = "Change_shopkeeper_PendingBalance";
         }
         else {
           types = "changePending_Accessory_TransactionType";
         }
         holder.received.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 HashMap<String, String> map = new HashMap<>();
                 map.put("type", types);
                 map.put("adminID", HelperFunctions.AdminID(context));
                 map.put("ID", data.getId());
                 map.put("ADMID", data.getAdminIDS());
                 ApiCall apiCall = new ApiCall(context);
                 apiCall.Insert(map, "AdminApi.php", new VolleyCallback() {
                     @Override
                     public void onSuccess(String result) {

                         try {
                             JSONObject ob = new JSONObject(result);
                             if (ob.getString("status").trim().equals("1")) {
                                 list.remove(pos);
                                 notifyDataSetChanged();
                             }
                             Toast.makeText(context, ob.getString("message"), Toast.LENGTH_SHORT).show();
                         } catch (Exception e) {
                             HelperFunctions.Message(context, Messages.JsonMsg);
                         }
                     }
                 });
             }
         });


     }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class View_Holder extends RecyclerView.ViewHolder {

        ImageView received;
        TextView amount,date,type,transaction_type,appID,expence_type,appID_title;
        TableRow exp_row,trans_row,type_row;

        public View_Holder(@NonNull View itemView) {
            super(itemView);

            received = itemView.findViewById(R.id.received);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
            type = itemView.findViewById(R.id.type);
            transaction_type = itemView.findViewById(R.id.transaction_type);
            appID = itemView.findViewById(R.id.appID);
            expence_type = itemView.findViewById(R.id.expence_type);
            appID_title = itemView.findViewById(R.id.appID_title);

            exp_row = itemView.findViewById(R.id.exp_row);
            trans_row = itemView.findViewById(R.id.trans_row);
            type_row = itemView.findViewById(R.id.type_row);

        }
    }
}
