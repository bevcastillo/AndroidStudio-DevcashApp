package com.example.devcash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.CustomAdapters.ProductlistAdapter;
import com.example.devcash.CustomAdapters.ServicelistAdapter;
import com.example.devcash.Object.CartItem;
import com.example.devcash.Object.CustomerTransaction;
import com.example.devcash.Object.Product;
import com.example.devcash.Object.PurchaseTransactionlistdata;
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

public class CashierPurchaseListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    RecyclerView recyclerViewProducts, recyclerViewServices;
    DatabaseReference dbreference;
    DatabaseReference ownerdbreference;
    FirebaseDatabase firebaseDatabase;
    TextView text_totprice, text_qty, texttotal, texttotaldiscount, textvat, textpriceqty, textsubtotal;
    LinearLayout moreoptionsbtn, btncharge, layoutbtnscan, layoutbtnnewsale;

    TextView regItemOff, regSubtotal, regVat, regVatExempt, regAmountDue, senItemOff, senSubtotal, senVat, senVatExempt, senDiscount, senAmountDue;
    LinearLayout regularLayout, seniorLayout;

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
        setContentView(R.layout.activity_cashier_purchase_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerViewProducts = (RecyclerView) findViewById(R.id.prodlist_recyclerview);
        recyclerViewServices = (RecyclerView) findViewById(R.id.servlist_recyclerview);

        textpriceqty = (TextView) findViewById(R.id.textpriceqty);

        regItemOff = (TextView) findViewById(R.id.itemOff);
        regSubtotal = (TextView) findViewById(R.id.regular_subtotal);
        regVat = (TextView) findViewById(R.id.regular_vat);
        regVatExempt = (TextView) findViewById(R.id.regular_vatExempt);
        regAmountDue = (TextView) findViewById(R.id.regular_amountDue);
        regularLayout = (LinearLayout) findViewById(R.id.regularcustomerLayout);



        senItemOff = (TextView) findViewById(R.id.seniorItemOff);
        senSubtotal = (TextView) findViewById(R.id.seniorSubtotal);
        senVat = (TextView) findViewById(R.id.seniorVat);
        senVatExempt = (TextView) findViewById(R.id.seniorvatExempt);
        senDiscount = (TextView) findViewById(R.id.seniorcitizenDiscount);
        senAmountDue = (TextView) findViewById(R.id.seniorAmountDue);
        seniorLayout = (LinearLayout) findViewById(R.id.seniorcitizenLayout);


        mtoolbar = (Toolbar) findViewById(R.id.toolbar_allpurchase);
        customertypeSpinner = (Spinner) findViewById(R.id.spinner_customertype);

        layoutbtnscan = (LinearLayout) findViewById(R.id.layoutbtnscanqrcode);
        layoutbtnnewsale = (LinearLayout) findViewById(R.id.layoutnewsale);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(CashierPurchaseListActivity.this, android.R.layout.simple_list_item_1,
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
//        displayPriceQty();

        //getting the selected customer type data from PurchaseInventorylistFragment
        Bundle bundle = this.getIntent().getExtras();
        if (bundle!=null){
            selectedCustomerType = bundle.getString("selectedCustomerType");

        }
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

                                        if (customerTransaction.getCustomer_cart()!=null){
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
                                        }else {
                                            Toast.makeText(CashierPurchaseListActivity.this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
                                        }

//                                        Toast.makeText(AllPurchaseActivity.this, cartItems.size()+" is the size of the cart", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(CashierPurchaseListActivity.this, "No customer transaction yet!"+customerId, Toast.LENGTH_SHORT).show();
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.moreoptionsbtn:
//                moreoptionsDialog();
                break;
            case R.id.chargebtn:
                Intent intent = new Intent(CashierPurchaseListActivity.this, CashierCustomerPaymentActivity.class);
                startActivity(intent);
                break;

            case R.id.layoutbtnscanqrcode:
                Intent intent1 = new Intent(CashierPurchaseListActivity.this, ScanQRCode.class);
                startActivity(intent1);
                break;
            case R.id.layoutnewsale:
//                startNewSale();
                break;
        }

    }
}
