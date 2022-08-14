package com.ss_technology.hanguoilproject.Admin_Activity.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ss_technology.hanguoilproject.Adapter.PendingPaymentAdapter;
import com.ss_technology.hanguoilproject.Admin_Activity.AdminProflie;
import com.ss_technology.hanguoilproject.Config.ApiCall;
import com.ss_technology.hanguoilproject.Config.HelperFunctions;
import com.ss_technology.hanguoilproject.Config.Messages;
import com.ss_technology.hanguoilproject.Config.VolleyCallback;
import com.ss_technology.hanguoilproject.Container.PaymentContainer;
import com.ss_technology.hanguoilproject.R;
import com.ss_technology.hanguoilproject.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class HomeFragment extends Fragment {

    public FragmentHomeBinding binding;
    ApiCall apiCall;
    ArrayList<PaymentContainer> list;
    private AdminProflie tabsmainActivity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(ab != null){
            ab.setDisplayHomeAsUpEnabled(true);
        }

        tabsmainActivity = (AdminProflie) getActivity();

        list = new ArrayList<>();
        apiCall = new ApiCall(getActivity());
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        RecyclerView rec = (RecyclerView) root.findViewById(R.id.rec);
        HashMap<String,String> map = new HashMap<>();
        map.put("type","getAdminPendingTransaction");
        map.put("adminID", tabsmainActivity.ID);
        apiCall.Insert(map, "AdminApi.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray array = new JSONArray(result);
                    for(int i=0; i<array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        PaymentContainer con = new PaymentContainer();
                        con.setId(object.getString("id"));
                        con.setAmount(object.getString("amount"));
                        con.setDate(object.getString("date"));
                        con.setType(object.getString("type"));
                        con.setTran_type(object.getString("status"));
                        con.setAppID(object.getString("appID"));
                        con.setExpence_type(object.getString("exp_type"));
                        con.setAdminIDS(object.getString("adminID"));
                        list.add(con);
                    }

                    if(!list.isEmpty()){
                        PendingPaymentAdapter adapter = new PendingPaymentAdapter(getActivity(),list,1);
                        rec.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        rec.setLayoutManager(layoutManager);
                        rec.setAdapter(adapter);
                    }

                } catch (JSONException e) {
                    HelperFunctions.Message(getActivity(), Messages.JsonMsg);
                }
            }
        });





        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            getActivity().finish();
        }
        return true;
    }
}