package com.example.devcash;

import android.preference.EditTextPreference;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;

public class OwnerSignupActivity extends AppCompatActivity {

    TextInputEditText enterpriseName, enterpriseNumEmployees, enterpriseAddress, enterpriseTIN,
    enterprisePermit, ownerLastname, ownerFirstname, ownerPhone, ownerEmail, ownerPassword;
    Spinner enterpriseType;
    String selectedEnterpriseType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_signup);

        //
        enterpriseName = (TextInputEditText) findViewById(R.id.register_enterprise_name);
        enterpriseNumEmployees = (TextInputEditText) findViewById(R.id.register_number_employees);
        enterpriseAddress = (TextInputEditText) findViewById(R.id.register_address);
        enterpriseTIN = (TextInputEditText) findViewById(R.id.register_TIN);
        enterprisePermit = (TextInputEditText) findViewById(R.id.register_permit);
        ownerLastname = (TextInputEditText) findViewById(R.id.register_lastname);
        ownerFirstname = (TextInputEditText) findViewById(R.id.register_firstname);
        ownerPhone = (TextInputEditText) findViewById(R.id.register_mobile_number);
        ownerEmail = (TextInputEditText) findViewById(R.id.register_email);
        ownerPassword = (TextInputEditText) findViewById(R.id.register_password);
    }
}
