package com.example.devcash.ADD_UI;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.devcash.Object.Discount;
import com.example.devcash.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class AddDiscountActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private DatabaseReference dbreference;
    private DatabaseReference ownerdbreference;
    private FirebaseDatabase firebaseInstance;
    private String DiscountId;

    RadioGroup disctype;
    RadioButton discbtn;
    String selecteddisc, selectedstatus;
    TextInputEditText disc_code, disc_value, disc_startdate, disc_enddate;
    Spinner spinnerstatus;

    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_discount);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //
        disc_code = (TextInputEditText) findViewById(R.id.textinput_discode);
        disc_value = (TextInputEditText) findViewById(R.id.textinput_amt);
        disc_startdate = (TextInputEditText) findViewById(R.id.textdisc_startdate);
        disc_enddate = (TextInputEditText) findViewById(R.id.textdisc_enddate);
        disctype = (RadioGroup) findViewById(R.id.rgroup_disctype);
        spinnerstatus = (Spinner) findViewById(R.id.spinner_discstatus);

        //
        disc_startdate.setOnClickListener(this);
        disc_enddate.setOnClickListener(this);
        spinnerstatus.setOnItemSelectedListener(this);

        firebaseInstance = FirebaseDatabase.getInstance();
        dbreference = firebaseInstance.getReference("/datadevcash");
        ownerdbreference = firebaseInstance.getReference("/datadevcash/owner");
        DiscountId = dbreference.push().getKey();
    }

    public void addDiscount(String disc_code, String disc_type, String disc_start, String disc_end, String disc_status, double disc_value){
        final Discount discount = new Discount(disc_code, disc_type, disc_start, disc_end, disc_status, disc_value);

        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        String key = ds.getKey();
                        dbreference.child("owner/"+key+"/business/discount").child(DiscountId).setValue(discount);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void insertDiscount(){
        String code = disc_code.getText().toString();
        double value = Double.parseDouble(disc_value.getText().toString());
        String start = disc_startdate.getText().toString();
        String end = disc_enddate.getText().toString();
        addDiscount(code, selecteddisc, start, end, selectedstatus, value);
        Toast.makeText(getApplicationContext(), "Discount Successfully added!", Toast.LENGTH_SHORT).show();
        finish();
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
            insertDiscount();
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
                                disc_startdate.setText(dayOfMonth + "/"
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
                                disc_enddate.setText(dayOfMonth + "/"
                                                + (month + 1) + "/" +year);
                            }
                        },eYear, eMonth, eDay);
                datePickerDialog.show();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int sid = parent.getId();
        switch (sid){
            case R.id.spinner_discstatus:
                selectedstatus = this.spinnerstatus.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
