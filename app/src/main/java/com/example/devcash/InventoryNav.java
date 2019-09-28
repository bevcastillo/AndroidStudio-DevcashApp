package com.example.devcash;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.example.devcash.Fragments.InventoryFragment;
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

public class InventoryNav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout invdrawer;
    NavigationView invnavigationView;
    Toolbar invtoolbar;

    TextView txtEmployeeName, txtEmployeeTask, txtEnterpriseName;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference ownerdbreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_nav);


        invtoolbar = findViewById(R.id.invtoolbar);
        setSupportActionBar(invtoolbar);

        invalidateOptionsMenu();

        invdrawer = findViewById(R.id.invdrawer_layout);
        invnavigationView = findViewById(R.id.invnav_view);

        View invheader = invnavigationView.getHeaderView(0);
        txtEmployeeName = (TextView) invheader.findViewById(R.id.nav_employeeName);
        txtEmployeeTask = (TextView) invheader.findViewById(R.id.nav_employeeTask);
        txtEnterpriseName = (TextView) invheader.findViewById(R.id.nav_enterpriseName);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, invdrawer, invtoolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        invdrawer.addDrawerListener(toggle);
        toggle.syncState();
        invnavigationView.setNavigationItemSelectedListener(this);
        invnavigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(invnavigationView.getMenu().findItem(R.id.nav_invInventory));


        //Firebase Database

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
        String entname = business.enterprise.getEnt_name();

//        ownername.setText(name);
        txtEnterpriseName.setText(entname);

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        final String ownerKey = dataSnapshot1.getKey();

                        ownerdbreference.child(ownerKey+"/business/employee").orderByChild("emp_username").equalTo(empusername).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        String empKey = dataSnapshot2.getKey();
                                        Employee employee = dataSnapshot2.getValue(Employee.class);

                                        String empLastname = employee.getEmp_lname();
                                        String empFirstname = employee.getEmp_fname();
                                        String empTask = employee.getEmp_task();
                                        String empName = empFirstname+" "+empLastname;

                                        txtEmployeeName.setText(""+empName);
                                        txtEmployeeTask.setText(empTask);

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
        invdrawer = findViewById(R.id.drawer_layout);
        if (invdrawer.isDrawerOpen(GravityCompat.START)) {
            invdrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inventory_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    //
    private void displaySelectedInvScreen(int id){
        Fragment invfragment = null;
        switch (id){
            case R.id.nav_invInventory:
                invfragment = new InventoryFragment();
                break;
            case R.id.nav_invprof:
                Intent intent = new Intent(InventoryNav.this, EmployeeAccountInfo.class);
                startActivity(intent);
                break;
            case R.id.nav_invlogout:
                logoutDialog();
                break;
        }

        if (invfragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.invflcontent, invfragment);
            ft.commit();
        }

        invdrawer = findViewById(R.id.invdrawer_layout);
        invdrawer.closeDrawer(GravityCompat.START);
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

                Intent logout = new Intent(InventoryNav.this, OwnerLoginActivity.class);
                startActivity(logout);
                Toast.makeText(InventoryNav.this, "You have been logged out!", Toast.LENGTH_SHORT).show();
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

        displaySelectedInvScreen(id);

        invdrawer = findViewById(R.id.invdrawer_layout);
        invdrawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
