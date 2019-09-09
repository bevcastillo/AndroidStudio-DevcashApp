package com.example.devcash;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.Fragments.PurchaseInventorylistFragment;
import com.example.devcash.Fragments.SalesFragment;
import com.example.devcash.Object.CustomerTransaction;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FinalCustomerActivity extends AppCompatActivity implements View.OnClickListener {

    Button newsalebtn;
    TextView cust_cash, cust_change;
    DatabaseReference ownerdbreference;
    FirebaseDatabase firebaseDatabase;
    int customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_customer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        cust_cash = (TextView) findViewById(R.id.textcash);
        cust_change = (TextView) findViewById(R.id.txtchange);
        newsalebtn = (Button) findViewById(R.id.newsalebtn);

        newsalebtn.setOnClickListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ownerdbreference = firebaseDatabase.getReference("datadevcash/owner");

        // We get from shared preference the customer id.
        SharedPreferences customerIdShared = getSharedPreferences("CustomerIdPref", MODE_PRIVATE);
        customerId = (customerIdShared.getInt("customer_id", 0));
    }

    @Override
    protected void onStart() {
        super.onStart();

        displayCashChange();

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

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.newsalebtn:
                SharedPreferences customerIdPref = getApplicationContext().getSharedPreferences("CustomerIdPref", MODE_PRIVATE);
                SharedPreferences.Editor customerIdEditor = customerIdPref.edit();
                int nextId = customerId + 1;
                customerIdEditor.putInt("customer_id", nextId);
                customerIdEditor.commit();

                Intent intent = new Intent(FinalCustomerActivity.this, DashboardActivity.class);
                startActivity(intent);

                break;
        }
    }

    public void displayCashChange(){
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        Toast.makeText(this, customerId+" is the customer id from displayCashChange", Toast.LENGTH_SHORT).show();

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        final String acctkey = dataSnapshot1.getKey();

                        ownerdbreference.child(acctkey+"/business/customer_transaction").orderByChild("customer_id").equalTo(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        String customertransactionkey = dataSnapshot2.getKey();
                                        CustomerTransaction customerTransaction = dataSnapshot2.getValue(CustomerTransaction.class);
                                        double cash = customerTransaction.getCash_received();
                                        double change = customerTransaction.getChange();

                                        cust_cash.setText(String.valueOf(cash));
                                        cust_change.setText(String.valueOf(change));

                                    }
                                }else {
                                    Toast.makeText(FinalCustomerActivity.this, "cust id does not exist", Toast.LENGTH_SHORT).show();
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
}
