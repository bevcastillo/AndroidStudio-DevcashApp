package com.example.devcash.Settings_UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.CustomAdapters.OwnerProfileAdapter;
import com.example.devcash.OwnerProfileList;
import com.example.devcash.R;

import java.util.ArrayList;

public class OwnerProfileActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lv;
    //data container
    ArrayList<OwnerProfileList> list = new ArrayList<OwnerProfileList>();
    //adapter
    OwnerProfileAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_profile);

        //instantiate
        lv = (ListView) this.findViewById(R.id.listview_myprofile);
        adapter = new OwnerProfileAdapter(this, list);

        //populate the list
        list.add(new OwnerProfileList("User ID","123456"));
        list.add(new OwnerProfileList("Name","Mary Jane Doe"));
        list.add(new OwnerProfileList("Birthdate","April 8, 1980"));
        list.add(new OwnerProfileList("Gender","Female"));
        list.add(new OwnerProfileList("Mobile number","+63 906 528 3986"));
        list.add(new OwnerProfileList("Username","itsmaryjane"));
        list.add(new OwnerProfileList("Email address","maryjane_doe@gmail.com"));
        list.add(new OwnerProfileList("DEACTIVATE",""));

        //delegate the adapter
        lv.setAdapter(adapter);

        //add listener to the listview
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OwnerProfileList selectedProfileList = this.list.get(position);

        String title = selectedProfileList.getProfileTitle();
        String details = selectedProfileList.getProfileDetails();

        switch (position){
            case 0:
                Toast.makeText(this,"You have clicked User ID.", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(this,"You have clicked Name.", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this,"You have clicked Birthdate.", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this,"You have clicked Gender.", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(this,"You have clicked Mobile number.", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                Toast.makeText(this,"You have clicked Username.", Toast.LENGTH_SHORT).show();
                break;
            case 6:
                Toast.makeText(this,"You have clicked Email address.", Toast.LENGTH_SHORT).show();
                break;
            case 7:
                Toast.makeText(this,"You have clicked Logout.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
