package com.example.devcash.Fragments;


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
import com.example.devcash.Fragments.DiscountsFragment;
import com.example.devcash.Fragments.ProductsFragment;
import com.example.devcash.Fragments.ServicesFragment;
import com.example.devcash.Lists.InventoryList;
import com.example.devcash.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class InventoryListFragment extends Fragment implements AdapterView.OnItemClickListener {

    ListView lvinventory;
    ArrayList<InventoryList> list = new ArrayList<InventoryList>();
    InventoryListAdapter adapter;


    public InventoryListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inventory_list, container, false);

        lvinventory = (ListView) view.findViewById(R.id.inventorylist_listview);
        adapter = new InventoryListAdapter(getActivity(),list);

        list.add(new InventoryList(R.drawable.ic_product,"Products"));
        list.add(new InventoryList(R.drawable.ic_services,"Services"));
        list.add(new InventoryList(R.drawable.ic_category, "Categories"));
        list.add(new InventoryList(R.drawable.ic_local_offer,"Discounts"));

        lvinventory.setAdapter(adapter);
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
                ProductsFragment productsFragment = new ProductsFragment();
                fragmentTransaction.add(R.id.inventorylist_fragmentcontainer, productsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case 1:
                ServicesFragment servicesFragment = new ServicesFragment();
                fragmentTransaction.add(R.id.inventorylist_fragmentcontainer, servicesFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case  2:
                CategoriesFragment categoriesFragment = new CategoriesFragment();
                fragmentTransaction.add(R.id.inventorylist_fragmentcontainer, categoriesFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case 3:
                DiscountsFragment discountsFragment = new DiscountsFragment();
                fragmentTransaction.add(R.id.inventorylist_fragmentcontainer, discountsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
        }
    }
}
