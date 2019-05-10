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


//        settings_list.add(new SettingsList(R.drawable.ic_profile, "Profile"));
//        settings_list.add(new SettingsList(R.drawable.ic_enterprise, "Enterprise"));
//        settings_list.add(new SettingsList(R.drawable.ic_password, "Password"));
//        settings_list.add(new SettingsList(R.drawable.ic_money, "Billing & Subscription"));
//        settings_list.add(new SettingsList(R.drawable.ic_receipt, "Receipts"));
//        settings_list.add(new SettingsList(R.drawable.ic_notifications, "Notification Settings"));
//        settings_list.add(new SettingsList(R.drawable.ic_help_black_24dp, "About"));

        //instantiate
        lv = (ListView) this.findViewById(R.id.listview_settings);
        adapter = new SettingsAdapter(this, list);

        //populate the list
        list.add(new SettingsList(R.drawable.ic_profile, "Profile"));
        list.add(new SettingsList(R.drawable.ic_enterprise, "Enterprise"));
        list.add(new SettingsList(R.drawable.ic_password, "Password"));
        list.add(new SettingsList(R.drawable.ic_money, "Billing & Subscription"));
        list.add(new SettingsList(R.drawable.ic_receipt, "Receipts"));
        list.add(new SettingsList(R.drawable.ic_notifications, "Notification Settings"));
        list.add(new SettingsList(R.drawable.ic_help_black_24dp, "About"));

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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(getApplicationContext(), "You have clicked " + view.getTag(), Toast.LENGTH_SHORT).show();
    }
}
