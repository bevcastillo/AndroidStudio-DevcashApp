package com.example.devcash;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.CustomAdapters.ProductlistAdapter;
import com.example.devcash.CustomAdapters.ReceiptProductAdapter;
import com.example.devcash.CustomAdapters.ReceiptServiceAdapter;
import com.example.devcash.CustomAdapters.ServicelistAdapter;
import com.example.devcash.Object.Account;
import com.example.devcash.Object.Business;
import com.example.devcash.Object.CartItem;
import com.example.devcash.Object.CustomerTransaction;
import com.example.devcash.Object.Employee;
import com.example.devcash.Object.Enterprise;
import com.example.devcash.Object.Product;
import com.example.devcash.Object.Services;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomerReceiptActivity extends AppCompatActivity implements View.OnClickListener {

    TextView enterprisename, enterpriseaddr, enterprisephone, receiptno, customertype, cashiername, txtdiscount, totalamt, datetime, txtvat, txtsubtotal, cash, change;
    RecyclerView showprodrecycler, showservicesrecycler;

    LinearLayout sendbyemailbtn, sendbymmsbtn;

    DatabaseReference ownerdbreference;
    FirebaseDatabase firebaseDatabase;

    List<Product> products;
    List<Services> services;
    List<CartItem> cartItems;
    CartItem cartItem;
    int customerId = 0;

    TextView regItemOff, regSubtotal, regVat, regVatExempt, regAmountDue, senItemOff, senSubtotal, senVat, senVatExempt, senDiscount, senAmountDue, regCash, regChange, seniorCash, seniorChange;
    LinearLayout regularLayout, seniorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_receipt);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        enterprisename = (TextView) findViewById(R.id.text_enterprisename);
        enterpriseaddr = (TextView) findViewById(R.id.text_enterpriseaddr);
        enterprisephone = (TextView) findViewById(R.id.text_enterprisephone);
        receiptno = (TextView) findViewById(R.id.text_receiptno);
        customertype = (TextView) findViewById(R.id.text_customertype);
        cashiername = (TextView) findViewById(R.id.text_cashiername);

        regItemOff = (TextView) findViewById(R.id.itemOff);
        regSubtotal = (TextView) findViewById(R.id.regular_subtotal);
        regVat = (TextView) findViewById(R.id.regular_vat);
        regVatExempt = (TextView) findViewById(R.id.regular_vatExempt);
        regAmountDue = (TextView) findViewById(R.id.regular_amountDue);
        regCash = (TextView) findViewById(R.id.regular_cash);
        regChange = (TextView) findViewById(R.id.regular_change);
        regularLayout = (LinearLayout) findViewById(R.id.regularcustomerLayout);



        senItemOff = (TextView) findViewById(R.id.seniorItemOff);
        senSubtotal = (TextView) findViewById(R.id.seniorSubtotal);
        senVat = (TextView) findViewById(R.id.seniorVat);
        senVatExempt = (TextView) findViewById(R.id.seniorvatExempt);
        senDiscount = (TextView) findViewById(R.id.seniorcitizenDiscount);
        senAmountDue = (TextView) findViewById(R.id.seniorAmountDue);
        seniorCash = (TextView) findViewById(R.id.senior_cash);
        seniorChange = (TextView) findViewById(R.id.senior_change);
        seniorLayout = (LinearLayout) findViewById(R.id.seniorcitizenLayout);

//        txtdiscount = (TextView) findViewById(R.id.text_discountamt);
//        totalamt = (TextView) findViewById(R.id.text_totalamt);
        datetime = (TextView) findViewById(R.id.text_transactiondatetime);
//        txtvat = (TextView) findViewById(R.id.text_totalvat);
//        txtsubtotal = (TextView) findViewById(R.id.text_subtotal);
//        cash = (TextView) findViewById(R.id.text_cash);
//        change = (TextView) findViewById(R.id.text_change);

        showprodrecycler = (RecyclerView) findViewById(R.id.receiptprod_recyclerview);
        showservicesrecycler = (RecyclerView) findViewById(R.id.receiptservice_recyclerview);

        sendbyemailbtn = (LinearLayout) findViewById(R.id.sendbyemail);
        sendbymmsbtn = (LinearLayout) findViewById(R.id.sendbytext);

        sendbyemailbtn.setOnClickListener(this);
        sendbymmsbtn.setOnClickListener(this);

        products = new ArrayList<>();
        services = new ArrayList<>();
        cartItems = new ArrayList<>();
        cartItem = new CartItem();

        firebaseDatabase = FirebaseDatabase.getInstance();
        ownerdbreference = firebaseDatabase.getReference("datadevcash/owner");


    }

    @Override
    protected void onStart() {
        super.onStart();

        displayAllPurchase();
        displayEntDetails();
//        displayPrice();
        displayTransactionDetails();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // We do this in-case we pressed task manager key
        products.clear();
        services.clear();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }else if (id == R.id.email_action){
//            emailDialog();
            Intent intent = new Intent(CustomerReceiptActivity.this, SendReceiptbyEmail.class);
            startActivity(intent);
        }else if (id == R.id.mms_action){

        }

        return super.onOptionsItemSelected(item);
    }

    private void emailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.customlayout_send_email, null);
        builder.setView(dialogView);

        final TextInputEditText cust_email= (TextInputEditText) dialogView.findViewById(R.id.edit_customer_email);

        builder.setPositiveButton("SEND", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    public void displayAllPurchase(){
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        SharedPreferences custIdShared = getApplicationContext().getSharedPreferences("CustomerIdPref", MODE_PRIVATE);
        customerId = (custIdShared.getInt("customer_id", 0));

        if (customerId <= 0) {
            customerId = customerId + 1;
        }

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        String acctkey = dataSnapshot1.getKey();

                        ownerdbreference.child(acctkey+"/business/customer_transaction").orderByChild("customer_id").equalTo(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        String customertransactionkey = dataSnapshot2.getKey();
                                        CustomerTransaction customerTransaction = dataSnapshot2.getValue(CustomerTransaction.class);
                                        Gson gson = new Gson();

                                        receiptno.setText("Receipt # "+(customerId));
                                        datetime.setText(customerTransaction.getTransaction_datetime());

                                        for(Map.Entry<String, Object> entry : customerTransaction.getCustomer_cart().entrySet()) {
                                            if (entry.getValue().toString().contains("product")) {
                                                String json = gson.toJson(entry.getValue());
                                                Log.d("PRODUCT JSON REP @@@@", json);
                                                String mJsonString = json;
                                                JsonParser parser = new JsonParser();
                                                JsonElement mJson =  parser.parse(mJsonString);

                                                JsonObject jsonObject = gson.fromJson(mJson, JsonObject.class);
                                                JsonElement prodJson = jsonObject.get("product");
                                                Product prodObj = gson.fromJson(prodJson, Product.class);

                                                cartItem.setProduct(prodObj);
                                                cartItems.add(cartItem);
                                                products.add(prodObj);

                                                ReceiptProductAdapter adapter = new ReceiptProductAdapter(products);
                                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                                showprodrecycler.setLayoutManager(layoutManager);
                                                showprodrecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                                showprodrecycler.setItemAnimator(new DefaultItemAnimator());
                                                showprodrecycler.setAdapter(adapter);

                                            } else {
                                                String json = gson.toJson(entry.getValue());
                                                String mJsonString = json;
                                                JsonParser parser = new JsonParser();
                                                JsonElement mJson =  parser.parse(mJsonString);

                                                JsonObject jsonObject = gson.fromJson(mJson, JsonObject.class);
                                                JsonElement servicesJson = jsonObject.get("services");
                                                Services servicesObj = gson.fromJson(servicesJson, Services.class);
                                                cartItem.setServices(servicesObj);
                                                cartItems.add(cartItem);
                                                services.add(servicesObj);

                                                ReceiptServiceAdapter adapter = new ReceiptServiceAdapter(services);
                                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                                showservicesrecycler.setLayoutManager(layoutManager);
                                                showservicesrecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                                showservicesrecycler.setItemAnimator(new DefaultItemAnimator());
                                                showservicesrecycler.setAdapter(adapter);
                                            }
                                        }
                                    }
                                }else {
//                                    Toast.makeText(CustomerReceiptActivity.this, "No customer transaction yet!"+customerId, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } // outer for-loop

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void displayEntDetails(){
        SharedPreferences ownerPref = getApplicationContext().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        SharedPreferences businessPref = getApplicationContext().getSharedPreferences("BusinessPref", MODE_PRIVATE);


        Gson gson = new Gson();
        String json = ownerPref.getString("account", "");
        String businessJson = businessPref.getString("business", "");

        Account account = gson.fromJson(json, Account.class);
        Business business = gson.fromJson(businessJson, Business.class);
        enterprisephone.setText(business.getOwner_mobileno());
        enterprisename.setText(business.enterprise.getEnt_name());
        enterpriseaddr.setText(business.enterprise.getEnt_addr());

    }

    public void displayTransactionDetails(){

        SharedPreferences ownerPref = getApplicationContext().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        SharedPreferences businessPref = getApplicationContext().getSharedPreferences("BusinessPref", MODE_PRIVATE);
        SharedPreferences empPref = getApplicationContext().getSharedPreferences("EmpPref", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = ownerPref.getString("account", "");
        String ownerAccountType = ownerPref.getString("account_type","");
        String employeeAccountType = empPref.getString("account_type", "");
        String businessJson = businessPref.getString("business", "");
        String employeeJson = empPref.getString("Employee", "");

        Account account = gson.fromJson(json, Account.class);
        Business business = gson.fromJson(businessJson, Business.class);
        Employee employee = gson.fromJson(employeeJson, Employee.class);

        String lname = business.getOwner_lname();
        String fname = business.getOwner_fname();
        String name = fname+" "+lname;

        if (!ownerAccountType.equals("")){
            cashiername.setText("Cashier: "+name);
        }

        if (!employeeAccountType.equals("")){
            String employeeName = employee.getEmp_fname() + " " + employee.getEmp_lname();
            cashiername.setText("Cashier: "+employeeName);
        }


        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        SharedPreferences custIdShared = getApplicationContext().getSharedPreferences("CustomerIdPref", MODE_PRIVATE);
        customerId = (custIdShared.getInt("customer_id", 0));

        SharedPreferences customerTypePref = getApplicationContext().getSharedPreferences("CustomerTypePref", MODE_PRIVATE);
        final String customerType = (customerTypePref.getString("customer_type","Regular Customer"));

        if (customerId <= 0) {
            customerId = customerId + 1;
        }

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        String acctkey = dataSnapshot1.getKey();

                        ownerdbreference.child(acctkey+"/business/customer_transaction").orderByChild("customer_id").equalTo(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot datasnapshot3: dataSnapshot.getChildren()){
                                        String customertransaction = datasnapshot3.getKey();

                                        CustomerTransaction customerTransaction = datasnapshot3.getValue(CustomerTransaction.class);

                                        String customerType = customerTransaction.getCustomer_type();
                                        double amountDue = customerTransaction.getAmount_due();
                                        double seniorDisc = customerTransaction.getSenior_discount();
                                        double subtotal = customerTransaction.getSubtotal();
                                        double itemDiscount = customerTransaction.getTotal_item_discount();
                                        int totalQty = customerTransaction.getTotal_item_qty();
                                        double vat = customerTransaction.getVat();
                                        double vatExempt = customerTransaction.getVat_exempt_sale();
                                        double cash = customerTransaction.getCash_received();
                                        double change = customerTransaction.getChange();

                                        if (customerType.equals("Regular Customer")){
                                            regularLayout.setVisibility(View.VISIBLE);
                                            regItemOff.setText(String.valueOf(itemDiscount));
                                            regSubtotal.setText(String.valueOf(subtotal));
                                            regVat.setText(String.valueOf(vat));
                                            regAmountDue.setText(String.valueOf(amountDue));
                                            regCash.setText(String.valueOf(cash));
                                            regChange.setText(String.valueOf(change));

                                        }else {
                                            seniorLayout.setVisibility(View.VISIBLE);
                                            senItemOff.setText(String.valueOf(itemDiscount));
                                            senSubtotal.setText(String.valueOf(subtotal));
                                            senVat.setText(String.valueOf(vat));
                                            senVatExempt.setText(String.valueOf(vatExempt));
                                            senDiscount.setText(String.valueOf(seniorDisc));
                                            senAmountDue.setText(String.valueOf(amountDue));
                                            seniorCash.setText(String.valueOf(cash));
                                            seniorChange.setText(String.valueOf(change));
                                        }

//                                        double cashreceived = customerTransaction.getCash_received();
//                                        double cashchange = customerTransaction.getChange();
//                                        double subtotal = customerTransaction.getSubtotal();
//                                        double totaldiscount = customerTransaction.getTotal_item_discount();
//                                        double totalprice = customerTransaction.getAmount_due();
//                                        double vat = customerTransaction.getVat();
//                                        String custType = customerTransaction.getCustomer_type();
//
//                                        cash.setText(String.valueOf(cashreceived));
//                                        change.setText(String.valueOf(cashchange));
//                                        txtsubtotal.setText(String.valueOf(subtotal));
//                                        txtdiscount.setText(String.valueOf(totaldiscount));
//                                        totalamt.setText(String.valueOf(totalprice));
//                                        txtvat.setText(String.valueOf(vat));
//                                        customertype.setText(custType);
                                    }
                                }else {
                                    //set condition here if the id does not exist
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.sendbyemail:
                Intent intent = new Intent(CustomerReceiptActivity.this, SendReceiptbyEmail.class);
                startActivity(intent);
                break;
            case R.id.sendbytext:
                Intent intent1 = new Intent(CustomerReceiptActivity.this, SendReceiptviaMMS.class);
                startActivity(intent1);
            break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_send_receipt, menu);

        return true;
    }
}
