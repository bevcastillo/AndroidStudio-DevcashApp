package com.example.devcash;

import android.content.Intent;
import android.preference.EditTextPreference;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.devcash.Database.DatabaseHelper;

public class OwnerSignupActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseHelper db;

    TextInputEditText enterpriseName, enterpriseNumEmployees, enterpriseAddress, enterpriseTIN,
        enterprisePermit, ownerLastname, ownerFirstname, ownerPhone, ownerEmail, ownerUsername,
            ownerPassword, ownerConfPassword;
    Spinner enterpriseType;
    String selectedEnterpriseType;
    Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_signup);

        //
        db = new DatabaseHelper(this);
        //
        enterpriseName = (TextInputEditText) findViewById(R.id.register_enterprise_name);
        enterpriseNumEmployees = (TextInputEditText) findViewById(R.id.register_number_employees);
        enterpriseAddress = (TextInputEditText) findViewById(R.id.register_address);
        enterpriseTIN = (TextInputEditText) findViewById(R.id.register_TIN);
        enterprisePermit = (TextInputEditText) findViewById(R.id.register_permit);
        ownerLastname = (TextInputEditText) findViewById(R.id.register_lastname);
        ownerFirstname = (TextInputEditText) findViewById(R.id.register_firstname);
        ownerPhone = (TextInputEditText) findViewById(R.id.register_mobile_number);
        ownerUsername = (TextInputEditText) findViewById(R.id.register_username);
        ownerEmail = (TextInputEditText) findViewById(R.id.register_email);
        ownerPassword = (TextInputEditText) findViewById(R.id.register_password);
        ownerConfPassword = (TextInputEditText) findViewById(R.id.register_confirm_password);

        btn_register = (Button) findViewById(R.id.btn_register);

        //adding listeners to the events
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.btn_register:

                String entname = enterpriseName.getText().toString();
                String numemp = enterpriseNumEmployees.getText().toString();
                String entaddr = enterpriseAddress.getText().toString();
                String entTIN = enterpriseTIN.getText().toString();
                String entpermit = enterprisePermit.getText().toString();
                String lname = ownerLastname.getText().toString();
                String fname = ownerFirstname.getText().toString();
                String username = ownerUsername.getText().toString();
                String phone = ownerPhone.getText().toString();
                String email = ownerEmail.getText().toString();
                String passw = ownerPassword.getText().toString();
                String confpassw = ownerConfPassword.getText().toString();

                //check if fields are empty
                if(!entname.equals("") && !numemp.equals("") && !entaddr.equals("")
                        && !entTIN.equals("") && !entpermit.equals("") && !lname.equals("")
                        && !fname.equals("") && !phone.equals("") && !email.equals("")
                        && !username.equals("") && !passw.equals("") && !confpassw.equals("")){

                    if(ownerPassword.equals(ownerConfPassword)){
                        Boolean checkUsername = db.checkUsername(username);
                        if(checkUsername== true){
                            Boolean insert_acct = db.insert_account(username, email, passw, null, null); //inserting into the account table
                            Boolean insert_own = db.insert_owner(lname, fname, null, null, null, null); //inserting into the owner table
                            if(insert_acct==true && insert_own==true){
                                Toast.makeText(this, "Successfully registered.", Toast.LENGTH_LONG).show();
                                Intent register_intent = new Intent(this, DashboardActivity.class);
                                startActivityForResult(register_intent, 0);
                            }//end if
                        }//end if
                    }else{
                        Toast.makeText(this,"Username already exists!", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(this,"Please fill in all fields!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
