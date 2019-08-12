package com.example.devcash.ADD_UI;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
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
import com.example.devcash.Object.ProductCondition;
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
    private DatabaseReference mydbreference;
    private DatabaseReference categoryfirebasereference;
    private DatabaseReference discountfirebasereference;
    private DatabaseReference prodcondfirebasereference;
    //
    private DatabaseReference businessprodfirebasereference;
    private FirebaseDatabase firebaseDatabase;
    private String ProductId;
    private String ProductCondId;


    ImageView prodphoto, addexpdate, addcondition;
    TextView takephoto, choosephoto;
    TextInputEditText prodexpdate, prodname, prodprice, prodrop, condcount, prodstock;
    Spinner prodcondition, produnit, spinnerprodcategory, spinnerdiscount;
    RadioGroup soldby;
    RadioButton soldbybtn;
    String selectedprodcond, selectedprodunit, selecteddisc, selectedsoldby, selectedcategory;
    CheckBox chkavail;
    LinearLayout prodcondlayout, prodexpdatelayout;
    int conditioncount;

    private static final int PICK_IMAGE = 100;

    Uri imageUri;
    DatePickerDialog expdatePicker;

    LinearLayout addlayout;
    private int mClickCounter = 0;

    Button btnshow;
    Spinner spinnercondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prodphoto = (ImageView) findViewById(R.id.prod_photo);
        addexpdate = (ImageView) findViewById(R.id.imgview_addexpdate);
        addcondition = (ImageView) findViewById(R.id.imgview_addcond);
        takephoto = (TextView) findViewById(R.id.txt_prodtakephoto);
        choosephoto = (TextView) findViewById(R.id.txt_prodchoosephoto);
//        prodexpdate = (TextInputEditText) findViewById(R.id.textprod_exp_date);
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
        spinnercondition = (Spinner) findViewById(R.id.spinner_prodcondition);
        condcount = (TextInputEditText) findViewById(R.id.textinput_itemcount);
        prodstock = (TextInputEditText) findViewById(R.id.textinput_instock);

        //
        addlayout = (LinearLayout) findViewById(R.id.addplayout);

        btnshow = (Button) findViewById(R.id.btnshow);
        //adding listeners to views
        takephoto.setOnClickListener(this);
        addexpdate.setOnClickListener(this);
        addcondition.setOnClickListener(this);
        choosephoto.setOnClickListener(this);
        btnshow.setOnClickListener(this);
//        prodexpdate.setOnClickListener(this);

//        prodcondition.setOnItemSelectedListener(this);
        produnit.setOnItemSelectedListener(this);
        spinnerdiscount.setOnItemSelectedListener(this);
        spinnerprodcategory.setOnItemSelectedListener(this);
        spinnercondition.setOnItemSelectedListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbreference = firebaseDatabase.getReference("/datadevcash");
        ProductId = dbreference.push().getKey();
        ProductCondId = dbreference.push().getKey();

        categoryfirebasereference = firebaseDatabase.getReference("/datadevcash/category");
        discountfirebasereference = firebaseDatabase.getReference("/datadevcash/discount");

        //
        businessprodfirebasereference = firebaseDatabase.getReference("/datadevcash/owner");


        final ArrayList<String> categories = new ArrayList<String>();
        final ArrayList<String> prodcond = new ArrayList<String>();

        ///

        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        businessprodfirebasereference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        String key = ds.getKey();
                        dbreference.child("owner"+key+"/business/category").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                    Category category = dataSnapshot1.getValue(Category.class);
                                    categories.add(category.getCategory_name());
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
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        categoryfirebasereference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
//                    Category category1 = dataSnapshot1.getValue(Category.class);
//                    categories.add(category1.getCategory_name());
//                }
//                categories.add("No Category");
//                categories.add("Create Category");
//                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AddProductActivity.this, R.layout.spinner_categoryitem, categories);
//                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_categoryitem);
//                spinnerprodcategory.setAdapter(spinnerArrayAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

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

    public void addProduct(final String prod_name, final String prod_unitof_measure, final String prod_status, final String prod_soldby, final double prod_price, final double prod_rop, int prod_stock){

        ProductCondition condition = new ProductCondition();
        Category category = new Category();
        Discount discount = new Discount();
        discount.setDisc_code(selecteddisc);
        category.setCategory_name(selectedcategory);
        condition.setCond_name(selectedprodcond);
        if(condcount.getText().toString().equals("") && selectedprodcond.equals("New")){
            int count = 0;
            condition.setCond_count(count);
        }else{
            int count = Integer.parseInt(condcount.getText().toString());
            condition.setCond_count(count);
        }

        final Product product = new Product(prod_name, prod_unitof_measure, prod_status, prod_soldby, prod_price, prod_rop, prod_stock);
        product.setProductCondition(condition);
        product.setCategory(category);
        product.setDiscount(discount);

        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        businessprodfirebasereference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        String key = ds.getKey();
                        dbreference.child("owner/"+key+"/business/product").child(ProductId).setValue(product);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void insertProduct(){
        String pname = prodname.getText().toString();
        String pstatus = chkavail.getText().toString();
        int pstock = Integer.parseInt(prodstock.getText().toString());
        double pprice = Double.parseDouble(prodprice.getText().toString());
        double prop = Double.parseDouble(prodrop.getText().toString());
        addProduct(pname, selectedprodunit, pstatus, selectedsoldby, pprice, prop, pstock);
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
//            case R.id.textprod_exp_date:
//                final Calendar c = Calendar.getInstance();
//                int mYear = c.get(Calendar.YEAR);
//                int mMonth = c.get(Calendar.MONTH);
//                int mDay = c.get(Calendar.DAY_OF_MONTH);
//
//                //date picker dialog
//                expdatePicker = new DatePickerDialog(AddProductActivity.this,
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                                prodexpdate.setText(dayOfMonth + "/"
//                                                    + (month + 1) + "/" + year);
//                            }
//                        },mYear,mMonth,mDay);
//                            expdatePicker.show();
//                break;
//            case R.id.imgview_addcond:
////                 mClickCounter++;
////                 Toast.makeText(getApplicationContext(), Integer.toString(mClickCounter), Toast.LENGTH_SHORT).show();
//                prodcondlayout = (LinearLayout) findViewById(R.id.container_prodcond);
//                View conditionchild = getLayoutInflater().inflate(R.layout.customcard_prodcond, null);
//                prodcondlayout.addView(conditionchild);
//
//                final Spinner spinnercondition = (Spinner) conditionchild.findViewById(R.id.spinner_prodcondition);
//                final TextInputEditText cond_itemcount = (TextInputEditText) conditionchild.findViewById(R.id.textinput_itemcount);
//                String item = cond_itemcount.getText().toString();
//                if(!cond_itemcount.getText().toString().equals("")){
//                    cond_count = Integer.parseInt(item);
//                    Toast.makeText(getApplicationContext(), cond_count, Toast.LENGTH_LONG).show();
//                }else{
//                    cond_count = 0;
//                }
//
//                spinnercondition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        selectedprodcond = spinnercondition.getItemAtPosition(position).toString();
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });
////                item_count = cond_itemcount.getText().toString();
//
//                btnshow.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(getApplicationContext(), "Condition: "+selectedprodcond+"\nItem Count: "+cond_count+"", Toast.LENGTH_LONG).show();
//                    }
//                });
//
//
//                break;
            case R.id.imgview_addexpdate:
                prodexpdatelayout = (LinearLayout) findViewById(R.id.container_expdate);
                View expdatechild = getLayoutInflater().inflate(R.layout.customcard_prodexpdate, null);
                prodexpdatelayout.addView(expdatechild);

                final TextInputEditText expdate = (TextInputEditText) expdatechild.findViewById(R.id.textprod_exp_date);
                final TextInputEditText exp_itemcount = (TextInputEditText) expdatechild.findViewById(R.id.textinput_expdatecount);

                expdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR);
                        int mMonth = c.get(Calendar.MONTH);
                        int mDay = c.get(Calendar.DAY_OF_MONTH);

                        //date picker dialog
                        expdatePicker = new DatePickerDialog(AddProductActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        expdate.setText(dayOfMonth + "/"
                                                + (month + 1) + "/" + year);
                                    }
                                },mYear,mMonth,mDay);
                        expdatePicker.show();
                    }
                });


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
            case R.id.spinner_prodcondition:
                selectedprodcond = this.spinnercondition.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
