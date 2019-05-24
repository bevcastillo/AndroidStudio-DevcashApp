package com.example.devcash.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.devcash.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendReceiptFragment extends Fragment {


    public SendReceiptFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_receipt, container, false);


        ImageView sendemail = view.findViewById(R.id.send_email);
        ImageView sendtext = view.findViewById(R.id.send_text);

        //handles buttons
        Button btnnewpurchase = view.findViewById(R.id.btn_newPurchase);


        btnnewpurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment salesfragment = new SalesFragment();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.finalpurchase_content, salesfragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

//        sendemail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(),"Email successfully sent.", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        sendtext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Text successfully sent.", Toast.LENGTH_SHORT).show();
//            }
//        });


        return view;
    }

}
