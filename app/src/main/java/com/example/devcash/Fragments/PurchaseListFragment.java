package com.example.devcash.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.devcash.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseListFragment extends Fragment {


    public PurchaseListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_purchase_list, container, false);

        //handles listview]
        ListView lvitemlist = view.findViewById(R.id.purchaseditem_listview);

        //set adapter
        //set click listener

        //show no data found text when listview is empty
        lvitemlist.setEmptyView(view.findViewById(R.id.txt_nopurchaseitem));

        // button click listener
        Button btnpay = view.findViewById(R.id.btn_paypurchasetransaction);
        btnpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment chargeFragment = new ChargeFragment();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.customersales_content, chargeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

}
