package com.example.devcash;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.Toast;

import com.example.devcash.Fragments.InventoryFragment;
import com.example.devcash.Fragments.SalesFragment;
import com.example.devcash.Object.Employee;

public class InventoryNav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout invdrawer;
    NavigationView invnav;
    Toolbar invtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_nav);


        invtoolbar = findViewById(R.id.invtoolbar);
        setSupportActionBar(invtoolbar);

        invdrawer = findViewById(R.id.invdrawer_layout);
        invnav = findViewById(R.id.invnav_view);

        View invheader = invnav.getHeaderView(0);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, invdrawer, invtoolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        invdrawer.addDrawerListener(toggle);
        toggle.syncState();
        invnav.setNavigationItemSelectedListener(this);
        invnav.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(invnav.getMenu().findItem(R.id.nav_invInventory));
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
