package com.example.devcash.ADD_UI;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.ContentResolver;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.Object.Category;
import com.example.devcash.Object.Discount;
import com.example.devcash.Object.ProductExpiration;
import com.example.devcash.Object.QRCode;
import com.example.devcash.Object.Services;
import com.example.devcash.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class AddServicesActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private DatabaseReference dbreference;
    private DatabaseReference categorydbreference;
    private DatabaseReference discountsdbreference;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ownerdbreference;
//    private StorageReference storageReference;
    private String ServicesId, QRCodeId;

    private StorageReference storageReference;
    private StorageTask uploadTask;


    String servname, servstatus;
    double servprce;

    ImageView servicesphoto;
    TextView takephoto, choosephoto;
    CheckBox chkavail;
    TextInputEditText servicename, serviceprice;
    TextInputLayout servNameLayout, servPriceLayout;
    Spinner spinnercategory, spinnerdiscounts;
    String selectedcategory, selecteddiscount;

    private static final int PICK_IMAGE = 100;

    double discountedPrice;

    private Uri imageUri;

    String discountCode, discountStart, discountEnd, discountStatus, discountType;
    double discountValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_services);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //
        servicename = (TextInputEditText) findViewById(R.id.textinput_servname);
        serviceprice = (TextInputEditText) findViewById(R.id.textinput_servprice);
        servNameLayout = (TextInputLayout) findViewById(R.id.serviceNameLayout);
        servPriceLayout = (TextInputLayout) findViewById(R.id.servicePriceLayout);

        servicesphoto = (ImageView) findViewById(R.id.services_photo);
        takephoto = (TextView) findViewById(R.id.txt_servicestakephoto);
        choosephoto = (TextView) findViewById(R.id.txt_serviceschoosephoto);
        chkavail = (CheckBox) findViewById(R.id.cbox_serv_avail);

        spinnercategory = (Spinner) findViewById(R.id.spinner_servcat);
        spinnerdiscounts = (Spinner) findViewById(R.id.spinner_serdisc);


        //adding listeners
        takephoto.setOnClickListener(this);
        choosephoto.setOnClickListener(this);
        spinnercategory.setOnItemSelectedListener(this);
        spinnerdiscounts.setOnItemSelectedListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbreference = firebaseDatabase.getReference("/datadevcash");
        ServicesId = dbreference.push().getKey();
        QRCodeId = dbreference.push().getKey();

        categorydbreference = firebaseDatabase.getReference("datadevcash/category");
        discountsdbreference = firebaseDatabase.getReference("datadevcash/discount");
        ownerdbreference = firebaseDatabase.getReference("datadevcash/owner");
        storageReference = FirebaseStorage.getInstance().getReference("Service");

        final ArrayList<String> categories = new ArrayList<String>();

        //
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
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AddServicesActivity.this, R.layout.spinner_categoryitem, categories);
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
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AddServicesActivity.this, R.layout.spinner_discountitem, discounts);
                                spinnerdiscounts.setAdapter(spinnerArrayAdapter);
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

    private boolean validateFields(){
        String serviceName = servicename.getText().toString();
        String servicePrice = serviceprice.getText().toString();

        if (serviceName.isEmpty()) {
            servNameLayout.setError("Fields cannot be empty");
            return false;
        }

        if (servicePrice.isEmpty()) {
            servPriceLayout.setError("Fields cannot be empty");
            return false;
        }

        return true;
//        boolean ok = true;
//
//        if (serviceName.isEmpty()){
//            servNameLayout.setError("Fields can not be empty.");
//            ok = false;
//            if (servicePrice.isEmpty()){
//                servPriceLayout.setError("Fields can not be empty");
//                ok = false;
//            }else {
//                servPriceLayout.setError(null);
//                ok = true;
//            }
//        }else {
//            servNameLayout.setError(null);
//            ok = true;
//        }
//
//        return ok;
    }

    private boolean checkDuplicates(){
        final String serviceName = servicename.getText().toString();
        final boolean[] ok = {true};

        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        String ownerKey = dataSnapshot1.getKey();

                        ownerdbreference.child(ownerKey+"/business/services").orderByChild("service_name").equalTo(serviceName).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    servNameLayout.setError("Service already exists.");
                                    ok[0] = false;
                                }else {
                                    servNameLayout.setError(null);
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

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    public void addServices(final String service_name, final String service_status, final double service_price, final double service_disc_price){

        final Discount discount = new Discount();
        final Category category = new Category();
        final QRCode qrCode = new QRCode();
        final Services services = new Services();


        //upload image
        final StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));

        uploadTask = fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        services.setService_image(uri.toString());

                        qrCode.setQr_category("Services");
                        qrCode.setQr_code(service_name);
                        qrCode.setQr_price(service_price);
                        qrCode.setQr_reference(service_name+service_price);
                        qrCode.setQr_disc_price(service_disc_price);
                        discount.setDisc_code(selecteddiscount);
                        category.setCategory_name(selectedcategory);

                        services.setService_status(service_status);
                        services.setService_name(service_name);
                        services.setService_price(service_price);
                        services.setService_reference(service_name+service_price);
//        services.setService_disc_price(service_disc_price);
                        services.setDiscounted_price(service_disc_price);
                        services.setDiscount(discount);
                        services.setCategory(category);
                        services.setQrCode(qrCode);


                        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
                        final String username = (shared.getString("owner_username", ""));

                        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                                        String key = ds.getKey();
                                        dbreference.child("owner/"+key+"/business/qrCode").child(QRCodeId).setValue(qrCode);
                                        dbreference.child("owner/"+key+"/business/services").child(ServicesId).setValue(services);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }
        });

    }


    public void insertServices(){

        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));


        //we will query to compare the selected discount to the discount object and get the discounted price
        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        final String acctkey = dataSnapshot1.getKey();

                        ownerdbreference.child(acctkey+"/business/discount").orderByChild("disc_code").equalTo(selecteddiscount).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                        if (discount.getDisc_type() != null) {
                                            discountType = discount.getDisc_type();
                                        } else {
                                            discountType = "Percentage";
                                        }
                                        discountValue = discount.getDisc_value();


                                        servname = servicename.getText().toString();
                                        if (serviceprice.getText() != null) {
                                            servprce = Double.parseDouble(serviceprice.getText().toString());
                                        }

                                        servstatus  = chkavail.getText().toString();

                                        if (discountStatus.equals("Active")){
                                            if (discountType.equals("Percentage")){
                                                discountedPrice = servprce - discountValue;

                                                addServices(servname, servstatus, servprce, discountedPrice);
                                                Toast.makeText(getApplicationContext(), "New services has been successfully added.", Toast.LENGTH_SHORT).show();
                                                finish();
//
//
                                            }else {
                                                //amount
                                                discountedPrice = servprce - discountValue;

                                                addServices(servname, servstatus, servprce, discountedPrice);
                                                Toast.makeText(getApplicationContext(), "New services has been successfully added.", Toast.LENGTH_SHORT).show();
                                                finish();

                                            }
                                        }else {
                                            //not active

                                        }
                                    }
                                }else {
                                    /// the user selected "No Discount"
                                    servname = servicename.getText().toString();
                                    servprce = Double.parseDouble(serviceprice.getText().toString());
                                    servstatus  = chkavail.getText().toString();
                                    discountedPrice = servprce - discountValue;
                                    addServices(servname, servstatus, servprce, discountedPrice);
                                    Toast.makeText(getApplicationContext(), "New Services Added!", Toast.LENGTH_SHORT).show();
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
        }else if(id == R.id.action_save){ //if SAVE is clicked

            if (validateFields()){
                if (checkDuplicates()){
                    addCheckBoxListener();
                    insertServices();
                }
            }
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.txt_serviceschoosephoto:
//                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//                startActivityForResult(gallery, PICK_IMAGE);
                choosePhoto();
                break;
            case R.id.txt_servicestakephoto:
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, 0);
                break;
        }
    }

    //handles opening the camera and gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            servicesphoto.setImageURI(imageUri);
        }else{
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            servicesphoto.setImageBitmap(bitmap);
        }

    }

    private void choosePhoto(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE);
    }

    private void uploadPhoto(){
        if(imageUri!=null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Adding..");
            progressDialog.show();


        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int sid = parent.getId();
        switch (sid){
            case R.id.spinner_servcat:
                selectedcategory = this.spinnercategory.getItemAtPosition(position).toString();
                if(selectedcategory.equals("Create Category")){
                    Intent create = new Intent(AddServicesActivity.this, AddCategoryActivity.class);
                    startActivity(create);
                }
                break;
            case R.id.spinner_serdisc:
                selecteddiscount = this.spinnerdiscounts.getItemAtPosition(position).toString();
                if(selecteddiscount.equals("Create Discount")){
                    Intent create = new Intent(AddServicesActivity.this, AddDiscountActivity.class);
                    startActivity(create);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
