package com.example.devcash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class HelpCenterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);

        // show Back button in the app bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // handles back button
    @Override
    public void onBackPressed() {
        finish();
    }

    ///////
    //handles back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }else
            return super.onOptionsItemSelected(item);
    }

}
