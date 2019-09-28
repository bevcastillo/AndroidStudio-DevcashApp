package com.example.devcash;

import android.accounts.OnAccountsUpdateListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.Fragments.AllReceiptsFragment;
import com.example.devcash.Fragments.EmployeesFragment;
import com.example.devcash.Fragments.InventoryFragment;
import com.example.devcash.Fragments.PaymentStatementFragment;
import com.example.devcash.Fragments.PurchaseInventorylistFragment;
import com.example.devcash.Fragments.SalesFragment;
import com.example.devcash.Object.Account;
import com.example.devcash.Object.Accountlistdata;
import com.example.devcash.Object.Business;
import com.example.devcash.Object.Owner;
import com.example.devcash.Object.Ownerlistdata;
import com.example.devcash.Settings_UI.SettingsActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageView ownerImage;

    private DatabaseReference dbreference;
    private DatabaseReference ownerdbreference;
    private FirebaseDatabase firebaseDatabase;

    TextView ownername, enterprisename, accountype;

    List<Accountlistdata> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ///
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //

        invalidateOptionsMenu();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        ownername = (TextView) header.findViewById(R.id.header_ownername);
        enterprisename = (TextView) header.findViewById(R.id.header_entname);
        accountype = (TextView) header.findViewById(R.id.header_acctype);
        ownerImage = (ImageView) header.findViewById(R.id.imageView);
        //

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbreference = firebaseDatabase.getReference("/datadevcash");
        ownerdbreference = firebaseDatabase.getReference("/datadevcash/owner");


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_sales));

    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences ownerPref = getApplicationContext().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        SharedPreferences businessPref = getApplicationContext().getSharedPreferences("BusinessPref", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = ownerPref.getString("account", "");
        String businessJson = businessPref.getString("business", "");

        Account account = gson.fromJson(json, Account.class);
        Business business = gson.fromJson(businessJson, Business.class);

        String lname = business.getOwner_lname();
        String fname = business.getOwner_fname();
        String name = fname+" "+lname;
        String entname = business.enterprise.getEnt_name();
        String owner_image  = business.getOwner_image();
        String enterprise_logo = business.getEnterprise().getEnt_image();

        Picasso.with(this).load(enterprise_logo).into(ownerImage);

        ownername.setText(name);
        enterprisename.setText(entname);
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

//        MenuItem item = menu.findItem(R.id.nav_inventory);
//        item.findItem(setVisible(false));



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
            case R.id.nav_sales:
                fragment = new PurchaseInventorylistFragment();
//                fragment = new SalesFragment();
                break;
            case R.id.nav_inventory:
                fragment = new InventoryFragment();
                break;
            case R.id.nav_employee:
                fragment = new EmployeesFragment();
                break;
            case R.id.nav_reports:
                String url = "https://www.devcash.com.ph/signup.php";
                Intent devcash = new Intent(Intent.ACTION_VIEW);
                devcash.setData(Uri.parse(url));
                startActivity(devcash);
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
            case R.id.nav_logout:
                logoutDialog();
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

    public void logoutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("LEAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences ownerPref = getSharedPreferences("OwnerPref", MODE_PRIVATE);
                SharedPreferences custIdShared = getApplicationContext().getSharedPreferences("CustomerIdPref", MODE_PRIVATE);
                SharedPreferences empPref = getApplicationContext().getSharedPreferences("EmpPref", MODE_PRIVATE);
                SharedPreferences customerTypePref = getApplicationContext().getSharedPreferences("CustomerTypePref", MODE_PRIVATE);
                SharedPreferences businessPref = getApplicationContext().getSharedPreferences("BusinessPref", MODE_PRIVATE);

                SharedPreferences.Editor editor = ownerPref.edit();
                SharedPreferences.Editor editor1 = custIdShared.edit();
                SharedPreferences.Editor editor2 = empPref.edit();
                SharedPreferences.Editor editor3 = customerTypePref.edit();
                SharedPreferences.Editor editor4 = businessPref.edit();

                editor.clear().commit();
                editor1.clear().commit();
                editor2.clear().commit();
                editor3.clear().commit();
                editor4.clear().commit();

                Intent logout = new Intent(DashboardActivity.this, IndexActivity.class);
                startActivity(logout);
                Toast.makeText(DashboardActivity.this, "You have been logged out!", Toast.LENGTH_SHORT).show();
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

        displaySelectedScreen(id);

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
