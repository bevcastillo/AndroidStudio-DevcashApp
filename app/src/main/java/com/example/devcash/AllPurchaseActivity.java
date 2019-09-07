package com.example.devcash;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.devcash.CustomAdapters.AllPurchaseAdapter;
import com.example.devcash.Object.PurchaseTransaction;
import com.example.devcash.Object.PurchaseTransactionlistdata;
import com.example.devcash.Object.PurchasedItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllPurchaseActivity extends AppCompatActivity {

    RecyclerView recyclerViewpurchaselist;
    DatabaseReference dbreference;
    DatabaseReference ownerdbreference;
    FirebaseDatabase firebaseDatabase;

    List<PurchaseTransactionlistdata> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_purchase);


        //
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerViewpurchaselist = (RecyclerView) findViewById(R.id.purchaselist_recyclerview);

        //firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        ownerdbreference = firebaseDatabase.getReference("datadevcash/owner");
    }

    @Override
    protected void onStart() {
        super.onStart();

        displayAllPurchase();
    }

    public void displayAllPurchase(){
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        String acctkey = dataSnapshot1.getKey();

                        ownerdbreference.child(acctkey+"/business/transaction").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                list = new ArrayList<>();
                                if(dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        PurchaseTransaction purchaseTransaction = dataSnapshot2.getValue(PurchaseTransaction.class);
                                        PurchaseTransactionlistdata listdata = new PurchaseTransactionlistdata();
                                        double services_subtotal = purchaseTransaction.getPurchasedItem().getServices().getSubtotal();
//                                        double products_subtotal = purchaseTransaction.getPurchasedItem().getProduct().getSubtotal();
                                        double total = purchaseTransaction.getPurch_tot_price();
                                        double qty = purchaseTransaction.getPurch_tot_qty();
//                                        String name = pur
                                        listdata.setPurch_subtotal(services_subtotal);
                                        listdata.setPurch_tot_price(total);
                                        listdata.setPurch_qty(qty);
                                        listdata.setPurchasedItem(purchaseTransaction.getPurchasedItem());
                                        list.add(listdata);
                                    }
                                    AllPurchaseAdapter adapter = new AllPurchaseAdapter(list);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                    recyclerViewpurchaselist.setLayoutManager(layoutManager);
                                    recyclerViewpurchaselist.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                    recyclerViewpurchaselist.setItemAnimator(new DefaultItemAnimator());
                                    recyclerViewpurchaselist.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }
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
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
