package com.example.devcash.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.example.devcash.ADD_UI.AddCategoryActivity;
import com.example.devcash.CustomAdapters.CategoryAdapter;
import com.example.devcash.Object.Category;
import com.example.devcash.Object.Categorylistdata;
import com.example.devcash.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    SwipeRefreshLayout pullToRefresh;

    Toolbar categoriesToolbar;
    Spinner categoriesSpinner;
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;

    //
    DatabaseReference dbreference;
    DatabaseReference categoryfirebaseDatabase;
    FirebaseDatabase firebaseDatabase;

    RecyclerView categoryrecyclerView;
    List<Categorylistdata> list;

    ArrayList<Category> categoryArrayList;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        categoryrecyclerView = (RecyclerView) view.findViewById(R.id.catrecyclerview);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbreference = firebaseDatabase.getReference("/datadevcash");
        categoryfirebaseDatabase = firebaseDatabase.getReference("/datadevcash/category");

        categoryfirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Category category = dataSnapshot1.getValue(Category.class);
                    Categorylistdata listdata = new Categorylistdata();
                    String catname = category.getCategory_name();
                    listdata.setCategory_name(catname);
                    list.add(listdata);
                }

                CategoryAdapter adapter = new CategoryAdapter(list);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                categoryrecyclerView.setLayoutManager(layoutManager);
                categoryrecyclerView.setItemAnimator(new DefaultItemAnimator());
                categoryrecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Something is wrong, please try that again.", Toast.LENGTH_SHORT).show();
            }
        });
        //
//        categoriesToolbar = (Toolbar) view.findViewById(R.id.toolbar_categories);
        categoriesSpinner = (Spinner) view.findViewById(R.id.spinner_categories);

        ///
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.custom_spinner_item,
                getResources().getStringArray(R.array.dropdownitempurchase));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(myAdapter);

        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),
                        categoriesSpinner.getSelectedItem().toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //add floating action button
        FloatingActionButton categories_fab = view.findViewById(R.id.addcategories_fab);
        categories_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // when add fab is pressed, go to add product activity
                Intent addcategory = new Intent(getActivity(), AddCategoryActivity.class);
                startActivity(addcategory);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        pullToRefresh = (SwipeRefreshLayout) getActivity().findViewById(R.id.category_pulltorefresh);
//        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//
//            @Override
//            public void onRefresh() {
//                categoryAdapter.notifyDataSetChanged();
//                pullToRefresh.setRefreshing(false);
//            }
//        });

////////
        getActivity().setTitle("Categories");
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
