package com.example.devcash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.devcash.CustomAdapters.OwnerProfileAdapter;

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

        //delegate the adapter
        lv.setAdapter(adapter);

        //add listener to the listview
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
