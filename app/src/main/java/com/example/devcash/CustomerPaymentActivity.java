package com.example.devcash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.Object.CustomerTransaction;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerPaymentActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout btncharge;
    TextView txtchargeamt;
    TextInputEditText textcashreceived;
    DatabaseReference ownerdbreference;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_payment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btncharge = (LinearLayout) findViewById(R.id.btnchargecustomer);
        txtchargeamt = (TextView) findViewById(R.id.textchargeamount);
        textcashreceived = (TextInputEditText) findViewById(R.id.text_cashreceived);

        btncharge.setOnClickListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ownerdbreference = firebaseDatabase.getReference("datadevcash/owner");

    }

    @Override
    protected void onStart() {
        super.onStart();

        displayPrice(); //display the total price to the button
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
            case R.id.btnchargecustomer:
                saveCashChange();
                break;
        }
    }

    public void saveCashChange(){
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        final String acctkey = dataSnapshot1.getKey();

                        ownerdbreference.child(acctkey+"/business/customer_transaction").orderByChild("customer_id").equalTo(1).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        String customertransactionkey = dataSnapshot2.getKey();
                                        CustomerTransaction customerTransaction = dataSnapshot2.getValue(CustomerTransaction.class);
                                        double subtotal = customerTransaction.getSubtotal();
                                        double cashreceived = Double.parseDouble(textcashreceived.getText().toString());

                                        if (cashreceived >= subtotal){
                                            double change = cashreceived - subtotal;
                                            ownerdbreference.child(acctkey+"/business/customer_transaction/").child(customertransactionkey+"/cash_received").setValue(cashreceived);
                                            ownerdbreference.child(acctkey+"/business/customer_transaction/").child(customertransactionkey+"/change").setValue(change);
                                            Toast.makeText(CustomerPaymentActivity.this, "Cash is saved", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(CustomerPaymentActivity.this, FinalCustomerActivity.class);
                                            startActivity(intent);

                                        }else {
                                            Toast.makeText(CustomerPaymentActivity.this, "short cash, please input higher cash amount!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }else {
                                    Toast.makeText(CustomerPaymentActivity.this, "cust id does not exist", Toast.LENGTH_SHORT).show();
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

    public void displayPrice(){
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        final String acctkey = dataSnapshot1.getKey();

                        ownerdbreference.child(acctkey+"/business/customer_transaction").orderByChild("customer_id").equalTo(1).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        String customertransactionkey = dataSnapshot2.getKey();

                                        CustomerTransaction customerTransaction = dataSnapshot2.getValue(CustomerTransaction.class);
                                        double subtotal = customerTransaction.getSubtotal();

                                        txtchargeamt.setText("Charge  ₱ "+ (subtotal));

                                    }
                                }else {
                                    //customer does not exist
                                    txtchargeamt.setText("Charge  ₱ 0.00");
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
