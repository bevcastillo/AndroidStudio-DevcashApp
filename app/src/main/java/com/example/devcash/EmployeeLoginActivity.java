package com.example.devcash;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.devcash.Object.Business;
import com.example.devcash.Object.Employee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class EmployeeLoginActivity extends AppCompatActivity implements View.OnClickListener {

    //
    private DatabaseReference databaseReference;
    private DatabaseReference businessownerdbreference;
    private FirebaseDatabase firebasedb;

    TextInputEditText empusername, emppassw;
    Button btnlogin;
    TextInputLayout empusernameLayout, emppasswLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_login);

        empusername = (TextInputEditText) findViewById(R.id.textinput_empemail);
        emppassw = (TextInputEditText) findViewById(R.id.textinput_emppassw);
        btnlogin = (Button) findViewById(R.id.btn_loginemp);
        empusernameLayout = (TextInputLayout) findViewById(R.id.layout_empemail);
        emppasswLayout = (TextInputLayout) findViewById(R.id.layout_emppassw);

        btnlogin.setOnClickListener(this);

        firebasedb = FirebaseDatabase.getInstance();
        databaseReference = firebasedb.getReference("/datadevcash");
        businessownerdbreference = firebasedb.getReference("/datadevcash/owner");
    }

    private boolean validateEmailPassw(){
        String empUsername = empusername.getText().toString().trim();
        String empPassword = emppassw.getText().toString().trim();
        boolean ok = true;

        if(empUsername.isEmpty()){
            empusernameLayout.setError("Username can not be empty!");
            ok = false;
            if (empPassword.isEmpty()){
                emppasswLayout.setError("Password can not be empty!");
                ok = false;
            }else {
                emppasswLayout.setError(null);
                ok = true;
            }
        }else {
            empusernameLayout.setError(null);
            ok = true;
        }

        return ok;
    }

    public void empLogin(){

        SharedPreferences empPref = getApplicationContext().getSharedPreferences("EmpPref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = empPref.edit();


        SharedPreferences ownerPref = getApplicationContext().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final SharedPreferences.Editor ownerEditor = ownerPref.edit();

        SharedPreferences businessPref = getApplicationContext().getSharedPreferences("BusinessPref", MODE_PRIVATE);
        final SharedPreferences.Editor businessEditor = businessPref.edit();

        final Gson gson = new Gson();

        final String empuser = empusername.getText().toString();
        final String emppassword = emppassw.getText().toString();

        databaseReference.child("owner")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            for (final DataSnapshot snap : snapshot.getChildren()) {
                                databaseReference.child("owner/"+snap.getKey())
                                            .child("business")
                                            .child("employee")
                                            .orderByChild("account/acct_uname")
                                            .equalTo(empuser)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.exists()) {
                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                            Employee employee = snapshot.getValue(Employee.class);

                                                            // query business
                                                            businessownerdbreference.orderByChild("business/owner_username")
                                                                    .equalTo(employee.getEmp_workfor())
                                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                            if (dataSnapshot.exists()) {
                                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                    final String businessKey = dataSnapshot1.getKey();

                                                                                    businessownerdbreference.child(businessKey+"/business").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                            if (dataSnapshot.exists()) {
                                                                                                Business business = dataSnapshot.getValue(Business.class);

                                                                                                // Save to shared preference
                                                                                                String businessJson = gson.toJson(business);
                                                                                                businessEditor.putString("business", businessJson);
                                                                                                businessEditor.commit();
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
                                                            // end query business.

                                                            if (employee.getAccount().getAcct_passw().equals(emppassword)) {
                                                                String json = gson.toJson(employee);
                                                                editor.putString("Employee", json);
                                                                editor.putString("emp_username", employee.getEmp_username());
                                                                editor.putString("account_type", "Employee");
                                                                editor.commit();
                                                                ownerEditor.putString("owner_username", employee.getEmp_workfor());
                                                                ownerEditor.commit();
                                                                if(employee.getEmp_task().equals("Cashiering")){
                                                                    editor.putString("emp_task", "Cashiering");
                                                                    editor.commit();

                                                                    Intent intent = new Intent(EmployeeLoginActivity.this, CashierNavigation.class);
                                                                    startActivity(intent);
                                                                    Toast.makeText(EmployeeLoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                                                } else if(employee.getEmp_task().equals("Inventory Monitoring")) {
                                                                    Intent invintent = new Intent(EmployeeLoginActivity.this, InventoryNav.class);
                                                                    startActivity(invintent);
                                                                    Toast.makeText(EmployeeLoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    noTaskDialog();
//                                                                    Toast.makeText(EmployeeLoginActivity.this, "Sorry, you have no task for today.", Toast.LENGTH_SHORT).show();
                                                                }

                                                            } else {
                                                                Toast.makeText(EmployeeLoginActivity.this, "Username/Password is incorrect, please try again.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                            }
                        } else {
                            Toast.makeText(EmployeeLoginActivity.this, "Username/Password is incorrect, please try again.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void noTaskDialog() {
        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View noTaskView = inflater.inflate(R.layout.customdialog_employeenotask, null);
        dialogbuilder.setView(noTaskView);

        dialogbuilder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();;
            }
        });
        dialogbuilder.show();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_loginemp:
                if (validateEmailPassw()){
                    empLogin();
                }
                break;
        }
    }
}
