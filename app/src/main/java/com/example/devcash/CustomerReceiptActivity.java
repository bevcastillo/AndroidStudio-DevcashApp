package com.example.devcash;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.CustomAdapters.ProductlistAdapter;
import com.example.devcash.CustomAdapters.ReceiptProductAdapter;
import com.example.devcash.CustomAdapters.ReceiptServiceAdapter;
import com.example.devcash.CustomAdapters.ServicelistAdapter;
import com.example.devcash.Object.CartItem;
import com.example.devcash.Object.CustomerTransaction;
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

public class CustomerReceiptActivity extends AppCompatActivity {

    TextView enterprisename, enterpriseaddr, enterprisephone, receiptno, customertype, cashiername, discount, totalamt, datetime;
    RecyclerView showprodrecycler, showservicesrecycler;

    DatabaseReference ownerdbreference;
    FirebaseDatabase firebaseDatabase;

    List<Product> products;
    List<Services> services;
    List<CartItem> cartItems;
    CartItem cartItem;
    int customerId = 0;

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
        discount = (TextView) findViewById(R.id.text_discountamt);
        totalamt = (TextView) findViewById(R.id.text_totalamt);
        datetime = (TextView) findViewById(R.id.text_transactiondatetime);

        showprodrecycler = (RecyclerView) findViewById(R.id.receiptprod_recyclerview);
        showservicesrecycler = (RecyclerView) findViewById(R.id.receiptservice_recyclerview);

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
        }

        return super.onOptionsItemSelected(item);
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
                                    Toast.makeText(CustomerReceiptActivity.this, "No customer transaction yet!"+customerId, Toast.LENGTH_SHORT).show();
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
}
