package com.example.devcash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.BufferOverflowException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void ownerLogin(){
        SharedPreferences ownerPref = getApplicationContext().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = ownerPref.edit();

        SharedPreferences businessPref = getApplicationContext().getSharedPreferences("BusinessPref", MODE_PRIVATE);
        final SharedPreferences.Editor businessEditor = businessPref.edit();

        final Gson gson = new Gson();

        final String owneruser = owneruname.getText().toString();
        final String ownerpassword = ownerpassw.getText().toString();

        MyUtility.username=owneruser;

        ownerbusinessReference.orderByChild("business/owner_username").equalTo(owneruser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    editor.putString("owner_username", owneruser);

                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        final String key = ds.getKey();

                        ownerbusinessReference.child(key+"/business").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Business business = dataSnapshot.getValue(Business.class);

                                    String businessJson = gson.toJson(business);
                                    businessEditor.putString("business", businessJson);
                                    businessEditor.commit();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        ownerbusinessReference.child(key+"/business/account").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for(DataSnapshot ds1 : dataSnapshot.getChildren()) {
                                        final String passkey = ds1.getKey();
                                        Account account = ds1.getValue(Account.class);
                                        if (account.getAcct_passw().equals(ownerpassword) && account.getAcct_uname().equals(owneruser)) {
                                            Toast.makeText(OwnerLoginActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                                            Intent owner_dashboard = new Intent(OwnerLoginActivity.this, DashboardActivity.class);
                                            startActivity(owner_dashboard);
                                            String accountJson = gson.toJson(account);
                                            editor.putString("account", accountJson);
                                            editor.commit();

                                        } else {
//                                            Toast.makeText(OwnerLoginActivity.this, "Username/Password is incorrect.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
//                                    Toast.makeText(OwnerLoginActivity.this, "Username/Password is incorrect!", Toast.LENGTH_SHORT).show();
                                } else{

                                    Toast.makeText(OwnerLoginActivity.this, "Account not found!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                }else{
//                    Toast.makeText(OwnerLoginActivity.this, "does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    private boolean validateEmailPassw(){
        String username = owneruname.getText().toString().trim();
        String passw = ownerpassw.getText().toString().trim();
        boolean ok = true;

        if(username.isEmpty()){
            textInputOwnerUsername.setError("Fields can not be empty.");
                ok = false;
            if(passw.isEmpty()){
                textInputOwnerPassw.setError("Fields can not be empty.");
                ok = false;
            }else{
                textInputOwnerPassw.setError(null);
                ok = true;
            }
        }else {
            textInputOwnerUsername.setError(null);
            ok = true;
        }

        return ok;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forgot_passw:
                Intent owner_forgotpassw = new Intent(OwnerLoginActivity.this, ForgotPasswordActivity.class);
                startActivity(owner_forgotpassw);
                break;
            case R.id.btn_login:
                if (validateEmailPassw()){
                    if(internetConnectionAvailable(5) == true){
                        ownerLogin();
                    } else{
                        progressDialog();
                    }
                }

                break;
        }
    }

    public void progressDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View progressView = inflater.inflate(R.layout.custom_progressbar, null);
        builder.setView(progressView);
        builder.show();
    }

    public void noInternetDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Please check your internet connection.");
        builder.setPositiveButton("OKAY", null);

        builder.show();
    }

    private boolean internetConnectionAvailable(int timeOut) {
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("google.com");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(timeOut, TimeUnit.MILLISECONDS);
            future.cancel(true);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
        }
        return inetAddress!=null && !inetAddress.equals("");
    }
}
