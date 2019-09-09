package com.example.devcash;

import android.app.Service;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.CustomAdapters.AllPurchaseAdapter;
import com.example.devcash.CustomAdapters.AllPurchasesAdapter;
import com.example.devcash.CustomAdapters.ProductlistAdapter;
import com.example.devcash.CustomAdapters.ServicelistAdapter;
import com.example.devcash.Object.CartItem;
import com.example.devcash.Object.CustomerTransaction;
import com.example.devcash.Object.Product;
import com.example.devcash.Object.PurchaseTransaction;
import com.example.devcash.Object.PurchaseTransactionlistdata;
import com.example.devcash.Object.PurchasedItem;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AllPurchaseActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerViewProducts, recyclerViewServices;
    DatabaseReference dbreference;
    DatabaseReference ownerdbreference;
    FirebaseDatabase firebaseDatabase;
    TextView text_totprice, text_qty;
    LinearLayout moreoptionsbtn;

    List<PurchaseTransactionlistdata> list;
    List<Product> products;
    List<Services> services;
    List<CartItem> cartItems;
    CartItem cartItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_purchase);


        //
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        recyclerViewpurchaselist = (RecyclerView) findViewById(R.id.purchaselist_recyclerview);

        recyclerViewProducts = (RecyclerView) findViewById(R.id.prodlist_recyclerview);
        recyclerViewServices = (RecyclerView) findViewById(R.id.servlist_recyclerview);

        text_qty = (TextView) findViewById(R.id.txt_qty);
        text_totprice = (TextView) findViewById(R.id.txt_totprice);
        products = new ArrayList<>();
        services = new ArrayList<>();
        cartItems = new ArrayList<>();
        cartItem = new CartItem();

        moreoptionsbtn = (LinearLayout) findViewById(R.id.moreoptionsbtn);

        moreoptionsbtn.setOnClickListener(this);

        //firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        ownerdbreference = firebaseDatabase.getReference("datadevcash/owner");
    }

    @Override
    protected void onStart() {
        super.onStart();

        displayAllPurchase();
        displayPriceQty();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // We do this in-case we pressed task manager key
        products.clear();
        services.clear();
    }

    public void displayAllPurchase(){
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        final Product[] prod = {new Product()};
        Services serv = new Services();

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        String acctkey = dataSnapshot1.getKey();

                        ownerdbreference.child(acctkey+"/business/customer_transaction").orderByChild("customer_id").equalTo(1).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    list = new ArrayList<>();
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        String customertransactionkey = dataSnapshot2.getKey();
                                        CustomerTransaction customerTransaction = dataSnapshot2.getValue(CustomerTransaction.class);
                                        Gson gson = new Gson();

                                        for(Map.Entry<String, Object> entry : customerTransaction.getCustomer_cart().entrySet()) {
//                                            Toast.makeText(AllPurchaseActivity.this, entry.getValue()+" value", Toast.LENGTH_SHORT).show();
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

                                                ProductlistAdapter adapter = new ProductlistAdapter(products);
                                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                                recyclerViewProducts.setLayoutManager(layoutManager);
                                                recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                                recyclerViewProducts.setItemAnimator(new DefaultItemAnimator());
                                                recyclerViewProducts.setAdapter(adapter);

//                                                products.add(prodObj);
//                                                Toast.makeText(AllPurchaseActivity.this, products.size()+" is the size of services", Toast.LENGTH_SHORT).show();


                                            } else {
                                                Log.d("SERVICE TAG!!!", entry.getValue().toString());
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

                                                ServicelistAdapter adapter = new ServicelistAdapter(services);
                                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                                recyclerViewServices.setLayoutManager(layoutManager);
                                                recyclerViewServices.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                                recyclerViewServices.setItemAnimator(new DefaultItemAnimator());
                                                recyclerViewServices.setAdapter(adapter);

//                                                ServicelistAdapter adapter = new ServicelistAdapter(cartItems);
//                                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//                                                recyclerViewServices.setLayoutManager(layoutManager);
//                                                recyclerViewServices.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//                                                recyclerViewServices.setItemAnimator(new DefaultItemAnimator());
//                                                recyclerViewServices.setAdapter(adapter);
//                                                adapter.notifyDataSetChanged();


//                                                services.add(servicesObj);
//                                                Toast.makeText(AllPurchaseActivity.this, services.size()+" is the size", Toast.LENGTH_SHORT).show();
                                            }
                                        }

//                                         Beverly
//                                        AllPurchaseAdapter adapter = new AllPurchaseAdapter(cartItems);
//                                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//                                        recyclerViewpurchaselist.setLayoutManager(layoutManager);
//                                        recyclerViewpurchaselist.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//                                        recyclerViewpurchaselist.setItemAnimator(new DefaultItemAnimator());
//                                        recyclerViewpurchaselist.setAdapter(adapter);
//                                        adapter.notifyDataSetChanged();



//                                                adapter.notifyDataSetChanged();


//                                        Toast.makeText(AllPurchaseActivity.this, cartItems.size()+" is the size of the cart", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(AllPurchaseActivity.this, "No customer transaction yet!", Toast.LENGTH_SHORT).show();
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

    public void displayPriceQty(){
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        final String acctkey = dataSnapshot1.getKey();

                        ownerdbreference.child(acctkey+"/business/customer_transaction").orderByChild("customer_id").equalTo(1).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        String customertransactionkey = dataSnapshot2.getKey();

                                        CustomerTransaction customerTransaction = dataSnapshot2.getValue(CustomerTransaction.class);
                                        double subtotal = customerTransaction.getSubtotal();
                                        int qty = (int) customerTransaction.getTotal_qty();

                                        text_qty.setText(String.valueOf(qty));
                                        text_totprice.setText("₱ "+ (subtotal));

                                    }
                                }else {
                                    //customer does not exist
                                    text_qty.setText("0");
                                    text_totprice.setText("₱ 0.00");
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




//    public void displayAllPurchase(){
//        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
//        final String username = (shared.getString("owner_username", ""));
//
//        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()){
//                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
//                        String acctkey = dataSnapshot1.getKey();
//
//                        ownerdbreference.child(acctkey+"/business/transaction").addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                list = new ArrayList<>();
//                                if(dataSnapshot.exists()){
//                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
//                                        PurchaseTransaction purchaseTransaction = dataSnapshot2.getValue(PurchaseTransaction.class);
//                                        PurchaseTransactionlistdata listdata = new PurchaseTransactionlistdata();
//                                        double services_subtotal = purchaseTransaction.getPurchasedItem().getServices().getService_subtotal();
////                                        double products_subtotal = purchaseTransaction.getPurchasedItem().getProduct().getSubtotal();
//                                        double total = purchaseTransaction.getPurch_tot_price();
//                                        double qty = purchaseTransaction.getPurch_tot_qty();
////                                        String name = pur
//                                        listdata.setPurch_subtotal(services_subtotal);
//                                        listdata.setPurch_tot_price(total);
//                                        listdata.setPurch_qty(qty);
//                                        listdata.setPurchasedItem(purchaseTransaction.getPurchasedItem());
//                                        list.add(listdata);
//                                    }
//                                    AllPurchaseAdapter adapter = new AllPurchaseAdapter(list);
//                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//                                    recyclerViewpurchaselist.setLayoutManager(layoutManager);
//                                    recyclerViewpurchaselist.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//                                    recyclerViewpurchaselist.setItemAnimator(new DefaultItemAnimator());
//                                    recyclerViewpurchaselist.setAdapter(adapter);
//                                    adapter.notifyDataSetChanged();
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

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

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.moreoptionsbtn:
                moreoptionsDialog();
                break;
        }
    }

    public void moreoptionsDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View optionsview = inflater.inflate(R.layout.customdialog_moreoptions, null);
        builder.setView(optionsview);

        final ImageView imgclosebtn = (ImageView) optionsview.findViewById(R.id.btnclose);

        imgclosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        builder.create();
        builder.show();
    }
}
