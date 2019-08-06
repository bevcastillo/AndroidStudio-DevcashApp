package com.example.devcash.ADD_UI;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/*
created by Beverly Castillo on August 4, 2019
 */

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private DatabaseReference dbreference;
    private DatabaseReference categoryfirebasereference;
    private DatabaseReference discountfirebasereference;
    private FirebaseDatabase firebaseDatabase;
    private String ProductId;


    ImageView prodphoto;
    TextView takephoto, choosephoto;
    TextInputEditText prodexpdate, prodname, prodprice, prodrop;
    Spinner prodcondition, produnit, spinnerprodcategory, spinnerdiscount;
    RadioGroup soldby;
    RadioButton soldbybtn;
    String selectedprodcond, selectedprodunit, selecteddisc, selectedsoldby, selectedcategory;
    CheckBox chkavail;

    private static final int PICK_IMAGE = 100;

    Uri imageUri;
    DatePickerDialog expdatePicker;

    LinearLayout addlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prodphoto = (ImageView) findViewById(R.id.prod_photo);
        takephoto = (TextView) findViewById(R.id.txt_prodtakephoto);
        choosephoto = (TextView) findViewById(R.id.txt_prodchoosephoto);
        prodexpdate = (TextInputEditText) findViewById(R.id.textprod_exp_date);
        prodname = (TextInputEditText) findViewById(R.id.textinput_prodname);
        prodprice = (TextInputEditText) findViewById(R.id.textinput_price);
        prodrop = (TextInputEditText) findViewById(R.id.textinput_ROP);
        prodcondition = (Spinner) findViewById(R.id.spinner_prod_condition);
        produnit = (Spinner) findViewById(R.id.spinner_unit);
        soldby = (RadioGroup) findViewById(R.id.radio_group_soldby);
        chkavail = (CheckBox) findViewById(R.id.cbox_prod_avail);

        //
        spinnerprodcategory = (Spinner) findViewById(R.id.spinner_prodcat);
        spinnerdiscount = (Spinner) findViewById(R.id.spinner_proddisc);

        //
        addlayout = (LinearLayout) findViewById(R.id.addplayout);

        takephoto.setOnClickListener(this);
        choosephoto.setOnClickListener(this);
        prodexpdate.setOnClickListener(this);

        prodcondition.setOnItemSelectedListener(this);
        produnit.setOnItemSelectedListener(this);
        spinnerdiscount.setOnItemSelectedListener(this);
        spinnerprodcategory.setOnItemSelectedListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbreference = firebaseDatabase.getReference("/datadevcash");
        ProductId = dbreference.push().getKey();

        categoryfirebasereference = firebaseDatabase.getReference("/datadevcash/category");
        discountfirebasereference = firebaseDatabase.getReference("/datadevcash/discount");

        final ArrayList<String> categories = new ArrayList<String>();

        categoryfirebasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Category category1 = dataSnapshot1.getValue(Category.class);
                    categories.add(category1.getCategory_name());
                }
                categories.add("No Category");
                categories.add("Create Category");
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AddProductActivity.this, R.layout.spinner_categoryitem, categories);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_categoryitem);
                spinnerprodcategory.setAdapter(spinnerArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final ArrayList<String> discounts = new ArrayList<String>();

        discountfirebasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Discount discount1 = dataSnapshot1.getValue(Discount.class);
                    discounts.add(discount1.getDisc_code());
                }
                discounts.add("No Discount");
                discounts.add("Create Discount");
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AddProductActivity.this, R.layout.spinner_discountitem, discounts);
                spinnerdiscount.setAdapter(spinnerArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void addProduct(String prod_name, String prod_unitof_measure, String prod_status, String prod_soldby, double prod_price, double prod_rop){
        Product product = new Product(prod_name, prod_unitof_measure, prod_status, prod_soldby, prod_price, prod_rop);
        dbreference.child("/products").child(ProductId).setValue(product);
    }

    public void insertProduct(){
        String pname = prodname.getText().toString();
        String pstatus = chkavail.getText().toString();
        double pprice = Double.parseDouble(prodprice.getText().toString());
        double prop = Double.parseDouble(prodrop.getText().toString());
        addProduct(pname, selectedprodunit, pstatus, selectedsoldby, pprice, prop);
        Toast.makeText(getApplicationContext(), "New Product Added!", Toast.LENGTH_SHORT).show();
        finish();

    }

    public void addRadioGroupListener(){
        int radioid=soldby.getCheckedRadioButtonId();
        soldbybtn=(RadioButton)findViewById(radioid);
        selectedsoldby=soldbybtn.getText().toString();

    }

    private void addCheckBoxListener() {
        if(chkavail.isChecked()){
            String avail = "Available";
            chkavail.setText(avail);
        }else{
            String notavail = "Not Available";
            chkavail.setText(notavail);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //inflate SAVE menu
        getMenuInflater().inflate(R.menu.savemenu, menu);
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
            addRadioGroupListener();
            addCheckBoxListener();
            insertProduct();
//            Snackbar.make(addlayout, "New Product Added", Snackbar.LENGTH_LONG)
//                    .setAction("UNDO", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                        }
//                    })
//                    .setActionTextColor(Color.RED)
//                    .show();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.txt_prodchoosephoto:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
                break;
            case R.id.txt_prodtakephoto:
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, 0);
                break;
            case R.id.textprod_exp_date:
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                //date picker dialog
                expdatePicker = new DatePickerDialog(AddProductActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                prodexpdate.setText(dayOfMonth + "/"
                                                    + (month + 1) + "/" + year);
                            }
                        },mYear,mMonth,mDay);
                            expdatePicker.show();
                break;
        }
    }


    //handles opening the camera and gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            prodphoto.setImageURI(imageUri);
        }else{
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            prodphoto.setImageBitmap(bitmap);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int sid = parent.getId();

        switch (sid){
            case R.id.spinner_prod_condition:
                selectedprodcond = this.prodcondition.getItemAtPosition(position).toString();
                break;
            case R.id.spinner_unit:
                selectedprodunit = this.produnit.getItemAtPosition(position).toString();
                break;
            case R.id.spinner_proddisc:
                selecteddisc = this.spinnerdiscount.getItemAtPosition(position).toString();
                if(selecteddisc.equals("Create Discount")){
                    Intent create = new Intent(AddProductActivity.this, AddDiscountActivity.class);
                    startActivity(create);
                }
                break;
            case R.id.spinner_prodcat:
                selectedcategory = this.spinnerprodcategory.getItemAtPosition(position).toString();
                if(selectedcategory.equals("Create Category")){
                    Intent create = new Intent(AddProductActivity.this, AddCategoryActivity.class);
                    startActivity(create);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
