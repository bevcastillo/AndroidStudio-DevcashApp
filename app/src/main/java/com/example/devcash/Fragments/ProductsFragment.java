package com.example.devcash.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.ADD_UI.AddProductActivity;
import com.example.devcash.CustomAdapters.ProductsAdapter;
import com.example.devcash.CustomAdapters.PurchaseInventoryProductsAdapter;
import com.example.devcash.Object.Product;
import com.example.devcash.Object.Productlistdata;
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
public class ProductsFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener, AdapterView.OnItemSelectedListener {

    DatabaseReference dbreference;
    DatabaseReference productsfirebaseDatabase;
    DatabaseReference ownerdbreference;
    FirebaseDatabase firebaseDatabase;

    RecyclerView prodrecyclerview;
    List<Productlistdata> list;
    ArrayList<Product> productArrayList;
    TextView emptylist;
    LinearLayout emptylayout;

    Toolbar productsToolbar;
    Spinner productsSpinner, conditionSpinner;
    ProgressBar progressBar;

    String selectedstatus, selectedcondition;

    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate the action menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        productsToolbar = (Toolbar) view.findViewById(R.id.toolbar_products);
        productsSpinner = (Spinner) view.findViewById(R.id.spinner_products);
        conditionSpinner = (Spinner) view.findViewById(R.id.spinner_condition);

        emptylist = (TextView) view.findViewById(R.id.emptylist);
        progressBar = (ProgressBar) view.findViewById(R.id.product_progressbar);

        prodrecyclerview = (RecyclerView) view.findViewById(R.id.recycler_prodlist);

        emptylayout = (LinearLayout) view.findViewById(R.id.layout_emptyprod);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbreference = firebaseDatabase.getReference("/datadevcash");
        productsfirebaseDatabase = firebaseDatabase.getReference("/datadevcash/products");
        ownerdbreference = firebaseDatabase.getReference("/datadevcash/owner");

        productsSpinner.setOnItemSelectedListener(this);


//        SharedPreferences shared = getActivity().getSharedPreferences("OwnerPref", MODE_PRIVATE);
//        final String username = (shared.getString("owner_username", ""));
//
//        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()){
//                   for(DataSnapshot ds: dataSnapshot.getChildren()){
//                        String key = ds.getKey();
//                       dbreference.child("owner/"+key+"/business/product").addValueEventListener(new ValueEventListener() {
//                           @Override
//                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                list = new ArrayList<>();
//                                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
//                                    Product product = dataSnapshot1.getValue(Product.class);
//                                    Productlistdata listdata = new Productlistdata();
//                                    String pname = product.getProd_name();
//                                    double prop = product.getProd_rop();
//                                    double pprice = product.getProd_price();
//                                    int pstock = product.getProd_stock();
//                                    String pexpdate = product.getProd_expdate();
//                                    int pexpdatecount = product.getProd_expdatecount();
//                                    String condname = product.getProductCondition().getCond_name();
//                                    String pstatus = product.getProd_status();
//                                    listdata.setProd_name(pname);
//                                    listdata.setProd_rop(prop);
//                                    listdata.setProd_status(pstatus);
//                                    listdata.setProd_price(pprice);
//                                    listdata.setProd_stock(pstock);
//                                    listdata.setCond_name(condname);
//                                    listdata.setProd_expdate(pexpdate);
//                                    listdata.setProd_expdatecount(pexpdatecount);
//                                    list.add(listdata);
//
//                                }
//                                ProductsAdapter adapter = new ProductsAdapter(list);
//                                RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getActivity());
//                                prodrecyclerview.setLayoutManager(pLayoutManager);
//                                prodrecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
//                                prodrecyclerview.setItemAnimator(new DefaultItemAnimator());
//                                prodrecyclerview.setAdapter(adapter);
//                                adapter.notifyDataSetChanged();
//                                progressBar.setVisibility(View.GONE);
//
//                               if (list.isEmpty()){
//                                   emptylayout.setVisibility(View.VISIBLE);
//                               }else{
//                                   emptylayout.setVisibility(View.GONE);
//                               }
//
//                           }
//
//                           @Override
//                           public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                           }
//                       });
//                   }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        ///
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.custom_spinner_item,
                getResources().getStringArray(R.array.dropdownallstatus));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productsSpinner.setAdapter(myAdapter);

        productsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedstatus = productsSpinner.getItemAtPosition(position).toString();
                if (productsSpinner.getSelectedItem().equals("All Status")){
                    dispAllStatus();
                }else if (productsSpinner.getSelectedItem().equals("Available")){
                    dispAvailable();
                }else if(productsSpinner.getSelectedItem().equals("Not Available")){
                    dispNotAvailable();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ///
        ArrayAdapter<String> conditionAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.custom_spinner_item,
                getResources().getStringArray(R.array.dropdownallcondition));
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionSpinner.setAdapter(conditionAdapter);

        conditionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(),
//                        conditionSpinner.getSelectedItem().toString(),
//                        Toast.LENGTH_SHORT).show();
                selectedcondition = conditionSpinner.getItemAtPosition(position).toString();
                if (conditionSpinner.getSelectedItem().equals("All Condition")){

                }else if (conditionSpinner.getSelectedItem().equals("New")){
                    dispNew();
                }else if (conditionSpinner.getSelectedItem().equals("Lost")){

                }else if (conditionSpinner.getSelectedItem().equals("Disposed of")){

                }else if(conditionSpinner.getSelectedItem().equals("Damaged")){

                }else{

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //add floating action button
        FloatingActionButton prod_fab = view.findViewById(R.id.addprod_fab);
        prod_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // when add fab is pressed, go to add product activity
                Intent addprod = new Intent(getActivity(), AddProductActivity.class);
                startActivity(addprod);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Products");
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

    public void dispAllStatus(){
        SharedPreferences shared = getActivity().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

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
                                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                    Product product = dataSnapshot1.getValue(Product.class);
                                    Productlistdata listdata = new Productlistdata();
                                    String pname = product.getProd_name();
                                    double prop = product.getProd_rop();
                                    double pprice = product.getProd_price();
                                    double discprice = product.getDiscounted_price();
                                    int pstock = product.getProd_stock();
                                    String pexpdate = product.getProd_expdate();
                                    int pexpdatecount = product.getProd_expdatecount();
                                    String condname = product.getProductCondition().getCond_name();
                                    String pstatus = product.getProd_status();
                                    listdata.setProd_name(pname);
                                    listdata.setProd_rop(prop);
                                    listdata.setProd_status(pstatus);
                                    listdata.setProd_price(pprice);
                                    listdata.setProd_stock(pstock);
                                    listdata.setCond_name(condname);
                                    listdata.setProd_expdate(pexpdate);
                                    listdata.setDiscounted_price(discprice);
                                    listdata.setProd_expdatecount(pexpdatecount);
                                    list.add(listdata);

                                }
                                ProductsAdapter adapter = new ProductsAdapter(list);
                                RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getActivity());
                                prodrecyclerview.setLayoutManager(pLayoutManager);
                                prodrecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
                                prodrecyclerview.setItemAnimator(new DefaultItemAnimator());
                                prodrecyclerview.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);

                                if (list.isEmpty()){
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

    public void dispAvailable(){
        SharedPreferences shared = getActivity().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        String ownerkey = dataSnapshot1.getKey();

                        ownerdbreference.child(ownerkey+"/business/product").orderByChild("prod_status").equalTo("Available").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                list = new ArrayList<>();

                                if (dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
//                                        list = new ArrayList<>();
                                        Product product = dataSnapshot2.getValue(Product.class);
                                        Productlistdata listdata = new Productlistdata();
                                        String pname = product.getProd_name();
                                        double prop = product.getProd_rop();
                                        double pprice = product.getProd_price();
                                        int pstock = product.getProd_stock();
                                        String pexpdate = product.getProd_expdate();
                                        int pexpdatecount = product.getProd_expdatecount();
                                        String condname = product.getProductCondition().getCond_name();
                                        String pstatus = product.getProd_status();
                                        listdata.setProd_name(pname);
                                        listdata.setProd_rop(prop);
                                        listdata.setProd_status(pstatus);
                                        listdata.setProd_price(pprice);
                                        listdata.setProd_stock(pstock);
                                        listdata.setCond_name(condname);
                                        listdata.setProd_expdate(pexpdate);
                                        listdata.setProd_expdatecount(pexpdatecount);
                                        list.add(listdata);
                                    }
                                    ProductsAdapter adapter = new ProductsAdapter(list);
                                    RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getActivity());
                                    prodrecyclerview.setLayoutManager(pLayoutManager);
                                    prodrecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
                                    prodrecyclerview.setItemAnimator(new DefaultItemAnimator());
                                    prodrecyclerview.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    progressBar.setVisibility(View.GONE);

                                    if (list.isEmpty()){
//                                        emptylayout.setVisibility(View.VISIBLE);
                                        Toast.makeText(getActivity(), "No items found", Toast.LENGTH_SHORT).show();
                                    }else{
                                        emptylayout.setVisibility(View.GONE);
                                    }

                                } else{
                                    Toast.makeText(getActivity(), "there are no available items", Toast.LENGTH_SHORT).show();
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

    public void dispNotAvailable(){
        SharedPreferences shared = getActivity().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        String ownerkey = dataSnapshot1.getKey();

                        ownerdbreference.child(ownerkey+"/business/product").orderByChild("prod_status").equalTo("Not Available").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                list = new ArrayList<>();

                                if (dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
//                                        list = new ArrayList<>();
                                        Product product = dataSnapshot2.getValue(Product.class);
                                        Productlistdata listdata = new Productlistdata();
                                        String pname = product.getProd_name();
                                        double prop = product.getProd_rop();
                                        double pprice = product.getProd_price();
                                        int pstock = product.getProd_stock();
                                        String pexpdate = product.getProd_expdate();
                                        int pexpdatecount = product.getProd_expdatecount();
                                        String condname = product.getProductCondition().getCond_name();
                                        String pstatus = product.getProd_status();
                                        listdata.setProd_name(pname);
                                        listdata.setProd_rop(prop);
                                        listdata.setProd_status(pstatus);
                                        listdata.setProd_price(pprice);
                                        listdata.setProd_stock(pstock);
                                        listdata.setCond_name(condname);
                                        listdata.setProd_expdate(pexpdate);
                                        listdata.setProd_expdatecount(pexpdatecount);
                                        list.add(listdata);
                                    }
                                    ProductsAdapter adapter = new ProductsAdapter(list);
                                    RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getActivity());
                                    prodrecyclerview.setLayoutManager(pLayoutManager);
                                    prodrecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
                                    prodrecyclerview.setItemAnimator(new DefaultItemAnimator());
                                    prodrecyclerview.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    progressBar.setVisibility(View.GONE);

                                    if (list.isEmpty()){
//                                        emptylayout.setVisibility(View.VISIBLE);
                                        Toast.makeText(getActivity(), "No items found", Toast.LENGTH_SHORT).show();
                                    }else{
                                        emptylayout.setVisibility(View.GONE);
                                    }

                                } else{
                                    Toast.makeText(getActivity(), "there are no available items", Toast.LENGTH_SHORT).show();
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



    public void dispNew(){
        SharedPreferences shared = getActivity().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        String acctkey = dataSnapshot1.getKey();

                        ownerdbreference.child(acctkey+"/business/product").orderByChild("productCondition/cond_name").equalTo("New").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        Product product = dataSnapshot2.getValue(Product.class);
                                        Productlistdata listdata = new Productlistdata();
                                        String pname = product.getProd_name();
                                        double prop = product.getProd_rop();
                                        double pprice = product.getProd_price();
                                        int pstock = product.getProd_stock();
                                        String pexpdate = product.getProd_expdate();
                                        int pexpdatecount = product.getProd_expdatecount();
                                        String condname = product.getProductCondition().getCond_name();
                                        String pstatus = product.getProd_status();
                                        listdata.setProd_name(pname);
                                        listdata.setProd_rop(prop);
                                        listdata.setProd_status(pstatus);
                                        listdata.setProd_price(pprice);
                                        listdata.setProd_stock(pstock);
                                        listdata.setCond_name(condname);
                                        listdata.setProd_expdate(pexpdate);
                                        listdata.setProd_expdatecount(pexpdatecount);
                                        list.add(listdata);
                                    }
                                    ProductsAdapter adapter = new ProductsAdapter(list);
                                    RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getActivity());
                                    prodrecyclerview.setLayoutManager(pLayoutManager);
                                    prodrecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
                                    prodrecyclerview.setItemAnimator(new DefaultItemAnimator());
                                    prodrecyclerview.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    progressBar.setVisibility(View.GONE);

                                    if (list.isEmpty()){
//                                        emptylayout.setVisibility(View.VISIBLE);
                                        Toast.makeText(getActivity(), "No items found", Toast.LENGTH_SHORT).show();
                                    }else{
                                        emptylayout.setVisibility(View.GONE);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int sid = parent.getId();

        switch (sid){
            case R.id.spinner_products:
                selectedstatus = productsSpinner.getItemAtPosition(position).toString();
                break;
            case R.id.spinner_condition:
                selectedcondition = conditionSpinner.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
