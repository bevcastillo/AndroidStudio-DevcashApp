package com.example.devcash;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.devcash.CustomAdapters.InventoryListAdapter;
import com.example.devcash.Lists.InventoryList;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class InventoryListFragment extends Fragment implements AdapterView.OnItemClickListener {

    ListView lvinventory;
    //data container
    ArrayList<InventoryList> list = new ArrayList<InventoryList>();
    //adapter
    InventoryListAdapter adapter;


    public InventoryListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inventory_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //handles listview
        lvinventory = (ListView) getActivity().findViewById(R.id.inventorylist_listview);
        adapter = new InventoryListAdapter(getActivity(),list);

        //populate the list
        list.add(new InventoryList(R.drawable.ic_product,"Products"));
        list.add(new InventoryList(R.drawable.ic_services,"Services"));
        list.add(new InventoryList(R.drawable.ic_category, "Categories"));
        list.add(new InventoryList(R.drawable.ic_tag,"Discounts"));

        //delegate the adapter
        lvinventory.setAdapter(adapter);

        //add listener to the listview
        lvinventory.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
