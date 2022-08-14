package com.ss_technology.hanguoilproject.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ss_technology.hanguoilproject.Admin_Activity.CustomerProfile;
import com.ss_technology.hanguoilproject.Admin_Activity.InvestorProfile;
import com.ss_technology.hanguoilproject.Config.BaseURL;
import com.ss_technology.hanguoilproject.Container.AdminListContainter;
import com.ss_technology.hanguoilproject.Container.Customer_Container;
import com.ss_technology.hanguoilproject.R;

import java.util.ArrayList;
import java.util.List;

public class Customer_Adapter extends RecyclerView.Adapter<Customer_Adapter.View_Holder> implements Filterable  {

    Context context;
    List<Customer_Container> list;
    List<Customer_Container> exampleListFull;

    public Customer_Adapter(Context context, List<Customer_Container> list) {
        this.context = context;
        this.list = list;
        exampleListFull = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new View_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_adapter_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {
      Customer_Container data = list.get(position);

      if(data.getType().trim().equals("1")){
          Picasso.get().load(BaseURL.ImagePath("customer")+data.getImage()).into(holder.profile);
      }
      else{
          Picasso.get().load(BaseURL.ImagePath("investor")+data.getImage()).into(holder.profile);
      }

      holder.name.setText(data.getName());
      holder.id.setText(data.getId());
      holder.mobile.setText(data.getMobile()+"  (mobile)");
      holder.cnic.setText(data.getCnic()+"  (cnic)");
      holder.address.setText(data.getAddress());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView profile;
        TextView name,id,mobile,cnic,address;

        public View_Holder(@NonNull View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.profile);
            name = itemView.findViewById(R.id.name);
            id = itemView.findViewById(R.id.id);
            mobile = itemView.findViewById(R.id.mobile);
            cnic = itemView.findViewById(R.id.cnic);
            address = itemView.findViewById(R.id.address);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if(list.get(getLayoutPosition()).getType().trim().equals("1")){
                // customer
                Intent intent = new Intent(context, CustomerProfile.class);
                intent.putExtra("ob",list.get(getLayoutPosition()));
                context.startActivity(intent);
            }
            else{
              // Investor
                Intent intent = new Intent(context, InvestorProfile.class);
                intent.putExtra("ob",list.get(getLayoutPosition()));
                context.startActivity(intent);
            }
        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Customer_Container> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Customer_Container item : exampleListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern) ||
                            item.getMobile().contains(filterPattern) ||
                            item.getId().contains(filterPattern)) {
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
