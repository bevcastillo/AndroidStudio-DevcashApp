package com.example.devcash.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.AllPurchaseActivity;
import com.example.devcash.CustomAdapters.PurchaseInventoryProductsAdapter;
import com.example.devcash.CustomAdapters.PurchaseInventoryServicesAdapter;
import com.example.devcash.MyUtility;
import com.example.devcash.Object.CustomerTransaction;
import com.example.devcash.Object.Product;
import com.example.devcash.Object.Productlistdata;
import com.example.devcash.Object.PurchaseTransactionlistdata;
import com.example.devcash.Object.Services;
import com.example.devcash.Object.Serviceslistdata;
import com.example.devcash.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseInventorylistFragment extends Fragment implements SearchView.OnQueryTextListener,
        MenuItem.OnActionExpandListener, View.OnClickListener {

    //
    DatabaseReference dbreference;
    DatabaseReference productsdbreference;
    DatabaseReference servicesdbreference;
    DatabaseReference ownerdbreference;
    FirebaseDatabase firebaseDatabase;

    List<Productlistdata> list;
    List<Serviceslistdata> slist;
    List<PurchaseTransactionlistdata> ptlist;

    ProgressBar invprogress;
    LinearLayout emptylayout;
    //
    Toolbar itemListToolbar;
    Spinner itemListSpinner;
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;
    LinearLayout btncharge;
    TextView text_totprice, text_qty;

    RecyclerView recyclerViewitemlist;
    String selectedinventorytype;


    public PurchaseInventorylistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_purchase_item_list, container, false);
        View view = inflater.inflate(R.layout.fragment_purchase_inventory_list, container, false);
        setHasOptionsMenu(true);

        itemListToolbar = (Toolbar) view.findViewById(R.id.toolbar_purchaseitemlist);
        itemListSpinner = (Spinner) view.findViewById(R.id.spinner_inventorytype);

        invprogress = (ProgressBar) view.findViewById(R.id.inv_progressbar);
        emptylayout = (LinearLayout) view.findViewById(R.id.layout_emptyinv);

        btncharge = (LinearLayout) view.findViewById(R.id.btn_chargeitem);

        text_qty = (TextView) view.findViewById(R.id.txt_qty);
        text_totprice = (TextView) view.findViewById(R.id.txt_totprice);

        btncharge.setOnClickListener(this);

        recyclerViewitemlist = (RecyclerView) view.findViewById(R.id.recyclerview_purchitemlist);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbreference = firebaseDatabase.getReference("/datadevcash");
        productsdbreference = firebaseDatabase.getReference("datadevcash/products");
        servicesdbreference = firebaseDatabase.getReference("datadevcash/services");
        ownerdbreference = firebaseDatabase.getReference("datadevcash/owner");

        ///
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.custom_spinner_item,
                getResources().getStringArray(R.array.inventory_type));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemListSpinner.setAdapter(myAdapter);


        itemListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedinventorytype = itemListSpinner.getItemAtPosition(position).toString();
                if(itemListSpinner.getSelectedItem().equals("Products")){
                    viewAllProducts();
                }else if((itemListSpinner.getSelectedItem().equals("Services"))){
                    viewAllServices();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        displayQtyPrice(); //display the total price and the total quantity to the button
    }

    //


    public void viewAllProducts(){

        SharedPreferences shared = getActivity().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

//        Toast.makeText(getActivity(), username, Toast.LENGTH_SHORT).show();

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        String key = ds.getKey();
                        dbreference.child("owner/"+key+"/business/product").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                list = new ArrayList<>();
                                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                        Product product = dataSnapshot1.getValue(Product.class);
                                        Productlistdata listdata = new Productlistdata();
                                        String prodname = product.getProd_name();
                                        double prodprice = product.getProd_price();
                                        listdata.setProd_name(prodname);
                                        listdata.setProd_price(prodprice);
                                        listdata.setProd_expdate(product.getProd_expdate());
                                        list.add(listdata);
                                    }
                                        PurchaseInventoryProductsAdapter adapter = new PurchaseInventoryProductsAdapter(list);
                                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),6);
                                        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                        recyclerViewitemlist.setLayoutManager(gridLayoutManager);
                                        recyclerViewitemlist.setItemAnimator(new DefaultItemAnimator());
                                        recyclerViewitemlist.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();

                                        invprogress.setVisibility(View.GONE);

                                        if(list.isEmpty()){
                                            emptylayout.setVisibility(View.VISIBLE);
                                        }else{
                                            emptylayout.setVisibility(View.GONE);
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


    public void viewAllServices(){
        SharedPreferences shared = getActivity().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        String key = ds.getKey();
                        dbreference.child("owner/"+key+"/business/services").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                slist = new ArrayList<>();
                                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                        Services services = dataSnapshot1.getValue(Services.class);
                                        Serviceslistdata slistdata = new Serviceslistdata();
                                        String sname = services.getService_name();
                                        double sprice = services.getService_price();
                                        slistdata.setServname(sname);
                                        slistdata.setServprice(sprice);
                                        slist.add(slistdata);
                                    }
                                        PurchaseInventoryServicesAdapter sadapter = new PurchaseInventoryServicesAdapter(slist);
                                        GridLayoutManager sgridLayoutManager = new GridLayoutManager(getActivity(),6);
                                        sgridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                        recyclerViewitemlist.setLayoutManager(sgridLayoutManager);
                                        recyclerViewitemlist.setItemAnimator(new DefaultItemAnimator());
                                        recyclerViewitemlist.setAdapter(sadapter);
                                        sadapter.notifyDataSetChanged();

                                        invprogress.setVisibility(View.GONE);

                                        if(slist.isEmpty()){
                                            emptylayout.setVisibility(View.VISIBLE);
                                        }else{
                                            emptylayout.setVisibility(View.GONE);
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

    //handles the search menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.searchmenu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search..");

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_chargeitem:
                Intent intent = new Intent(getActivity(), AllPurchaseActivity.class);
                startActivity(intent);

                break;
        }
    }

    public void displayQtyPrice(){

        SharedPreferences shared = getActivity().getSharedPreferences("OwnerPref", MODE_PRIVATE);
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
}
