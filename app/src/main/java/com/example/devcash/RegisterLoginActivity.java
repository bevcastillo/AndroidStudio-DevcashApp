package com.example.devcash;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterLoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnUserLogin, btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);

        ///
        btnUserLogin = findViewById(R.id.btn_login);
        btnSignup = findViewById(R.id.btn_signup);

        btnUserLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_login:
                Intent login = new Intent(RegisterLoginActivity.this, OwnerLoginActivity.class);
                startActivity(login);
                break;

            case R.id.btn_signup:
                String url = "https://www.devcash.com.ph/signup.php";
                Intent devcash = new Intent(Intent.ACTION_VIEW);
                devcash.setData(Uri.parse(url));
                startActivity(devcash);
                break;
        }
    }
}
