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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

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
    ListView lv;
    ArrayList<CategoryList> categoryListArrayList = new ArrayList<CategoryList>();
    CategoryAdapter categoryAdapter;

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

        //handles listview



//        //show no data found text when listview is empty
//        lvcategories.setEmptyView(view.findViewById(R.id.emptycategory_face));
//        lvcategories.setEmptyView(view.findViewById(R.id.empty_category));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        ListView lvcategories = (ListView) getView().findViewById(R.id.categorylist_listview);
        db = new DatabaseHelper(getActivity());


        lvcategories.setAdapter(categoryAdapter);

        categoryAdapter = new CategoryAdapter(getActivity(), categoryListArrayList);
        categoryListArrayList = db.getAllCategory();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

    //    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.refundsettingsmenu, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()){
//            case R.id.action_refund:
//                Toast.makeText(getActivity(),"You have pressed refund menu.", Toast.LENGTH_SHORT).show();
//            case R.id.send_email:
//                Toast.makeText(getActivity(), "You have pressed send via email menu.", Toast.LENGTH_SHORT).show();
//            case R.id.send_text:
//                Toast.makeText(getActivity(), "You have pressed send via SMS", Toast.LENGTH_SHORT).show();
//        }
//
//        return false;
//    }
}
