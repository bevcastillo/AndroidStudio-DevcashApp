package com.example.devcash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.devcash.CustomAdapters.EnterpriseAdapter;

import java.util.ArrayList;

public class EnterpriseActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lv;
    //data container
    ArrayList<EnterpriseList> list = new ArrayList<EnterpriseList>();
    //adapter
    EnterpriseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise);

        //instantiate
        lv = (ListView) this.findViewById(R.id.listview_enterprise);
        adapter = new EnterpriseAdapter(this, list);

        //populate the list
        list.add(new EnterpriseList("Enterprise ID","123456"));
        list.add(new EnterpriseList("Enterprise Category","Micro"));
        list.add(new EnterpriseList("Enterprise Name","Mary Jane Hardware Supplies"));
        list.add(new EnterpriseList("Enterprise Type","Hardware Supplies"));
        list.add(new EnterpriseList("Total number of employees","10"));
        list.add(new EnterpriseList("Address","Colon Street Cebu City"));
        list.add(new EnterpriseList("Telephone number","(032) 272 1234"));
        list.add(new EnterpriseList("Business permit numner","12345612347894564564"));
        list.add(new EnterpriseList("Business TIN","12345612347894564564"));

        //delegate the adapter
        lv.setAdapter(adapter);

        //add listener to the listview
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
