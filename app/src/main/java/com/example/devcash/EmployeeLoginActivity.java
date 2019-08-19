package com.example.devcash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_login);

        empusername = (TextInputEditText) findViewById(R.id.textinput_empemail);
        emppassw = (TextInputEditText) findViewById(R.id.textinput_emppassw);
        btnlogin = (Button) findViewById(R.id.btn_loginemp);

        btnlogin.setOnClickListener(this);

        firebasedb = FirebaseDatabase.getInstance();
        databaseReference = firebasedb.getReference("/datadevcash");
        businessownerdbreference = firebasedb.getReference("/datadevcash/owner");
    }

    public void empLogin(){

        SharedPreferences empPref = getApplicationContext().getSharedPreferences("EmpPref", MODE_PRIVATE);
        SharedPreferences ownerPref = getApplicationContext().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = empPref.edit();
        final SharedPreferences.Editor ownerEditor = ownerPref.edit();

        final Gson gson = new Gson();

        final String empuser = empusername.getText().toString();
        final String emppassword = emppassw.getText().toString();

        if(empuser.equals("") || emppassword.equals("")) {
            Toast.makeText(this, "Username and Password is required", Toast.LENGTH_SHORT).show();
            return;
        }

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
                                                            if (employee.getAccount().getAcct_passw().equals(emppassword)) {
                                                                String json = gson.toJson(employee);
                                                                editor.putString("Employee", json);
                                                                editor.commit();
                                                                ownerEditor.putString("owner_username", employee.getEmp_workfor());
                                                                ownerEditor.commit();
                                                                if(employee.getEmp_task().equals("Cashiering")){
                                                                    Intent intent = new Intent(EmployeeLoginActivity.this, CashierNavigation.class);
                                                                    startActivity(intent);
                                                                    Toast.makeText(EmployeeLoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                                                } else if(employee.getEmp_task().equals("Inventory Monitoring")) {
                                                                    Intent invintent = new Intent(EmployeeLoginActivity.this, InventoryNav.class);
                                                                    startActivity(invintent);
                                                                    Toast.makeText(EmployeeLoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(EmployeeLoginActivity.this, "Sorry, you have no task for today.", Toast.LENGTH_SHORT).show();
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



//        databaseReference.child("owner").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                        final String ownerKey = ds.getKey();
//                        if (ds.exists()) {
//                            databaseReference.child("owner/"+ownerKey+"/business/account").addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    Log.i("ACCOUNT SNAPSHOT!!!", dataSnapshot.getKey());
//
//                                    if (dataSnapshot.exists()) {
//                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                                            String accountKey = dataSnapshot1.getKey();
//
//                                            businessownerdbreference.orderByChild("business/account/"+accountKey+"/acct_uname").equalTo(empuser)
//                                                .addListenerForSingleValueEvent(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                        if (dataSnapshot.exists()) {
//                                                            Log.i("Account Tag!!", dataSnapshot.getKey());
//                                                            Toast.makeText(EmployeeLoginActivity.this, "exists "+empuser, Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                    }
//                                                });
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//                            });
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        businessownerdbreference.child("business/account").startAt(empuser).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String key = dataSnapshot.getKey();
//                Toast.makeText(EmployeeLoginActivity.this, key, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_loginemp:
                empLogin();
                break;
        }
    }
}
