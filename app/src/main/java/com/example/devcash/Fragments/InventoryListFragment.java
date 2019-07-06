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

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class InventoryListFragment extends Fragment {

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
        initImageBitmaps();
//
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.inventorylist_recycleview);
//        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
//                .build()); //adding a divider into the recyclerview list
//
//        InventoryRecyclerViewAdapter adapter = new InventoryRecyclerViewAdapter();
//        recyclerView.setAdapter(adapter);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    private void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps");

        mIcon.add(R.drawable.ic_product);
        mLabel.add("Products");

        mIcon.add(R.drawable.ic_customer);
        mLabel.add("Services");

        mIcon.add(R.drawable.ic_category);
        mLabel.add("Categories");

        mIcon.add(R.drawable.ic_tag);
        mLabel.add("Discounts");

        initRecyclerView();

    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView recyclerView = getView().findViewById(R.id.inventorylist_recycleview);
        InventoryRecyclerViewAdapter adapter = new InventoryRecyclerViewAdapter(getActivity(), mIcon, mLabel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

//    //adding a listener to the recycleview list
//    @Override
//    public void onInventoryClick(int position) {
//
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        switch (position){
//            case 0:
//                ProductsFragment productsFragment = new ProductsFragment();
//                fragmentTransaction.replace(R.id.inventorylist_fragmentcontainer, productsFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//                break;
//            case 1:
//                ServicesFragment servicesFragment = new ServicesFragment();
//                fragmentTransaction.replace(R.id.inventorylist_fragmentcontainer, servicesFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//                break;
//            case  2:
//                CategoriesFragment categoriesFragment = new CategoriesFragment();
//                fragmentTransaction.replace(R.id.inventorylist_fragmentcontainer, categoriesFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//                break;
//            case 3:
//                DiscountsFragment discountsFragment = new DiscountsFragment();
//                fragmentTransaction.replace(R.id.inventorylist_fragmentcontainer, discountsFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//                break;
//        }
//
//
//    }


//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        InventoryList selectedList = this.list.get(position);
//
//        int icon = selectedList.getIcon();
//        String title = selectedList.getInventory_title();
//
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        switch (position){
//            case 0:
//                ProductsFragment productsFragment = new ProductsFragment();
//                fragmentTransaction.add(R.id.inventorylist_fragmentcontainer, productsFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//                break;
//            case 1:
//                ServicesFragment servicesFragment = new ServicesFragment();
//                fragmentTransaction.add(R.id.inventorylist_fragmentcontainer, servicesFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//                break;
//            case  2:
//                CategoriesFragment categoriesFragment = new CategoriesFragment();
//                fragmentTransaction.add(R.id.inventorylist_fragmentcontainer, categoriesFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//                break;
//            case 3:
//                DiscountsFragment discountsFragment = new DiscountsFragment();
//                fragmentTransaction.add(R.id.inventorylist_fragmentcontainer, discountsFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//                break;
//        }
//    }
}
