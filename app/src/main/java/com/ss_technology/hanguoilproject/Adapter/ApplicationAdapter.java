package com.ss_technology.hanguoilproject.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ss_technology.hanguoilproject.Admin_Activity.ApplicationList;
import com.ss_technology.hanguoilproject.Admin_Activity.View_Application;
import com.ss_technology.hanguoilproject.Container.AdminListContainter;
import com.ss_technology.hanguoilproject.Container.Application_Container;
import com.ss_technology.hanguoilproject.R;

import java.util.ArrayList;
import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.View_Holder> implements Filterable {

    Activity context;
    List<Application_Container> list;
    List<Application_Container> exampleListFull;

    public ApplicationAdapter(Activity context, List<Application_Container> list) {
        this.context = context;
        this.list = list;
        exampleListFull = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new View_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.application_list_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {
       Application_Container data = list.get(position);
       holder.ID.setText(data.getId());
       holder.name.setText(data.getCus_Name());
       holder.mobile.setText(data.getCus_Mobile());
       holder.item_name.setText(data.getProductname());
       holder.company.setText(data.getCompany_Name());
       holder.model_no.setText(data.getModel_no());
       holder.type.setText(data.getItem_name());
       holder.total_price.setText(data.getTotal_price());
       holder.date.setText(data.getDate());

       holder.view_application.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(context, View_Application.class);
               intent.putExtra("id",data.getId());
               context.startActivity(intent);
           }
       });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class View_Holder extends RecyclerView.ViewHolder {

        TextView ID,name,mobile,item_name,model_no,type,company,total_price,date;
        ImageView cus_profile,view_application;

        public View_Holder(@NonNull View itemView) {
            super(itemView);

            ID = itemView.findViewById(R.id.ID);
            name = itemView.findViewById(R.id.name);
            mobile = itemView.findViewById(R.id.mobile);
            item_name = itemView.findViewById(R.id.item_name);
            model_no = itemView.findViewById(R.id.model_no);
            type = itemView.findViewById(R.id.type);
            company = itemView.findViewById(R.id.company);
            total_price = itemView.findViewById(R.id.total_price);
            date = itemView.findViewById(R.id.date);
            view_application = itemView.findViewById(R.id.view_application);

        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Application_Container> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Application_Container item : exampleListFull) {
                    if (item.getId().toLowerCase().contains(filterPattern)) {
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
