package com.example.devcash.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.devcash.ADD_UI.AddCategoryActivity;
import com.example.devcash.CustomAdapters.CategoryAdapter;
import com.example.devcash.Database.DatabaseHelper;
import com.example.devcash.Lists.CategoryList;
import com.example.devcash.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    DatabaseHelper db;
    CategoryAdapter categoryAdapter;
    SwipeRefreshLayout pullToRefresh;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

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

        pullToRefresh = (SwipeRefreshLayout) getActivity().findViewById(R.id.category_pulltorefresh);

        //handles listview
        ListView lvcategories = (ListView) getActivity().findViewById(R.id.categorylist_listview);
        db = new DatabaseHelper(getActivity());

        ArrayList<CategoryList> categoryListArrayList = new ArrayList<CategoryList>();
        categoryListArrayList = db.getAllCategory();
        categoryAdapter = new CategoryAdapter(getActivity(), categoryListArrayList);

        //show no data found text when listview is empty
        lvcategories.setEmptyView(view.findViewById(R.id.empty_category));

        lvcategories.setAdapter(categoryAdapter);

        //setting a setOnRefreshListener on the SwipeDownLayout
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(true);

            }
        });

///////////
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

}
