package com.example.devcash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.Object.Account;
import com.example.devcash.Object.Business;
import com.example.devcash.Object.Enterprise;
import com.example.devcash.Object.Owner;
import com.example.devcash.Object.TaskObj;
import com.example.devcash.Settings_UI.ForgotPasswordActivity;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.nio.BufferOverflowException;
import java.util.Iterator;
import java.util.Map;

public class OwnerLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference databaseReference;
    private DatabaseReference ownerDatabaseReference;
    private FirebaseDatabase firebasedb;
    private DatabaseReference accountDatabaseReference;
    private DatabaseReference ownerbusinessReference;
    private DatabaseReference accountOwnerReference;
    Button btnOwnerLogin;

    private TextInputLayout textInputOwnerUsername, textInputOwnerPassw;
    TextInputEditText owneruname, ownerpassw;
    TextView textForgotPassw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_login);

        textInputOwnerUsername = findViewById(R.id.text_input_user_email);
        textInputOwnerPassw = findViewById(R.id.text_input_passw);
        textForgotPassw = findViewById(R.id.forgot_passw);
        btnOwnerLogin = findViewById(R.id.btn_login);
        owneruname = (TextInputEditText) findViewById(R.id.textinput_uname);
        ownerpassw = (TextInputEditText) findViewById(R.id.textinput_passw);

        btnOwnerLogin.setOnClickListener(this);

        textForgotPassw.setOnClickListener(this);

        firebasedb = FirebaseDatabase.getInstance();
        databaseReference = firebasedb.getReference("/datadevcash/owner");
        ownerDatabaseReference = firebasedb.getReference("/datadevcash/owner");
        accountDatabaseReference = firebasedb.getReference("/datadevcash/owner/account");

        //
        ownerbusinessReference = firebasedb.getReference("/datadevcash/owner");

    }

    public void ownerLogin(){
        SharedPreferences ownerPref = getApplicationContext().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = ownerPref.edit();

        final Gson gson = new Gson();


        final String owneruser = owneruname.getText().toString();
        final String ownerpassword = ownerpassw.getText().toString();

        ownerbusinessReference.orderByChild("business/owner_username").equalTo(owneruser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
//                    Toast.makeText(getApplicationContext(), owneruser+" exists", Toast.LENGTH_SHORT).show();
                    editor.putString("owner_username", owneruser);
                    editor.putString("owner_passw", ownerpassword);
                    editor.commit();

                    ownerbusinessReference.orderByChild("business/account/acct_passw").equalTo(ownerpassword).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Toast.makeText(getApplication(), "Successfully logged in.", Toast.LENGTH_SHORT).show();
                                Intent owner_dashboard = new Intent(OwnerLoginActivity.this, DashboardActivity.class);
                                startActivity(owner_dashboard);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    // iterate
//                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                        Business business1 = ds.getValue(Business.class);
//
//                        Log.i("BUSINESS TAG!!", business1.getAccount().getAcct_uname());
////                        Map<String, Business> map = (Map<String, Business>) dataSnapshot.getValue();
////                        Iterator iter = map.keySet().iterator();
////
////                        while(iter.hasNext()) {
////                            String key = (String) iter.next();
////                            business = (Business) map.get(key);
////                        }
////
////                        Log.i("BUSINESS TAG@@@@@", String.valueOf(business));
//
////                        Log.i("MY MAP BUSINESS TAG", "Value is: " + map.keySet().iterator());
////                        Business business = ds.getValue(Business.class);
////                        Account account = ds.child("account").getValue(Account.class);
//
//
////                        Log.i("BUSINESS TAG!!!", );
////                        Toast.makeText(OwnerLoginActivity.this, account.getAcct_email()+"", Toast.LENGTH_SHORT).show();
//                    }
                }else{
                    Toast.makeText(OwnerLoginActivity.this, "does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        accountDatabaseReference.orderByChild("acct_uname").equalTo(owneruser).addListenerForSingleValueEvent(new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//            if(dataSnapshot.exists()){
//                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
//                    Account account = dataSnapshot1.getValue(Account.class);
//                    if(account.getAcct_passw().equals(ownerpassword)){
//                        if(account.getAcct_type().equals("Owner")){
//                            String json = gson.toJson(account);
//                            editor.putString("acct_uname", owneruser);
//                            editor.putString("acct_passw", ownerpassword);
//                            editor.putString("acct_type", "Owner");
//                            editor.putString("Account", json);
//                            editor.commit();
//                            Toast.makeText(getApplicationContext(), "Successfully logged in.", Toast.LENGTH_SHORT).show();
//                            Intent owner_dashboard = new Intent(OwnerLoginActivity.this, DashboardActivity.class);
//                            startActivity(owner_dashboard);
//                        }else{
//                            Toast.makeText(getApplicationContext(), "No access", Toast.LENGTH_SHORT).show();
//                        }
//                    }else{
//                        Toast.makeText(getApplicationContext(), "Invalid username/password", Toast.LENGTH_LONG).show();
//                    }
//                }//end for
//            }else{
//                Toast.makeText(getApplicationContext(), "Username does not exist", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//        }
//    });


//        ownerDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot ownerSnap : dataSnapshot.getChildren()) {
////                    Log.i("OWNER TAG!!!", ownerSnap.child("owner_username").getValue(String.class));
////                    Owner owner = ownerSnap.getValue(Owner.class);
//                    Account account = ownerSnap.child("account").getValue(Account.class);
//                    Enterprise ent = ownerSnap.child("enterprise").getValue(Enterprise.class);
//                    String ownerFname = ownerSnap.child("owner_fname").getValue(String.class);
//                    String ownerLname = ownerSnap.child("owner_lname").getValue(String.class);
//                    String ownerUname = ownerSnap.child("owner_username").getValue(String.class);
//                    Owner owner = new Owner(ownerLname, ownerFname, ownerUname, account, ent);
//                    Log.i("OWNER TAG!!!!", owner.getOwner_fname());
//
//                    // check if the user credentials is same from the filled up form.
//                    if (!account.getAcct_uname().equals(owneruname.getText().toString()) && !account.getAcct_passw().equals(ownerpassw.getText().toString())) {
//                        Toast.makeText(OwnerLoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
//                        break;
//                    } else {
//                        Toast.makeText(OwnerLoginActivity.this, "Proceed to next activity", Toast.LENGTH_SHORT).show();
//                        Log.i("OWNER ACCOUNT", owneruname.getText().toString());
//                        break;
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        ownerDatabaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
//                    Owner owner = dataSnapshot1.getValue(Owner.class);
//                    String ownerusername = owner.getAccount().getAcct_uname();
//                    String ownerpassword = owner.getAccount().getAcct_passw();
//                    String acctype = owner.getAccount().getAcct_type();
//
//                    String uname = owneruname.getText().toString();
//                    String passw = ownerpassw.getText().toString();
//                    if(ownerusername.equals(uname) && ownerpassword.equals(passw)){
//                        if(acctype.equals("Owner")){
//                            Toast.makeText(getApplication(), "Successfully logged in.", Toast.LENGTH_SHORT).show();
//                            Intent owner_dashboard = new Intent(OwnerLoginActivity.this, DashboardActivity.class);
//                            startActivity(owner_dashboard);
//                        }else{
//                            Toast.makeText(getApplicationContext(), "Not owner!", Toast.LENGTH_SHORT).show();
//                        }
//                    }else{
//                        Toast.makeText(getApplicationContext(), "Account does not exist!", Toast.LENGTH_SHORT).show();
//                    }
////                    Toast.makeText(getApplicationContext(), dataSnapshot1.toString(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

//    private boolean ownerExists() {
//
//    }

//    private boolean validateEmail(){
//        String owner_useremail = textInputOwnerUsername.getEditText().getText().toString().trim();
//
//        if(owner_useremail.isEmpty()){
//            textInputOwnerUsername.setError("Fields can't be empty!");
//            return false;
//        }else{
//            textInputOwnerUsername.setError(null);
//            return true;
//        }
//    }

//    private boolean validatePassword(){
//        String owner_passw = textInputOwnerPassw.getEditText().getText().toString().trim();
//
//        if(owner_passw.isEmpty()){
//            textInputOwnerPassw.setError("Fields can't be empty!");
//            return false;
//        }else{
//            textInputOwnerPassw.setError(null);
//            return true;
//        }
//    }
    
//    public void confirmInput(View v){
//
//        if(!validateEmail() && !validateEmail()){
//            return;
//        }
//
//        Toast.makeText(getApplication(), "Successfully logged in.", Toast.LENGTH_SHORT).show();
//        Intent owner_dashboard = new Intent(OwnerLoginActivity.this, DashboardActivity.class);
//        startActivity(owner_dashboard);
//    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forgot_passw:
                Intent owner_forgotpassw = new Intent(OwnerLoginActivity.this, ForgotPasswordActivity.class);
                startActivity(owner_forgotpassw);
                break;
            case R.id.btn_login:
                ownerLogin();
                break;
        }
    }
}
