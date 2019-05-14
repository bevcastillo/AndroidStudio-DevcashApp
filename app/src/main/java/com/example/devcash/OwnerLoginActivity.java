package com.example.devcash;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.Settings_UI.ForgotPasswordActivity;

public class OwnerLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout textInputOwnerUsername, textInputOwnerPassw;
    TextView textForgotPassw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_login);

        textInputOwnerUsername = findViewById(R.id.text_input_user_email);
        textInputOwnerPassw = findViewById(R.id.text_input_passw);
        textForgotPassw = findViewById(R.id.forgot_passw);

        textForgotPassw.setOnClickListener(this);
    }

    private boolean validateEmail(){
        String owner_useremail = textInputOwnerUsername.getEditText().getText().toString().trim();

        if(owner_useremail.isEmpty()){
            textInputOwnerUsername.setError("Fields can't be empty!");
            return false;
        }else{
            textInputOwnerUsername.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){
        String owner_passw = textInputOwnerPassw.getEditText().getText().toString().trim();

        if(owner_passw.isEmpty()){
            textInputOwnerPassw.setError("Fields can't be empty!");
            return false;
        }else{
            textInputOwnerPassw.setError(null);
            return true;
        }
    }
    
    public void confirmInput(View v){

        if(!validateEmail() || !validateEmail()){
            return;
        }

        Toast.makeText(getApplication(), "Successfully logged in.", Toast.LENGTH_SHORT).show();
        Intent owner_dashboard = new Intent(OwnerLoginActivity.this, DashboardActivity.class);
        startActivity(owner_dashboard);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forgot_passw:
                Intent owner_forgotpassw = new Intent(OwnerLoginActivity.this, ForgotPasswordActivity.class);
                startActivity(owner_forgotpassw);
                break;
        }
    }
}
