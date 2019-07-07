package com.example.devcash.Fragments;


import android.os.Bundle;
import android.support.annotation.LongDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.LoginFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.devcash.CustomAdapters.InventoryListAdapter;
import com.example.devcash.InventoryRecyclerViewAdapter;
import com.example.devcash.InventoryRecyclerViewDataList;
import com.example.devcash.Model.InventoryList;
import com.example.devcash.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.security.PublicKey;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class InventoryListFragment extends Fragment implements InventoryRecyclerViewAdapter.onInventoryListener{

    private static final String TAG = "InventoryListFragment";
    //variables
    private ArrayList<Integer> mIcon = new ArrayList<>();
    private ArrayList<String> mLabel = new ArrayList<>();

//        implements InventoryRecyclerViewAdapter.OnInventoryListener{

//    ListView lvinventory;
//    ArrayList<InventoryList> list = new ArrayList<InventoryList>();
//    InventoryListAdapter adapter;

    public InventoryListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inventory_list, container, false);


        Log.d(TAG, "onCreateView: started");
        initImageBitmaps(view);
////
//
//        InventoryRecyclerViewAdapter adapter = new InventoryRecyclerViewAdapter();
//        recyclerView.setAdapter(adapter);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    private void initImageBitmaps(View view){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps");

        mIcon.add(R.drawable.ic_product);
        mLabel.add("Products");

        mIcon.add(R.drawable.ic_customer);
        mLabel.add("Services");

        mIcon.add(R.drawable.ic_category);
        mLabel.add("Categories");

        mIcon.add(R.drawable.ic_tag);
        mLabel.add("Discounts");

        initRecyclerView(view);

    }

    private void initRecyclerView(View view){
//        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView recyclerView = view.findViewById(R.id.inventorylist_recycleview);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .build()); //adding a divider into the recyclerview list

        InventoryRecyclerViewAdapter adapter = new InventoryRecyclerViewAdapter(getActivity(), mIcon, mLabel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    //method created from OnInventoryListener from InventoryListFragment.java
    //handles the onclick for the recycleview items
    @Override
    public void onInventoryClick(int position) {
//        mLabel.get(position); //get the position of the clicked item

    }
}
