package com.ss_technology.hanguoilproject.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ss_technology.hanguoilproject.Container.PendingBalance_Container;
import com.ss_technology.hanguoilproject.R;

import java.util.List;

public class Pending_Balance_Adapter extends RecyclerView.Adapter<Pending_Balance_Adapter.View_Holder>{

    Context context;
    List<PendingBalance_Container> list;
    OnClist_Listner onClist_listner;

    public Pending_Balance_Adapter(Context context, List<PendingBalance_Container> list, OnClist_Listner onClist_listner) {
        this.context = context;
        this.list = list;
        this.onClist_listner = onClist_listner;
    }

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Pending_Balance_Adapter.View_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_balance_adapter_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {

       PendingBalance_Container data = list.get(position);

       holder.invest_amount.setText(data.getInvest_amount());
       holder.date.setText(data.getDate());
       holder.profit.setText(data.getProfit());
       holder.cusID.setText(data.getCusID());
       holder.appID.setText(data.getAppID());
       holder.paid.setText(data.getPaid());
       holder.payable.setText(data.getPayable());
       holder.total.setText(data.getTotal_amount());

       holder.addBalance.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               onClist_listner.values(data.getId(),data.getInvesID(),data.getPayable());
           }
       });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class View_Holder extends RecyclerView.ViewHolder {

        TextView invest_amount,date,profit,cusID,appID,paid,payable,total;
        ImageView addBalance;

        public View_Holder(@NonNull View itemView) {
            super(itemView);

            invest_amount = itemView.findViewById(R.id.invest_amount);
            date = itemView.findViewById(R.id.date);
            profit = itemView.findViewById(R.id.profit);
            cusID = itemView.findViewById(R.id.cusID);
            appID = itemView.findViewById(R.id.appID);
            paid = itemView.findViewById(R.id.paid);
            payable  = itemView.findViewById(R.id.payable);
            total  = itemView.findViewById(R.id.total);
            addBalance  = itemView.findViewById(R.id.addBalance);

        }
    }

    public interface OnClist_Listner{
        public void values(String id,String invesID,String pay);
    }
}
