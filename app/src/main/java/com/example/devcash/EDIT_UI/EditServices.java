package com.example.devcash.EDIT_UI;

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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.devcash.ADD_UI.AddServicesActivity;
import com.example.devcash.CustomAdapters.ServicesAdapter;
import com.example.devcash.Object.Category;
import com.example.devcash.Object.Discount;
import com.example.devcash.Object.QRCode;
import com.example.devcash.Object.Services;
import com.example.devcash.Object.Serviceslistdata;
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
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class EditServices extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private DatabaseReference dbreference;
    private DatabaseReference ownerdbreference;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference;
    private StorageTask uploadTask;

    private TextInputEditText servname, servprice;
    private TextInputLayout serviceNameLayout, servicePriceLayout;
    private Spinner spinnerCategory, spinnerDiscount;
    private CheckBox chckavail;
    private String selectedcategory, selecteddiscount, strservname, strchkavail, strimageUrl;
    private double strservprice;
    private int pos, pos1;
    private LinearLayout layoutdelete, choosePhoto, takePhoto;
    String discountCode, discountStart, discountEnd, discountStatus, discountType;
    double discountValue, discountedPrice, servicePrice;
    Uri imageUri;
    ImageView imageView;

    private static final int PICK_IMAGE = 100;

    List<Serviceslistdata> serviceslist;
//    ArrayList<Category> categoryArrayList = new ArrayList<Category>();
    ArrayList<String> categoryArrayList = new ArrayList<String>();
    ArrayList<String> discountArrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_services);

        //
        servname = (TextInputEditText) findViewById(R.id.textinput_editservname);
        spinnerCategory = (Spinner) findViewById(R.id.spinner_editservcat);
        spinnerDiscount = (Spinner) findViewById(R.id.spinner_editserdisc);
        chckavail = (CheckBox) findViewById(R.id.cbox_editservavail);
        servprice = (TextInputEditText) findViewById(R.id.textinput_editservprice);
        serviceNameLayout = (TextInputLayout) findViewById(R.id.layoutServiceName);
        servicePriceLayout = (TextInputLayout) findViewById(R.id.layoutServicePrice);
        layoutdelete = (LinearLayout) findViewById(R.id.layout_delservices);
        choosePhoto = (LinearLayout) findViewById(R.id.choose_photo);
        takePhoto = (LinearLayout) findViewById(R.id.take_photo);
        imageView = (ImageView) findViewById(R.id.services_photo);

        choosePhoto.setOnClickListener(this);
        takePhoto.setOnClickListener(this);
        spinnerCategory.setOnItemSelectedListener(this);
        spinnerDiscount.setOnItemSelectedListener(this);
        layoutdelete.setOnClickListener(this);

        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbreference = firebaseDatabase.getReference("/datadevcash/");
        ownerdbreference = firebaseDatabase.getReference("/datadevcash/owner");
        storageReference = FirebaseStorage.getInstance().getReference("Service");

        //
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
                                    categoryArrayList.add(category.getCategory_name());
                                }
                                categories.add("No Category");
                                categories.add("Create Category");
                                categoryArrayList.add("No Category");
                                categoryArrayList.add("Create Category");
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(EditServices.this, R.layout.spinner_categoryitem, categories);
                                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_categoryitem);
                                spinnerCategory.setAdapter(spinnerArrayAdapter);
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

        ///

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
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(EditServices.this, R.layout.spinner_discountitem, discounts);
                                spinnerDiscount.setAdapter(spinnerArrayAdapter);
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

        getServDetails();

        Bundle bundle = this.getIntent().getExtras();
        if (bundle!=null){
            strservname = bundle.getString("service_name");
            strservprice = bundle.getDouble("service_price");
            strchkavail = bundle.getString("service_status");
            strimageUrl = bundle.getString("image_url");

            servname.setText(strservname);
            servprice.setText(Double.toString(strservprice));

            if(strchkavail.equals("Available")){
                chckavail.setChecked(true);
            } else{
                chckavail.setChecked(false);
            }

        }

    }

    private boolean validateFields(){
        String serviceName = servname.getText().toString();
        String servicePrice = servprice.getText().toString();

        if (serviceName.isEmpty()) {
            serviceNameLayout.setError("Fields cannot be empty");
            return false;
        }

        if (servicePrice.isEmpty()) {
            servicePriceLayout.setError("Fields cannot be empty");
            return false;
        }

        return true;
//        boolean ok = true;
//
//        if (serviceName.isEmpty()){
//            serviceNameLayout.setError("Fields can not be empty.");
//            ok = false;
//            if (servicePrice.isEmpty()){
//                servicePriceLayout.setError("Fields can not be empty.");
//                ok = false;
//            }else {
//                servicePriceLayout.setError(null);
//                ok = true;
//            }
//        }else {
//            serviceNameLayout.setError(null);
//            ok = true;
//        }
//
//        return ok;
    }

    public void getServDetails(){
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        final String ownerkey = dataSnapshot1.getKey();
                        ownerdbreference.child(ownerkey+"/business/services")
                                .orderByChild("service_name").equalTo(strservname).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){

                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        String servkey = dataSnapshot2.getKey();
                                        Services services = dataSnapshot2.getValue(Services.class);
                                        String serviceImageUrl = services.getService_image();

                                        Picasso.with(EditServices.this).load(serviceImageUrl).into(imageView);


                                        ownerdbreference.child(ownerkey+"/business/services").child(servkey+"/category").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.exists()){
                                                    Category category = dataSnapshot.getValue(Category.class);
                                                    selectedcategory = category.getCategory_name();

                                                    for(int i = 0; i < categoryArrayList.size(); i++ ){
                                                        if(categoryArrayList.get(i).equals(selectedcategory)){
                                                            pos = i;
                                                        }
                                                        spinnerCategory.setSelection(pos);
                                                    }

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                        ownerdbreference.child(ownerkey+"/business/services").child(servkey+"/discount").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.exists()){
                                                    Discount discount3 = dataSnapshot.getValue(Discount.class);
                                                    selecteddiscount = discount3.getDisc_code();

                                                    for(int i = 0; i < discountArrayList.size(); i++ ){
                                                        if(discountArrayList.get(i).equals(selecteddiscount)){
                                                            pos1 = i;
                                                        }
                                                        spinnerDiscount.setSelection(pos1);
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap =  MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    public void updateServices(){
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        final String ownerKey = dataSnapshot1.getKey();

                        ownerdbreference.child(ownerKey+"/business/services").orderByChild("service_name").equalTo(strservname).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        final String servicesKey = dataSnapshot2.getKey();
                                        Services services = dataSnapshot2.getValue(Services.class);
                                        strchkavail = services.getService_status();

                                        if (chckavail.isChecked()){
                                            strchkavail = "Available";
                                        }else {
                                            strchkavail = "Not Available";
                                        }

                                        //upload image
//                                        final StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
//
//                                        uploadTask = fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                            @Override
//                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                                    @Override
//                                                    public void onSuccess(Uri uri) {
//                                                        ownerdbreference.child(ownerKey+"/business/services/").child(servicesKey+"/service_image").setValue(uri.toString());
//                                                    }
//                                                });
//                                            }
//                                        });

                                        final String myqr_ref = servname.getText().toString()+Double.valueOf(servprice.getText().toString());
                                        //updating inside the service node
                                        ownerdbreference.child(ownerKey+"/business/services/").child(servicesKey+"/service_name").setValue(servname.getText().toString());
                                        ownerdbreference.child(ownerKey+"/business/services/").child(servicesKey+"/service_price").setValue(Double.valueOf(servprice.getText().toString()));
                                        ownerdbreference.child(ownerKey+"/business/services/").child(servicesKey+"/service_reference").setValue(myqr_ref);
                                        ownerdbreference.child(ownerKey+"/business/services/").child(servicesKey+"/service_status").setValue(strchkavail);
////
//                                        //updating inside the service/qrCode
                                        ownerdbreference.child(ownerKey+"/business/services/").child(servicesKey+"/qrCode/qr_code").setValue(servname.getText().toString());
                                        ownerdbreference.child(ownerKey+"/business/services/").child(servicesKey+"/qrCode/qr_price").setValue(Double.valueOf(servprice.getText().toString()));
                                        ownerdbreference.child(ownerKey+"/business/services/").child(servicesKey+"/qrCode/qr_reference").setValue(myqr_ref);

                                        ownerdbreference.child(ownerKey+"/business/services/").child(servicesKey+"/discount/disc_code").setValue(selecteddiscount);
                                        ownerdbreference.child(ownerKey+"/business/services/").child(servicesKey+"/category/category_name").setValue(selectedcategory);

                                        //updating the discounted price based on the selected discount
                                        ownerdbreference.child(ownerKey+"/business/discount").orderByChild("disc_code").equalTo(selecteddiscount).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()){
                                                    //if the selected discount exists
                                                    for (DataSnapshot dataSnapshot3: dataSnapshot.getChildren()){
                                                        String discountKey = dataSnapshot3.getKey();
                                                        Discount discount = dataSnapshot3.getValue(Discount.class);
                                                        discountCode = discount.getDisc_code();
                                                        discountStart = discount.getDisc_start();
                                                        discountEnd = discount.getDisc_end();
                                                        discountType = discount.getDisc_type();
                                                        discountValue = discount.getDisc_value();
                                                        discountStatus = discount.getDisc_status();

                                                        servicePrice = Double.parseDouble(servprice.getText().toString());
                                                        if (discountStatus.equals("Active")){
                                                            if (discountType.equals("Percentage")) {
                                                                discountedPrice = servicePrice - discountValue;

                                                                //update firebase
                                                                ownerdbreference.child(ownerKey+"/business/services/").child(servicesKey+"/discounted_price").setValue(discountedPrice);
                                                                ownerdbreference.child(ownerKey+"/business/services/").child(servicesKey+"/qrCode/qr_disc_price").setValue(discountedPrice);

                                                            }else {
                                                                //amount
                                                                discountedPrice = servicePrice - discountValue;
                                                                ownerdbreference.child(ownerKey+"/business/services/").child(servicesKey+"/discounted_price").setValue(discountedPrice);
                                                                ownerdbreference.child(ownerKey+"/business/services/").child(servicesKey+"/qrCode/qr_disc_price").setValue(discountedPrice);
                                                            }
                                                        }else {
                                                            //the discount is not active
                                                        }


                                                    }
                                                }else {
                                                    //if the discount does not exist, then we set the discounted price to whatever the new price set by the user
                                                    ownerdbreference.child(ownerKey+"/business/services/").child(servicesKey+"/discounted_price").setValue(Double.valueOf(servprice.getText().toString()));
                                                    ownerdbreference.child(ownerKey+"/business/services/").child(servicesKey+"/qrCode/qr_disc_price").setValue(Double.valueOf(servprice.getText().toString()));
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                        //updating the qrCode node
                                        ownerdbreference.child(ownerKey+"/business/qrCode").orderByChild("qr_code").equalTo(strservname).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()){
                                                    for (DataSnapshot dataSnapshot3: dataSnapshot.getChildren()){
                                                        final String qrCodeKey = dataSnapshot3.getKey();
                                                        String newReference = servname.getText().toString()+""+servprice.getText().toString();

                                                        ownerdbreference.child(ownerKey+"/business/qrCode/").child(qrCodeKey+"/qr_code").setValue(servname.getText().toString());
                                                        ownerdbreference.child(ownerKey+"/business/qrCode/").child(qrCodeKey+"/qr_price").setValue(Double.valueOf(servprice.getText().toString()));
                                                        ownerdbreference.child(ownerKey+"/business/qrCode/").child(qrCodeKey+"/qr_reference").setValue(newReference);

                                                        ownerdbreference.child(ownerKey+"/business/discount").orderByChild("disc_code").equalTo(selecteddiscount).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.exists()){
                                                                    for (DataSnapshot dataSnapshot4: dataSnapshot.getChildren()){
                                                                        Discount discount1 = dataSnapshot4.getValue(Discount.class);
                                                                        discountCode = discount1.getDisc_code();
                                                                        discountStart = discount1.getDisc_start();
                                                                        discountEnd = discount1.getDisc_end();
                                                                        discountType = discount1.getDisc_type();
                                                                        discountValue = discount1.getDisc_value();
                                                                        discountStatus = discount1.getDisc_status();

                                                                        servicePrice = Double.parseDouble(servprice.getText().toString());
                                                                        if (discountStatus.equals("Active")){
                                                                            if (discountType.equals("Percentage")) {
//                                                                              discountedPrice = servicePrice - discountValue;

                                                                                //update firebase
                                                                                ownerdbreference.child(ownerKey+"/business/qrCode/").child(qrCodeKey+"/qr_disc_price").setValue(discountedPrice);
                //

                                                                            }else {
                                                                                //amount
                                                                                discountedPrice = servicePrice - discountValue;
                                                                                ownerdbreference.child(ownerKey+"/business/qrCode/").child(qrCodeKey+"/qr_disc_price").setValue(discountedPrice);
                                                                            }
                                                                        }else {
                                                                            //the discount is not active
                                                                        }
                                                                    }
                                                                }else {
                                                                    //selected discount does not exist, then we set the discount to whatever is the original price
                                                                    ownerdbreference.child(ownerKey+"/business/qrCode/").child(qrCodeKey+"/qr_disc_price").setValue(Double.parseDouble(servprice.getText().toString()));
//
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
                                    Toast.makeText(EditServices.this, "Services has been successfully updated.", Toast.LENGTH_SHORT).show();
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


    public void deleteServices(){
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this services?");
        builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                final String ownerkey = dataSnapshot1.getKey();

                                ownerdbreference.child(ownerkey+"/business/services")
                                        .orderByChild("service_name").equalTo(strservname).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            for(DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                                String servkey = dataSnapshot2.getKey();
                                                Services services = dataSnapshot2.getValue(Services.class);

                                                ownerdbreference.child(ownerkey+"/business/services").child(servkey).setValue(null);

                                                //deleting the qrCode
                                                ownerdbreference.child(ownerkey+"/business/qrCode").orderByChild("qr_code").equalTo(strservname).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()){
                                                            for (DataSnapshot dataSnapshot3: dataSnapshot.getChildren()){
                                                                String qrCodeKey = dataSnapshot3.getKey();

                                                                ownerdbreference.child(ownerkey+"/business/qrCode").child(qrCodeKey).setValue(null);
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
                                Toast.makeText(EditServices.this, "Services has been successfully deleted.", Toast.LENGTH_SHORT).show();
                                finish();

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

//    public void deleteServices(){
//        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
//        final String username = (shared.getString("owner_username", ""));
//
//        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()){
//                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
//                        final String ownerkey = dataSnapshot1.getKey();
//
//                        ownerdbreference.child(ownerkey+"/business/services")
//                                .orderByChild("service_name").equalTo(strservname).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                if(dataSnapshot.exists()){
//                                    for(DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
//                                        String servkey = dataSnapshot2.getKey();
//
//                                        ownerdbreference.child(ownerkey+"/business/services").child(servkey+"/service_name").setValue(null);
//                                        ownerdbreference.child(ownerkey+"/business/services").child(servkey+"/service_price").setValue(null);
//                                        ownerdbreference.child(ownerkey+"/business/services").child(servkey+"/service_status").setValue(null);
//                                        ownerdbreference.child(ownerkey+"/business/services").child(servkey+"/category/category_name").setValue(null);
//                                        ownerdbreference.child(ownerkey+"/business/services").child(servkey+"/discount/disc_code").setValue(null);
//                                        ownerdbreference.child(ownerkey+"/business/services").child(servkey+"/discount/disc_value").setValue(null);
//
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                        Toast.makeText(EditServices.this, "Services is deleted", Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }


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
        switch (id){
            case R.id.home:
                onBackPressed();
                return true;
            case R.id.action_save:
                if (validateFields()){
                    updateServices();
                }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int sid = parent.getId();
        switch (sid){
            case R.id.spinner_editservcat:
                selectedcategory = spinnerCategory.getItemAtPosition(position).toString();
                break;
            case R.id.spinner_editserdisc:
                selecteddiscount = spinnerDiscount.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        int sid = v.getId();
        switch (sid){
            case R.id.layout_delservices:
                deleteServices();
                break;
            case R.id.choose_photo:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
                break;
            case R.id.take_photo:
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }else{
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
    }
}
