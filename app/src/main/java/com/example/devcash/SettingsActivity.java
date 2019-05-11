package com.example.devcash;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lv;
    //data container
    ArrayList<SettingsList> list = new ArrayList<SettingsList>();
    //adapter
    SettingsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // show Back button in the app bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //////////


        //instantiate
        lv = (ListView) this.findViewById(R.id.listview_settings);
        adapter = new SettingsAdapter(this, list);

        //populate the list
        list.add(new SettingsList(R.drawable.employee, "Profile"));
        list.add(new SettingsList(R.drawable.store, "Enterprise"));
        list.add(new SettingsList(R.drawable.password, "Password"));
        list.add(new SettingsList(R.drawable.cash_billing, "Billing & Subscription"));
        list.add(new SettingsList(R.drawable.receipt, "Receipts"));
        list.add(new SettingsList(R.drawable.notification, "Notifications Settings"));
        list.add(new SettingsList(R.drawable.info, "About"));

        //delegate the adapter
        lv.setAdapter(adapter);

        //add listener to the listview
        lv.setOnItemClickListener(this);
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

    /// listeners to settings listview
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(getApplicationContext(), "You have clicked " + view.getTag(), Toast.LENGTH_SHORT).show();
    }
}
