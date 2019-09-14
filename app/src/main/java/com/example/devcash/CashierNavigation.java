package com.example.devcash;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.Toast;

import com.example.devcash.Fragments.PurchaseInventorylistFragment;
import com.example.devcash.Fragments.SalesFragment;

public class CashierNavigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout cashierdrawer;
    NavigationView cashiernav;
    Toolbar cashiertoolbar;

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


        //
        ActionBarDrawerToggle cashiertoggle = new ActionBarDrawerToggle(
                this, cashierdrawer, cashiertoolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        cashierdrawer.addDrawerListener(cashiertoggle);
        cashiertoggle.syncState();
        cashiernav.setNavigationItemSelectedListener(this);
        cashiernav.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(cashiernav.getMenu().findItem(R.id.nav_cashiersales));

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
//        getMenuInflater().inflate(R.menu.cashier_navigation, menu);
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

    //method for the cashier navigation
    private void dispSelectedCashierScreen(int id){
        Fragment cashfragment = null;

        switch (id){
            case R.id.nav_cashiersales:
                cashfragment = new PurchaseInventorylistFragment();
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
                Intent cashierlogout = new Intent(CashierNavigation.this, EmployeeLoginActivity.class);
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

//        switch (id){
//            case R.id.nav_empsales:
//                fragment = new SalesFragment();
//                break;
//            case R.id.nav_empprof:
//                Toast.makeText(this, "Employee Profile", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.nav_emplogout:
//                Toast.makeText(this, "Employee Logout", Toast.LENGTH_SHORT).show();
//                break;
//        }
//
//        if (fragment != null){
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.flcontent, fragment);
//            ft.commit();
//        }

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
    }

    public static class NoTask extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_no_task);
        }
    }
}
