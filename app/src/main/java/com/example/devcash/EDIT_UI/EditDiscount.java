package com.example.devcash.EDIT_UI;

import android.content.DialogInterface;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.devcash.Object.Discount;
import com.example.devcash.Object.Discountlistdata;
import com.example.devcash.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class EditDiscount extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TextInputEditText code, value, startdate, enddate;
    private String dtype, selecteddstatus,dcode, dstart, dend;
    private double dvalue;
    private Spinner status;
    private RadioGroup radioGrouptype;
    RadioButton radioButtonpercentage, radioButtonamount;
    private LinearLayout deletelayout;
    private int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_discount);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        code = (TextInputEditText) findViewById(R.id.textinput_discode);
        radioGrouptype = (RadioGroup) findViewById(R.id.rgroup_disctype);
        radioButtonpercentage = (RadioButton) findViewById(R.id.radiobtn_percent);
        radioButtonamount = (RadioButton) findViewById(R.id.radiobtn_amt);
        value = (TextInputEditText) findViewById(R.id.textinput_amt);
        startdate = (TextInputEditText) findViewById(R.id.textdisc_startdate);
        enddate = (TextInputEditText) findViewById(R.id.textdisc_enddate);
        status = (Spinner) findViewById(R.id.spinner_discstatus);

        deletelayout = (LinearLayout) findViewById(R.id.layout_delcategory);

        //listeners
        deletelayout.setOnClickListener(this);
        status.setOnItemSelectedListener(this);
        //

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.savemenu,menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        String [] statuslist = getResources().getStringArray(R.array.disc_status);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null){
            dcode = bundle.getString("discountcode");
            dvalue = bundle.getDouble("discountvalue");
            selecteddstatus = bundle.getString("discountstatus");

            this.code.setText(dcode);
            this.value.setText(Double.toString(dvalue));

            for (int i=0; i<statuslist.length; i++){
                if(statuslist[i].equals(selecteddstatus)){
                    pos = i;
                }
                status.setSelection(pos);
            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Unsaved changes");
        builder.setMessage("Are you sure you want to leave without saving changes?");
        builder.setPositiveButton("LEAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.home:
                onBackPressed();
                return true;
            case R.id.action_save:

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.layout_delcategory:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int sid = parent.getId();
        switch (sid){
            case R.id.spinner_discstatus:
                selecteddstatus = this.status.getItemAtPosition(position).toString();  
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
