package com.example.devcash.EDIT_UI;

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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.devcash.ADD_UI.AddProductActivity;
import com.example.devcash.Object.Category;
import com.example.devcash.Object.Discount;
import com.example.devcash.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatabaseReference dbreference;
    private DatabaseReference ownerdbreference;
    private FirebaseDatabase firebaseDatabase;

    private TextInputEditText productname, prodprice, prodcondcount, prodstock, prodrop, prodexpdate, prodexpcount;
    private Spinner spinnerunit, spinnercategory, spinnerdiscount, spinnercondition;
    private CheckBox checkBoxavail;
    private RadioGroup radioGroupsoldby;
    private RadioButton radioeach, radioweight;
    private String selectedcategory, selectedunit, selectedcondition, selecteddiscount, strprodname, strprodexpdate, strprodrop, strprodavail;
    private double strprice;
    private int strprodstock, strprodexpcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //
        productname = (TextInputEditText) findViewById(R.id.textinput_editprodname);
        prodprice = (TextInputEditText) findViewById(R.id.textinput_editprice);
        prodcondcount = (TextInputEditText) findViewById(R.id.textinput_editcondcount);
        prodstock = (TextInputEditText) findViewById(R.id.textinput_editinstock);
        prodrop = (TextInputEditText) findViewById(R.id.textinput_editROP);
        prodexpdate = (TextInputEditText) findViewById(R.id.textprod_editexpdate);
        prodexpcount = (TextInputEditText) findViewById(R.id.textinput_editexpcount);
        checkBoxavail = (CheckBox) findViewById(R.id.cbox_editprodavail);
        spinnerunit = (Spinner) findViewById(R.id.spinner_editunit);
        spinnercategory = (Spinner) findViewById(R.id.spinner_editprodcat);
        spinnercondition = (Spinner) findViewById(R.id.spinner_editprodcond);
        spinnerdiscount = (Spinner) findViewById(R.id.spinner_editproddisc);

        //listeners
        spinnerdiscount.setOnItemSelectedListener(this);
        spinnerunit.setOnItemSelectedListener(this);
        spinnercategory.setOnItemSelectedListener(this);
        spinnercondition.setOnItemSelectedListener(this);

        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbreference = firebaseDatabase.getReference("datadevcash");
        ownerdbreference = firebaseDatabase.getReference("datadevcash/owner");

        final ArrayList<String> categories = new ArrayList<String>();
        final ArrayList<String> prodcond = new ArrayList<String>();

        ///

        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        String key = ds.getKey();
                        dbreference.child("owner/"+key+"/business/category").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                    Category category = dataSnapshot1.getValue(Category.class);
                                    categories.add(category.getCategory_name());
                                }
                                categories.add("No Category");
                                categories.add("Create Category");
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(EditProduct.this, R.layout.spinner_categoryitem, categories);
                                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_categoryitem);
                                spinnercategory.setAdapter(spinnerArrayAdapter);
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

        final ArrayList<String> discounts = new ArrayList<String>();

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        String key = ds.getKey();
                        dbreference.child("owner/"+key+"/business/discount").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                    Discount discount1 = dataSnapshot1.getValue(Discount.class);
                                    discounts.add(discount1.getDisc_code());
                                }
                                discounts.add("No Discount");
                                discounts.add("Create Discount");
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(EditProduct.this, R.layout.spinner_discountitem, discounts);
                                spinnerdiscount.setAdapter(spinnerArrayAdapter);
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
    protected void onStart() {
        super.onStart();

        Bundle bundle = this.getIntent().getExtras();
        if (bundle!=null){
            strprodname = bundle.getString("product_name");
            strprice = bundle.getDouble("product_price");
            strprodstock = bundle.getInt("product_stock");
            strprodavail = bundle.getString("product_status");
            strprodexpdate = bundle.getString("product_expdate");
            strprodexpcount = bundle.getInt("product_expcount");
            productname.setText(strprodname);
            prodprice.setText(Double.toString(strprice));
            prodstock.setText(Integer.toString(strprodstock));
            prodexpdate.setText(strprodexpdate);
            prodexpcount.setText(Integer.toString(strprodexpcount));

            Toast.makeText(this, strprodexpcount+" is the count", Toast.LENGTH_SHORT).show();

            if (strprodavail.equals("Available")){
                checkBoxavail.setChecked(true);
            } else{
                checkBoxavail.setChecked(false);
            }
        }
    }

    public void getProductData(){
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        String ownerkey = dataSnapshot1.getKey();


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
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }else if(id == R.id.action_save){
            getProductData();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int sid = parent.getId();

        switch (sid){
            case R.id.spinner_editproddisc:
                selecteddiscount = spinnerdiscount.getItemAtPosition(position).toString();
                break;
            case R.id.spinner_editunit:
                selectedunit = spinnerunit.getItemAtPosition(position).toString();
                break;
            case R.id.spinner_editprodcat:
                selectedcategory = spinnercategory.getItemAtPosition(position).toString();
                break;
            case R.id.spinner_editprodcond:
                selectedcondition = spinnercondition.getItemAtPosition(position).toString();
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
