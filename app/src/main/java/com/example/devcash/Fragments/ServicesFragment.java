package com.example.devcash.Fragments;


import android.app.Service;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.devcash.ADD_UI.AddServicesActivity;
import com.example.devcash.CustomAdapters.EmployeesAdapter;
import com.example.devcash.CustomAdapters.ServicesAdapter;
import com.example.devcash.Object.Category;
import com.example.devcash.Object.Employee;
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
public class ServicesFragment extends Fragment implements SearchView.OnQueryTextListener,
        MenuItem.OnActionExpandListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    Toolbar servicesToolbar;
    Spinner statusSpinner;
    String selectedstatus;

    DatabaseReference dbreference;
    DatabaseReference servicesdbreference;
    DatabaseReference ownerdbreference;
    FirebaseDatabase firebaseDatabase;
    ProgressBar servicesprogress;
    LinearLayout emptylayout, noitemlayout;

    EditText searchservice;
    Button searchbtn;

    RecyclerView servrecyclerview;

    List<Serviceslistdata> serviceslist;
    ArrayList<Services> employeeArrayList;

    public ServicesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate the menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_services, container, false);

        servicesToolbar = (Toolbar) view.findViewById(R.id.toolbar_services);
        statusSpinner = (Spinner) view.findViewById(R.id.spinner_servstatus);

        servicesprogress = (ProgressBar) view.findViewById(R.id.services_progressbar);
        emptylayout = (LinearLayout) view.findViewById(R.id.layout_emptyservices);
        noitemlayout = (LinearLayout) view.findViewById(R.id.layout_noitem);

        servrecyclerview = (RecyclerView) view.findViewById(R.id.recycler_servlist);

        searchservice = (EditText) view.findViewById(R.id.servicesearchtext);
        searchbtn = (Button) view.findViewById(R.id.servicesearchbtn);

        searchbtn.setOnClickListener(this);

        statusSpinner.setOnItemSelectedListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbreference = firebaseDatabase.getReference("datadevcash");
        servicesdbreference = firebaseDatabase.getReference("datadevcash/services");
        ownerdbreference = firebaseDatabase.getReference("datadevcash/owner");

        ///
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.custom_spinner_item,
                getResources().getStringArray(R.array.dropdownallstatus));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(myAdapter);

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (statusSpinner.getSelectedItem().toString().equals("All Status")){
                    dispAllStats();
                } else if (statusSpinner.getSelectedItem().toString().equals("Available")){
                    dispAvailable();
                }else if (statusSpinner.getSelectedItem().toString().equals("Not Available")){
                    dispNotAvailable();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        FloatingActionButton services_fab = view.findViewById(R.id.addservices_fab);
        services_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addservices = new Intent(getActivity(), AddServicesActivity.class);
                startActivity(addservices);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Services");
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

    public void dispAllStats(){
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
                                serviceslist = new ArrayList<>();
                                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                    Services services = dataSnapshot1.getValue(Services.class);
                                    Serviceslistdata serviceslistdata = new Serviceslistdata();
                                    String sname = services.getService_name();
                                    double sprice = services.getService_price();
                                    double discountedprice = services.getDiscounted_price();
                                    String sstatus = services.getService_status();
                                    serviceslistdata.setServname(sname);
                                    serviceslistdata.setServprice(sprice);
                                    serviceslistdata.setServstatus(sstatus);
                                    serviceslistdata.setDiscounted_price(discountedprice);
                                    serviceslist.add(serviceslistdata);
                                }

                                ServicesAdapter servicesAdapter = new ServicesAdapter(serviceslist);
                                RecyclerView.LayoutManager sLayoutManager = new LinearLayoutManager(getActivity());
                                servrecyclerview.setLayoutManager(sLayoutManager);
                                servrecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
                                servrecyclerview.setItemAnimator(new DefaultItemAnimator());
                                servrecyclerview.setAdapter(servicesAdapter);
                                servicesAdapter.notifyDataSetChanged();

                                servicesprogress.setVisibility(View.GONE);

                                if(serviceslist.isEmpty()){
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
                        String acctkey = dataSnapshot1.getKey();

                        ownerdbreference.child(acctkey+"/business/services").orderByChild("service_status").equalTo("Available").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        serviceslist = new ArrayList<>();
                                        Services services = dataSnapshot2.getValue(Services.class);
                                        Serviceslistdata serviceslistdata = new Serviceslistdata();
                                        String sname = services.getService_name();
                                        double sprice = services.getService_price();
                                        double discPrice = services.getDiscounted_price();
                                        String sstatus = services.getService_status();
                                        serviceslistdata.setServname(sname);
                                        serviceslistdata.setServprice(sprice);
                                        serviceslistdata.setServstatus(sstatus);
                                        serviceslistdata.setDiscounted_price(discPrice);
                                        serviceslist.add(serviceslistdata);
                                    }
                                    ServicesAdapter servicesAdapter = new ServicesAdapter(serviceslist);
                                    RecyclerView.LayoutManager sLayoutManager = new LinearLayoutManager(getActivity());
                                    servrecyclerview.setLayoutManager(sLayoutManager);
                                    servrecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
                                    servrecyclerview.setItemAnimator(new DefaultItemAnimator());
                                    servrecyclerview.setAdapter(servicesAdapter);
                                    servicesAdapter.notifyDataSetChanged();

                                    servicesprogress.setVisibility(View.GONE);

                                    if(serviceslist.isEmpty()){
                                        noitemlayout.setVisibility(View.VISIBLE);
                                    }else{
                                        noitemlayout.setVisibility(View.GONE);
                                    }
                                }else{
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
                        String acctkey = dataSnapshot1.getKey();

                        ownerdbreference.child(acctkey+"/business/services").orderByChild("service_status").equalTo("Not Available").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        serviceslist = new ArrayList<>();
                                        Services services = dataSnapshot2.getValue(Services.class);
                                        Serviceslistdata serviceslistdata = new Serviceslistdata();
                                        String sname = services.getService_name();
                                        double sprice = services.getService_price();
                                        String sstatus = services.getService_status();
                                        serviceslistdata.setServname(sname);
                                        serviceslistdata.setServprice(sprice);
                                        serviceslistdata.setServstatus(sstatus);
                                        serviceslist.add(serviceslistdata);
                                    }
                                    ServicesAdapter servicesAdapter = new ServicesAdapter(serviceslist);
                                    RecyclerView.LayoutManager sLayoutManager = new LinearLayoutManager(getActivity());
                                    servrecyclerview.setLayoutManager(sLayoutManager);
                                    servrecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
                                    servrecyclerview.setItemAnimator(new DefaultItemAnimator());
                                    servrecyclerview.setAdapter(servicesAdapter);
                                    servicesAdapter.notifyDataSetChanged();

                                    servicesprogress.setVisibility(View.GONE);

                                    if(serviceslist.isEmpty()){
                                        noitemlayout.setVisibility(View.VISIBLE);
                                    }else{
                                        noitemlayout.setVisibility(View.GONE);
                                    }
                                }else{
                                    noitemlayout.setVisibility(View.VISIBLE);
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
            case R.id.spinner_servstatus:
                selectedstatus = statusSpinner.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.servicesearchbtn:
                searchForServices();
                break;
        }
    }

    private void searchForServices() {
        final String inputText = searchservice.getText().toString();

        SharedPreferences shared = getActivity().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        String ownerKey = dataSnapshot1.getKey();

                        ownerdbreference.child(ownerKey+"/business/services").orderByChild("service_name").equalTo(inputText).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        serviceslist = new ArrayList<>();
                                        Services services = dataSnapshot2.getValue(Services.class);
                                        Serviceslistdata serviceslistdata = new Serviceslistdata();
                                        String sname = services.getService_name();
                                        double sprice = services.getService_price();
                                        double discountedprice = services.getDiscounted_price();
                                        String sstatus = services.getService_status();
                                        serviceslistdata.setServname(sname);
                                        serviceslistdata.setServprice(sprice);
                                        serviceslistdata.setServstatus(sstatus);
                                        serviceslistdata.setDiscounted_price(discountedprice);
                                        serviceslist.add(serviceslistdata);
                                    }
                                    ServicesAdapter servicesAdapter = new ServicesAdapter(serviceslist);
                                    RecyclerView.LayoutManager sLayoutManager = new LinearLayoutManager(getActivity());
                                    servrecyclerview.setLayoutManager(sLayoutManager);
                                    servrecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
                                    servrecyclerview.setItemAnimator(new DefaultItemAnimator());
                                    servrecyclerview.setAdapter(servicesAdapter);
                                    servicesAdapter.notifyDataSetChanged();

                                    servicesprogress.setVisibility(View.GONE);

                                    if(serviceslist.isEmpty()){
                                        noitemlayout.setVisibility(View.VISIBLE);
                                    }else{
                                        noitemlayout.setVisibility(View.GONE);
                                    }
                                }else {
                                    noitemlayout.setVisibility(View.VISIBLE);
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
