package com.example.devcash;

import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AllPurchaseActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    RecyclerView recyclerViewProducts, recyclerViewServices;
    DatabaseReference dbreference;
    DatabaseReference ownerdbreference;
    FirebaseDatabase firebaseDatabase;
    TextView text_totprice, text_qty, texttotal, texttotaldiscount, textvat, textpriceqty, textsubtotal;
    LinearLayout moreoptionsbtn, btncharge, layoutbtnscan, layoutbtnnewsale;

    List<PurchaseTransactionlistdata> list;
    List<Product> products;
    List<Services> services;
    List<CartItem> cartItems;
    CartItem cartItem;
    int customerId = 0;

    Toolbar mtoolbar;
    Spinner customertypeSpinner;
    String selectedCustomerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_purchase);


        //
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        recyclerViewpurchaselist = (RecyclerView) findViewById(R.id.purchaselist_recyclerview);

        recyclerViewProducts = (RecyclerView) findViewById(R.id.prodlist_recyclerview);
        recyclerViewServices = (RecyclerView) findViewById(R.id.servlist_recyclerview);


        textpriceqty = (TextView) findViewById(R.id.textpriceqty);
//        text_totprice = (TextView) findViewById(R.id.txt_totprice);
        texttotal = (TextView) findViewById(R.id.text_total);
        texttotaldiscount = (TextView) findViewById(R.id.text_discount);
        textvat = (TextView) findViewById(R.id.text_vat);
        textsubtotal = (TextView) findViewById(R.id.text_subtotal);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar_allpurchase);
        customertypeSpinner = (Spinner) findViewById(R.id.spinner_customertype);

        layoutbtnscan = (LinearLayout) findViewById(R.id.layoutbtnscanqrcode);
        layoutbtnnewsale = (LinearLayout) findViewById(R.id.layoutnewsale);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(AllPurchaseActivity.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.customer_type));

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customertypeSpinner.setAdapter(spinnerAdapter);

        customertypeSpinner.setOnItemSelectedListener(this);
        layoutbtnnewsale.setOnClickListener(this);
        layoutbtnscan.setOnClickListener(this);


        products = new ArrayList<>();
        services = new ArrayList<>();
        cartItems = new ArrayList<>();
        cartItem = new CartItem();

        moreoptionsbtn = (LinearLayout) findViewById(R.id.moreoptionsbtn);
        btncharge = (LinearLayout) findViewById(R.id.chargebtn);


        moreoptionsbtn.setOnClickListener(this);
        btncharge.setOnClickListener(this);

        //firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        ownerdbreference = firebaseDatabase.getReference("datadevcash/owner");

        SharedPreferences custIdShared = getApplicationContext().getSharedPreferences("CustomerIdPref", MODE_PRIVATE);
        customerId = (custIdShared.getInt("customer_id", 0));

        if (customerId <= 0) {
            customerId = customerId + 1;
        }
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

        SharedPreferences customerIdPref = getApplicationContext().getSharedPreferences("CustomerIdPref", MODE_PRIVATE);
        final SharedPreferences.Editor customerIdEditor = customerIdPref.edit();

        Services serv = new Services();

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

                                                ServicelistAdapter adapter = new ServicelistAdapter(services);
                                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                                recyclerViewServices.setLayoutManager(layoutManager);
                                                recyclerViewServices.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                                recyclerViewServices.setItemAnimator(new DefaultItemAnimator());
                                                recyclerViewServices.setAdapter(adapter);
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
                                    Toast.makeText(AllPurchaseActivity.this, "No customer transaction yet!"+customerId, Toast.LENGTH_SHORT).show();
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

                        ownerdbreference.child(acctkey+"/business/customer_transaction").orderByChild("customer_id").equalTo(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        String customertransactionkey = dataSnapshot2.getKey();

                                        CustomerTransaction customerTransaction = dataSnapshot2.getValue(CustomerTransaction.class);
                                        double vat = customerTransaction.getVat() *100/100;
                                        double subtotal = customerTransaction.getSubtotal();
                                        double total = customerTransaction.getTotal_price();
                                        int quantity = (int) customerTransaction.getTotal_qty();
                                        double qty = customerTransaction.getTotal_qty();
                                        double totaldiscount = customerTransaction.getTotal_discount();

//                                        text_qty.setText(String.valueOf(qty));
//                                        text_totprice.setText("₱ "+ (subtotal));
//                                        texttotal.setText("₱ "+ (total));
//                                        texttotaldiscount.setText(String.valueOf(customerTransaction.getTotal_discount()));
//                                        textvat.setText(String.valueOf(customerTransaction.getVat()*100/100));


                                        textsubtotal.setText(String.valueOf(subtotal));
                                        texttotaldiscount.setText(String.valueOf(totaldiscount));
                                        textvat.setText(String.valueOf(vat));
                                        texttotal.setText(String.valueOf(total));

                                        textpriceqty.setText(qty+" item = ₱"+(total));



                                    }
                                }else {
                                    //customer does not exist
                                    textpriceqty.setText("No items = ₱0.00");
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
            case R.id.chargebtn:
                Intent intent = new Intent(AllPurchaseActivity.this, CustomerPaymentActivity.class);
                startActivity(intent);
                break;

            case R.id.layoutbtnscanqrcode:
                Intent intent1 = new Intent(AllPurchaseActivity.this, ScanQRCode.class);
                startActivity(intent1);
                break;
            case R.id.layoutnewsale:
                startNewSale();
                break;
        }
    }

    public void moreoptionsDialog(){
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        final View optionsview = inflater.inflate(R.layout.customdialog_moreoptions, null);
        dialog.setView(optionsview);

        final ImageView imgclosebtn = (ImageView) optionsview.findViewById(R.id.btnclose);
        final TextView add_discount = (TextView) optionsview.findViewById(R.id.option_discount);
        final TextView clear_cart = (TextView) optionsview.findViewById(R.id.option_clearcart);
        final TextView new_transaction = (TextView) optionsview.findViewById(R.id.option_newtransaction);

        new_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewSale();
            }
        });

        clear_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
                String username = (shared.getString("owner_username", ""));

//                Toast.makeText(AllPurchaseActivity.this, "Clear the cart of "+customerId, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AllPurchaseActivity.this, DashboardActivity.class);
                startActivity(intent);

                clearCart(customerId, username);
            }
        });
//
        imgclosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

//        builder.create();
        dialog.show();
    }

    public void startNewSale() {
        // get first from shared preference.
        SharedPreferences shared = getSharedPreferences("CustomerIdPref", MODE_PRIVATE);
        int sharedPrefCustId = (shared.getInt("customer_id", 0));
        int newId = sharedPrefCustId + 1;

        // store the new customer id to shared preference.
        SharedPreferences customerIdPref = getApplicationContext().getSharedPreferences("CustomerIdPref", MODE_PRIVATE);
        SharedPreferences.Editor customerIdEditor = customerIdPref.edit();
        customerIdEditor.putInt("customer_id", newId);
        customerIdEditor.commit();

        // go to activity
        Intent intent = new Intent(AllPurchaseActivity.this, DashboardActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Starting a new sale.", Toast.LENGTH_SHORT).show();
    }

    public void clearCart(final int customerId, String ownerUsername) {
        ownerdbreference.orderByChild("business/owner_username").equalTo(ownerUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        final String acctkey = dataSnapshot1.getKey();

                        ownerdbreference.child(acctkey+"/business/customer_transaction").orderByChild("customer_id").equalTo(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        final String customertransactionkey = dataSnapshot2.getKey();

                                        ownerdbreference.child(acctkey+"/business/customer_transaction")
                                                        .child(customertransactionkey+"/customer_cart")
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.exists()) {
                                                                    for (DataSnapshot dataSnapshot3 : dataSnapshot.getChildren()) {
                                                                        String cartKey = dataSnapshot3.getKey();

                                                                        // delete the items in cart.
                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction")
                                                                                        .child(customertransactionkey+"/customer_cart/"+cartKey)
                                                                                        .setValue(null)
                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                Toast.makeText(AllPurchaseActivity.this, "Cart has been cleared successfully!", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        });

                                                                        // update subtotal
                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction")
                                                                                .child(customertransactionkey+"/subtotal")
                                                                                .setValue(0);
                                                                        // update total_qty
                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction")
                                                                                .child(customertransactionkey+"/total_qty")
                                                                                .setValue(0);
                                                                    }

                                                                } else {
//                                                                    Toast.makeText(AllPurchaseActivity.this, "cart not found.", Toast.LENGTH_SHORT).show();
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int sid = parent.getId();

        switch (sid){
            case R.id.spinner_customertype:
                selectedCustomerType = customertypeSpinner.getSelectedItem().toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
