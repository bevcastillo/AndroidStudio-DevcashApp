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
        MenuItem.OnActionExpandListener {

    Toolbar servicesToolbar;
    Spinner servicesSpinner;

    DatabaseReference dbreference;
    DatabaseReference servicesdbreference;
    DatabaseReference businessownerdbreference;
    FirebaseDatabase firebaseDatabase;
    ProgressBar servicesprogress;
    LinearLayout emptylayout;

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
        servicesSpinner = (Spinner) view.findViewById(R.id.spinner_services);

        servicesprogress = (ProgressBar) view.findViewById(R.id.services_progressbar);
        emptylayout = (LinearLayout) view.findViewById(R.id.layout_emptyservices);

        servrecyclerview = (RecyclerView) view.findViewById(R.id.recycler_servlist);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbreference = firebaseDatabase.getReference("datadevcash");
        servicesdbreference = firebaseDatabase.getReference("datadevcash/services");
        businessownerdbreference = firebaseDatabase.getReference("datadevcash/owner");

        SharedPreferences shared = getActivity().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));


        businessownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
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


//        servicesdbreference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                serviceslist = new ArrayList<>();
//                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
//                    Services services = dataSnapshot1.getValue(Services.class);
//                    Serviceslistdata serviceslistdata = new Serviceslistdata();
//                    String sname = services.getService_name();
//                    double sprice = services.getService_price();
//                    String sstatus = services.getService_status();
//                    serviceslistdata.setServname(sname);
//                    serviceslistdata.setServprice(sprice);
//                    serviceslistdata.setServstatus(sstatus);
//                    serviceslist.add(serviceslistdata);
//                }
//
//                ServicesAdapter servicesAdapter = new ServicesAdapter(serviceslist);
//                RecyclerView.LayoutManager sLayoutManager = new LinearLayoutManager(getActivity());
//                servrecyclerview.setLayoutManager(sLayoutManager);
//                servrecyclerview.setItemAnimator(new DefaultItemAnimator());
//                servrecyclerview.setAdapter(servicesAdapter);
//                servicesAdapter.notifyDataSetChanged();
//
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
                getResources().getStringArray(R.array.dropdownallservices));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servicesSpinner.setAdapter(myAdapter);

        servicesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),
                        servicesSpinner.getSelectedItem().toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //add floating action button
        FloatingActionButton services_fab = view.findViewById(R.id.addservices_fab);
        services_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // when add fab is pressed, go to add product activity
                Intent addservices = new Intent(getActivity(), AddServicesActivity.class);
                startActivity(addservices);
            }
        });
        //set adapter
        // set click listener

        //show no data found text when listview is empty
//        lvservices.setEmptyView(view.findViewById(R.id.emptyservices_face));
//        lvservices.setEmptyView(view.findViewById(R.id.empty_services));

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
}
