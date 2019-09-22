package com.example.devcash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class CashierCustomerPaymentActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout btncharge;
    TextView txtchargeamt, txttotaldue;
    TextInputEditText textcashreceived;
    DatabaseReference ownerdbreference;
    FirebaseDatabase firebaseDatabase;
    int customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_customer_payment);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btncharge = (LinearLayout) findViewById(R.id.btnchargecustomer);
        txtchargeamt = (TextView) findViewById(R.id.textchargeamount);
        txttotaldue = (TextView) findViewById(R.id.txttotalamount);
        textcashreceived = (TextInputEditText) findViewById(R.id.text_cashreceived);

        btncharge.setOnClickListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ownerdbreference = firebaseDatabase.getReference("datadevcash/owner");

        // We get from shared preference the customer id.
        SharedPreferences customerIdShared = getSharedPreferences("CustomerIdPref", MODE_PRIVATE);
        customerId = (customerIdShared.getInt("customer_id", 0));

        if (customerId <= 0) {
            customerId = customerId + 1;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.btnchargecustomer:
                // update product stock
                SharedPreferences empPref = getApplicationContext().getSharedPreferences("EmpPref", MODE_PRIVATE);
                String empTask = empPref.getString("emp_task", "");

                    saveCashChange();
                    callIntent();

                break;
        }
    }

    private void callIntent() {
        Intent intent = new Intent(CashierCustomerPaymentActivity.this, CashierFinalCustomerActivity.class);
                                                            startActivity(intent);
    }

    public void saveCashChange() {
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        SharedPreferences empPref = getSharedPreferences("EmpPref", MODE_PRIVATE);
        final String empUsername = empPref.getString("emp_username", "");

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        final String acctkey = dataSnapshot1.getKey();

                        ownerdbreference.child(acctkey+"/business/employee").orderByChild("emp_username").equalTo(empUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        String empKey = dataSnapshot2.getKey();

                                        ownerdbreference.child(acctkey+"/business/customer_transaction").orderByChild("customer_id").equalTo(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()){
                                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                                        String customertransactionkey = dataSnapshot2.getKey();
                                                        CustomerTransaction customerTransaction = dataSnapshot2.getValue(CustomerTransaction.class);
                                                        double subtotal = customerTransaction.getAmount_due();
                                                        double cashreceived = Double.parseDouble(textcashreceived.getText().toString());
                                                        double total = customerTransaction.getAmount_due();
                                                        double amount_due = customerTransaction.getAmount_due();

                                                        if (cashreceived >= amount_due){

//                                                            Intent intent = new Intent(CashierCustomerPaymentActivity.this, CashierFinalCustomerActivity.class);
//                                                            startActivity(intent);

                                                            String change_str = String.format("%.2f", cashreceived - amount_due);
                                                            double change = Double.parseDouble(change_str);

                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/").child(customertransactionkey+"/cash_received").setValue(cashreceived);
                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/").child(customertransactionkey+"/change").setValue(change);
                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/").child(customertransactionkey+"/transaction_status").setValue("Completed");
                                                            Toast.makeText(CashierCustomerPaymentActivity.this, "Cash is saved", Toast.LENGTH_SHORT).show();

                                                        }else {
                                                            Toast.makeText(CashierCustomerPaymentActivity.this, "Short cash!", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                }else {
//                                    Toast.makeText(CustomerPaymentActivity.this, "cust id does not exist"+customerId, Toast.LENGTH_SHORT).show();
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
