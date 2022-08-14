package com.ss_technology.hanguoilproject.Admin_Activity.ui.dashboard;

import android.os.Bundle;
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


public class DashboardFragment extends Fragment {

    ApiCall apiCall;
    ArrayList<PaymentContainer> list;
    private AdminProflie tabsmainActivity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(ab != null){
            ab.setDisplayHomeAsUpEnabled(true);
        }

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        tabsmainActivity = (AdminProflie) getActivity();

        list = new ArrayList<>();
        apiCall = new ApiCall(getActivity());
        RecyclerView rec = (RecyclerView) root.findViewById(R.id.rec);

        HashMap<String,String> map = new HashMap<>();
        map.put("type","getAccessoryPendingpayment");
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
                        con.setAppID(object.getString("accessID"));
                        con.setAdminIDS(object.getString("sellID"));
                        list.add(con);
                    }

                    if(!list.isEmpty()){
                        PendingPaymentAdapter adapter = new PendingPaymentAdapter(getActivity(),list,0);
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
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            getActivity().finish();
        }
        return true;
    }
}