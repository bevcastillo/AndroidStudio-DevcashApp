package com.example.devcash;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.CustomAdapters.ReceiptProductAdapter;
import com.example.devcash.CustomAdapters.ReceiptServiceAdapter;
import com.example.devcash.Object.Account;
import com.example.devcash.Object.Business;
import com.example.devcash.Object.CartItem;
import com.example.devcash.Object.CustomerTransaction;
import com.example.devcash.Object.Employee;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewReceiptActivity extends AppCompatActivity {

    TextView txtReceiptNo;
    String receiptNo, amountDue, cash_received, change, customer_type, senior_discount, subtotal, total_item_discount, total_item_qty, transaction_status, vat, vatE;
    int customerId;
    TextView txtAmountDue, txtReceiptNumber, txtCustomerType, txtCashierName, txtTransactionDate,
            txtitemOff, txtregularSubtotal, txtRegularVat, txtRegularVatExempt, txtRegularAmountDue, txtCashReceived, txtChange,
            senItemOff, senSubtotal, senVat, senVatExempt, senDiscount, senAmountDue, seniorCash, seniorChange;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ownerdbreference;
    RecyclerView prodRecycler, serviceRecycler;
    LinearLayout regularLayout, seniorLayout;

    List<Product> products;
    List<Services> services;
    List<CartItem> cartItems;
    CartItem cartItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_receipt);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        txtReceiptNo = (TextView) findViewById(R.id.text_receiptno);
        txtAmountDue = (TextView) findViewById(R.id.text_amountDue);
        txtReceiptNumber = (TextView) findViewById(R.id.text_receiptno);
        txtCustomerType = (TextView) findViewById(R.id.text_customertype);
        txtCashierName = (TextView) findViewById(R.id.text_cashiername);
        txtTransactionDate = (TextView) findViewById(R.id.text_transactiondatetime);
        txtitemOff = (TextView) findViewById(R.id.itemOff);
        txtregularSubtotal = (TextView) findViewById(R.id.regular_subtotal);
        txtRegularVat = (TextView) findViewById(R.id.regular_vat);
        txtRegularVatExempt = (TextView) findViewById(R.id.regular_vatExempt);
        txtRegularAmountDue = (TextView) findViewById(R.id.regular_amountDue);
        txtCashReceived = (TextView) findViewById(R.id.regular_cash);
        txtChange = (TextView) findViewById(R.id.regular_change);

        senSubtotal = (TextView) findViewById(R.id.seniorSubtotal);
        senVat = (TextView) findViewById(R.id.seniorVat);
        senVatExempt = (TextView) findViewById(R.id.seniorvatExempt);
        senDiscount = (TextView) findViewById(R.id.seniorcitizenDiscount);
        senAmountDue = (TextView) findViewById(R.id.seniorAmountDue);
        seniorCash = (TextView) findViewById(R.id.senior_cash);
        seniorChange = (TextView) findViewById(R.id.senior_change);


        prodRecycler = (RecyclerView) findViewById(R.id.receiptprod_recyclerview);
        serviceRecycler = (RecyclerView) findViewById(R.id.receiptservice_recyclerview);

        regularLayout = (LinearLayout) findViewById(R.id.regularcustomerLayout);
        seniorLayout = (LinearLayout) findViewById(R.id.seniorcitizenLayout);


        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        ownerdbreference = firebaseDatabase.getReference("datadevcash/owner");

        products = new ArrayList<>();
        services = new ArrayList<>();
        cartItems = new ArrayList<>();
        cartItem = new CartItem();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_refund_send_receipt, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        customerId = getIntent().getExtras().getInt("customer_id");

        getReceiptDetails();
        displayAllPurchase();
    }

    private void getReceiptDetails() {


        SharedPreferences ownerPref = getApplicationContext().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        SharedPreferences businessPref = getApplicationContext().getSharedPreferences("BusinessPref", MODE_PRIVATE);
        SharedPreferences empPref = getApplicationContext().getSharedPreferences("EmpPref", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = ownerPref.getString("account", "");
        String ownerAccountType = ownerPref.getString("account_type","");
        String employeeAccountType = empPref.getString("account_type", "");
        String businessJson = businessPref.getString("business", "");
        String employeeJson = empPref.getString("Employee", "");

        Account account = gson.fromJson(json, Account.class);
        Business business = gson.fromJson(businessJson, Business.class);
        Employee employee = gson.fromJson(employeeJson, Employee.class);

        String lname = business.getOwner_lname();
        String fname = business.getOwner_fname();
        String name = fname+" "+lname;

        if (!ownerAccountType.equals("")){
            txtCashierName.setText("Cashier: "+name);
        }

        if (!employeeAccountType.equals("")){
            String employeeName = employee.getEmp_fname() + " " + employee.getEmp_lname();
            txtCashierName.setText("Cashier: "+employeeName);
        }


        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                        String ownerKey = dataSnapshot2.getKey();

                        ownerdbreference.child(ownerKey+"/business/customer_transaction").orderByChild("customer_id")
                                .equalTo(customerId)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            for (DataSnapshot dataSnapshot21: dataSnapshot.getChildren()){
                                                String customerTransactionKey = dataSnapshot21.getKey();


                                                CustomerTransaction customerTransaction = dataSnapshot21.getValue(CustomerTransaction.class);
//                                                Toast.makeText(ViewReceiptActivity.this, customerTransaction.getSubtotal()+" is the subtotal", Toast.LENGTH_SHORT).show();
                                                double amountDue = customerTransaction.getAmount_due();
                                                double cash_received = customerTransaction.getCash_received();
                                                double change = customerTransaction.getChange();
                                                int receipt_id = customerTransaction.getCustomer_id();
                                                String customer_type = customerTransaction.getCustomer_type();
                                                double senior_discount = customerTransaction.getSenior_discount();
                                                double subtotal = customerTransaction.getSubtotal();
                                                double total_discount = customerTransaction.getTotal_item_discount();
                                                int total_item_count = customerTransaction.getTotal_item_qty();
                                                String transaction_status = customerTransaction.getTransaction_status();
                                                double vat = customerTransaction.getVat();
                                                double vatExemptSale = customerTransaction.getVat_exempt_sale();
                                                String transaction_date = customerTransaction.getTransaction_datetime();

                                                txtTransactionDate.setText(transaction_date);
                                                txtReceiptNumber.setText("Receipt #"+String.valueOf(receipt_id));
                                                txtAmountDue.setText(String.valueOf(amountDue));

                                                if (customer_type.equals("Regular Customer")){
                                                    regularLayout.setVisibility(View.VISIBLE);
                                                    txtitemOff.setText(String.valueOf(total_discount));
                                                    txtregularSubtotal.setText(String.valueOf(subtotal));
                                                    txtRegularVat.setText(String.valueOf(vat));
                                                    txtAmountDue.setText(String.valueOf(amountDue));
                                                    txtRegularAmountDue.setText(String.valueOf(amountDue));
                                                    txtCashReceived.setText(String.valueOf(cash_received));
                                                    txtChange.setText(String.valueOf(change));

                                                }else {
                                                    seniorLayout.setVisibility(View.VISIBLE);
                                                    senItemOff.setText(String.valueOf(total_discount));
                                                    senSubtotal.setText(String.valueOf(subtotal));
                                                    senVat.setText(String.valueOf(vat));
                                                    senVatExempt.setText(String.valueOf(vatExemptSale));
                                                    senDiscount.setText(String.valueOf(senior_discount));
                                                    senAmountDue.setText(String.valueOf(amountDue));
                                                    seniorCash.setText(String.valueOf(cash_received));
                                                    seniorChange.setText(String.valueOf(change));
                                                }

                                                //from customer cart

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

    public void displayAllPurchase(){
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

//        SharedPreferences custIdShared = getApplicationContext().getSharedPreferences("CustomerIdPref", MODE_PRIVATE);
//        customerId = (custIdShared.getInt("customer_id", 0));
//
//        if (customerId <= 0) {
//            customerId = customerId + 1;
//        }

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
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        String customertransactionkey = dataSnapshot2.getKey();
                                        CustomerTransaction customerTransaction = dataSnapshot2.getValue(CustomerTransaction.class);
                                        Gson gson = new Gson();

//                                        receiptno.setText("Receipt # "+(customerId));
//                                        datetime.setText(customerTransaction.getTransaction_datetime());

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

                                                ReceiptProductAdapter adapter = new ReceiptProductAdapter(products);
                                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                                prodRecycler.setLayoutManager(layoutManager);
                                                prodRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                                prodRecycler.setItemAnimator(new DefaultItemAnimator());
                                                prodRecycler.setAdapter(adapter);

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
                                                serviceRecycler.setLayoutManager(layoutManager);
                                                serviceRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                                serviceRecycler.setItemAnimator(new DefaultItemAnimator());
                                                serviceRecycler.setAdapter(adapter);
                                            }
                                        }
                                    }
                                }else {
//                                    Toast.makeText(CustomerReceiptActivity.this, "No customer transaction yet!"+customerId, Toast.LENGTH_SHORT).show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.refund_action:
//                Toast.makeText(this, "This is refund", Toast.LENGTH_SHORT).show();
                break;
            case R.id.email_action:
//                Toast.makeText(this, "Email", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mms_action:
//                Toast.makeText(this, "MMS", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
