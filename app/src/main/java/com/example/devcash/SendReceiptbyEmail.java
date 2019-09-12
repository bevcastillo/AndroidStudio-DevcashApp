package com.example.devcash;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.devcash.CustomAdapters.ReceiptProductAdapter;
import com.example.devcash.CustomAdapters.ReceiptServiceAdapter;
import com.example.devcash.Object.CartItem;
import com.example.devcash.Object.CustomerTransaction;
import com.example.devcash.Object.Product;
import com.example.devcash.Object.Services;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
* created by Beverly Castillo on September 11, 2019
* */

public class SendReceiptbyEmail extends AppCompatActivity implements View.OnClickListener {

    LinearLayout sendemail;
    TextInputEditText customer_email;

    DatabaseReference ownerdbreference;
    FirebaseDatabase firebaseDatabase;

    List<Product> products;
    List<Services> services;
    List<CartItem> cartItems;
    CartItem cartItem;
    int customerId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_receiptby_email);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sendemail = (LinearLayout) findViewById(R.id.btnsendemail);
        sendemail.setOnClickListener(this);

        customer_email = (TextInputEditText) findViewById(R.id.text_customeremail);

        //
//        ActivityCompat.requestPermissions(SendReceiptbyEmail.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                PackageManager.PERMISSION_GRANTED);


        firebaseDatabase = FirebaseDatabase.getInstance();
        ownerdbreference = firebaseDatabase.getReference("datadevcash/owner");


        //
        products = new ArrayList<>();
        services = new ArrayList<>();
        cartItems = new ArrayList<>();
        cartItem = new CartItem();
    }


    @Override
    protected void onStart() {
        super.onStart();

        displayAllPurchase();
    }

    public void convertReceiptToPdf(View view){
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Paint paint = new Paint();
        String mString = "Sample";
        int x=10;
        int y=25;
        page.getCanvas().drawText(mString, x,y, paint);
        pdfDocument.finishPage(page);

        String mfilePath = Environment.getExternalStorageDirectory().getPath() + "/receipt.pdf";
        File file = new File(mfilePath);

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

        pdfDocument.close();
    }


    public void sendEmail(){
        String[] recipients = {customer_email.getText().toString()};
        Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto: "));

        //prompts email clients
        email.setType("message/rfc822");

        email.putExtra(Intent.EXTRA_EMAIL, recipients);
        email.putExtra(Intent.EXTRA_SUBJECT, "Customer Receipt");
        email.putExtra(Intent.EXTRA_TEXT,
                Html.fromHtml(new StringBuilder()
                .append("<p><b>Mayol's Enterprises</b></p>")
                .append("<small>Receipt#12</small>")
                .toString()));

        try {
            // the user can choose the email client
            startActivity(Intent.createChooser(email, "Select an email client"));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SendReceiptbyEmail.this, "No email client installed.",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void displayAllPurchase(){
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        SharedPreferences custIdShared = getApplicationContext().getSharedPreferences("CustomerIdPref", MODE_PRIVATE);
        customerId = (custIdShared.getInt("customer_id", 0));

        if (customerId <= 0) {
            customerId = customerId + 1;
        }

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        String acctkey = dataSnapshot1.getKey();

                        ownerdbreference.child(acctkey+"/business/customer_transaction").orderByChild("customer_id").equalTo(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    Toast.makeText(SendReceiptbyEmail.this, "customer id exists", Toast.LENGTH_SHORT).show();
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        String customertransactionkey = dataSnapshot2.getKey();
                                        CustomerTransaction customerTransaction = dataSnapshot2.getValue(CustomerTransaction.class);
                                        Gson gson = new Gson();

//                                        receiptno.setText("Receipt #"+(customerId));

                                        CustomerTransaction customerTransaction1 = dataSnapshot2.getValue(CustomerTransaction.class);
                                        int receiptno = customerTransaction1.getCustomer_id();
                                        double cash = customerTransaction.getCash_received();
                                        double change = customerTransaction.getChange();
                                        double discount = customerTransaction.getTotal_discount();
                                        double totprice = customerTransaction.getTotal_price();


                                        String customerReceiptContent = "-------------------------------"+
                                                                        "<p>&nbsp;&nbsp;&nbsp;RECEIPT&nbsp;&nbsp;&nbsp;</p>"+
                                                                        "<p>Receipt#"+receiptno+"</p>" +
                                                                        "<p>Total Due-----"+totprice+"</p>" +
                                                                        "<p>Discount------"+discount+"</p>"+
                                                                        "<p>Cash----------"+cash+"</p>"+
                                                                        "<p>Change--------"+change+"</p>" ;

                                        //
                                        String[] recipients = {customer_email.getText().toString()};
                                        Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto: "));

                                        //prompts email clients
                                        email.setType("message/rfc822");

                                        email.putExtra(Intent.EXTRA_EMAIL, recipients);
                                        email.putExtra(Intent.EXTRA_SUBJECT, "Customer Receipt");
                                        email.putExtra(Intent.EXTRA_TEXT,
                                                Html.fromHtml(new StringBuilder()
                                                        .append(customerReceiptContent).toString()));

                                        try {
                                            // the user can choose the email client
                                            startActivity(Intent.createChooser(email, "Select an email client"));

                                        } catch (android.content.ActivityNotFoundException ex) {
                                            Toast.makeText(SendReceiptbyEmail.this, "No email client installed.",
                                                    Toast.LENGTH_LONG).show();
                                        }


                                        for(Map.Entry<String, Object> entry : customerTransaction.getCustomer_cart().entrySet()) {
                                            if (entry.getValue().toString().contains("product")) {
                                                String json = gson.toJson(entry.getValue());
                                                Log.d("PRODUCT JSON REP @@@@", json);
                                                String mJsonString = json;
                                                JsonParser parser = new JsonParser();
                                                JsonElement mJson =  parser.parse(mJsonString);

                                                JsonObject jsonObject = gson.fromJson(mJson, JsonObject.class);
                                                JsonElement prodJson = jsonObject.get("product");
                                                Product prodObj = gson.fromJson(prodJson, Product.class);

                                                cartItem.setProduct(prodObj);
                                                cartItems.add(cartItem);
                                                products.add(prodObj);
                                                String itemname = prodObj.getProd_name();
                                                double itemqty = prodObj.getProd_qty();
                                                double discprice = prodObj.getDiscounted_price();

//                                                Toast.makeText(SendReceiptbyEmail.this, itemname+" is the item name", Toast.LENGTH_LONG).show();

                                                ReceiptProductAdapter adapter = new ReceiptProductAdapter(products);
                                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//                                                showprodrecycler.setLayoutManager(layoutManager);
//                                                showprodrecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//                                                showprodrecycler.setItemAnimator(new DefaultItemAnimator());
//                                                showprodrecycler.setAdapter(adapter);

                                            } else {
                                                String json = gson.toJson(entry.getValue());
                                                String mJsonString = json;
                                                JsonParser parser = new JsonParser();
                                                JsonElement mJson =  parser.parse(mJsonString);

                                                JsonObject jsonObject = gson.fromJson(mJson, JsonObject.class);
                                                JsonElement servicesJson = jsonObject.get("services");
                                                Services servicesObj = gson.fromJson(servicesJson, Services.class);
                                                cartItem.setServices(servicesObj);
                                                cartItems.add(cartItem);
                                                services.add(servicesObj);

                                                ReceiptServiceAdapter adapter = new ReceiptServiceAdapter(services);
                                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//                                                showservicesrecycler.setLayoutManager(layoutManager);
//                                                showservicesrecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//                                                showservicesrecycler.setItemAnimator(new DefaultItemAnimator());
//                                                showservicesrecycler.setAdapter(adapter);
                                            }
                                        }
                                    }
                                }else {
                                    Toast.makeText(SendReceiptbyEmail.this, "No customer transaction yet!"+customerId, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } // outer for-loop

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.btnsendemail:
//                sendEmail();
                displayAllPurchase();
                break;
        }
    }
}
