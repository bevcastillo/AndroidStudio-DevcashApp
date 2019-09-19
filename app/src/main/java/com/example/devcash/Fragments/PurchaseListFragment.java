package com.example.devcash.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.devcash.AllPurchaseActivity;
import com.example.devcash.CustomAdapters.ProductlistAdapter;
import com.example.devcash.CustomAdapters.ServicelistAdapter;
import com.example.devcash.Object.CartItem;
import com.example.devcash.Object.CustomerTransaction;
import com.example.devcash.Object.Product;
import com.example.devcash.Object.PurchaseTransactionlistdata;
import com.example.devcash.Object.Services;
import com.example.devcash.QRCodeFragment;
import com.example.devcash.R;
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

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseListFragment extends Fragment implements View.OnClickListener {

    private Toolbar purchaseListToolbar;
    private Spinner purchaseListSpinner;
    private Button btnpay;
    private RecyclerView recyclerViewpurchlist, anotherrecyler;

    private Toolbar toolbar;
    private DrawerLayout mDrawer;

    private LinearLayout layoutScanCode, layoutNewTransaction;

    private DatabaseReference ownerdbreference;
    private FirebaseDatabase firebaseDatabase;

    List<PurchaseTransactionlistdata> list;
    List<Product> products;
    List<Services> services;
    List<CartItem> cartItems;
    CartItem cartItem;
    int customerId = 0;


    public PurchaseListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_list, container, false);

        purchaseListToolbar = (Toolbar) view.findViewById(R.id.toolbar_purchaselist);
        purchaseListSpinner = (Spinner) view.findViewById(R.id.spinner_customertype);
        recyclerViewpurchlist = (RecyclerView) view.findViewById(R.id.recycler_purchtransaction);
        anotherrecyler = (RecyclerView) view.findViewById(R.id.recycler_services);

        //recycler_purchtransaction
        btnpay = (Button) view.findViewById(R.id.btn_paypurchasetransaction);

        //
        layoutScanCode = (LinearLayout) view.findViewById(R.id.layout_transaction_qrcode);
        layoutNewTransaction = (LinearLayout) view.findViewById(R.id.layout_transaction_new);

        ///

        //setting up the hamburger icon to show in the custom toolbar
//        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//
//        mDrawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);


        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.customer_type));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        purchaseListSpinner.setAdapter(myAdapter);

        purchaseListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),
                        purchaseListSpinner.getSelectedItem().toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //adding listeners to the linear layouts
        layoutScanCode.setOnClickListener(this);
        layoutNewTransaction.setOnClickListener(this);
        btnpay.setOnClickListener(this);



        products = new ArrayList<>();
        services = new ArrayList<>();
        cartItems = new ArrayList<>();
        cartItem = new CartItem();

        //

        firebaseDatabase = FirebaseDatabase.getInstance();
        ownerdbreference = firebaseDatabase.getReference("datadevcash/owner");

        SharedPreferences custIdShared = getActivity().getSharedPreferences("CustomerIdPref", MODE_PRIVATE);
        customerId = (custIdShared.getInt("customer_id", 0));

        if (customerId <= 0) {
            customerId = customerId + 1;
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        displayAllPurchase();
        displayPriceQty();


    }

    public void displayAllPurchase(){
        SharedPreferences shared = getActivity().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        SharedPreferences customerIdPref = getActivity().getSharedPreferences("CustomerIdPref", MODE_PRIVATE);
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
                                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                                recyclerViewpurchlist.setLayoutManager(layoutManager);
                                                recyclerViewpurchlist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                                                recyclerViewpurchlist.setItemAnimator(new DefaultItemAnimator());
                                                recyclerViewpurchlist.setAdapter(adapter);

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
                                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                                anotherrecyler.setLayoutManager(layoutManager);
                                                anotherrecyler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                                                anotherrecyler.setItemAnimator(new DefaultItemAnimator());
                                                anotherrecyler.setAdapter(adapter);
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
                                    Toast.makeText(getActivity(), "No customer transaction yet!"+customerId, Toast.LENGTH_SHORT).show();
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
        SharedPreferences shared = getActivity().getSharedPreferences("OwnerPref", MODE_PRIVATE);
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
                                        double total = customerTransaction.getAmount_due();
                                        int quantity = (int) customerTransaction.getTotal_item_qty();
//                                        double qty = customerTransaction.getTotal_qty();
                                        double totaldiscount = customerTransaction.getTotal_item_discount();



//                                        textsubtotal.setText(String.valueOf(subtotal));
//                                        texttotaldiscount.setText(String.valueOf(totaldiscount));
//                                        textvat.setText(String.valueOf(vat));
//                                        texttotal.setText(String.valueOf(total));
//
//                                        textpriceqty.setText(qty+" item = ₱"+(total));



                                    }
                                }else {
                                    //customer does not exist
//                                    textpriceqty.setText("No items = ₱0.00");
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflate menu
        inflater.inflate(R.menu.purchaselist_menu, menu);
        menu.findItem(R.id.action_new).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.btn_paypurchasetransaction:
                Fragment chargeFragment = new ChargeFragment();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.customersales_content, chargeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.layout_transaction_qrcode:
//                Toast.makeText(getActivity(), "Scan Item", Toast.LENGTH_SHORT).show();
                Fragment QRCodeFragment = new QRCodeFragment();

                FragmentManager fragmentManager1 = getFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.customersales_content, QRCodeFragment);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();

                break;
            case R.id.layout_transaction_new:
                Toast.makeText(getActivity(), "New CustomerTransaction", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
