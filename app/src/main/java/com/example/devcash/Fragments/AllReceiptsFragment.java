package com.example.devcash.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.devcash.CustomAdapters.AllReceiptsAdapter;
import com.example.devcash.CustomAdapters.CategoryAdapter;
import com.example.devcash.Object.CustomerTransaction;
import com.example.devcash.Object.CustomerTransactionlistdata;
import com.example.devcash.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllReceiptsFragment extends Fragment {

    RecyclerView allreceiptrecycler;

    DatabaseReference ownerdbreference;
    FirebaseDatabase firebaseDatabase;

    List<CustomerTransactionlistdata> list;
    ArrayList<CustomerTransaction> customerTransactionArrayList;


    public AllReceiptsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_receipts, container, false);

        allreceiptrecycler = (RecyclerView) view.findViewById(R.id.allreceipt_recyclerview);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ownerdbreference = firebaseDatabase.getReference("datadevcash/owner");
        DatabaseReference myReference = firebaseDatabase.getReference("example/titleSet");


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        displayAllReceipt();
    }

    private void displayAllReceipt() {



        SharedPreferences shared = getActivity().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        String ownerKey = dataSnapshot1.getKey();

                        ownerdbreference.child(ownerKey+"/business/customer_transaction").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                list = new ArrayList<>();
                                for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                    CustomerTransaction customerTransaction = dataSnapshot2.getValue(CustomerTransaction.class);
                                    CustomerTransactionlistdata lisdata = new CustomerTransactionlistdata();
                                    String dateTime = customerTransaction.getTransaction_datetime();
                                    int customerId = customerTransaction.getCustomer_id();
                                    double amountDue = customerTransaction.getAmount_due();
                                    lisdata.setAmount_due(amountDue);
                                    lisdata.setTransaction_datetime(dateTime);
                                    lisdata.setCustomer_id(customerId);
                                    list.add(lisdata);
                                }
                                AllReceiptsAdapter adapter = new AllReceiptsAdapter(list);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                allreceiptrecycler.setLayoutManager(layoutManager);
                                allreceiptrecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                                allreceiptrecycler.setItemAnimator(new DefaultItemAnimator());
                                allreceiptrecycler.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Receipts");
    }
}
