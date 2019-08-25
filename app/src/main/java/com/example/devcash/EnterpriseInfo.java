package com.example.devcash;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.Object.Account;
import com.example.devcash.Object.Business;
import com.example.devcash.Object.Employee;
import com.example.devcash.Object.Enterprise;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class EnterpriseInfo extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatabaseReference ownerdbreference;
    private FirebaseDatabase firebaseDatabase;
    private TextInputEditText entname, entempno, entno, entpermit, entaddr, entemail;
    private String enterprisename, enterprisepermit, enterpriseno, enterpriseaddr, enterprisemail, selectedcategory, selectedentype;
    private Long enterpriseempno;
    private Spinner spinnercategory;
    private TextView entype;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_info);

        entname = (TextInputEditText) findViewById(R.id.edittext_entname);
        entempno = (TextInputEditText) findViewById(R.id.edittext_numemp);
        entno = (TextInputEditText) findViewById(R.id.edittext_entphone);
        entpermit = (TextInputEditText) findViewById(R.id.edittext_permit);
        entaddr = (TextInputEditText) findViewById(R.id.edittext_entaddr);
        entemail = (TextInputEditText) findViewById(R.id.edittext_entemailaddr);
        entype = (TextView) findViewById(R.id.txt_enttype);
        entempno = (TextInputEditText) findViewById(R.id.edittext_numemp);
        spinnercategory = (Spinner) findViewById(R.id.spinner_enttype);

        //listeners
        spinnercategory.setOnItemSelectedListener(this);

        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        ownerdbreference = firebaseDatabase.getReference("/datadevcash/owner");
    }

    @Override
    protected void onStart() {
        super.onStart();

        //
        displayEntDetails();

    }

    public void displayEntDetails(){
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username")
                .equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        String ownerkey = dataSnapshot1.getKey();

                        ownerdbreference.child(ownerkey+"/business/enterprise").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    Enterprise enterprise = dataSnapshot.getValue(Enterprise.class);
                                    enterprisename = enterprise.getEnt_name();
                                    enterpriseaddr = enterprise.getEnt_addr();
                                    enterprisemail = enterprise.getEnt_email();
                                    enterprisepermit = enterprise.getEnt_permitno();
                                    selectedcategory = enterprise.getEnt_cat();
                                    enterpriseno = enterprise.getEnt_telno();
                                    enterpriseempno = enterprise.getEnt_no_emp();
                                    selectedentype = enterprise.getEnt_type();

                                    entname.setText(enterprisename);
                                    entaddr.setText(enterpriseaddr);
                                    entemail.setText(enterprisemail);
                                    entpermit.setText(enterprisepermit);
                                    entype.setText(selectedentype);
                                    entno.setText(enterpriseno);
                                    entempno.setText(enterpriseempno.toString());

                                    String [] enterpriselist = getResources().getStringArray(R.array.ent_types);

                                    for(int i = 0; i<enterpriselist.length; i++){
                                        if(enterpriselist[i].equals(selectedcategory)){
                                            pos = i;
                                        }
                                        spinnercategory.setSelection(pos);
                                        break;
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

    public void updateEntDetails(){
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username")
                .equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        final String ownerkey = dataSnapshot1.getKey();

                        ownerdbreference.child(ownerkey+"/business/enterprise").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    Enterprise enterprise = dataSnapshot.getValue(Enterprise.class);

                                    if(!enterprise.getEnt_name().equals(entname.getText().toString())) {
                                        ownerdbreference.child(ownerkey+"/business/enterprise").child("ent_name").setValue(entname.getText().toString());
                                    }

                                    if(!enterprise.getEnt_addr().equals(entaddr.getText().toString())) {
                                        ownerdbreference.child(ownerkey+"/business/enterprise").child("ent_addr").setValue(entaddr.getText().toString());
                                    }

                                    if(!enterprise.getEnt_email().equals(entemail.getText().toString())) {
                                        ownerdbreference.child(ownerkey+"/business/enterprise").child("ent_email").setValue(entemail.getText().toString());
                                    }

                                    if(!enterprise.getEnt_no_emp().equals(Long.valueOf(entempno.getText().toString()))) {
                                        ownerdbreference.child(ownerkey+"/business/enterprise").child("ent_no_emp").setValue(Long.valueOf(entempno.getText().toString()));
                                    }

                                    if(!enterprise.getEnt_permitno().equals(entpermit.getText().toString())) {
                                        ownerdbreference.child(ownerkey+"/business/enterprise").child("ent_permitno").setValue(entpermit.getText().toString());
                                    }

                                    if(!enterprise.getEnt_telno().equals(entno.getText().toString())) {
                                        ownerdbreference.child(ownerkey+"/business/enterprise").child("ent_telno").setValue(entno.getText().toString());
                                    }

                                    if(!enterprise.getEnt_cat().equals(selectedcategory)) {
                                        ownerdbreference.child(ownerkey+"/business/enterprise").child("ent_cat").setValue(selectedcategory);
                                    }


//                                    ownerdbreference.child(ownerkey+"/business/enterprise").child("ent_name").setValue(entname.getText().toString());
//                                    ownerdbreference.child(ownerkey+"/business/enterprise").child("ent_addr").setValue(entaddr.getText().toString());

//                                    ownerdbreference.child(ownerkey+"/business/enterprise").child("ent_email").setValue(entemail.getText().toString());
//                                    ownerdbreference.child(ownerkey+"/business/enterprise").child("ent_no_emp").setValue(Long.valueOf(entempno.getText().toString()));
//                                    ownerdbreference.child(ownerkey+"/business/enterprise").child("ent_permitno").setValue(entpermit.getText().toString());
//                                    ownerdbreference.child(ownerkey+"/business/enterprise").child("ent_telno").setValue(entno.getText().toString());
//                                    displayEntDetails();
//                                    ownerdbreference.child(ownerkey+"/business/enterprise").child("ent_cat").setValue(selectedcategory);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Toast.makeText(EnterpriseInfo.this, "Enterprise is updated!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.savemenu,menu);
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
        switch (id){
            case R.id.home:
                onBackPressed();
                return true;
            case R.id.action_save:
                updateEntDetails();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int sid = parent.getId();
        switch (sid){
            case R.id.spinner_enttype:
                selectedcategory = this.spinnercategory.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
