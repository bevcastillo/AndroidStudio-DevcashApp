package com.example.devcash;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.devcash.Object.Account;
import com.example.devcash.Object.Business;
import com.google.gson.Gson;

public class OwnerInformation extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText ownerlname, ownerfname, ownerphone, owneraddr, ownerbdate, acctuname, acctemail, acctpassw;
    RadioGroup radioGroupgender;
    RadioButton selectedradiobtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_information);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ownerlname = (TextInputEditText) findViewById(R.id.owner_lname);
        ownerfname = (TextInputEditText) findViewById(R.id.owner_fname);
        ownerphone = (TextInputEditText) findViewById(R.id.owner_phone);
        owneraddr = (TextInputEditText) findViewById(R.id.owner_addr);
        ownerbdate = (TextInputEditText)findViewById(R.id.owner_dob);
        acctuname = (TextInputEditText) findViewById(R.id.owner_username);
        acctemail = (TextInputEditText) findViewById(R.id.owner_email);
        acctpassw = (TextInputEditText) findViewById(R.id.owner_password);

        ownerbdate.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences ownerPref = getApplicationContext().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        SharedPreferences businessPref = getApplicationContext().getSharedPreferences("BusinessPref", MODE_PRIVATE);


        Gson gson = new Gson();
        String json = ownerPref.getString("account", "");
        String businessJson = businessPref.getString("business", "");

        Account account = gson.fromJson(json, Account.class);
        Business business = gson.fromJson(businessJson, Business.class);

        ownerlname.setText(business.getOwner_lname());
        ownerfname.setText(business.getOwner_fname());
        ownerphone.setText(business.getOwner_mobileno());


        acctuname.setText(account.getAcct_uname());
        acctemail.setText(account.getAcct_email());
        acctpassw.setText(account.getAcct_passw());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.savemenu, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Unsaved changes");
        builder.setMessage("Are you sure you want to leave without saving changes?");
        builder.setPositiveButton("LEAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
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

    public void updateOwner(Business business){

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }else if(id == R.id.action_save){

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.owner_dob:

                break;
        }
    }
}
