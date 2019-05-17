package com.example.devcash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.example.devcash.Fragments.CategoriesFragment;
import com.example.devcash.Fragments.DiscountsFragment;
import com.example.devcash.Fragments.EmployeesFragment;
import com.example.devcash.Fragments.ProductsFragment;
import com.example.devcash.Fragments.ReportsFragment;
import com.example.devcash.Fragments.SalesFragment;
import com.example.devcash.Fragments.ServicesFragment;
import com.example.devcash.Settings_UI.SettingsActivity;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ///
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override

    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    //switch for nav drawer menu
    private void displaySelectedScreen(int id){
        Fragment fragment = null;

        switch (id){
            case R.id.nav_dashboard:
                Toast.makeText(this, "Dashboard", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_sales:
                fragment = new SalesFragment();
                break;
            case R.id.nav_inventory:
                break;
            case R.id.nav_products:
                fragment = new ProductsFragment();
                break;
            case R.id.nav_services:
                fragment = new ServicesFragment();
                break;
            case R.id.nav_categories:
                fragment = new CategoriesFragment();
                break;
            case R.id.nav_discounts:
                fragment = new DiscountsFragment();
                break;
            case R.id.nav_employee:
                fragment = new EmployeesFragment();
                break;
            case R.id.nav_reports:
                break;
            case R.id.nav_salesrep:

                break;
            case R.id.nav_inventoryrep:
                fragment = new InventoryReportsFragment();
                break;
            case R.id.nav_payment:
                fragment = new PaymentStatementFragment();
                break;
            case R.id.nav_receipts:
                fragment = new AllReceiptsFragment();
                break;
            case R.id.nav_help:
                Intent toHelp = new Intent(DashboardActivity.this, HelpCenterActivity.class);
                startActivity(toHelp);
                break;
            case R.id.nav_settings:
                Intent tosettings = new Intent(DashboardActivity.this, SettingsActivity.class);
                startActivity(tosettings);
                break;
        }

        if (fragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flcontent, fragment);
            ft.commit();
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedScreen(id);

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
