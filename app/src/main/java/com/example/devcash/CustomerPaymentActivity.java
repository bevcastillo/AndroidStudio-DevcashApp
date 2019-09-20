package com.example.devcash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.Object.CustomerTransaction;
import com.example.devcash.Object.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Map;

public class CustomerPaymentActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout btncharge;
    TextView txtchargeamt, txttotaldue;
    TextInputEditText textcashreceived;
    DatabaseReference ownerdbreference;
    FirebaseDatabase firebaseDatabase;
    int customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_payment);

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
                // update product stock
                updateProductStock();
                saveCashChange();
                break;
        }
    }

    private void updateProductStock() {
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username")
                .equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                final String acctKey = dataSnapshot1.getKey();

                                ownerdbreference.child(acctKey+"/business/customer_transaction")
                                        .orderByChild("customer_id")
                                        .equalTo(customerId)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {

                                                    for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                                        String customerTransactionKey = dataSnapshot2.getKey();
                                                        CustomerTransaction customerTransaction = dataSnapshot2.getValue(CustomerTransaction.class);
                                                        Gson gson = new Gson();

                                                        // we iterate every items in the cart.
                                                        for (Map.Entry<String, Object> entry : customerTransaction.getCustomer_cart().entrySet()) {
                                                            if (entry.getValue().toString().contains("product")) {
                                                                String json = gson.toJson(entry.getValue());
                                                                Log.d("PRODUCT JSON REP @@@@", json);
                                                                String mJsonString = json;
                                                                JsonParser parser = new JsonParser();
                                                                JsonElement mJson =  parser.parse(mJsonString);

                                                                JsonObject jsonObject = gson.fromJson(mJson, JsonObject.class);
                                                                JsonElement prodJson = jsonObject.get("product");
                                                                final Product prodObj = gson.fromJson(prodJson, Product.class);

                                                                // we update now the product object
                                                                ownerdbreference.child(acctKey+"/business/product")
                                                                        .orderByChild("prod_reference")
                                                                        .equalTo(prodObj.getProd_reference())
                                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                if (dataSnapshot.exists()) {
                                                                                    for (DataSnapshot dataSnapshot3 : dataSnapshot.getChildren()) {
                                                                                        String prodKey = dataSnapshot3.getKey();
                                                                                        Product prod = dataSnapshot3.getValue(Product.class);
                                                                                        double newStock = prod.getProd_stock() - prodObj.getProd_qty();
                                                                                        ownerdbreference.child(acctKey+"/business/product/"+prodKey+"/prod_stock").setValue(newStock);
                                                                                    }

                                                                                } else {
                                                                                    Toast.makeText(CustomerPaymentActivity.this, "Product not found", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                            }
                                                                        });
                                                            }
                                                        }

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


    public void saveCashChange(){
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

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
                                        double subtotal = customerTransaction.getAmount_due();
                                        double cashreceived = Double.parseDouble(textcashreceived.getText().toString());
                                        double total = customerTransaction.getAmount_due();
                                        double amount_due = customerTransaction.getAmount_due();

                                        if (cashreceived >= amount_due){
                                            String change_str = String.format("%.2f", cashreceived - amount_due);
                                            double change = Double.parseDouble(change_str);

                                            ownerdbreference.child(acctkey+"/business/customer_transaction/").child(customertransactionkey+"/cash_received").setValue(cashreceived);
                                            ownerdbreference.child(acctkey+"/business/customer_transaction/").child(customertransactionkey+"/change").setValue(change);
                                            Toast.makeText(CustomerPaymentActivity.this, "Cash is saved", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(CustomerPaymentActivity.this, FinalCustomerActivity.class);
                                            startActivity(intent);

                                        }else {
                                            Toast.makeText(CustomerPaymentActivity.this, "Short cash!", Toast.LENGTH_LONG).show();
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

    public void displayPrice(){
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        SharedPreferences custIdShared = getApplicationContext().getSharedPreferences("CustomerIdPref", MODE_PRIVATE);
        customerId = (custIdShared.getInt("customer_id", 0));

        if (customerId <= 0) {
            customerId = customerId + 1;
        }

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
                                        double total = customerTransaction.getAmount_due();

                                        txtchargeamt.setText("Charge  ₱ "+ (total));
                                        txttotaldue.setText(String.valueOf(total));
                                    }
                                }else {
                                    //customer does not exist
                                    txtchargeamt.setText("Charge  ₱ 0.00");
                                    txttotaldue.setText("0.00");
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
