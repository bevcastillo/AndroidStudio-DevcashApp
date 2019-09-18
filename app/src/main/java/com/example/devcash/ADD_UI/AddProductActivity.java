package com.example.devcash.ADD_UI;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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
import com.example.devcash.Object.ProductExpiration;
import com.example.devcash.Object.QRCode;
import com.example.devcash.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*
created by Beverly Castillo on August 4, 2019
 */

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private DatabaseReference dbreference;
    private DatabaseReference mydbreference;
    //
    private DatabaseReference ownerdbreference;
    private FirebaseDatabase firebaseDatabase;
    private String ProductId, QRCodeId;
    private String ProductCondId, pexpdate, count;


    ImageView prodphoto, addexpdate, addcondition;
    TextView takephoto, choosephoto;
    TextInputEditText prodexpdate, prodname, prodprice, prodrop, condcount, prodstock, prodbrand, exp_itemcount;
    TextInputLayout productNameLayout, productBrandLayout, productPriceLayout, productStockLayout, productRopLayout;
    Spinner prodcondition, produnit, spinnerprodcategory, spinnerdiscount;
    RadioGroup soldby;
    RadioButton soldbybtn, radioeach, radioweight;
    String selectedprodcond, selectedprodunit, selecteddisc, selectedsoldby, selectedcategory, pbrand;
    CheckBox chkavail;
    LinearLayout prodcondlayout, prodexpdatelayout;
    int conditioncount;

    String code, type;
    String pname;

    TextView edit_discountedprice;
    int pstock;

    //
    String discountCode, discountStart, discountEnd, discountStatus, discountType;
    String pstatus;
    public double pprice;
    public int prop;
    public double discountedPrice;
    public double discountValue;

    private static final int PICK_IMAGE = 100;

    Uri imageUri;
    DatePickerDialog expdatePicker;

    LinearLayout addlayout;
    private int mClickCounter = 0;
    String p_reference;

    Button btnshow;
    Spinner spinnercondition;

    LinearLayout measurementlayout;

    final List<ProductExpiration> productExpDates = new ArrayList<ProductExpiration>();
    final List<Discount> productDiscount = new ArrayList<Discount>();

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
        prodexpdate = (TextInputEditText) findViewById(R.id.textprod_exp_date);
        prodname = (TextInputEditText) findViewById(R.id.textinput_prodname);
        prodprice = (TextInputEditText) findViewById(R.id.textinput_price);
        prodrop = (TextInputEditText) findViewById(R.id.textinput_ROP);
        prodcondition = (Spinner) findViewById(R.id.spinner_prod_condition);
        produnit = (Spinner) findViewById(R.id.spinner_unit);
        soldby = (RadioGroup) findViewById(R.id.radio_group_soldby);
        chkavail = (CheckBox) findViewById(R.id.cbox_prod_avail);

        edit_discountedprice = (TextView) findViewById(R.id.prod_disc_price);
        //
        spinnerprodcategory = (Spinner) findViewById(R.id.spinner_prodcat);
        spinnerdiscount = (Spinner) findViewById(R.id.spinner_proddisc);
        spinnercondition = (Spinner) findViewById(R.id.spinner_prodcondition);
        condcount = (TextInputEditText) findViewById(R.id.textinput_itemcount);
        prodstock = (TextInputEditText) findViewById(R.id.textinput_instock);
        prodbrand = (TextInputEditText) findViewById(R.id.textinput_prodbrand);

        productNameLayout = (TextInputLayout) findViewById(R.id.prodnameLayout);
        productBrandLayout = (TextInputLayout) findViewById(R.id.prodBrandLayout);
        productPriceLayout = (TextInputLayout) findViewById(R.id.prodPriceLayout);
        productStockLayout = (TextInputLayout) findViewById(R.id.prodStockLayout);
        productRopLayout = (TextInputLayout) findViewById(R.id.productRopLayout);


        //
        addlayout = (LinearLayout) findViewById(R.id.addplayout);

        btnshow = (Button) findViewById(R.id.btnshow);

        radioeach = (RadioButton) findViewById(R.id.radiobtn_each);
        radioweight = (RadioButton) findViewById(R.id.radiobtn_weight);

        measurementlayout = (LinearLayout) findViewById(R.id.layout_measurement);

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

        ///

        soldby.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(radioeach.isChecked()){
                    selectedsoldby = radioeach.getText().toString();
                    measurementlayout.setVisibility(View.INVISIBLE);
                }else{
                    selectedsoldby = radioweight.getText().toString();
                    measurementlayout.setVisibility(View.VISIBLE);
                }
            }
        });

        ///

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbreference = firebaseDatabase.getReference("/datadevcash");
        ProductId = dbreference.push().getKey();
        QRCodeId = dbreference.push().getKey();
        ProductCondId = dbreference.push().getKey();


        //
        ownerdbreference = firebaseDatabase.getReference("/datadevcash/owner");


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
                                    code = discount1.getDisc_code();
                                    type = discount1.getDisc_type();
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

//    private void checkStockRop(){
//        double stock = Double.parseDouble(prodstock.getText().toString());
//        double rop = Double.parseDouble(prodrop.getText().toString());
//
//        if (rop >= stock){
//            productRopLayout.setError("ROP is greater than your stock!");
//        }else {
//            productRopLayout.setError(null);
//        }
//
//    }

    private boolean checkStockRop(){
        double stock = Double.parseDouble(prodstock.getText().toString());
        double rop = Double.parseDouble(prodrop.getText().toString());
        final boolean[] ok = {true};

        if (rop > stock){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.customdialog_warning, null);
            builder.setView(dialogView);

            builder.setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ok[0] = true;
                }
            });

            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        return ok[0];
    }

    private boolean checkDuplicateProdName(){ //check if the user input same data that already exist in the firebase database
        final String productName = prodname.getText().toString();
        final boolean[] ok = {true};

        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        //query from the database
        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        String ownerKey = dataSnapshot1.getKey();

                        ownerdbreference.child(ownerKey+"/business/product").orderByChild("prod_name").equalTo(productName).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    productNameLayout.setError("Product name already exists.");
                                    ok[0] = false;
                                }else {
                                    productNameLayout.setError(null);
                                    ok[0] = true;
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

        return ok[0];
    }

    private boolean validateFields(){ //trappings if the user filled in all fields
        String productName = prodname.getText().toString();
        String productBrand = prodbrand.getText().toString();
        String productPriceStr = prodprice.getText().toString();
        String productStock = prodstock.getText().toString();
        String productRop = prodrop.getText().toString();
        boolean ok = true;

        if (productName.isEmpty()){
            productNameLayout.setError("Fields can not be empty.");
            ok = false;
            if (productBrand.isEmpty()){
                productBrandLayout.setError("Fields can not be empty.");
                ok = false;
                if (productPriceStr.isEmpty()){
                    productPriceLayout.setError("Fields can not be empty.");
                    ok = false;
                    if (productStock.equals("")){
                        productStockLayout.setError("Fields can not be empty.");
                        ok = false;
                        if (productRop.equals("")){
                            productRopLayout.setError("Fields can not be empty.");
                            ok = false;
                        }else {
                            productRopLayout.setError(null);
                            ok = true;
                        }
                    }else {
                        productStockLayout.setError(null);
                        ok = true;
                    }
                }else {
                    productPriceLayout.setError(null);
                    ok = true;
                }
            }else {
                productBrandLayout.setError(null);
                ok = true;
            }
        }else {
            productNameLayout.setError(null);
            ok = true;
        }



        return ok;

    }

    public void addProduct(final String prod_name, final String prod_brand, final String prod_unitof_measure, final String prod_status, final double prod_price, final int prod_rop, int prod_stock, final String prod_reference, final double discounted_price){

        ProductCondition condition = new ProductCondition();
        Category category = new Category();
        Discount discount = new Discount();
        final QRCode qrCode = new QRCode();

        qrCode.setQr_category("Product");
        qrCode.setQr_code(prod_name);
        qrCode.setQr_price(prod_price);
        qrCode.setQr_disc_price(discounted_price);

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

        final Product product = new Product(prod_name, prod_brand, prod_unitof_measure, prod_status, prod_price, prod_rop, prod_stock, prod_reference, discounted_price);
        product.setProductCondition(condition);
        product.setCategory(category);
        product.setDiscount(discount);
        product.setQrCode(qrCode);

        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        SharedPreferences productshared = getSharedPreferences("ProductPref",MODE_PRIVATE); //creating a shared preference for products
        final SharedPreferences.Editor product_editor = productshared.edit();

        final Gson gson = new Gson();

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    product_editor.putString("product_id", ProductId); //saving the ProductId to shared preference

                    for(DataSnapshot ds: dataSnapshot.getChildren()){

                        final String key = ds.getKey();

                        if (productExpDates.size()>0){
                            for(ProductExpiration productExpDate : productExpDates) {
                                product.setProd_expdate(productExpDate.getProd_expdate());
                                product.setProd_expdatecount(productExpDate.getProd_expdatecount());
                                String pname = product.getProd_name();
                                product.setProd_reference(pname+productExpDate.getProd_expdate());
                                qrCode.setQr_reference(pname+productExpDate.getProd_expdate());
                                product.setQrCode(qrCode);

                                dbreference.child("owner/"+key+"/business/qrCode").push().setValue(qrCode);
                                dbreference.child("owner/"+key+"/business/product").push().setValue(product);

                                String productJson = gson.toJson(product);
                                product_editor.putString("product", productJson);
                                product_editor.commit();

                            }
                        } else{
                            product.setProd_expdate("No Expiration");
                            String pname = product.getProd_name();
                            product.setProd_reference(pname+product.getProd_expdate().trim());
                            qrCode.setQr_reference(pname+product.getProd_expdate().trim());
                            product.setQrCode(qrCode);

//                            product.setDiscounted_price();
                            dbreference.child("owner/"+key+"/business/product").child(ProductId).setValue(product);
                            dbreference.child("owner/"+key+"/business/qrCode").child(QRCodeId).setValue(qrCode);
                            String productJson = gson.toJson(product);
                            product_editor.putString("product", productJson);
                            product_editor.commit();

                        }


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void insertProduct(){

        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));


        //we will query to compare the selected discount to the discount object and get the discounted price
        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        final String acctkey = dataSnapshot1.getKey();

                        ownerdbreference.child(acctkey+"/business/discount").orderByChild("disc_code").equalTo(selecteddisc).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        String discountkey = dataSnapshot2.getKey();

                                        Discount discount = dataSnapshot2.getValue(Discount.class);
                                        discountCode = discount.getDisc_code();
                                        discountStart = discount.getDisc_start();
                                        discountEnd = discount.getDisc_end();
                                        discountStatus = discount.getDisc_status();
                                        discountType = discount.getDisc_type();
                                        discountValue = discount.getDisc_value();

                                        pname = prodname.getText().toString();

                                        if(prodbrand.getText().toString().equals("")){
                                            prodbrand.setText("No Brand");
                                            pbrand =prodbrand.getText().toString();

                                        }else{
                                            pbrand = prodbrand.getText().toString();
                                        }
                                        pstatus = chkavail.getText().toString();
                                        pstock = Integer.parseInt(prodstock.getText().toString());
                                        pprice = Double.parseDouble(prodprice.getText().toString());
                                        prop = Integer.parseInt(prodrop.getText().toString());

                                        ProductExpiration expiration = new ProductExpiration();
                                        String expdate = expiration.getProd_expdate();
                                        p_reference = pname+expdate;


                                        if (discountStatus.equals("Active")){
                                            if (discountType.equals("Percentage")){
                                                discountedPrice = pprice - discountValue;

                                                addProduct(pname, pbrand, selectedprodunit, pstatus, pprice, prop, pstock, p_reference, discountedPrice);
                                                Toast.makeText(getApplicationContext(), "Product has been successfully added.", Toast.LENGTH_SHORT).show();
                                                finish();
//
//
                                            }else {
                                                //amount
                                                discountedPrice = pprice - discountValue;
                                                addProduct(pname, pbrand, selectedprodunit, pstatus, pprice, prop, pstock, p_reference, discountedPrice);
                                                Toast.makeText(getApplicationContext(), "Product has been successfully added.", Toast.LENGTH_SHORT).show();
                                                finish();

                                            }
                                        }else {
                                            //not active
                                        }
                                    }
                                }else {
                                    /// the user selected "No Discount"
                                    pname = prodname.getText().toString();
                                    pprice = Double.parseDouble(prodprice.getText().toString());
                                    pstatus = chkavail.getText().toString();
                                    pprice = Double.parseDouble(prodprice.getText().toString());
                                    prop = Integer.parseInt(prodrop.getText().toString());
                                    pstock = Integer.parseInt(prodstock.getText().toString());
                                    discountedPrice = pprice; //discounted price is equals to the original price
                                    addProduct(pname, pbrand, selectedprodunit, pstatus, pprice, prop, pstock, p_reference, discountedPrice);
                                    Toast.makeText(getApplicationContext(), "New Product Added!", Toast.LENGTH_SHORT).show();
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
//            addRadioGroupListener();
//            getDiscountData();

            if (validateFields()){
                if (checkDuplicateProdName()){
                    if (checkStockRop()){
                        addCheckBoxListener();
                        insertProduct();
                        getExpDate();
                    }
                }
            }


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
            case R.id.imgview_addexpdate:
                prodexpdatelayout = (LinearLayout) findViewById(R.id.container_expdate);
                View expdatechild = getLayoutInflater().inflate(R.layout.customcard_prodexpdate, null);
                prodexpdatelayout.addView(expdatechild);

                final TextInputEditText expdate = (TextInputEditText) expdatechild.findViewById(R.id.textprod_exp_date);
//                TextInputEditText exp_itemcount = (TextInputEditText) expdatechild.findViewById(R.id.textinput_expdatecount);
                exp_itemcount = (TextInputEditText) expdatechild.findViewById(R.id.textinput_expdatecount);

                final ProductExpiration productExpDate = new ProductExpiration();

//                count = exp_itemcount.getText().toString();
//
//                if(!count.equals("")) {
//                    productExpDate.setProd_expdatecount(Integer.valueOf(count));
//                }

                // listen for after typing of user
                exp_itemcount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(!s.toString().equals("")) {
                            productExpDate.setProd_expdatecount(Integer.valueOf(s.toString()));
                        } else {
                            productExpDate.setProd_expdatecount(0);
                        }

                    }
                });

                expdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR);
                        int mMonth = c.get(Calendar.MONTH);
                        int mDay = c.get(Calendar.DAY_OF_MONTH);

//                        final List<ProductExpiration> productExpDates = new ArrayList<ProductExpiration>();

                        //date picker dialog
                        expdatePicker = new DatePickerDialog(AddProductActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                        expdate.setText(dayOfMonth + "/"
                                                + (month + 1) + "/" + year);

                                        productExpDate.setProd_expdate(expdate.getText().toString());

//                                        pexpdate = expdate.getText().toString();
                                    }
                                },mYear,mMonth,mDay);
                        expdatePicker.show();
                    }
                });

                productExpDates.add(productExpDate);

                break;
        }
    }

    public void getExpDate(){

//        Toast.makeText(this, pexpdate+" is the selected expdate", Toast.LENGTH_SHORT).show();
        for(ProductExpiration productExpDate : productExpDates) {
//            Toast.makeText(this, productExpDate.getProd_expdate()+" is the selected expdate with a count of: "+productExpDate.getProd_expdatecount(), Toast.LENGTH_SHORT).show();
        }

//        Toast.makeText(this, exp_itemcount.getText().toString()+" is the count", Toast.LENGTH_SHORT).show();
    }

    public void getDiscountCode(){
        for (Discount discount : productDiscount){

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
