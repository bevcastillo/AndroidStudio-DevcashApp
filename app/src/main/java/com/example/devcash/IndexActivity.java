package com.example.devcash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IndexActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnowner, btnemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        btnowner = (Button) findViewById(R.id.btnforowners);
        btnemp = (Button) findViewById(R.id.btnforemp);

        btnowner.setOnClickListener(this);
        btnemp.setOnClickListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);

        if(hasFocus){
            hideSystemUI();
        }

    }

    private void hideSystemUI() {
        //enables regular immersive mode
        //for "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE,
        //or for "sticky immersive", replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        //set the content to appear under the system bars so that the
                        //content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                        // hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN );
    }

    //shows the system bars by removing all the flags except
    // for the ones that make the content appear under the system bars
    private void showSystemUI(){
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN );
    }

    @Override
    public void onClick(View v) {
        int bid = v.getId();
        switch (bid){
            case R.id.btnforowners:
                Intent owner = new Intent(IndexActivity.this, OwnerLoginActivity.class);
                startActivity(owner);
            break;
            case R.id.btnforemp:
                Intent emp = new Intent(IndexActivity.this, EmployeeLoginActivity.class);
                startActivity(emp);
            break;
        }

    }
}
