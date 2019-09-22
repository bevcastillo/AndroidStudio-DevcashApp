package com.example.devcash;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.Fragments.CashierAllPurchaseFragment;
import com.example.devcash.Fragments.PurchaseInventorylistFragment;
import com.example.devcash.Fragments.SalesFragment;
import com.example.devcash.Object.Account;
import com.example.devcash.Object.Business;
import com.example.devcash.Object.Employee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class CashierNavigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout cashierdrawer;
    NavigationView cashiernav;
    Toolbar cashiertoolbar;

    TextView txtEmployeeName, txtEmployeeTask, txtEnterpriseName;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference ownerdbreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_navigation);

        //

        cashiertoolbar = findViewById(R.id.cashiertoolbar);
        setSupportActionBar(cashiertoolbar);

        cashierdrawer = findViewById(R.id.cashierdrawer_layout);
        cashiernav = findViewById(R.id.cashiernav_view);


        //
        View cashheader = cashiernav.getHeaderView(0);
        txtEmployeeName = (TextView) cashheader.findViewById(R.id.nav_cashierName);
        txtEmployeeTask = (TextView) cashheader.findViewById(R.id.nav_cashierTask);
        txtEnterpriseName = (TextView) cashheader.findViewById(R.id.nav_cashierenterpriseName);


        //
        ActionBarDrawerToggle cashiertoggle = new ActionBarDrawerToggle(
                this, cashierdrawer, cashiertoolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        cashierdrawer.addDrawerListener(cashiertoggle);
        cashiertoggle.syncState();
        cashiernav.setNavigationItemSelectedListener(this);
        cashiernav.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(cashiernav.getMenu().findItem(R.id.nav_cashiersales));

        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        ownerdbreference = firebaseDatabase.getReference("datadevcash/owner");

    }

    @Override
    protected void onStart() {
        super.onStart();


        SharedPreferences ownerPref = getApplicationContext().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (ownerPref.getString("owner_username", ""));

        SharedPreferences businessPref = getApplicationContext().getSharedPreferences("BusinessPref", MODE_PRIVATE);

        SharedPreferences empPref = getApplicationContext().getSharedPreferences("EmpPref", MODE_PRIVATE);
        final String empusername = (empPref.getString("emp_username", ""));


        Gson gson = new Gson();
        String json = ownerPref.getString("account", "");
        String businessJson = businessPref.getString("business", "");

        Account account = gson.fromJson(json, Account.class);
        Business business = gson.fromJson(businessJson, Business.class);

        String lname = business.getOwner_lname();
        String fname = business.getOwner_fname();
        String employee_name = fname+" "+lname;
//        String entname = business.enterprise.getEnt_name();
        String entname = business.getEnterprise().getEnt_name();

//        ownername.setText(name);
        txtEnterpriseName.setText(entname);

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        String ownerKey = dataSnapshot1.getKey();

                        ownerdbreference.child(ownerKey+"/business/employee").orderByChild("emp_username").equalTo(empusername).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        String employeeKey = dataSnapshot2.getKey();
                                        Employee employee = dataSnapshot2.getValue(Employee.class);
                                        String employeeLastname = employee.getEmp_lname();
                                        String employeeFirstname = employee.getEmp_fname();
                                        String employeeTask = employee.getEmp_task();

                                        txtEmployeeName.setText(employeeFirstname+" "+employeeLastname);
                                        txtEmployeeTask.setText(employeeTask);


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


    @Override
    public void onBackPressed() {
        cashierdrawer = findViewById(R.id.cashierdrawer_layout);
        if (cashierdrawer.isDrawerOpen(GravityCompat.START)) {
            cashierdrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    private void dispSelectedCashierScreen(int id){
        Fragment cashfragment = null;

        switch (id){
            case R.id.nav_cashiersales:
                cashfragment = new PurchaseInventorylistFragment();
//                cashfragment = new CashierAllPurchaseFragment();
                break;
            case R.id.nav_cashierprof:
                Intent intent = new Intent(CashierNavigation.this, EmployeeAccountInfo.class);
                startActivity(intent);
                break;
            case R.id.nav_cashierlogout:
                logoutDialog();
                break;
        }

        if(cashfragment!=null){
            FragmentTransaction cashft = getSupportFragmentManager().beginTransaction();
            cashft.replace(R.id.empflcontent, cashfragment);
            cashft.commit();
        }

        cashierdrawer = findViewById(R.id.cashierdrawer_layout);
        cashierdrawer.closeDrawer(GravityCompat.START);
    }

    public void logoutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("LEAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();

                SharedPreferences empPref = getApplicationContext().getSharedPreferences("EmpPref", MODE_PRIVATE);
                SharedPreferences.Editor empeditor = empPref.edit();

                editor.clear();
                empeditor.clear();
                editor.commit();
                empeditor.commit();
                Intent cashierlogout = new Intent(CashierNavigation.this, IndexActivity.class);
                startActivity(cashierlogout);
                Toast.makeText(CashierNavigation.this, "You have been logged out!", Toast.LENGTH_SHORT).show();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        dispSelectedCashierScreen(id);

        cashierdrawer = findViewById(R.id.cashierdrawer_layout);
        cashierdrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static class NoTask extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_no_task);
        }
    }
}
