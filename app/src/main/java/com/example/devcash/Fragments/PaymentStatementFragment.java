package com.example.devcash.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.devcash.PaymentInfoActivity;
import com.example.devcash.R;
import com.example.devcash.Settings_UI.SettingsActivity;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentStatementFragment extends Fragment implements View.OnClickListener {


    public PaymentStatementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_statement, container, false);

        //
        TextView textViewBill = (TextView) view.findViewById(R.id.text_viewbill);
        TextView textViewAccountDetails = (TextView) view.findViewById(R.id.text_viewaccountdetails);

        //add listeners to the textviews
        textViewBill.setOnClickListener(this);
        textViewAccountDetails.setOnClickListener(this);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Payment Statement");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_viewbill:
                Intent detailedreceipt = new Intent(getActivity(), PaymentInfoActivity.class);
                startActivity(detailedreceipt);
                break;
            case R.id.text_viewaccountdetails:
                Intent toSettings = new Intent(getActivity(), SettingsActivity.class);
                startActivity(toSettings);
                break;
        }
    }


}
