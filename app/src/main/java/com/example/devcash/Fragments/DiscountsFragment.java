package com.example.devcash.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.devcash.ADD_UI.AddDiscountActivity;
import com.example.devcash.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiscountsFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    List<String> allValues;
    private ArrayAdapter<String> adapter;
    private Context context;

    Toolbar discountToolbar;
    Spinner discountSpinner;


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

        discountToolbar = (Toolbar) view.findViewById(R.id.toolbar_discounts);
        discountSpinner = (Spinner) view.findViewById(R.id.spinner_discounts);

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
        if(newText == null || newText.trim().isEmpty()){
            resetSearch();
            return false;
        }

        List<String> filteredValues = new ArrayList<String>(allValues);
        for(String value : allValues){
            if(!value.toLowerCase().contains(newText.toLowerCase())){
                filteredValues.remove(value);
            }
        }

        adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, filteredValues);
//        setListAdapter(adapter);

        return false;
    }

    public void resetSearch(){
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, allValues);
//        setListAdapter(adapter);
    }


    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

//    private void populateList(){
//        allValues = new ArrayList<>();
//
//        allValues.add("Sample search result 1");
//        allValues.add("Sample search result 2");
//        allValues.add("Sample search result 3");
//        allValues.add("Sample search result 4");
//        allValues.add("Sample search result 5");
//        allValues.add("Sample search result 6");
//
//        adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, allValues);
////        setListAdapter(adapter);
//    }

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
