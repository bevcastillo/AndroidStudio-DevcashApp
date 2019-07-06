package com.example.devcash.Settings_UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.devcash.CustomAdapters.EnterpriseAdapter;
import com.example.devcash.Model.EnterpriseList;
import com.example.devcash.R;

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
        list.add(new EnterpriseList("Enterprise ID","123456", R.drawable.ic_chevron_right_white));
        list.add(new EnterpriseList("Enterprise Category","Micro", R.drawable.ic_chevron_right));
        list.add(new EnterpriseList("Enterprise Name","Mary Jane Hardware Supplies", R.drawable.ic_chevron_right));
        list.add(new EnterpriseList("Enterprise Type","Hardware Supplies", R.drawable.ic_chevron_right));
        list.add(new EnterpriseList("Total number of employees","10", R.drawable.ic_chevron_right));
        list.add(new EnterpriseList("Address","Colon Street Cebu City", R.drawable.ic_chevron_right));
        list.add(new EnterpriseList("Telephone number","(032) 272 1234", R.drawable.ic_chevron_right));
        list.add(new EnterpriseList("Business permit numner","12345612347894564564", R.drawable.ic_chevron_right));
        list.add(new EnterpriseList("Business TIN","12345612347894564564", R.drawable.ic_chevron_right));

        //delegate the adapter
        lv.setAdapter(adapter);

        //add listener to the listview
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EnterpriseList selectedEnterpriseList = this.list.get(position);

        String title = selectedEnterpriseList.getEnterpriseTitle();
        String details = selectedEnterpriseList.getEnterpriseDetails();

        switch (position){
            case 0:
                Toast.makeText(this,"You have clicked Enterprise ID.", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(this,"You have clicked Enterprise Category.", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this,"You have clicked Enterprise Name.", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this,"You have clicked Enterprise Type.", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(this,"You have clicked Total number of employees.", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                Toast.makeText(this,"You have clicked Address.", Toast.LENGTH_SHORT).show();
                break;
            case 6:
                Toast.makeText(this,"You have clicked Telephone number.", Toast.LENGTH_SHORT).show();
                break;
            case 7:
                Toast.makeText(this,"You have clicked Business Permit number.", Toast.LENGTH_SHORT).show();
                break;
            case 8:
                Toast.makeText(this,"You have clicked Business TIN.", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
