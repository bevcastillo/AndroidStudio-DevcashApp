package com.example.devcash.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.devcash.QRCodeFragment;
import com.example.devcash.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseListFragment extends Fragment implements View.OnClickListener {

    private Toolbar purchaseListToolbar;
    private Spinner purchaseListSpinner;
    private Button btnpay;
    private RecyclerView recyclerViewpurchlist;

    private Toolbar toolbar;
    private DrawerLayout mDrawer;

    private LinearLayout layoutScanCode, layoutNewTransaction;

    private DatabaseReference ownerdbreference;
    private FirebaseDatabase firebaseDatabase;


    public PurchaseListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_list, container, false);

        purchaseListToolbar = (Toolbar) view.findViewById(R.id.toolbar_purchaselist);
        purchaseListSpinner = (Spinner) view.findViewById(R.id.spinner_customertype);
        recyclerViewpurchlist = (RecyclerView) view.findViewById(R.id.recycler_purchtransaction);

        //
        btnpay = (Button) view.findViewById(R.id.btn_paypurchasetransaction);

        //
        layoutScanCode = (LinearLayout) view.findViewById(R.id.layout_transaction_qrcode);
        layoutNewTransaction = (LinearLayout) view.findViewById(R.id.layout_transaction_new);

        ///

        //setting up the hamburger icon to show in the custom toolbar
//        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//
//        mDrawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);


        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.customer_type));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        purchaseListSpinner.setAdapter(myAdapter);

        purchaseListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),
                        purchaseListSpinner.getSelectedItem().toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //adding listeners to the linear layouts
        layoutScanCode.setOnClickListener(this);
        layoutNewTransaction.setOnClickListener(this);
        btnpay.setOnClickListener(this);



        //

        firebaseDatabase = FirebaseDatabase.getInstance();
        ownerdbreference = firebaseDatabase.getReference("datadevcash/owner");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();



    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflate menu
        inflater.inflate(R.menu.purchaselist_menu, menu);
        menu.findItem(R.id.action_new).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.btn_paypurchasetransaction:
                Fragment chargeFragment = new ChargeFragment();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.customersales_content, chargeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.layout_transaction_qrcode:
//                Toast.makeText(getActivity(), "Scan Item", Toast.LENGTH_SHORT).show();
                Fragment QRCodeFragment = new QRCodeFragment();

                FragmentManager fragmentManager1 = getFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.customersales_content, QRCodeFragment);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();

                break;
            case R.id.layout_transaction_new:
                Toast.makeText(getActivity(), "New Transaction", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
