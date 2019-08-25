package com.example.devcash.EDIT_UI;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.provider.MediaStore;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.devcash.Object.Discount;
import com.example.devcash.Object.Discountlistdata;
import com.example.devcash.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

public class EditDiscount extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TextInputEditText code, value, startdate, enddate;
    private String selectedtype, selecteddstatus,dcode, dstart, dend;
    double dvalue;
    private Spinner status;
    private RadioGroup radioGrouptype;
    RadioButton radioButtonpercentage, radioButtonamount, btntype;
    private LinearLayout deletelayout;
    private int pos;

    private DatePickerDialog datePickerDialog;

    private DatabaseReference dbreference;
    private DatabaseReference ownerdbreference;
    private FirebaseDatabase firebaseDatabase;


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
        startdate.setOnClickListener(this);
        enddate.setOnClickListener(this);
        deletelayout.setOnClickListener(this);
        status.setOnItemSelectedListener(this);
        //

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbreference = firebaseDatabase.getReference("/datadevcash");
        ownerdbreference = firebaseDatabase.getReference("/datadevcash/owner");
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
            dstart = bundle.getString("discountstart");
            dend = bundle.getString("discountend");
            selectedtype = bundle.getString("discounttype");


            for (int i=0; i<statuslist.length; i++){
                if(statuslist[i].equals(selecteddstatus)){
                    pos = i;
                }
                code.setText(dcode);
                value.setText(Double.toString(dvalue));
                status.setSelection(pos);
                startdate.setText(dstart);
                enddate.setText(dend);
                getDiscDetails();

            }
        }
    }

    public void getDiscDetails(){
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username")
                .equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        final String ownerkey = ds.getKey();
                        ownerdbreference.child(ownerkey+"/business/discount")
                                        .orderByChild("disc_code")
                                        .equalTo(dcode)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                               if (dataSnapshot.exists()){
                                                   for (DataSnapshot ds1: dataSnapshot.getChildren()){
                                                       Discount discount = ds1.getValue(Discount.class);
                                                       selectedtype = discount.getDisc_type();
                                                       if(selectedtype.equals(radioButtonamount.getText())){
                                                           radioButtonamount.setChecked(true);
                                                           radioButtonpercentage.setChecked(false);
                                                       } else {
                                                           radioButtonamount.setChecked(false);
                                                           radioButtonpercentage.setChecked(true);
                                                       }
                                                   }
                                               }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updateDiscount(){
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("/business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(final DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        final String ownerkey = dataSnapshot1.getKey();
                        ownerdbreference.child(ownerkey+"/business/discount")
                                .orderByChild("disc_code")
                                .equalTo(dcode).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for (DataSnapshot ds1: dataSnapshot.getChildren()){
                                        String discountkey = ds1.getKey();
                                        Discount discount = ds1.getValue(Discount.class);
                                        String getcode = discount.getDisc_code();
                                        if(getcode.equals(dcode)){
                                            ownerdbreference.child(ownerkey+"/business/discount/").child(discountkey+"/disc_code").setValue(code.getText().toString());
                                            ownerdbreference.child(ownerkey+"/business/discount/").child(discountkey+"/disc_end").setValue(enddate.getText().toString());
                                            ownerdbreference.child(ownerkey+"/business/discount/").child(discountkey+"/disc_start").setValue(startdate.getText().toString());
                                            ownerdbreference.child(ownerkey+"/business/discount/").child(discountkey+"/disc_status").setValue(selecteddstatus);
                                            ownerdbreference.child(ownerkey+"/business/discount/").child(discountkey+"/disc_type").setValue(selectedtype);
                                            ownerdbreference.child(ownerkey+"/business/discount/").child(discountkey+"/disc_value").setValue(Double.parseDouble(value.getText().toString()));


                                            ownerdbreference.child(ownerkey+"/business/product")
                                                    .orderByChild("/discount/disc_code")
                                                    .equalTo(dcode)
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if(dataSnapshot.exists()){
                                                                for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                                                    ownerdbreference.child(ownerkey+"/business/product").child(dataSnapshot2.getKey()+"/discount/disc_code").setValue(code.getText().toString());
                                                                    ownerdbreference.child(ownerkey+"/business/product").child(dataSnapshot2.getKey()+"/discount/disc_end").setValue(enddate.getText().toString());
                                                                    ownerdbreference.child(ownerkey+"/business/product").child(dataSnapshot2.getKey()+"/discount/disc_start").setValue(startdate.getText().toString());
                                                                    ownerdbreference.child(ownerkey+"/business/product").child(dataSnapshot2.getKey()+"/discount/disc_status").setValue(selecteddstatus);
                                                                    ownerdbreference.child(ownerkey+"/business/product").child(dataSnapshot2.getKey()+"/discount/disc_type").setValue(selectedtype);
                                                                    ownerdbreference.child(ownerkey+"/business/product").child(dataSnapshot2.getKey()+"/discount/disc_value").setValue(Double.parseDouble(value.getText().toString()));

                                                                    ownerdbreference.child(ownerkey+"/business/services")
                                                                            .orderByChild("/discount/disc_code")
                                                                            .equalTo(dcode)
                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                    if(dataSnapshot.exists()){
                                                                                        for (DataSnapshot dataSnapshot3: dataSnapshot.getChildren()){
                                                                                            ownerdbreference.child(ownerkey+"/business/services").child(dataSnapshot3.getKey()+"/discount/disc_code").setValue(code.getText().toString());
                                                                                            ownerdbreference.child(ownerkey+"/business/services").child(dataSnapshot3.getKey()+"/discount/disc_end").setValue(enddate.getText().toString());
                                                                                            ownerdbreference.child(ownerkey+"/business/services").child(dataSnapshot3.getKey()+"/discount/disc_start").setValue(startdate.getText().toString());
                                                                                            ownerdbreference.child(ownerkey+"/business/services").child(dataSnapshot3.getKey()+"/discount/disc_status").setValue(selecteddstatus);
                                                                                            ownerdbreference.child(ownerkey+"/business/services").child(dataSnapshot3.getKey()+"/discount/disc_type").setValue(selectedtype);
                                                                                            ownerdbreference.child(ownerkey+"/business/services").child(dataSnapshot3.getKey()+"/discount/disc_value").setValue(Double.parseDouble(value.getText().toString()));
                                                                                        }
                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                        }

                                    }
                                }

                                Toast.makeText(EditDiscount.this, "Discount is updated!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    Toast.makeText(EditDiscount.this, username+" does not exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void deleteDiscount(){
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("/business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(final DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        final String ownerkey = dataSnapshot1.getKey();
                        ownerdbreference.child(ownerkey+"/business/discount")
                                .orderByChild("disc_code")
                                .equalTo(dcode).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for (DataSnapshot ds1: dataSnapshot.getChildren()){
                                        String discountkey = ds1.getKey();
                                        Discount discount = ds1.getValue(Discount.class);
                                        String getcode = discount.getDisc_code();
                                        if(getcode.equals(dcode)){
                                            ownerdbreference.child(ownerkey+"/business/discount/").child(discountkey+"/disc_code").setValue(null);
                                            ownerdbreference.child(ownerkey+"/business/discount/").child(discountkey+"/disc_end").setValue(null);
                                            ownerdbreference.child(ownerkey+"/business/discount/").child(discountkey+"/disc_start").setValue(null);
                                            ownerdbreference.child(ownerkey+"/business/discount/").child(discountkey+"/disc_status").setValue(null);
                                            ownerdbreference.child(ownerkey+"/business/discount/").child(discountkey+"/disc_type").setValue(null);
                                            ownerdbreference.child(ownerkey+"/business/discount/").child(discountkey+"/disc_value").setValue(null);


                                            ownerdbreference.child(ownerkey+"/business/product")
                                                    .orderByChild("/discount/disc_code")
                                                    .equalTo(dcode)
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if(dataSnapshot.exists()){
                                                                for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                                                    ownerdbreference.child(ownerkey+"/business/product").child(dataSnapshot2.getKey()+"/discount/disc_code").setValue("No Discount");
                                                                    ownerdbreference.child(ownerkey+"/business/product").child(dataSnapshot2.getKey()+"/discount/disc_end").setValue("");
                                                                    ownerdbreference.child(ownerkey+"/business/product").child(dataSnapshot2.getKey()+"/discount/disc_start").setValue("");
                                                                    ownerdbreference.child(ownerkey+"/business/product").child(dataSnapshot2.getKey()+"/discount/disc_status").setValue("");
                                                                    ownerdbreference.child(ownerkey+"/business/product").child(dataSnapshot2.getKey()+"/discount/disc_type").setValue("");
                                                                    ownerdbreference.child(ownerkey+"/business/product").child(dataSnapshot2.getKey()+"/discount/disc_value").setValue("");

                                                                    ownerdbreference.child(ownerkey+"/business/services")
                                                                            .orderByChild("/discount/disc_code")
                                                                            .equalTo(dcode)
                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                    if(dataSnapshot.exists()){
                                                                                        for (DataSnapshot dataSnapshot3: dataSnapshot.getChildren()){
                                                                                            ownerdbreference.child(ownerkey+"/business/services").child(dataSnapshot3.getKey()+"/discount/disc_code").setValue("No Discount");
                                                                                            ownerdbreference.child(ownerkey+"/business/services").child(dataSnapshot3.getKey()+"/discount/disc_end").setValue("");
                                                                                            ownerdbreference.child(ownerkey+"/business/services").child(dataSnapshot3.getKey()+"/discount/disc_start").setValue("");
                                                                                            ownerdbreference.child(ownerkey+"/business/services").child(dataSnapshot3.getKey()+"/discount/disc_status").setValue("");
                                                                                            ownerdbreference.child(ownerkey+"/business/services").child(dataSnapshot3.getKey()+"/discount/disc_type").setValue("");
                                                                                            ownerdbreference.child(ownerkey+"/business/services").child(dataSnapshot3.getKey()+"/discount/disc_value").setValue("");
                                                                                        }
                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                        }

                                    }
                                }

                                Toast.makeText(EditDiscount.this, "Discount is updated!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    Toast.makeText(EditDiscount.this, username+" does not exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                addRadioGroupListener();
                updateDiscount();

        }

        return super.onOptionsItemSelected(item);
    }

     public void addRadioGroupListener(){
        int radioid = radioGrouptype.getCheckedRadioButtonId();
         btntype = (RadioButton) findViewById(radioid);
         selectedtype = btntype.getText().toString();
     }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.layout_delcategory:
                deleteDiscount();
                break;
            case R.id.textdisc_startdate:
                final Calendar scalendar = Calendar.getInstance();
                int sYear = scalendar.get(Calendar.YEAR);
                int sMonth = scalendar.get(Calendar.MONTH);
                int sDay = scalendar.get(Calendar.DAY_OF_MONTH);

                //
                datePickerDialog = new DatePickerDialog(EditDiscount.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        startdate.setText(dayOfMonth + "/"
                                            + (month + 1) + "/" + year) ;
                    }
                }, sYear, sMonth, sDay);
                datePickerDialog.show();
                break;
            case R.id.textdisc_enddate:
                final Calendar ecalendar = Calendar.getInstance();
                int eYear = ecalendar.get(Calendar.YEAR);
                int eMonth = ecalendar.get(Calendar.MONTH);
                int eDay = ecalendar.get(Calendar.DAY_OF_MONTH);

                //
                datePickerDialog = new DatePickerDialog(EditDiscount.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        enddate.setText(dayOfMonth + "/"
                                        + (month + 1) + "/" + year);
                    }
                }, eYear, eMonth, eDay);
                datePickerDialog.show();
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
