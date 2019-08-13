package com.example.devcash.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
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
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.devcash.ADD_UI.AddDiscountActivity;
import com.example.devcash.CustomAdapters.DiscountAdapter;
import com.example.devcash.Object.Discount;
import com.example.devcash.Object.Discountlistdata;
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
public class DiscountsFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {
    private Context context;

    Toolbar discountToolbar;
    Spinner discountSpinner;

    //
    DatabaseReference dbreference;
    DatabaseReference discountdbreference;
    DatabaseReference ownerdbreference;
    FirebaseDatabase firebaseDatabase;

    RecyclerView discountrecycler;
    List<Discountlistdata> list;

    ArrayList<Discount> discountArrayList;


    public DiscountsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        setHasOptionsMenu(true);
//        populateList();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discounts, container, false);

        discountrecycler = (RecyclerView) view.findViewById(R.id.recycler_discount);
        discountToolbar = (Toolbar) view.findViewById(R.id.toolbar_discounts);
        discountSpinner = (Spinner) view.findViewById(R.id.spinner_discounts);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbreference = firebaseDatabase.getReference("/datadevcash");
        discountdbreference = firebaseDatabase.getReference("datadevcash/discount");
        ownerdbreference = firebaseDatabase.getReference("datadevcash/owner");

        SharedPreferences shared = getActivity().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        String key = ds.getKey();
                        dbreference.child("owner/"+key+"/business/discount").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               list = new ArrayList<>();
                               for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                   Discount discount = dataSnapshot1.getValue(Discount.class);
                                   Discountlistdata listdata = new Discountlistdata();
                                   String dcode = discount.getDisc_code();
                                   String dstart = discount.getDisc_start();
                                   String dend = discount.getDisc_end();
                                   String dstatus = discount.getDisc_status();
                                   double dvalue = discount.getDisc_value();
                                   listdata.setDisc_code(dcode);
                                   listdata.setDisc_start(dstart);
                                   listdata.setDisc_end(dend);
                                   listdata.setDisc_status(dstatus);
                                   listdata.setDisc_value(dvalue);
                                   list.add(listdata);
                               }
                                    DiscountAdapter discountAdapter = new DiscountAdapter(list);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                    discountrecycler.setLayoutManager(layoutManager);
                                    discountrecycler.setItemAnimator(new DefaultItemAnimator());
                                    discountrecycler.setAdapter(discountAdapter);
                                    discountAdapter.notifyDataSetChanged();
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

//        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()){
//                    for(DataSnapshot ds: dataSnapshot.getChildren()){
//                        String key = ds.getKey();
//                        dbreference.child("owner"+key+"business/discount").addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                list = new ArrayList<>();
//                                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
//                                Discount discount = dataSnapshot1.getValue(Discount.class);
//                                Discountlistdata listdata = new Discountlistdata();
//                                String dcode = discount.getDisc_code();
//                                String type = discount.getDisc_type();
//                                double value = discount.getDisc_value();
//                                String start = discount.getDisc_start();
//                                String end = discount.getDisc_end();
//                                String status = discount.getDisc_status();
//                                listdata.setDisc_code(dcode);
//                                listdata.setDisc_type(type);
//                                listdata.setDisc_value(value);
//                                listdata.setDisc_start(start);
//                                listdata.setDisc_end(end);
//                                listdata.setDisc_status(status);
//                                list.add(listdata);
//                            }
//                                DiscountAdapter discountAdapter = new DiscountAdapter(list);
//                                RecyclerView.LayoutManager dlayoutManager = new LinearLayoutManager(getActivity());
//                                discountrecycler.setLayoutManager(dlayoutManager);
//                                discountrecycler.setItemAnimator(new DefaultItemAnimator());
//                                discountrecycler.setAdapter(discountAdapter);
//                                discountAdapter.notifyDataSetChanged();
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

//        discountdbreference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                list = new ArrayList<>();
//                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
//                    Discount discount = dataSnapshot1.getValue(Discount.class);
//                    Discountlistdata listdata = new Discountlistdata();
//                    String dcode = discount.getDisc_code();
//                    String type = discount.getDisc_type();
//                    double value = discount.getDisc_value();
//                    String start = discount.getDisc_start();
//                    String end = discount.getDisc_end();
//                    String status = discount.getDisc_status();
//                    listdata.setDisc_code(dcode);
//                    listdata.setDisc_type(type);
//                    listdata.setDisc_value(value);
//                    listdata.setDisc_start(start);
//                    listdata.setDisc_end(end);
//                    listdata.setDisc_status(status);
//                    list.add(listdata);
//                }
//                DiscountAdapter discountAdapter = new DiscountAdapter(list);
//                RecyclerView.LayoutManager dlayoutManager = new LinearLayoutManager(getActivity());
//                discountrecycler.setLayoutManager(dlayoutManager);
//                discountrecycler.setItemAnimator(new DefaultItemAnimator());
//                discountrecycler.setAdapter(discountAdapter);
//                discountAdapter.notifyDataSetChanged();
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
                getResources().getStringArray(R.array.dropdownalldiscounts));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        discountSpinner.setAdapter(myAdapter);

        discountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),
                        discountSpinner.getSelectedItem().toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //add floating action button
        FloatingActionButton disc_fab = view.findViewById(R.id.adddiscounts_fab);
        disc_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // when add fab is pressed, go to add product activity
                Intent addprod = new Intent(getActivity(), AddDiscountActivity.class);
                startActivity(addprod);
            }
        });

        //handles listview
//        ListView lvdiscounts = view.findViewById(R.id.discountlist_listview);

        //set adapter
        // set click listener

//        //show no data found text when listview is empty
//        lvdiscounts.setEmptyView(view.findViewById(R.id.emptydiscount_face));
//        lvdiscounts.setEmptyView(view.findViewById(R.id.empty_discount));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Discounts");
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
        return true;
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
}
