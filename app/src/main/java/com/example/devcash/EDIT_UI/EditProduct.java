package com.example.devcash.EDIT_UI;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.devcash.ADD_UI.AddProductActivity;
import com.example.devcash.Object.Category;
import com.example.devcash.Object.Discount;
import com.example.devcash.Object.Product;
import com.example.devcash.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class EditProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private DatabaseReference dbreference;
    private DatabaseReference ownerdbreference;
    private FirebaseDatabase firebaseDatabase;

    private TextInputEditText productname, prodprice, prodcondcount, prodstock, prodrop, prodexpdate, prodexpcount;
    private Spinner spinnerunit, spinnercategory, spinnerdiscount, spinnercondition;
    private CheckBox checkBoxavail;
    private RadioGroup radioGroupsoldby;
    private RadioButton radioeach, radioweight;
    private String selectedcategory, selectedunit, selectedcondition, selecteddiscount, strprodname, strprodexpdate, strprodavail, prodreference;
    private double strprice, strprodrop;
    private int strprodstock, strprodexpcount, pos, pos1;
    private LinearLayout deletelayout;
    private TextInputLayout productNameLayout;
    private String discountCode, discountStart, discountEnd, discountType, discountStatus, strExpirationDate, newExpirationDate;
    private double productPrice, discountValue, discountedPrice;
    private DatePickerDialog datePickerDialog;

    ArrayList<String> categoryArrayList = new ArrayList<String>();
    ArrayList<String> discountArrayList = new ArrayList<String>();



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
        deletelayout = (LinearLayout) findViewById(R.id.layout_delcategory);
        productNameLayout = (TextInputLayout) findViewById(R.id.editProdname_layout);

        //listeners
        spinnerdiscount.setOnItemSelectedListener(this);
        spinnerunit.setOnItemSelectedListener(this);
        spinnercategory.setOnItemSelectedListener(this);
        spinnercondition.setOnItemSelectedListener(this);
        deletelayout.setOnClickListener(this);
        prodexpdate.setOnClickListener(this);

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
                                    categoryArrayList.add(category.getCategory_name());
                                }
                                categories.add("No Category");
                                categories.add("Create Category");
                                categoryArrayList.add("No Category");
                                categoryArrayList.add("Create Category");
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
                                    discountArrayList.add(discount1.getDisc_code());
                                }
                                discounts.add("No Discount");
                                discounts.add("Create Discount");
                                discountArrayList.add("No Discount");
                                discountArrayList.add("Create Discount");
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

        getProdDetails();

        Bundle bundle = this.getIntent().getExtras();
        if (bundle!=null){
            strprodname = bundle.getString("product_name");
            strprice = bundle.getDouble("product_price");
            strprodstock = bundle.getInt("product_stock");
            strprodavail = bundle.getString("product_status");
            strprodexpdate = bundle.getString("product_expdate");
            strprodexpcount = bundle.getInt("product_expcount");
            strprodrop = bundle.getDouble("product_rop");

            prodreference = strprodname+""+strprodexpdate;

            productname.setText(strprodname);
            prodprice.setText(Double.toString(strprice));
            prodstock.setText(Integer.toString(strprodstock));

            if (prodexpdate.equals("No Expiration")){
                prodexpdate.setText("");
            }

            if (!prodexpdate.equals("No Expiration")){
                prodexpdate.setText(strprodexpdate);
            }

            prodexpcount.setText(Integer.toString(strprodexpcount));
            prodrop.setText(Double.toString(strprodrop));

            if (strprodavail.equals("Available")){
                checkBoxavail.setChecked(true);
            } else{
                checkBoxavail.setChecked(false);
            }
        }
    }

    private void getProdDetails() {
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        final String ownerKey = dataSnapshot1.getKey();

                        ownerdbreference.child(ownerKey+"/business/product").orderByChild("prod_reference").equalTo(prodreference).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        String productKey = dataSnapshot2.getKey();
                                        Product product = dataSnapshot2.getValue(Product.class);
                                        selectedcategory = product.getCategory().getCategory_name();
                                        selecteddiscount = product.getDiscount().getDisc_code();

                                        for(int i = 0; i < categoryArrayList.size(); i++ ){
                                            if(categoryArrayList.get(i).equals(selectedcategory)){
                                                pos = i;
                                            }
                                            spinnercategory.setSelection(pos);
                                        }

                                        for(int i = 0; i < discountArrayList.size(); i++ ){
                                            if(discountArrayList.get(i).equals(selecteddiscount)){
                                                pos1 = i;
                                            }
                                            spinnerdiscount.setSelection(pos1);
                                        }


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
            updateProduct();

        }
        return super.onOptionsItemSelected(item);

    }

    private void updateProduct() {
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        final String ownerKey = dataSnapshot1.getKey();

                        ownerdbreference.child(ownerKey+"/business/product").orderByChild("prod_reference").equalTo(prodreference).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        final String productKey = dataSnapshot2.getKey();
                                        Product product = dataSnapshot2.getValue(Product.class);
                                        strprodavail = product.getProd_status();
//                                        if (strExpirationDate.equals("")){
//                                            newExpirationDate = "No Expiration";
//                                            Toast.makeText(EditProduct.this, newExpirationDate+"", Toast.LENGTH_SHORT).show();
//                                        }else {
//                                            newExpirationDate = strExpirationDate;
//                                            Toast.makeText(EditProduct.this, newExpirationDate+"", Toast.LENGTH_SHORT).show();
//                                        }

//                                        if (!strExpirationDate.equals(null)){
//                                            Toast.makeText(EditProduct.this, strExpirationDate+"", Toast.LENGTH_SHORT).show();
//                                        }else {
//                                            Toast.makeText(EditProduct.this, "does not expire", Toast.LENGTH_SHORT).show();
//                                        }
                                        final String newProductReference = productname.getText().toString()+""+newExpirationDate;

                                        if (checkBoxavail.isChecked()){
                                            strprodavail = "Available";
                                        }else {
                                            strprodavail = "Not Available";
                                        }



//                                        ownerdbreference.child(ownerKey+"/business/product/").child(productKey+"/prod_status").setValue(strprodavail);
//                                        ownerdbreference.child(ownerKey+"/business/product/").child(productKey+"/prod_name").setValue(productname.getText().toString());
//                                        ownerdbreference.child(ownerKey+"/business/product/").child(productKey+"/prod_price").setValue(Double.parseDouble(prodprice.getText().toString()));
//                                        ownerdbreference.child(ownerKey+"/business/product/").child(productKey+"/prod_stock").setValue(Integer.parseInt(prodstock.getText().toString()));
//                                        ownerdbreference.child(ownerKey+"/business/product/").child(productKey+"/prod_rop").setValue(Double.parseDouble(prodrop.getText().toString()));
//                                        ownerdbreference.child(ownerKey+"/business/product/").child(productKey+"/prod_unitof_measure").setValue(selectedunit);
//                                        ownerdbreference.child(ownerKey+"/business/product/").child(productKey+"/prod_expdate").setValue(newExpirationDate);
//                                        ownerdbreference.child(ownerKey+"/business/product/").child(productKey+"/prod_reference").setValue(newProductReference);

                                        //updating the product/qrCode
//                                        ownerdbreference.child(ownerKey+"/business/product/").child(productKey+"/qrCode/qr_code").setValue(productname.getText().toString());
//                                        ownerdbreference.child(ownerKey+"/business/product/").child(productKey+"/qrCode/qr_price").setValue(Double.parseDouble(prodprice.getText().toString()));
//                                        ownerdbreference.child(ownerKey+"/business/product/").child(productKey+"/qrCode/qr_reference").setValue(newProductReference);

                                        //updating the discount and category
//                                        ownerdbreference.child(ownerKey+"/business/product/").child(productKey+"/discount/disc_code").setValue(selecteddiscount);
//                                        ownerdbreference.child(ownerKey+"/business/product/").child(productKey+"/category/category_name").setValue(selectedcategory);

                                        //updating the discounted price based on the selected discount
                                        ownerdbreference.child(ownerKey+"/business/discount").orderByChild("disc_code").equalTo(selecteddiscount).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()){
                                                    //the selected discount exists
                                                    for (DataSnapshot dataSnapshot3: dataSnapshot.getChildren()){
                                                        String discountKey = dataSnapshot3.getKey();
                                                        Discount discount = dataSnapshot3.getValue(Discount.class);
                                                        discountStatus = discount.getDisc_status();
                                                        discountStart = discount.getDisc_start();
                                                        discountEnd = discount.getDisc_end();
                                                        discountCode = discount.getDisc_code();
                                                        discountType = discount.getDisc_type();
                                                        discountValue = discount.getDisc_value();

                                                        productPrice = Double.parseDouble(prodprice.getText().toString());
                                                        if (discountStatus.equals("Active")){
                                                            if (discountType.equals("Percentage")){
//                                                                discountedPrice = productPrice - discountValue;
//                                                                ownerdbreference.child(ownerKey+"/business/product/").child(productKey+"/discounted_price").setValue(discountedPrice);
//                                                                ownerdbreference.child(ownerKey+"/business/product/").child(productKey+"/qrCode/qr_disc_price").setValue(discountedPrice);
                                                            }else {
                                                                //type is amount
//                                                                discountedPrice = productPrice - discountValue;
//                                                                ownerdbreference.child(ownerKey+"/business/product/").child(productKey+"/discounted_price").setValue(discountedPrice);
//                                                                ownerdbreference.child(ownerKey+"/business/product/").child(productKey+"/qrCode/qr_disc_price").setValue(discountedPrice);
                                                            }
                                                        }else {
                                                            //the discount is not active
                                                        }
                                                    }

                                                }else {
                                                    //the selected discount is "No Discount", hence, we just set the discounted price to the original price
//                                                    ownerdbreference.child(ownerKey+"/business/product/").child(productKey+"/discounted_price").setValue(Double.parseDouble(prodprice.getText().toString()));
//                                                    ownerdbreference.child(ownerKey+"/business/product/").child(productKey+"/qrCode/qr_disc_price").setValue(Double.parseDouble(prodprice.getText().toString()));
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                        //updating the qrCode node
                                        ownerdbreference.child(ownerKey+"/business/qrCode").orderByChild("qr_reference").equalTo(newProductReference).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()){
                                                    Toast.makeText(EditProduct.this, "reference exists from qrCode"+newProductReference, Toast.LENGTH_SHORT).show();
                                                }else {
                                                    Toast.makeText(EditProduct.this, "reference does not exist"+newProductReference, Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                    Toast.makeText(EditProduct.this, "Product has been successfully updated.", Toast.LENGTH_SHORT).show();
                                    finish();
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.layout_delcategory:
                deleteProduct();
                break;
            case R.id.textprod_editexpdate:
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(EditProduct.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                prodexpdate.setText(dayOfMonth + "/"
                                        + (month + 1) + "/" + year);
                                strExpirationDate = prodexpdate.getText().toString();

                            }
                        },mYear,mMonth,mDay);
                datePickerDialog.show();
                break;
        }
    }

    private void deleteProduct() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this product?");
        builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
                final String username = (shared.getString("owner_username", ""));

                ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                final String ownerKey = dataSnapshot1.getKey();

                                ownerdbreference.child(ownerKey+"/business/product").orderByChild("prod_reference").equalTo(prodreference).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                                String prodKey = dataSnapshot2.getKey();
                                                Product product = dataSnapshot2.getValue(Product.class);

                                                ownerdbreference.child(ownerKey+"/business/product/").child(prodKey).setValue(null);

                                                ownerdbreference.child(ownerKey+"/business/qrCode").orderByChild("qr_reference").equalTo(prodreference).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()){
                                                            for (DataSnapshot dataSnapshot3: dataSnapshot.getChildren()){
                                                                String qrCodeKey = dataSnapshot3.getKey();

                                                                ownerdbreference.child(ownerKey+"/business/qrCode").child(qrCodeKey).setValue(null);
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                            Toast.makeText(EditProduct.this, "Product has been deleted.", Toast.LENGTH_SHORT).show();
                                            finish();
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
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
