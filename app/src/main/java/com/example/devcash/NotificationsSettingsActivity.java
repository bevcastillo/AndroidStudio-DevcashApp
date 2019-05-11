package com.example.devcash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.devcash.CustomAdapters.NotificationSettingsAdapter;

import java.util.ArrayList;

public class NotificationsSettingsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lv;
    //data container
    ArrayList<NotificationsSettingsList> list = new ArrayList<NotificationsSettingsList>();
    //adapter
    NotificationSettingsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_settings);

        //instantiate
        lv = (ListView) this.findViewById(R.id.listview_settingsnotifcation);
        adapter = new NotificationSettingsAdapter(this, list);

        //populate the list
        list.add(new NotificationsSettingsList("Cancelled Transactions","Get notifications when cashier cancels a purchase transaction."));
        list.add(new NotificationsSettingsList("Item Condition Change","Get notifications when inventory checker change the item's condition."));
        list.add(new NotificationsSettingsList("Low Stock Notification","Get notifications on items that are low or out of stock."));

        //delagate the adapter
        lv.setAdapter(adapter);

        //add listnerto the listview
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // code here....
    }
}
