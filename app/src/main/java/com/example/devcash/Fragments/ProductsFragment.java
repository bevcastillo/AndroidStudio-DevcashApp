package com.example.devcash.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.devcash.ADD_UI.AddProductActivity;
import com.example.devcash.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment {


    public ProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        //add floating action button
        FloatingActionButton prod_fab = view.findViewById(R.id.addprod_fab);
        prod_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // when add fab is pressed, go to add product activity
                Intent addprod = new Intent(getActivity(), AddProductActivity.class);
                startActivity(addprod);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Products");
    }



}
