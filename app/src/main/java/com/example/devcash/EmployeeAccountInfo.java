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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.Object.Account;
import com.example.devcash.Object.Employee;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmployeeAccountInfo extends AppCompatActivity {

    private DatabaseReference dbreference;
    private DatabaseReference ownerdbreference;
    private FirebaseDatabase firebaseDatabase;

    private TextView txtname, txtusername, txttask;
    private TextInputEditText editemail, editphone, editpassword;
    private RadioGroup radioGroupgender;
    private RadioButton radioButtongender, radioButtonMale, radioButtonFemale;
    String employeename, employeeusername, employeephone, selectedgender, employeetask, employeeacctpassw, employeemeail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_account_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //
        txtname = (TextView) findViewById(R.id.txtemployeename);
        txttask = (TextView) findViewById(R.id.txtemployeetask);
        txtusername = (TextView) findViewById(R.id.txtemployeeusername);
        editemail = (TextInputEditText) findViewById(R.id.edittext_empemail);
        editphone = (TextInputEditText) findViewById(R.id.edittext_empphone);
        editpassword = (TextInputEditText) findViewById(R.id.edittext_empassword);
        radioGroupgender = (RadioGroup) findViewById(R.id.radiogroup_empgender);
        radioButtonMale = (RadioButton) findViewById(R.id.radioempmale);
        radioButtonFemale = (RadioButton) findViewById(R.id.radioempfemale);


        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbreference = firebaseDatabase.getReference("/datadevcash");
        ownerdbreference = firebaseDatabase.getReference("/datadevcash/owner");
    }

    @Override
    protected void onStart() {
        super.onStart();

        showEmpDetails(); //show employee details
    }

    public void addRadioGroupListener(){
        int radioid = radioGroupgender.getCheckedRadioButtonId();
        radioButtongender = (RadioButton) findViewById(radioid);
        selectedgender = radioButtongender.getText().toString();
    }

    public void showEmpDetails(){
        SharedPreferences empPref = getApplicationContext().getSharedPreferences("EmpPref", MODE_PRIVATE);
        final String empusername = (empPref.getString("emp_username", ""));

        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("/business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        final String ownerkey = dataSnapshot1.getKey();
                        ownerdbreference.child(ownerkey+"/business/employee")
                                .orderByChild("emp_username").equalTo(empusername).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        final String empkey = dataSnapshot2.getKey();
                                        Employee employee = dataSnapshot2.getValue(Employee.class);

                                        employeename = employee.getEmp_fname()+" "+employee.getEmp_lname();
                                        employeeusername = employee.getEmp_username();
                                        employeephone = employee.getEmp_phone();
                                        employeetask = employee.getEmp_task();
                                        selectedgender = employee.getEmp_gender();

                                        txtname.setText(employeename);
                                        txttask.setText(employeetask);
                                        txtusername.setText(employeeusername);
                                        editphone.setText(employeephone);

                                        if (selectedgender.equals(radioButtonMale.getText())){
                                            radioButtonMale.setChecked(true);
                                            radioButtonFemale.setChecked(false);
                                        } else{
                                            radioButtonFemale.setChecked(true);
                                            radioButtonMale.setChecked(false);
                                        }

                                        ownerdbreference.child(ownerkey+"/business/employee")
                                                .orderByChild("/account/acct_uname")
                                                .equalTo(empusername).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.exists()){
                                                    for (DataSnapshot dataSnapshot3: dataSnapshot.getChildren()){
                                                        Employee employee1 = dataSnapshot3.getValue(Employee.class);

                                                        employeeacctpassw = employee1.account.getAcct_passw();
                                                        employeemeail = employee1.account.getAcct_email();
                                                        editpassword.setText(employeeacctpassw);
                                                        editemail.setText(employeemeail);
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updateEmployee(){
        SharedPreferences empPref = getApplicationContext().getSharedPreferences("EmpPref", MODE_PRIVATE);
        final String empusername = (empPref.getString("emp_username", ""));

        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("/business/owner_username")
                .equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        final String ownerkey = dataSnapshot1.getKey();
                        ownerdbreference.child(ownerkey+"/business/account")
                                .orderByChild("acct_uname").equalTo(empusername).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        String acctkey = dataSnapshot2.getKey();
                                        //updating from account
                                        ownerdbreference.child(ownerkey+"/business/account").child(acctkey+"/acct_passw").setValue(editpassword.getText().toString());
                                        ownerdbreference.child(ownerkey+"/business/account").child(acctkey+"/acct_email").setValue(editemail.getText().toString());

                                        //
                                        ownerdbreference.child(ownerkey+"/business/employee").orderByChild("account/acct_uname").equalTo(empusername).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.exists()){
                                                    for(DataSnapshot dataSnapshot3: dataSnapshot.getChildren()){
                                                        String empkey = dataSnapshot3.getKey();
                                                        Employee employee = dataSnapshot3.getValue(Employee.class);
                                                        selectedgender = employee.getEmp_gender();

                                                        ownerdbreference.child(ownerkey+"/business/employee").child(empkey+"/account/acct_passw").setValue(editpassword.getText().toString());
                                                        ownerdbreference.child(ownerkey+"/business/employee").child(empkey+"/account/acct_email").setValue(editemail.getText().toString());
                                                        ownerdbreference.child(ownerkey+"/business/employee").child(empkey+"/emp_phone").setValue(editphone.getText().toString());
                                                        addRadioGroupListener();
                                                        ownerdbreference.child(ownerkey+"/business/employee").child(empkey+"/emp_gender").setValue(selectedgender);

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
                        Toast.makeText(EmployeeAccountInfo.this, "Employee is updated!", Toast.LENGTH_SHORT).show();
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
        switch (id){
            case R.id.home:
                onBackPressed();
                return true;
            case R.id.action_save:
                updateEmployee();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
