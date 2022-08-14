package com.ss_technology.hanguoilproject.Admin_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.ss_technology.hanguoilproject.Config.ApiCall;
import com.ss_technology.hanguoilproject.Config.BaseURL;
import com.ss_technology.hanguoilproject.Config.HelperFunctions;
import com.ss_technology.hanguoilproject.Config.KeepMeLogin;
import com.ss_technology.hanguoilproject.Config.Messages;
import com.ss_technology.hanguoilproject.Config.VolleyCallback;
import com.ss_technology.hanguoilproject.Container.Application_Container;
import com.ss_technology.hanguoilproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class View_Application extends AppCompatActivity {

    TextView discount_amount,status,reject_reasion;
    String message,color;
    ImageView cus_Image,inves_Img;
    ApiCall apiCall;

    // Customer TextView
    TextView name,mobile,id,f_name,address;

    // Investor TextView
    TextView name_in,mobile_in,id_in,cnic_in,address_in;
    CardView invesCard;
    LinearLayout install_layout,discount_layout,install_layout_view,pending_button_layout;
    // Application TextView
    TextView appID,age,monthly_income,business_type,business_address,date,model_no,cmp_name,item_name,item_type,org_price,percentage,total_price,installment_mon,monthly_payment,advance_payment,ref_by,description;

    TextView paid_amount,number_of_trans,remaining,remaingin_adavance_payment;
    TextInputEditText discount_amounts;
    Button submitDiscount,deliver_application_btn;

    TableLayout active_button_layout;
    int remaining_amount;

    // Active applicaion button
    Button add_installement_btn,complete_app_btn,full_paid_advance;
    Application_Container data;
    KeepMeLogin keepMeLogin;

    // Pending Application
    Button pending_btn_delete,pending_btn_accept,pending_btn_reject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_application);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        data = new Application_Container();
        apiCall = new ApiCall(this);
        keepMeLogin = new KeepMeLogin(this);

        HashMap<String,String> mm = new HashMap<>();
        mm.put("adminID",HelperFunctions.AdminID(this));
        mm.put("type","getApplicationList");
        mm.put("appID",getIntent().getStringExtra("id"));

        apiCall.Insert(mm, Messages.FileName, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray array = new JSONArray(result);
                    JSONObject object = array.getJSONObject(0);
                    data.setCusID(object.getString("cusID"));
                    data.setCus_Name(object.getString("name"));
                    data.setCus_Mobile(object.getString("mobile"));
                    data.setCompany_Name(object.getString("comp"));
                    data.setItem_name(object.getString("item"));
                    data.setId(object.getString("id"));
                    data.setAge(object.getString("age"));
                    data.setMonthly_income(object.getString("monthly_income"));
                    data.setBusiness_type(object.getString("business_type"));
                    data.setBusiness_address(object.getString("business_address"));
                    data.setDate(object.getString("date"));
                    data.setModel_no(object.getString("model_no"));
                    data.setCompanyID(object.getString("companyID"));
                    data.setProductname(object.getString("product_name"));
                    data.setItem_typeID(object.getString("item_type_id"));
                    data.setProduct_orginal_price(object.getString("product_orginal_price"));
                    data.setPercentage_on_prod(object.getString("percentage_on_prod"));
                    data.setTotal_price(object.getString("total_price"));
                    data.setInstallment_months(object.getString("installment_months"));
                    data.setMonthly_payment(object.getString("monthly_payment"));
                    data.setAdvance_payment(object.getString("advance_payment"));
                    data.setStatus(object.getString("status"));
                    data.setRef_by(object.getString("ref_by"));
                    data.setDelivery_image(object.getString("delivery_image"));
                    data.setInvestorID(object.getString("investorID"));
                    data.setDiscount_amount(object.getString("discount_amount"));
                    data.setItem_des(object.getString("item_des"));
                    data.setRej_des(object.getString("rej_des"));
                    data.setAdvance_payment_status(object.getString("advance_payment_status"));
                    data.setAdvance_payment_paid(object.getString("advance_payment_paid"));
                    data.setActive_date(object.getString("active_date"));

                    init();
                    ApiCalls();

                }
                catch (Exception e){
                    Toast.makeText(View_Application.this, Messages.JsonMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    void setRemAmount(int value){
        remaining_amount = value;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    // -------------------------------------------------------

    public void init(){

        submitDiscount = findViewById(R.id.submitDiscount);
        discount_amounts = findViewById(R.id.discount_amounts);
        install_layout = findViewById(R.id.install_layout);
        invesCard = findViewById(R.id.invesCard);
        inves_Img = findViewById(R.id.inves_Img);
        name_in = findViewById(R.id.name_in);
        id_in = findViewById(R.id.id_in);
        mobile_in = findViewById(R.id.mobile_in);
        cnic_in = findViewById(R.id.cnic_in);
        address_in = findViewById(R.id.address_in);

        pending_button_layout = findViewById(R.id.pending_button_layout);
        full_paid_advance = findViewById(R.id.full_paid_advance);
        install_layout_view = findViewById(R.id.install_layout_view);
        remaingin_adavance_payment = findViewById(R.id.remaingin_adavance_payment);
        deliver_application_btn = findViewById(R.id.deliver_application_btn);

        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        id = findViewById(R.id.id);
        f_name = findViewById(R.id.f_name);
        address = findViewById(R.id.address);
        cus_Image = findViewById(R.id.cus_Image);

        appID = findViewById(R.id.appID);
        age = findViewById(R.id.age);
        monthly_income = findViewById(R.id.monthly_income);
        business_type = findViewById(R.id.business_type);
        business_address = findViewById(R.id.business_address);
        date = findViewById(R.id.date);
        model_no = findViewById(R.id.model_no);
        cmp_name = findViewById(R.id.cmp_name);
        item_name = findViewById(R.id.item_name);
        item_type = findViewById(R.id.item_type);
        org_price = findViewById(R.id.org_price);
        percentage = findViewById(R.id.percentage);
        total_price = findViewById(R.id.total_price);
        installment_mon = findViewById(R.id.installment_mon);
        monthly_payment = findViewById(R.id.monthly_payment);
        advance_payment = findViewById(R.id.advance_payment);
        ref_by = findViewById(R.id.ref_by);
        description = findViewById(R.id.description);
        discount_layout = findViewById(R.id.discount_layout);
        active_button_layout = findViewById(R.id.active_button_layout);

        appID.setText(data.getId());
        age.setText(data.getAge());
        monthly_income.setText(data.getMonthly_income());
        business_type.setText(data.getBusiness_type());
        business_address.setText(data.getBusiness_address());
        date.setText(data.getDate());
        model_no.setText(data.getModel_no());
        cmp_name.setText(data.getCompany_Name());
        item_name.setText(data.getProductname());
        item_type.setText(data.getItem_name());
        org_price.setText(data.getProduct_orginal_price());
        percentage.setText(data.getPercentage_on_prod()+"%");
        total_price.setText(data.getTotal_price());
        installment_mon.setText(data.getInstallment_months());
        monthly_payment.setText(data.getMonthly_payment());
        advance_payment.setText(data.getAdvance_payment());
        ref_by.setText(data.getRef_by());
        description.setText(data.getItem_des());

        paid_amount = findViewById(R.id.paid_amount);
        number_of_trans = findViewById(R.id.number_of_trans);
        remaining = findViewById(R.id.remaining);

        add_installement_btn = findViewById(R.id.add_installement_btn);
        complete_app_btn = findViewById(R.id.complete_app_btn);

        if(data.getStatus().trim().equals("3")){
            discount_layout.setVisibility(View.VISIBLE);
            active_button_layout.setVisibility(View.VISIBLE);
        }

        status = findViewById(R.id.status);
        discount_amount = findViewById(R.id.discount_amount);
        discount_amount.setText("Discount Amount: ( "+data.getDiscount_amount()+" )");
        reject_reasion = findViewById(R.id.reject_reasion);


        if(data.getStatus().trim().equals("0")){
            message = "Application Status ( Pending )";
            color = "#03A9F4";
            pending_button_layout.setVisibility(View.VISIBLE);
            install_layout_view.setVisibility(View.GONE);


        }
        else if(data.getStatus().trim().equals("1")){
            message = "Application Status ( Accepted )";
            color = "#3F51B5";
            install_layout_view.setVisibility(View.GONE);
            if(data.getAdvance_payment_status().equals("0")){
                remaingin_adavance_payment.setVisibility(View.VISIBLE);
                remaingin_adavance_payment.setText("Advance amount is not paid completly, (Paid Amount = "+data.getAdvance_payment()+") and (Remaining Amount = "+data.getAdvance_payment_paid()+")");
                if(keepMeLogin.getAdminType().trim().equals("1")){
                    full_paid_advance.setVisibility(View.VISIBLE);
                }
                deliver_application_btn.setVisibility(View.GONE);
            }
            else{
                deliver_application_btn.setVisibility(View.VISIBLE);
                full_paid_advance.setVisibility(View.GONE);
            }

        }
        else if(data.getStatus().trim().equals("3")){
            message = "Application Status ( Active )";
            color = "#4CAF50";
        }
        else if(data.getStatus().trim().equals("4")){
            message = "Application Status ( Completed )";
            color = "#FF9800";
        }
        else if(data.getStatus().trim().equals("5")){
            message = "Application Status ( Rejected )";
            color = "#F44336";
            reject_reasion.setVisibility(View.VISIBLE);
            reject_reasion.setText("Rejection Reasion=> "+data.getRej_des());
            install_layout_view.setVisibility(View.GONE);
        }

        status.setText(message);
        status.setBackgroundColor(Color.parseColor(color));

        pending_btn_delete = findViewById(R.id.pending_btn_delete);
        pending_btn_accept = findViewById(R.id.pending_btn_accept);
        pending_btn_reject = findViewById(R.id.pending_btn_reject);

        if(keepMeLogin.getAdminType().equals("2")){
            pending_btn_reject.setVisibility(View.GONE);
            pending_btn_accept.setVisibility(View.GONE);
        }

    }

    // -------------------------------------------------------

    void ApiCalls(){
        // ************************* Add Installment Start ************************

        add_installement_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(View_Application.this,R.style.DialogStyle);
                bottomSheetDialog.setContentView(R.layout.add_transaction_view);
                bottomSheetDialog.setCancelable(false);

                TextInputEditText amounts = bottomSheetDialog.findViewById(R.id.amounts);
                amounts.setText(data.getMonthly_payment());

                Button closebtn = bottomSheetDialog.findViewById(R.id.closebtn);
                closebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });

                Button submitTransaction = bottomSheetDialog.findViewById(R.id.submitTransaction);
                submitTransaction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String amount = amounts.getText().toString();
                        if (HelperFunctions.verify(amount)) {
                            HashMap<String, String> m = new HashMap<>();
                            m.put("type", "addApplicationInstallement");
                            m.put("adminID", HelperFunctions.AdminID(View_Application.this));
                            m.put("appID", data.getId());
                            m.put("amount", amount);
                            m.put("adminType",keepMeLogin.getAdminType());

                            apiCall.Insert(m, Messages.FileName, new VolleyCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    try {
                                        JSONObject object = new JSONObject(result);
                                        if (object.getString("status").trim().equals("1")) {
                                            HelperFunctions.Message(View_Application.this, object.getString("message"));
                                            View_Application.super.recreate();
                                        }
                                        else{
                                            HelperFunctions.Message(View_Application.this, object.getString("message"));
                                        }
                                        bottomSheetDialog.dismiss();

                                    } catch (Exception e) {
                                        HelperFunctions.Message(View_Application.this, Messages.JsonMsg);
                                    }
                                }
                            });

                        }
                        else {
                            Toast.makeText(View_Application.this, "Amount should not be empty", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                bottomSheetDialog.show();
            }
        });
        // ************************* Add Installment End ************************



        // ############################# Submit discount amount start #####################
        submitDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = discount_amounts.getText().toString();
                if(HelperFunctions.verify(amount)){
                    HashMap<String,String> disMap = new HashMap<>();
                    disMap.put("amount",amount);
                    disMap.put("AppID",data.getId());
                    disMap.put("type","submitApplicationDiscount");
                    disMap.put("adminID",HelperFunctions.AdminID(View_Application.this));

                    apiCall.Insert(disMap, Messages.FileName, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject object = new JSONObject(result);
                                if(object.getString("status").trim().equals("1")){
                                    Toast.makeText(View_Application.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                    discount_amounts.getText().clear();
                                    View_Application.super.recreate();
                                }
                                else{
                                    Toast.makeText(View_Application.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (Exception e){
                                HelperFunctions.Message(View_Application.this,Messages.JsonMsg);
                            }
                        }
                    });

                }
                else{
                    HelperFunctions.Message(View_Application.this,"Enter amount first");
                }
            }
        });

        // ############################# Submit discount amount end #####################


        // @@@@@@@@@@@@@@@@@@@ Get application details start @@@@@@@@@@@@@@@@@@

        HashMap<String,String> maps = new HashMap<>();
        maps.put("adminID", HelperFunctions.AdminID(this));
        maps.put("type","getApplicationDetails_View");
        maps.put("appID",data.getId());
        apiCall.Insert2(maps, "AdminApi.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    JSONObject customer = object.getJSONObject("customer");

                    name.setText(customer.getString("name"));
                    id.setText(customer.getString("id"));
                    mobile.setText(customer.getString("mobile"));
                    address.setText(customer.getString("address"));
                    f_name.setText(customer.getString("fname"));
                    Picasso.get().load(BaseURL.ImagePath("customer")+customer.getString("image")).into(cus_Image);

                    if(!object.getString("investor").trim().equals("null")){
                        invesCard.setVisibility(View.VISIBLE);
                        JSONObject investor = object.getJSONObject("investor");
                        name_in.setText(investor.getString("name"));
                        id_in.setText(investor.getString("id"));
                        mobile_in.setText(investor.getString("mobile"));
                        address_in.setText(investor.getString("address"));
                        cnic_in.setText(investor.getString("cnic"));
                        Picasso.get().load(BaseURL.ImagePath("investor")+investor.getString("image")).into(inves_Img);
                    }

                    paid_amount.setText(object.getString("total_paid_amount"));
                    number_of_trans.setText(object.getString("num_of_installment"));
                    remaining.setText(object.getString("remaining_amount"));

                    remaining_amount = Integer.parseInt(object.getString("remaining_amount"));
                    setRemAmount(Integer.parseInt(object.getString("remaining_amount")));


                    if(remaining_amount > 0){
                        add_installement_btn.setVisibility(View.VISIBLE);
                        complete_app_btn.setVisibility(View.GONE);
                    }
                    else{
                        add_installement_btn.setVisibility(View.GONE);
                        if (keepMeLogin.getAdminType().equals("1")){
                            complete_app_btn.setVisibility(View.VISIBLE);
                        }

                    }

                    if(!object.getString("transaction").trim().equals("null")){
                        JSONArray transaction = object.getJSONArray("transaction");
                        for (int i=0; i<transaction.length(); i++){
                            JSONObject tran = transaction.getJSONObject(i);
                            TextView textView = new TextView(View_Application.this);
                            String type = "";
                            if(tran.getString("type").trim().equals("A")){
                                type = "A payment";
                            }
                            else{
                                type = "M payment";
                            }
                            textView.setText(tran.getString("id")+"  --  "+tran.getString("date")+"  --  "+tran.getString("amount")+" PKR  --  "+type);
                            install_layout.addView(textView);
                        }
                    }

                }
                catch (Exception e){
                    Toast.makeText(View_Application.this, Messages.JsonMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // @@@@@@@@@@@@@@@@@@@ Get application details end @@@@@@@@@@@@@@@@@@


        // $$$$$$$$$$$$$$$$$$$$$$$ Complete Application start $$$$$$$$$$$$$$$$$$$$$$$
        complete_app_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> map = new HashMap<>();
                map.put("adminID",HelperFunctions.AdminID(View_Application.this));
                map.put("type","completeApplication");
                map.put("appID",data.getId());
                apiCall.Insert(map, Messages.FileName, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                      try {
                          JSONObject object = new JSONObject(result);
                          if(object.getString("status").trim().equals("1")){
                              startActivity(new Intent(View_Application.this,Admin_Home.class));
                              finish();
                              Toast.makeText(View_Application.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                          }else{
                              Toast.makeText(View_Application.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                          }
                      }
                      catch (Exception e){
                          Toast.makeText(View_Application.this, Messages.JsonMsg, Toast.LENGTH_SHORT).show();
                      }
                    }
                });
            }
        });

        // $$$$$$$$$$$$$$$$$$$$$$$ Complete Application end $$$$$$$$$$$$$$$$$$$$$$$


        // !!!!!!!!!!!!!!!!!!!! Delete Application start !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        pending_btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> map = new HashMap<>();
                map.put("adminID",HelperFunctions.AdminID(View_Application.this));
                map.put("type","deleteApplication");
                map.put("appID",data.getId());
                apiCall.Insert(map, Messages.FileName, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                      try {
                          JSONObject object = new JSONObject(result);
                          if(object.getString("status").trim().equals("1")){
                              Toast.makeText(View_Application.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                              startActivity(new Intent(getApplicationContext(),Admin_Home.class));
                              finish();
                          }
                          else{
                              Toast.makeText(View_Application.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                          }
                      }
                      catch (Exception e){
                          Toast.makeText(View_Application.this, Messages.JsonMsg, Toast.LENGTH_SHORT).show();
                      }
                    }
                });
            }
        });
        // !!!!!!!!!!!!!!!!!!!! Delete Application end !!!!!!!!!!!!!!!!!!!!!!!!!!!!!


        // ~~~~~~~~~~~~~~~~~~~~~~~~ Reject Appplication start ~~~~~~~~~~~~~~~~~~~~~~~
        pending_btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(View_Application.this,R.style.DialogStyle);
                bottomSheetDialog.setContentView(R.layout.reject_application_bottom);
                bottomSheetDialog.setCancelable(false);


                EditText comment = bottomSheetDialog.findViewById(R.id.rej_comment);

                Button closebtn = bottomSheetDialog.findViewById(R.id.closebtn);
                closebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });

                Button submitRejectbtn = bottomSheetDialog.findViewById(R.id.submitReject);
                submitRejectbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String cmt = comment.getText().toString();
                        if(HelperFunctions.verify(cmt)){
                            HashMap<String,String> map = new HashMap<>();
                            map.put("adminID",HelperFunctions.AdminID(View_Application.this));
                            map.put("type","rejectApplicationWithReasion");
                            map.put("appID",data.getId());
                            map.put("comment",cmt);

                            apiCall.Insert(map, Messages.FileName, new VolleyCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    try {
                                        JSONObject object = new JSONObject(result);
                                        if(object.getString("status").trim().equals("1")){
                                            Toast.makeText(View_Application.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(),Admin_Home.class));
                                            finish();
                                        }
                                        else{
                                            Toast.makeText(View_Application.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    catch (Exception e){
                                        Toast.makeText(View_Application.this, Messages.JsonMsg, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else {
                            comment.setError("Enter reject app comment!");
                            Toast.makeText(View_Application.this, "Enter reject app comment!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                bottomSheetDialog.show();
            }
        });
        // ~~~~~~~~~~~~~~~~~~~~~~~~ Reject Appplication start ~~~~~~~~~~~~~~~~~~~~~~~


        // ++++++++++++++++++++++++ Accept Application start ++++++++++++++++++++++++++
        pending_btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(View_Application.this,R.style.DialogStyle);
                bottomSheetDialog.setContentView(R.layout.accept_view_bottomsheet);
                bottomSheetDialog.setCancelable(false);


                TextInputEditText amounts = bottomSheetDialog.findViewById(R.id.amounts);
                amounts.setText(data.getAdvance_payment());

                Button closebtn = bottomSheetDialog.findViewById(R.id.closebtn);
                closebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });

                Button submitAccept = bottomSheetDialog.findViewById(R.id.submitAccept);
                submitAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String amt = amounts.getText().toString();
                        if(HelperFunctions.verify(amt)){
                            HashMap<String,String> map = new HashMap<>();
                            map.put("adminID",HelperFunctions.AdminID(View_Application.this));
                            map.put("type","acceptApplicationWithadvance");
                            map.put("appID",data.getId());
                            map.put("amount",amt);

                            apiCall.Insert(map, Messages.FileName, new VolleyCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    try {
                                        JSONObject object = new JSONObject(result);
                                        if(object.getString("status").trim().equals("1")){
                                            Toast.makeText(View_Application.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(),Admin_Home.class));
                                            finish();
                                        }
                                        else{
                                            Toast.makeText(View_Application.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    catch (Exception e){
                                        Toast.makeText(View_Application.this, Messages.JsonMsg, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(View_Application.this, "Amount shouldn`t be empty!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                bottomSheetDialog.show();
            }
        });
        // ++++++++++++++++++++++++ Accept Application end ++++++++++++++++++++++++++


        // ================== Fully paid advance payment start ======================
        full_paid_advance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> map = new HashMap<>();
                map.put("type","FullyPaidAdvancePayment");
                map.put("adminID",HelperFunctions.AdminID(View_Application.this));
                map.put("appID",data.getId());
                apiCall.Insert(map, Messages.FileName, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                     try{
                         JSONObject object = new JSONObject(result);
                         if(object.getString("status").trim().equals("1")){
                             Toast.makeText(View_Application.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                             View_Application.super.recreate();
                         }
                         else{
                             Toast.makeText(View_Application.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                         }
                     }
                     catch (Exception e){
                         Toast.makeText(View_Application.this, Messages.JsonMsg, Toast.LENGTH_SHORT).show();
                     }
                    }
                });
            }
        });
        // ================== Fully paid advance payment start ======================

        // ||||||||||||||||| Delivery Application start |||||||||||||||||||||||
         deliver_application_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(View_Application.this,Deliver_Application.class);
                 intent.putExtra("id",data.getId());
                 startActivity(intent);
             }
         });
        // ||||||||||||||||| Delivery Application end |||||||||||||||||||||||

    }

}