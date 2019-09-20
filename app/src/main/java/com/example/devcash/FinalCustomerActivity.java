package com.example.devcash;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.Fragments.PurchaseInventorylistFragment;
import com.example.devcash.Fragments.SalesFragment;
import com.example.devcash.Object.Account;
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

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
//        finish();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unable to go back to previous page");
        builder.setMessage("Transaction is already saved and customer has been charged for the purchased item.");
        builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }else if (id == R.id.void_action){
//            Toast.makeText(this, "You have pressed void", Toast.LENGTH_SHORT).show();
            voidDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void voidDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to void this transaction?");
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_void_transaction, null);
        builder.setView(dialogView);

        final TextInputEditText owner_password = (TextInputEditText) dialogView.findViewById(R.id.editText_passw);
        final TextInputLayout owner_password_layout = (TextInputLayout) dialogView.findViewById(R.id.voidPasswlayout);

        Toast.makeText(this, owner_password.getText().toString()+" is my password inputted", Toast.LENGTH_SHORT).show();

        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));


        builder.setPositiveButton("VOID", new DialogInterface.OnClickListener() {

            final String ownerPasswStr = owner_password.getText().toString();

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (ownerPasswStr.isEmpty()){
                    owner_password_layout.setError("Fields can not be empty.");
                }else {
                    owner_password_layout.setError(null);
                }

                //query the owner password

                ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                final String ownerKey = dataSnapshot1.getKey();

                                ownerdbreference.child(ownerKey+"/business/account").orderByChild("acct_uname").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                                String accountKey = dataSnapshot2.getKey();
                                                Account account = dataSnapshot2.getValue(Account.class);
                                                String ownerpassword = account.getAcct_passw();
                                                String accountType = account.getAcct_type();

                                                if (accountType.equals("Owner")){
                                                    Toast.makeText(FinalCustomerActivity.this, ownerpassword+ownerPasswStr+" is the password", Toast.LENGTH_SHORT).show();
                                                    Toast.makeText(FinalCustomerActivity.this, ownerPasswStr+" another password", Toast.LENGTH_SHORT).show();
                                                    if (ownerpassword.equals(ownerPasswStr)){
                                                        Toast.makeText(FinalCustomerActivity.this, "They are equal", Toast.LENGTH_SHORT).show();
                                                    }else {
                                                        Toast.makeText(FinalCustomerActivity.this, "not equal", Toast.LENGTH_SHORT).show();
                                                    }
                                                }else {
                                                    Toast.makeText(FinalCustomerActivity.this, "employee", Toast.LENGTH_SHORT).show();
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
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_void, menu);
        return true;
    }


}
