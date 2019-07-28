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
    TextInputEditText entName, entNumEmp, entAddr, entTIN, entPermit, ownerlname, ownerfname, ownerphone,
                        ownerEmail, ownerUsername, ownerPassw, ownerConfPassw;
    Spinner entType;
    String selectedEntType;
    Button btnregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_signup);

//        //
        db = new DatabaseHelper(this);
//        //
        entName = (TextInputEditText) findViewById(R.id.register_enterprise_name);
        entNumEmp = (TextInputEditText) findViewById(R.id.register_number_employees);
        entAddr = (TextInputEditText) findViewById(R.id.register_address);
        entTIN = (TextInputEditText) findViewById(R.id.register_TIN);
        entPermit = (TextInputEditText) findViewById(R.id.register_permit);
        ownerlname = (TextInputEditText) findViewById(R.id.register_lastname);
        ownerfname = (TextInputEditText) findViewById(R.id.register_firstname);
        ownerphone = (TextInputEditText) findViewById(R.id.register_mobile_number);
        ownerUsername = (TextInputEditText) findViewById(R.id.register_username);
        ownerEmail = (TextInputEditText) findViewById(R.id.register_email);
        ownerPassw = (TextInputEditText) findViewById(R.id.register_password);
        ownerConfPassw = (TextInputEditText) findViewById(R.id.register_confirm_password);

        btnregister = (Button) findViewById(R.id.btn_register);

        //adding listeners to the events
        btnregister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_register:
                String entname = entName.getText().toString();
                String numemp = entNumEmp.getText().toString();
                String entaddr = entAddr.getText().toString();
                String tin = entTIN.getText().toString();
                String permit = entPermit.getText().toString();
                String ownlname = ownerlname.getText().toString();
                String ownfname = ownerfname.getText().toString();
                String ownuser = ownerUsername.getText().toString();
                String ownemail = ownerEmail.getText().toString();
                String ownpassw = ownerPassw.getText().toString();
                String confpassw = ownerConfPassw.getText().toString();

                //checking if fields are empty
                if(!entname.equals("") && !numemp.equals("") && !entaddr.equals("")
                        && !tin.equals("") && !permit.equals("")
                        && !ownlname.equals("") && !ownfname.equals("")
                        && !ownuser.equals("") && !ownemail.equals("")
                        && !ownpassw.equals("") &&!confpassw.equals("")){
                    if(ownpassw.equals(confpassw)){
                        Boolean checkusername = db.checkUsername(ownuser);
                        if(checkusername==true){
                            Boolean insert_acct = db.insert_account(ownuser, ownemail, ownpassw, null, null); //inserting into the account table
//                            Boolean insert_own = db.insert_owner(ownlname, ownfname, null, null, null, null); //inserting into the owner table
                            if(insert_acct==true){
                                Toast.makeText(getApplicationContext(), "Successfully Registered!", Toast.LENGTH_LONG).show();
                                Intent tosales = new Intent(OwnerSignupActivity.this, DashboardActivity.class);
                                startActivity(tosales);
                            }else{
                                Toast.makeText(getApplicationContext(), "Error in trying to register your acount.", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Username already exist!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Password did not match!", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Fields can't be empty!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
