package com.example.devcash;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.example.devcash.CustomAdapters.ApplyDiscountAdapter;
import com.example.devcash.CustomAdapters.DiscountAdapter;
import com.example.devcash.Object.Discount;
import com.example.devcash.Object.Discountlistdata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddDiscountActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ownerdbreference;
    RecyclerView recyclerView;

    List<Discountlistdata> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_discount2);

        recyclerView = (RecyclerView) findViewById(R.id.add_discount);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ownerdbreference = firebaseDatabase.getReference("datadevcash/owner");
    }

    @Override
    protected void onStart() {
        super.onStart();

        displayAllDiscounts();
    }

    private void displayAllDiscounts() {

        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        String ownerKey = dataSnapshot1.getKey();

                        ownerdbreference.child(ownerKey+"/business/discount").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                list = new ArrayList<>();
                                for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                    Discount discount = dataSnapshot2.getValue(Discount.class);
                                    Discountlistdata listdata = new Discountlistdata();
                                    String discountCode = discount.getDisc_code();
                                    listdata.setDisc_code(discountCode);
                                    list.add(listdata);

                                    Toast.makeText(AddDiscountActivity.this, discountCode+" is the code", Toast.LENGTH_SHORT).show();
                                }

                                ApplyDiscountAdapter discountAdapter = new ApplyDiscountAdapter(list);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, true));
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(discountAdapter);
                                discountAdapter.notifyDataSetChanged();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_apply, menu);
        return true;
    }
}
