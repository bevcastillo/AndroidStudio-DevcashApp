package com.example.devcash.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.devcash.ADD_UI.AddEmployeeActivity;
import com.example.devcash.ADD_UI.AddProductActivity;
import com.example.devcash.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeesFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {


    public EmployeesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate the  menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employees, container, false);

        //
        getActivity().setTitle("Employee");

        //add floating action button
        FloatingActionButton emp_fab = view.findViewById(R.id.addemp_fab);
        emp_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                // when add fab is pressed, go to add product activity
                Intent addprod = new Intent(getActivity(), AddEmployeeActivity.class);
                startActivity(addprod);

            }
        });

//        //handles listview
//        ListView lvemployees = view.findViewById(R.id.emplist_listview);
//
//        //set adapter
//        // set click listener
//
//        //show no data found text when listview is empty
//        lvemployees.setEmptyView(view.findViewById(R.id.emptyemployees_face));
//        lvemployees.setEmptyView(view.findViewById(R.id.empty_employees));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
}
