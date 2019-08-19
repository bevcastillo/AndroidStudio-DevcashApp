package com.example.devcash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.devcash.Settings_UI.HelpCenterSales;

public class HelpCenterActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout gettingstarted, sales, inventory, employees, reports, payments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);

        // show Back button in the app bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //
        gettingstarted = (LinearLayout) findViewById(R.id.gettingstartedlayout);
        sales = (LinearLayout) findViewById(R.id.saleslayout);
        inventory = (LinearLayout) findViewById(R.id.inventorylayout);
        employees = (LinearLayout) findViewById(R.id.employeeslayout);
        reports = (LinearLayout) findViewById(R.id.reportslayout);
        payments = (LinearLayout) findViewById(R.id.paymentslayout);

        gettingstarted.setOnClickListener(this);
        sales.setOnClickListener(this);
        inventory.setOnClickListener(this);
        employees.setOnClickListener(this);
        reports.setOnClickListener(this);
        payments.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id){
            case R.id.gettingstartedlayout:
                Intent gettingstarted = new Intent(this, GettingStarted.class);
                startActivity(gettingstarted);
                break;
            case R.id.saleslayout:
                Intent sales = new Intent(this, HelpCenterSales.class);
                startActivity(sales);
                break;
            case R.id.inventorylayout:
                Intent inventory = new Intent(this, HelpCenterInventory.class);
                startActivity(inventory);
                break;
            case R.id.employeeslayout:
                break;
            case R.id.reportslayout:
                break;
            case R.id.paymentslayout: break;
        }

    }
}
