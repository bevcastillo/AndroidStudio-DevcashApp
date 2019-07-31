package com.example.devcash.ADD_UI;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.devcash.R;

import java.util.Calendar;

public class AddDiscountActivity extends AppCompatActivity implements View.OnClickListener {

    RadioGroup disctype;
    RadioButton discbtn;
    String selecteddisc;
    TextInputEditText startdate,enddate;

    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_discount);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //
        disctype = (RadioGroup) findViewById(R.id.rgroup_disctype);

        startdate = (TextInputEditText) findViewById(R.id.textdisc_startdate);
        enddate = (TextInputEditText) findViewById(R.id.textdisc_enddate);

        //
        startdate.setOnClickListener(this);
        enddate.setOnClickListener(this);
    }

    public void addRadioGroupListener(){
        int radioid = disctype.getCheckedRadioButtonId();
        discbtn = (RadioButton) findViewById(radioid);
        selecteddisc = discbtn.getText().toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.savemenu, menu);
        return true;
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
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }else if(id == R.id.action_save){
            addRadioGroupListener();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.textdisc_startdate:
                final Calendar scalendar = Calendar.getInstance();
                int mYear = scalendar.get(Calendar.YEAR);
                int mMonth = scalendar.get(Calendar.MONTH);
                int mDay = scalendar.get(Calendar.DAY_OF_MONTH);

                //date picker dialog
                datePickerDialog = new DatePickerDialog(AddDiscountActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //set day, month and year in the textinputedittext
                                startdate.setText(dayOfMonth + "/"
                                        + (month + 1) + "/" + year);
                            }
                        },mYear,mMonth,mDay);
                datePickerDialog.show();
                break;
            case R.id.textdisc_enddate:
                final Calendar endcalendar = Calendar.getInstance().getInstance();
                int eYear = endcalendar.get(Calendar.YEAR);
                int eMonth = endcalendar.get(Calendar.MONTH);
                int eDay = endcalendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(AddDiscountActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                enddate.setText(dayOfMonth + "/"
                                                + (month + 1) + "/" +year);
                            }
                        },eYear, eMonth, eDay);
                datePickerDialog.show();
                break;
        }
    }
}
