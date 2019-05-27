package com.example.devcash;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.devcash.CustomAdapters.InventoryListAdapter;
import com.example.devcash.Fragments.CategoriesFragment;
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
        View view = inflater.inflate(R.layout.fragment_inventory_list, container, false);

        //handles listview
        lvinventory = (ListView) view.findViewById(R.id.inventorylist_listview);
        adapter = new InventoryListAdapter(getActivity(),list);

        //populate the list
        list.add(new InventoryList(R.drawable.ic_product,"Products"));
        list.add(new InventoryList(R.drawable.ic_services,"Services"));
        list.add(new InventoryList(R.drawable.ic_category, "Categories"));
        list.add(new InventoryList(R.drawable.ic_local_offer,"Discounts"));

        //delegate the adapter
        lvinventory.setAdapter(adapter);

        //add listeners
        lvinventory.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        InventoryList selectedList = this.list.get(position);

        int icon = selectedList.getIcon();
        String title = selectedList.getInventory_title();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (position){
            case 0:
                Toast.makeText(getActivity(), "You have clicked product list", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(getActivity(), "You have clicked services list", Toast.LENGTH_SHORT).show();
                break;
            case  2:
                CategoriesFragment categoriesFragment = new CategoriesFragment();
                fragmentTransaction.add(R.id.inventorylist_fragmentcontainer, categoriesFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case 3:
                Toast.makeText(getActivity(), "You have clicked discount list", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
