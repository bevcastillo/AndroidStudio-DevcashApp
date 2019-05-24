package com.example.devcash.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.devcash.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerChargeFragment extends Fragment {


    public CustomerChargeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_charge, container, false);

        //handles button
        Button btnchage = view.findViewById(R.id.btn_chargepurchase);
        btnchage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newpurchasefragment = new FinalPurchasePaymentFragment();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.customercharge_content, newpurchasefragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

}
