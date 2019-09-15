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
import android.widget.LinearLayout;
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
    DatabaseReference ownerdbreference;
    FirebaseDatabase firebaseDatabase;
    int customerId;
    TextView txtcustomer_cash, txtcustomer_change;
    Button btnshowreceipt, btnstartnewsale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_customer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtcustomer_cash = (TextView) findViewById(R.id.txtcustomercash);
        txtcustomer_change = (TextView) findViewById(R.id.txtcustomerchange);
        btnshowreceipt = (Button) findViewById(R.id.btn_showreceipt);
        btnstartnewsale = (Button) findViewById(R.id.btn_startnewsale);

        btnshowreceipt.setOnClickListener(this);
        btnstartnewsale.setOnClickListener(this);


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
            case R.id.btn_startnewsale:
                SharedPreferences customerIdPref = getApplicationContext().getSharedPreferences("CustomerIdPref", MODE_PRIVATE);
                SharedPreferences.Editor customerIdEditor = customerIdPref.edit();
                int nextId = customerId + 1;
                customerIdEditor.putInt("customer_id", nextId);
                customerIdEditor.commit();

                Intent intent = new Intent(FinalCustomerActivity.this, DashboardActivity.class);
                startActivity(intent);

                break;
            case R.id.btn_showreceipt:
                Intent intent1 = new Intent(FinalCustomerActivity.this, CustomerReceiptActivity.class);
                startActivity(intent1);
                break;
        }
    }

    public void displayCashChange(){
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

//        Toast.makeText(this, customerId+" is the customer id from displayCashChange", Toast.LENGTH_SHORT).show();

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

                                        CustomerTransaction customerTransaction = dataSnapshot2.getValue(CustomerTransaction.class);
                                        double cash = customerTransaction.getCash_received();
                                        double change = customerTransaction.getChange();

                                        txtcustomer_cash.setText(String.valueOf(cash));
                                        txtcustomer_change.setText(String.valueOf(change));

                                    }
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
