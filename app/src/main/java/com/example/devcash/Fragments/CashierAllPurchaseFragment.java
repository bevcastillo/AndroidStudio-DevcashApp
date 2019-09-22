package com.example.devcash.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.AllPurchaseActivity;
import com.example.devcash.CashierNavigation;
import com.example.devcash.CashierPurchaseListActivity;
import com.example.devcash.CustomAdapters.PurchaseInventoryProductsAdapter;
import com.example.devcash.CustomAdapters.PurchaseInventoryServicesAdapter;
import com.example.devcash.DashboardActivity;
import com.example.devcash.Object.CustomerCart;
import com.example.devcash.Object.CustomerTransaction;
import com.example.devcash.Object.Product;
import com.example.devcash.Object.Productlistdata;
import com.example.devcash.Object.PurchaseTransactionlistdata;
import com.example.devcash.Object.Services;
import com.example.devcash.Object.Serviceslistdata;
import com.example.devcash.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CashierAllPurchaseFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference ownerdbreference;

    List<Productlistdata> list;
    List<Serviceslistdata> slist;
    List<PurchaseTransactionlistdata> ptlist;
    Map<String, Object> cartMap;

    ProgressBar invprogress;
    LinearLayout emptylayout, scanqrcode, newsale;
    //
    Toolbar itemListToolbar;
    Spinner itemListSpinner, spinnerCustomerType;
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView text_totprice, text_qty;

    LinearLayout chargebtnlayout;
    TextView qtypricetext;

    RecyclerView recyclerViewitemlist;
    String selectedinventorytype, selectedcustomertype, transactionDateTime;
    int customerId;
    EditText editSearch;
    Button searchBtn;

    String dateTime;


    public CashierAllPurchaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cashier_all_purchase, container, false);


        itemListToolbar = (Toolbar) view.findViewById(R.id.toolbar_purchaseitemlist);
        itemListSpinner = (Spinner) view.findViewById(R.id.spinner_inventorytype);

        invprogress = (ProgressBar) view.findViewById(R.id.inv_progressbar);
        emptylayout = (LinearLayout) view.findViewById(R.id.layout_emptyinv);
        qtypricetext = (TextView) view.findViewById(R.id.textqtyprice);
        chargebtnlayout = (LinearLayout) view.findViewById(R.id.btn_chargeitem);
        scanqrcode = (LinearLayout) view.findViewById(R.id.scanqrcode_layout);
        newsale = (LinearLayout) view.findViewById(R.id.layoutnewsale);
        spinnerCustomerType = (Spinner) view.findViewById(R.id.spinner_customertype);
        editSearch = (EditText) view.findViewById(R.id.itemssarchtext);
        searchBtn = (Button) view.findViewById(R.id.itemssearchbtn);


        searchBtn.setOnClickListener(this);

        chargebtnlayout.setOnClickListener(this);
        scanqrcode.setOnClickListener(this);
        newsale.setOnClickListener(this);
        itemListSpinner.setOnItemSelectedListener(this);
        spinnerCustomerType.setOnItemSelectedListener(this);

        recyclerViewitemlist = (RecyclerView) view.findViewById(R.id.recyclerview_purchitemlist);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ownerdbreference = firebaseDatabase.getReference("datadevcash/owner");
        cartMap = new HashMap<String, Object>();

        // shared pref
        SharedPreferences custIdShared = getActivity().getSharedPreferences("CustomerIdPref", MODE_PRIVATE);
        customerId = (custIdShared.getInt("customer_id", 0));

        if (customerId <= 0) {
            customerId = customerId + 1;
        }

        ///
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.custom_spinner_item,
                getResources().getStringArray(R.array.inventory_type));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemListSpinner.setAdapter(myAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        displayQtyPrice();
    }

    private void displayQtyPrice() {

        SharedPreferences shared = getActivity().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));


        SharedPreferences empPref = getActivity().getSharedPreferences("EmpPref", MODE_PRIVATE);
        final String empUsername = empPref.getString("emp_username", "");

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        final String acctkey = dataSnapshot1.getKey();

                        ownerdbreference.child(acctkey+"/business/employee").orderByChild("emp_username").equalTo(empUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        ownerdbreference.child(acctkey+"/business/customer_transaction").orderByChild("customer_id").equalTo(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()){
                                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                                        CustomerTransaction customerTransaction = dataSnapshot2.getValue(CustomerTransaction.class);
                                                        int quantity = customerTransaction.getTotal_item_qty();
                                                        double total = customerTransaction.getAmount_due();

                                                        qtypricetext.setText(quantity+" items = ₱"+total);

                                                    }
                                                }else {
                                                    //customer does not exist
                                                    qtypricetext.setText("No items = ₱0.00");
                                                    qtypricetext.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                                                    chargebtnlayout.setEnabled(false);
                                                    chargebtnlayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.custom_btn_stroke));
//                                    Toast.makeText(getActivity(), "Please add items to cart!", Toast.LENGTH_SHORT).show();
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

    public void viewAllProducts(){

        SharedPreferences shared = getActivity().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        SharedPreferences empPref = getActivity().getSharedPreferences("EmpPref", MODE_PRIVATE);
        final String empUsername = empPref.getString("emp_username", "");

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        final String key = ds.getKey();

                        ownerdbreference.child(key+"/business/employee").orderByChild("emp_username").equalTo(empUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                        String empKey = dataSnapshot1.getKey();

                                        ownerdbreference.child(key+"/business/product").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            list = new ArrayList<>();
                                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                                Product product = dataSnapshot1.getValue(Product.class);
                                                Productlistdata listdata = new Productlistdata();
                                                String prodname = product.getProd_name();
                                                double prodprice = product.getProd_price();
                                                double discountedprice = product.getDiscounted_price();
                                                String status = product.getProd_status();
                                                int prodStock = product.getProd_stock();
                                                String image = product.getProd_image();

                                                if (status.equals("Available")){
                                                    if (prodStock > 0){
                                                        listdata.setProd_name(prodname);
                                                        listdata.setProd_price(prodprice);
                                                        listdata.setDiscounted_price(discountedprice);
                                                        listdata.setProd_expdate(product.getProd_expdate());
                                                        listdata.setProd_image(image);
                                                        list.add(listdata);
                                                    }
                                                }

                                            }
                                            PurchaseInventoryProductsAdapter adapter = new PurchaseInventoryProductsAdapter(getContext(), list);
                                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),5);
                                            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                            recyclerViewitemlist.setLayoutManager(gridLayoutManager);
                                            recyclerViewitemlist.setItemAnimator(new DefaultItemAnimator());
                                            recyclerViewitemlist.setAdapter(adapter);
                                            adapter.notifyDataSetChanged();

                                            invprogress.setVisibility(View.GONE);

                                            if(list.isEmpty()){
                                                emptylayout.setVisibility(View.VISIBLE);
                                            }else{
                                                emptylayout.setVisibility(View.GONE);
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

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id){
            case R.id.btn_chargeitem:
                Intent intent = new Intent(getActivity(), CashierPurchaseListActivity.class);
                intent.putExtra("selectedCustomerType", selectedcustomertype); //passing the selected customer to the intent
                startActivity(intent);

                break;
            case R.id.scanqrcode_layout:
                scanQrCode();
                break;
            case R.id.layoutnewsale:
                startNewSale();
                break;
            case R.id.itemssearchbtn:
                searchItem();
                break;
        }

    }

    private void searchItem() {

        final String inputSearch = editSearch.getText().toString();

        SharedPreferences shared = getActivity().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        SharedPreferences empPref = getActivity().getSharedPreferences("EmpPref", MODE_PRIVATE);
        final String empUsername = empPref.getString("emp_username", "");



        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        String ownerKey = dataSnapshot1.getKey();

                        ownerdbreference.child(ownerKey+"/business/employee").orderByChild("emp_username").equalTo(empUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        String empKey = dataSnapshot2.getKey();

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

    private void scanQrCode() {
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(false);
        integrator.forSupportFragment(this).initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(getActivity(), "Scanning was cancelled", Toast.LENGTH_LONG).show();
            }
            else {
                String qrItem = result.getContents();
                saveToFirebaseUsingScanner(qrItem);
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void saveToFirebaseUsingScanner(final String qrItem) {

        SharedPreferences shared = getActivity().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        SharedPreferences empPref = getActivity().getSharedPreferences("EmpPref", MODE_PRIVATE);
        final String empUsername = empPref.getString("emp_username", "");

        final String CustomerCartId = ownerdbreference.push().getKey();

        // get from product node.
        ownerdbreference.orderByChild("business/owner_username")
                .equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                final String acctKey = dataSnapshot1.getKey();

                                ownerdbreference.child(acctKey+"/business/employee").orderByChild("emp_username").equalTo(empUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                                String empKey = dataSnapshot2.getKey();

                                                // if we scan a product.
                                                ownerdbreference.child(acctKey+"/business/product").orderByChild("prod_reference").equalTo(qrItem).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()){
                                                            for (DataSnapshot dataSnapshot11: dataSnapshot.getChildren()){
                                                                String prodKey = dataSnapshot11.getKey();
                                                                final Product product = dataSnapshot11.getValue(Product.class);

//                                                Toast.makeText(getActivity(), product.getDiscounted_price()+" is the discounted price", Toast.LENGTH_SHORT).show();
                                                                product.setProd_qty(1);

                                                                double subtotal = product.getDiscounted_price() * product.getProd_qty();
                                                                product.setProd_subtotal(subtotal);

                                                                final String preference = product.getProd_name()+product.getProd_expdate();

                                                                final CustomerCart customerCart = new CustomerCart();
                                                                customerCart.setProduct(product);

                                                                cartMap.put(CustomerCartId, customerCart);

                                                                final CustomerTransaction customerTransaction = new CustomerTransaction();
                                                                customerTransaction.setCustomer_type(selectedcustomertype);
                                                                customerTransaction.setCustomer_id(customerId);
                                                                customerTransaction.setCustomer_cart(cartMap);

                                                                // save the added customer id to shared pref.
                                                                SharedPreferences customerIdPref = getActivity().getSharedPreferences("CustomerIdPref", MODE_PRIVATE);
                                                                final SharedPreferences.Editor customerIdEditor = customerIdPref.edit();
                                                                customerIdEditor.putInt("customer_id", customerId);
                                                                customerIdEditor.commit();

                                                                // save now to customer_transaction node.
                                                                ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        if (dataSnapshot.exists()){
                                                                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                                                                final String acctkey = dataSnapshot1.getKey();

                                                                                ownerdbreference.child(acctkey+"/business/customer_transaction").orderByChild("customer_id").equalTo(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                        if (dataSnapshot.exists()){
                                                                                            for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                                                                                final String customertransactionkey = dataSnapshot2.getKey();
                                                                                                final CustomerTransaction customerTransaction1 = dataSnapshot2.getValue(CustomerTransaction.class);
                                                                                                final double currentSubtotal = customerTransaction1.getSubtotal();
                                                                                                final double currentAmountDue = customerTransaction1.getAmount_due();
                                                                                                final int currentTotalQty = customerTransaction1.getTotal_item_qty();
                                                                                                String customerType = customerTransaction1.getCustomer_type();
                                                                                                final double currentTotalDiscount = customerTransaction1.getTotal_item_discount();

                                                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/customer_cart")
                                                                                                        .orderByChild("product/prod_reference")
                                                                                                        .equalTo(preference)
                                                                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                            @Override
                                                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                                if (dataSnapshot.exists()){
                                                                                                                    for (DataSnapshot dataSnapshot3: dataSnapshot.getChildren()){
                                                                                                                        String cartkey = dataSnapshot3.getKey();

                                                                                                                        CustomerCart customerCart1 = dataSnapshot3.getValue(CustomerCart.class);
                                                                                                                        String prodreference = customerCart1.getProduct().getProd_reference();
                                                                                                                        double prodSubtotal = customerCart1.getProduct().getProd_subtotal();
                                                                                                                        double prodPrice = customerCart1.getProduct().getProd_price();
                                                                                                                        int prodQty = customerCart1.getProduct().getProd_qty();
                                                                                                                        double discountedPrice = customerCart1.getProduct().getDiscounted_price();

                                                                                                                        //if product already exists and we need to update some fields
                                                                                                                        if (prodreference.equals(preference)){

                                                                                                                            if (selectedcustomertype.equals("Senior Citizen")){

                                                                                                                                int totalQty = currentTotalQty + 1;

                                                                                                                                String productSubtotalStr = String.format("%.2f", discountedPrice * totalQty);
                                                                                                                                double productSubtotal = Double.parseDouble(productSubtotalStr);
                                                                                                                                double subtotalVat = Double.parseDouble(productSubtotalStr);

                                                                                                                                String vatExemptSaleStr = String.format("%.2f", subtotalVat / 1.12);
                                                                                                                                double vatExemptSale = Double.parseDouble(vatExemptSaleStr);

                                                                                                                                String lessVatStr = String.format("%.2f", subtotalVat - vatExemptSale);
                                                                                                                                double lessVat = Double.parseDouble(lessVatStr);

                                                                                                                                String seniorDiscountStr = String.format("%.2f", vatExemptSale * .20);
                                                                                                                                double seniorDiscount = Double.parseDouble(seniorDiscountStr);

                                                                                                                                String totalDueStr = String.format("%.2f", vatExemptSale - seniorDiscount);
                                                                                                                                double totalDue = Double.parseDouble(totalDueStr);

                                                                                                                                String partialItemDiscSubtotalStr = String.format("%.2f", discountedPrice * prodQty);
                                                                                                                                double partialItemDiscSubtotal = Double.parseDouble(partialItemDiscSubtotalStr);

                                                                                                                                String partialItemOrigPriceSubtotalStr = String.format("%.2f", prodPrice * prodQty);
                                                                                                                                double partialItemOrigPriceSubtotal = Double.parseDouble(partialItemOrigPriceSubtotalStr);

                                                                                                                                String totalDiscountStr = String.format("%.2f", partialItemDiscSubtotal - partialItemOrigPriceSubtotal);
                                                                                                                                double totalDiscount = Double.parseDouble(totalDiscountStr);


                                                                                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/customer_cart/").child(cartkey+"/product/prod_qty").setValue(prodQty+1);
                                                                                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/customer_cart/").child(cartkey+"/product/prod_subtotal").setValue(productSubtotal);

                                                                                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/senior_discount").setValue(seniorDiscount);
                                                                                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/subtotal").setValue(subtotalVat);
                                                                                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/total_item_discount").setValue(totalDiscount);
                                                                                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/total_item_qty").setValue(totalQty);
                                                                                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/vat").setValue(lessVat);
                                                                                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/vat_exempt_sale").setValue(vatExemptSale);
                                                                                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/amount_due").setValue(totalDue);
                                                                                                                                cartMap.clear();
                                                                                                                                Toast.makeText(getActivity(), "Quantity has been updated.", Toast.LENGTH_SHORT).show();
                                                                                                                            }else {
                                                                                                                                //if regular customer

                                                                                                                                int totalQty = currentTotalQty + 1;

                                                                                                                                String productSubtotalStr = String.format("%.2f", discountedPrice * totalQty);
                                                                                                                                double productSubtotal = Double.parseDouble(productSubtotalStr);

                                                                                                                                String totalDueStr = String.format("%.2f", currentAmountDue + discountedPrice);
                                                                                                                                double totalDue = Double.parseDouble(totalDueStr);


                                                                                                                                String subtotalStr = String.format("%.2f", totalDue / 1.12);
                                                                                                                                double subtotal = Double.parseDouble(subtotalStr);

                                                                                                                                String vatStr = String.format("%.2f", totalDue - subtotal);
                                                                                                                                double vat = Double.parseDouble(vatStr);

                                                                                                                                String partialDiscount_str = String.format("%.2f", prodPrice * prodQty);
                                                                                                                                //
                                                                                                                                //
                                                                                                                                double partialDiscount = Double.parseDouble(partialDiscount_str);
                                                                                                                                double totalDiscount = productSubtotal - partialDiscount;
//
                                                                                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/customer_cart/").child(cartkey+"/product/prod_qty").setValue(prodQty+1);
                                                                                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/customer_cart/").child(cartkey+"/product/prod_subtotal").setValue(productSubtotal);
                                                                                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/total_item_qty").setValue(totalQty);
                                                                                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/vat").setValue(vat);
                                                                                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/subtotal").setValue(subtotal);
                                                                                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/amount_due").setValue(totalDue);
                                                                                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/total_item_discount").setValue(totalDiscount);
                                                                                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/vat_exempt_sale").setValue(0.00);
                                                                                                                                cartMap.clear();
                                                                                                                                Toast.makeText(getActivity(), "Quantity has been updated.", Toast.LENGTH_SHORT).show();
                                                                                                                            }
                                                                                                                        }else {
//                                                                                                    Toast.makeText(getActivity(), customerTransaction1.getSubtotal()+" is the current subtotal", Toast.LENGTH_SHORT).show();
                                                                                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/customer_cart").push().setValue(customerCart);
                                                                                                                        }
                                                                                                                    }
                                                                                                                }else {

                                                                                                                    //transaction already exist, but we will add different product to the cart

                                                                                                                    double discountedprice = product.getDiscounted_price();
                                                                                                                    int prodqty = product.getProd_qty();
                                                                                                                    double prodprice = product.getProd_price();

                                                                                                                    if (selectedcustomertype.equals("Senior Citizen")){

                                                                                                                        int totalQty = currentTotalQty + 1;

                                                                                                                        String subtotalStr = String.format("%.2f", currentSubtotal + discountedprice); //////// old subtotal + discounted price of the selected item
                                                                                                                        double subtotal = Double.parseDouble(subtotalStr);

                                                                                                                        String vatExemptSaleStr = String.format("%.2f", subtotal / 1.12);
                                                                                                                        double vatExempt = Double.parseDouble(vatExemptSaleStr);

                                                                                                                        String vatStr = String.format("%.2f", subtotal - vatExempt);
                                                                                                                        double vat = Double.parseDouble(vatStr);

                                                                                                                        String seniorDiscountStr = String.format("%.2f", vatExempt * 0.20);
                                                                                                                        double seniorDiscount = Double.parseDouble(seniorDiscountStr);

                                                                                                                        String totalDueStr = String.format("%.2f", vatExempt - seniorDiscount);
                                                                                                                        double totalDue = Double.parseDouble(totalDueStr);

                                                                                                                        String partialSubtotalStr = String.format("%.2f", discountedprice * prodqty);
                                                                                                                        double partialSubtotal = Double.parseDouble(partialSubtotalStr);

                                                                                                                        String partialDiscountStr = String.format("%.2f", prodprice * prodqty);
                                                                                                                        double partialDiscount = Double.parseDouble(partialDiscountStr);

                                                                                                                        String totalDiscountStr = String.format("%.2f", partialSubtotal - partialDiscount);
                                                                                                                        double totalDiscount = Double.parseDouble(totalDiscountStr);
//
                                                                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/total_item_qty").setValue(totalQty);
                                                                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/subtotal").setValue(subtotal);
                                                                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/vat").setValue(vat);
                                                                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/vat_exempt_sale").setValue(vatExempt);
                                                                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/senior_discount").setValue(seniorDiscount);
                                                                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/amount_due").setValue(totalDue);
                                                                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/total_item_discount").setValue(totalDiscount);
                                                                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/customer_cart").updateChildren(cartMap);
                                                                                                                        cartMap.clear();
                                                                                                                        Toast.makeText(getActivity(), "Item has been added to cart", Toast.LENGTH_SHORT).show();

                                                                                                                    }else {
                                                                                                                        //customer is Regular Customer
                                                                                                                        int totalQty = currentTotalQty + 1;

                                                                                                                        String totalDueStr = String.format("%.2f", currentAmountDue + (discountedprice * prodqty));
                                                                                                                        double totalDue = Double.parseDouble(totalDueStr);

                                                                                                                        String subtotalStr = String.format("%.2f", totalDue / 1.12);
                                                                                                                        double subtotal = Double.parseDouble(subtotalStr);

                                                                                                                        String vatStr = String.format("%.2f", totalDue - subtotal);
                                                                                                                        double vat = Double.parseDouble(vatStr);
//
                                                                                                                        String partialSubtotalStr = String.format("%.2f", discountedprice * prodqty);
                                                                                                                        double partialSubtotal = Double.parseDouble(partialSubtotalStr);
                                                                                                                        String partialDiscountStr = String.format("%.2f", prodprice * prodqty);
                                                                                                                        double partialDiscount = Double.parseDouble(partialDiscountStr);

                                                                                                                        String totalDiscountStr = String.format("%.2f", partialSubtotal - partialDiscount);
                                                                                                                        double totalDiscount = Double.parseDouble(totalDiscountStr);

//
                                                                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/total_item_qty").setValue(totalQty);
                                                                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/vat").setValue(vat);
                                                                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/subtotal").setValue(subtotal);
                                                                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/amount_due").setValue(totalDue);
                                                                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/total_item_discount").setValue(totalDiscount);
                                                                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/vat_exempt_sale").setValue(0.00);
                                                                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/customer_cart").updateChildren(cartMap);
                                                                                                                        cartMap.clear();
                                                                                                                        Toast.makeText(getActivity(), "Item has been added to cart", Toast.LENGTH_SHORT).show();
                                                                                                                    }
                                                                                                                }
                                                                                                            }

                                                                                                            @Override
                                                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                            }
                                                                                                        });

                                                                                            }
                                                                                        }else {
                                                                                            // this is the logic for creating new data.

                                                                                            if (selectedcustomertype.equals("Senior Citizen")){

                                                                                                double discountedprice = product.getDiscounted_price();
                                                                                                int prodqty = product.getProd_qty();
                                                                                                double prodprice = product.getProd_price();


                                                                                                String subtotalVatStr = String.format("%.2f", discountedprice * prodqty);
                                                                                                double subtotalVat = Double.parseDouble(subtotalVatStr);

                                                                                                String vatExemptSaleStr = String.format("%.2f", (discountedprice * prodqty) / 1.12);
                                                                                                double vatExemptSale = Double.parseDouble(vatExemptSaleStr);

                                                                                                String lessVatStr = String.format("%.2f", subtotalVat - vatExemptSale);
                                                                                                double lessVat = Double.parseDouble(lessVatStr);

                                                                                                String seniorDiscountStr = String.format("%.2f", vatExemptSale * .20);
                                                                                                double seniorDiscount = Double.parseDouble(seniorDiscountStr);

                                                                                                String totalDueStr = String.format("%.2f", vatExemptSale - seniorDiscount);
                                                                                                double totalDue = Double.parseDouble(totalDueStr);

                                                                                                String partialItemDiscSubtotalStr = String.format("%.2f", discountedprice * prodqty);
                                                                                                double partialItemDiscSubtotal = Double.parseDouble(partialItemDiscSubtotalStr);

                                                                                                String partialItemOrigPriceSubtotalStr = String.format("%.2f", prodprice * prodqty);
                                                                                                double partialItemOrigPriceSubtotal = Double.parseDouble(partialItemOrigPriceSubtotalStr);

                                                                                                String totalDiscountStr = String.format("%.2f", partialItemDiscSubtotal - partialItemOrigPriceSubtotal);
                                                                                                double totalDiscount = Double.parseDouble(totalDiscountStr);

                                                                                                customerTransaction.setTransaction_datetime(dateTime);
                                                                                                customerTransaction.setCustomer_type(selectedcustomertype);
                                                                                                customerTransaction.setTotal_item_qty(prodqty);
                                                                                                customerTransaction.setSubtotal(subtotalVat); //with vat
                                                                                                customerTransaction.setTotal_item_discount(totalDiscount); //total discount (if the item is discounted)
                                                                                                customerTransaction.setVat(lessVat); //vat off
                                                                                                customerTransaction.setVat_exempt_sale(vatExemptSale); //this is the total price minus the 12% vat
                                                                                                customerTransaction.setSenior_discount(seniorDiscount); //this is the value of the 20% senior citizen discount
                                                                                                customerTransaction.setAmount_due(totalDue); //amount that will be billed to the senior citizen

                                                                                                ownerdbreference.child(acctkey+"/business/customer_transaction").push().setValue(customerTransaction); //creating a new customer transaction node
                                                                                                Toast.makeText(getActivity(), "Item has been added to cart.", Toast.LENGTH_SHORT).show();

                                                                                            }else {
                                                                                                //if the customer type is regular customer

                                                                                                double discountedprice = product.getDiscounted_price();
                                                                                                int prodqty = product.getProd_qty();
                                                                                                double prodprice = product.getProd_price();

                                                                                                String totalAmountDueStr = String.format("%.2f", discountedprice * prodqty);
                                                                                                double totalAmountDue = Double.parseDouble(totalAmountDueStr);

                                                                                                String subtotalStr = String.format("%.2f",  totalAmountDue / 1.12);
                                                                                                double subtotal = Double.parseDouble(subtotalStr);

                                                                                                String vatStr = String.format("%.2f", totalAmountDue - subtotal);
                                                                                                double vat = Double.parseDouble(vatStr);

                                                                                                String partialItemDiscSubtotalStr = String.format("%.2f", discountedprice * prodqty);
                                                                                                double partialItemDiscSubtotal = Double.parseDouble(partialItemDiscSubtotalStr);

                                                                                                String partialItemOrigPriceSubtotalStr = String.format("%.2f", prodprice * prodqty);
                                                                                                double partialItemOrigPriceSubtotal = Double.parseDouble(partialItemOrigPriceSubtotalStr);

                                                                                                String totalDiscountStr = String.format("%.2f", partialItemDiscSubtotal - partialItemOrigPriceSubtotal);
                                                                                                double totalDiscount = Double.parseDouble(totalDiscountStr);

                                                                                                customerTransaction.setTransaction_datetime(dateTime);
                                                                                                customerTransaction.setCustomer_type(selectedcustomertype);
                                                                                                customerTransaction.setSubtotal(subtotal);
                                                                                                customerTransaction.setTotal_item_qty(prodqty);
                                                                                                customerTransaction.setVat(vat);
                                                                                                customerTransaction.setVat_exempt_sale(0.00);
                                                                                                customerTransaction.setAmount_due(totalAmountDue);
                                                                                                customerTransaction.setTotal_item_discount(totalDiscount);

                                                                                                ownerdbreference.child(acctkey+"/business/customer_transaction").push().setValue(customerTransaction); //creating a new customer transaction node
                                                                                                Toast.makeText(getActivity(), "Item has been added to cart.", Toast.LENGTH_SHORT).show();

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
                                                        }else {
//
                                                            // query to service node
                                                            ownerdbreference.child(acctKey+"/business/services")
                                                                    .orderByChild("service_reference").equalTo(qrItem)
                                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                            if (dataSnapshot.exists()){
                                                                                for (DataSnapshot dataSnapshot11: dataSnapshot.getChildren()){
                                                                                    String servKey = dataSnapshot11.getKey();
                                                                                    final Services services = dataSnapshot11.getValue(Services.class);
                                                                                    services.setService_qty(1);
                                                                                    double subtotal = services.getDiscounted_price() * services.getService_qty();
                                                                                    services.setService_subtotal(subtotal);
                                                                                    final String serviceReference = services.getService_name()+services.getService_price();
                                                                                    final CustomerCart customerCart = new CustomerCart();
                                                                                    customerCart.setServices(services);

                                                                                    cartMap.put(CustomerCartId, customerCart);

                                                                                    final CustomerTransaction customerTransaction = new CustomerTransaction();
                                                                                    customerTransaction.setCustomer_type(selectedcustomertype);
                                                                                    customerTransaction.setCustomer_id(customerId);
                                                                                    customerTransaction.setCustomer_cart(cartMap);

                                                                                    // save the added customer id to shared pref.
                                                                                    SharedPreferences customerIdPref = getActivity().getSharedPreferences("CustomerIdPref", MODE_PRIVATE);
                                                                                    final SharedPreferences.Editor customerIdEditor = customerIdPref.edit();
                                                                                    customerIdEditor.putInt("customer_id", customerId);
                                                                                    customerIdEditor.commit();

                                                                                    // save now to customer_transaction node.
                                                                                    ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                            if (dataSnapshot.exists()){
                                                                                                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                                                                                    final String acctkey = dataSnapshot1.getKey();

                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction").orderByChild("customer_id").equalTo(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                        @Override
                                                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                            if (dataSnapshot.exists()){
                                                                                                                for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                                                                                                    final String customertransactionkey = dataSnapshot2.getKey();
                                                                                                                    final CustomerTransaction customerTransaction1 = dataSnapshot2.getValue(CustomerTransaction.class);
                                                                                                                    final double currentSubtotal = customerTransaction1.getSubtotal();
                                                                                                                    final double currentAmountDue = customerTransaction1.getAmount_due();
                                                                                                                    final int currentTotalQty = customerTransaction1.getTotal_item_qty();
                                                                                                                    String customerType = customerTransaction1.getCustomer_type();
                                                                                                                    final double currentTotalDiscount = customerTransaction1.getTotal_item_discount();

                                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/customer_cart")
                                                                                                                            .orderByChild("services/service_reference")
                                                                                                                            .equalTo(serviceReference)
                                                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                @Override
                                                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                                                    if (dataSnapshot.exists()){
                                                                                                                                        for (DataSnapshot dataSnapshot3: dataSnapshot.getChildren()){
                                                                                                                                            String cartkey = dataSnapshot3.getKey();

                                                                                                                                            CustomerCart customerCart1 = dataSnapshot3.getValue(CustomerCart.class);
                                                                                                                                            String servreference = customerCart1.getServices().getService_reference();
                                                                                                                                            double servSubtotal = customerCart1.getServices().getService_subtotal();
                                                                                                                                            double servPrice = customerCart1.getServices().getService_price();
                                                                                                                                            int servQty = customerCart1.getServices().getService_qty();
                                                                                                                                            double discountedPrice = customerCart1.getServices().getDiscounted_price();

                                                                                                                                            //if service already exists and we need to update some fields
                                                                                                                                            if (servreference.equals(serviceReference)){

                                                                                                                                                if (selectedcustomertype.equals("Senior Citizen")){
                                                                                                                                                    int totalQty = currentTotalQty + 1;

                                                                                                                                                    String productSubtotalStr = String.format("%.2f", discountedPrice * totalQty);
                                                                                                                                                    double productSubtotal = Double.parseDouble(productSubtotalStr);
                                                                                                                                                    double subtotalVat = Double.parseDouble(productSubtotalStr);

                                                                                                                                                    String vatExemptSaleStr = String.format("%.2f", subtotalVat / 1.12);
                                                                                                                                                    double vatExemptSale = Double.parseDouble(vatExemptSaleStr);

                                                                                                                                                    String lessVatStr = String.format("%.2f", subtotalVat - vatExemptSale);
                                                                                                                                                    double lessVat = Double.parseDouble(lessVatStr);

                                                                                                                                                    String seniorDiscountStr = String.format("%.2f", vatExemptSale * .20);
                                                                                                                                                    double seniorDiscount = Double.parseDouble(seniorDiscountStr);

                                                                                                                                                    String totalDueStr = String.format("%.2f", vatExemptSale - seniorDiscount);
                                                                                                                                                    double totalDue = Double.parseDouble(totalDueStr);

                                                                                                                                                    String partialItemDiscSubtotalStr = String.format("%.2f", discountedPrice * totalQty);
                                                                                                                                                    double partialItemDiscSubtotal = Double.parseDouble(partialItemDiscSubtotalStr);

                                                                                                                                                    String partialItemOrigPriceSubtotalStr = String.format("%.2f", servPrice * totalQty);
                                                                                                                                                    double partialItemOrigPriceSubtotal = Double.parseDouble(partialItemOrigPriceSubtotalStr);

                                                                                                                                                    String totalDiscountStr = String.format("%.2f", partialItemDiscSubtotal - partialItemOrigPriceSubtotal);
                                                                                                                                                    double totalDiscount = Double.parseDouble(totalDiscountStr);


                                                                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/customer_cart/").child(cartkey+"/services/service_qty").setValue(servQty+1);
                                                                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/customer_cart/").child(cartkey+"/services/service_subtotal").setValue(productSubtotal);

                                                                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/senior_discount").setValue(seniorDiscount);
                                                                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/subtotal").setValue(subtotalVat);
                                                                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/total_item_discount").setValue(totalDiscount);
                                                                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/total_item_qty").setValue(totalQty);
                                                                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/vat").setValue(lessVat);
                                                                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/vat_exempt_sale").setValue(vatExemptSale);
                                                                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/amount_due").setValue(totalDue);
                                                                                                                                                    cartMap.clear();
                                                                                                                                                    Toast.makeText(getActivity(), "Quantity has been updated.", Toast.LENGTH_SHORT).show();

                                                                                                                                                }else {
                                                                                                                                                    //if Regular Customer

                                                                                                                                                    int totalQty = currentTotalQty + 1;

                                                                                                                                                    String productSubtotalStr = String.format("%.2f", discountedPrice * totalQty);
                                                                                                                                                    double productSubtotal = Double.parseDouble(productSubtotalStr);

                                                                                                                                                    String totalDueStr = String.format("%.2f", currentAmountDue + discountedPrice);
                                                                                                                                                    double totalDue = Double.parseDouble(totalDueStr);

                                                                                                                                                    String subtotalStr = String.format("%.2f", totalDue / 1.12);
                                                                                                                                                    double subtotal = Double.parseDouble(subtotalStr);

                                                                                                                                                    String vatStr = String.format("%.2f", totalDue - subtotal);
                                                                                                                                                    double vat = Double.parseDouble(vatStr);

                                                                                                                                                    String partialDiscount_str = String.format("%.2f", servPrice * totalQty);
                                                                                                                                                    //
                                                                                                                                                    //
                                                                                                                                                    double partialDiscount = Double.parseDouble(partialDiscount_str);
                                                                                                                                                    double totalDiscount = productSubtotal - partialDiscount;
//
//
                                                                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/customer_cart/").child(cartkey+"/services/service_qty").setValue(servQty+1);
                                                                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/customer_cart/").child(cartkey+"/services/service_subtotal").setValue(productSubtotal);
                                                                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/total_item_qty").setValue(totalQty);
                                                                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/vat").setValue(vat);
                                                                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/subtotal").setValue(subtotal);
                                                                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/amount_due").setValue(totalDue);
                                                                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/total_item_discount").setValue(totalDiscount);
                                                                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/vat_exempt_sale").setValue(0.00);
                                                                                                                                                    cartMap.clear();
                                                                                                                                                    Toast.makeText(getActivity(), "Quantity has been updated.", Toast.LENGTH_SHORT).show();

                                                                                                                                                }
                                                                                                                                            }else {
//                                                                                                    Toast.makeText(getActivity(), customerTransaction1.getSubtotal()+" is the current subtotal", Toast.LENGTH_SHORT).show();
                                                                                                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/customer_cart").push().setValue(customerCart);
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    }else {

                                                                                                                                        //transaction already exist, but we will add different service to the cart

                                                                                                                                        double discountedprice = services.getDiscounted_price();
                                                                                                                                        int servqty = services.getService_qty();
                                                                                                                                        double servprice = services.getService_price();

                                                                                                                                        if (selectedcustomertype.equals("Senior Citizen")){

                                                                                                                                            int totalQty = currentTotalQty + 1;

                                                                                                                                            String subtotalStr = String.format("%.2f", currentSubtotal + discountedprice); //////// old subtotal + discounted price of the selected item
                                                                                                                                            double subtotal = Double.parseDouble(subtotalStr);

                                                                                                                                            String vatExemptSaleStr = String.format("%.2f", subtotal / 1.12);
                                                                                                                                            double vatExempt = Double.parseDouble(vatExemptSaleStr);

                                                                                                                                            String vatStr = String.format("%.2f", subtotal - vatExempt);
                                                                                                                                            double vat = Double.parseDouble(vatStr);

                                                                                                                                            String seniorDiscountStr = String.format("%.2f", vatExempt * 0.20);
                                                                                                                                            double seniorDiscount = Double.parseDouble(seniorDiscountStr);

                                                                                                                                            String totalDueStr = String.format("%.2f", vatExempt - seniorDiscount);
                                                                                                                                            double totalDue = Double.parseDouble(totalDueStr);

                                                                                                                                            String partialSubtotalStr = String.format("%.2f", discountedprice * servqty);
                                                                                                                                            double partialSubtotal = Double.parseDouble(partialSubtotalStr);

                                                                                                                                            String partialDiscountStr = String.format("%.2f", servprice * servqty);
                                                                                                                                            double partialDiscount = Double.parseDouble(partialDiscountStr);

                                                                                                                                            String totalDiscountStr = String.format("%.2f", partialSubtotal - partialDiscount);
                                                                                                                                            double totalDiscount = Double.parseDouble(totalDiscountStr);
//
                                                                                                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/total_item_qty").setValue(totalQty);
                                                                                                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/subtotal").setValue(subtotal);
                                                                                                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/vat").setValue(vat);
                                                                                                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/vat_exempt_sale").setValue(vatExempt);
                                                                                                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/senior_discount").setValue(seniorDiscount);
                                                                                                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/amount_due").setValue(totalDue);
                                                                                                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/total_item_discount").setValue(totalDiscount);
                                                                                                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/customer_cart").updateChildren(cartMap);
                                                                                                                                            cartMap.clear();

                                                                                                                                        }else {
                                                                                                                                            //customer is Regular Customer
                                                                                                                                            int totalQty = currentTotalQty + 1;

                                                                                                                                            String totalDueStr = String.format("%.2f", currentAmountDue + discountedprice);
                                                                                                                                            double totalDue = Double.parseDouble(totalDueStr);

                                                                                                                                            String subtotalStr = String.format("%.2f", totalDue / 1.12);
                                                                                                                                            double subtotal = Double.parseDouble(subtotalStr);

                                                                                                                                            String vatStr = String.format("%.2f", totalDue - subtotal);
                                                                                                                                            double vat = Double.parseDouble(vatStr);
//
                                                                                                                                            String partialSubtotalStr = String.format("%.2f", discountedprice * servqty);
                                                                                                                                            double partialSubtotal = Double.parseDouble(partialSubtotalStr);
                                                                                                                                            String partialDiscountStr = String.format("%.2f", servprice * servqty);
                                                                                                                                            double partialDiscount = Double.parseDouble(partialDiscountStr);

                                                                                                                                            String totalDiscountStr = String.format("%.2f", partialSubtotal - partialDiscount);
                                                                                                                                            double totalDiscount = Double.parseDouble(totalDiscountStr);

//
                                                                                                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/total_item_qty").setValue(totalQty);
                                                                                                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/vat").setValue(vat);
                                                                                                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/subtotal").setValue(subtotal);
                                                                                                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/amount_due").setValue(totalDue);
                                                                                                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/total_item_discount").setValue(totalDiscount);
                                                                                                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/vat_exempt_sale").setValue(0.00);
                                                                                                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+ customertransactionkey +"/customer_cart").updateChildren(cartMap);
                                                                                                                                            cartMap.clear();
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                }

                                                                                                                                @Override
                                                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                                                }
                                                                                                                            });

                                                                                                                }
                                                                                                            }else {
                                                                                                                // this is the logic for creating new data.

                                                                                                                double discountedprice = services.getDiscounted_price();
                                                                                                                int servqty = services.getService_qty();
                                                                                                                double servprice = services.getService_price();

                                                                                                                if (selectedcustomertype.equals("Senior Citizen")){

                                                                                                                    String subtotalVatStr = String.format("%.2f", discountedprice * servqty);
                                                                                                                    double subtotalVat = Double.parseDouble(subtotalVatStr);

                                                                                                                    String vatExemptSaleStr = String.format("%.2f", (discountedprice * servqty) / 1.12);
                                                                                                                    double vatExemptSale = Double.parseDouble(vatExemptSaleStr);

                                                                                                                    String lessVatStr = String.format("%.2f", subtotalVat - vatExemptSale);
                                                                                                                    double lessVat = Double.parseDouble(lessVatStr);

                                                                                                                    String seniorDiscountStr = String.format("%.2f", vatExemptSale * .20);
                                                                                                                    double seniorDiscount = Double.parseDouble(seniorDiscountStr);

                                                                                                                    String totalDueStr = String.format("%.2f", vatExemptSale - seniorDiscount);
                                                                                                                    double totalDue = Double.parseDouble(totalDueStr);

                                                                                                                    String partialItemDiscSubtotalStr = String.format("%.2f", discountedprice * servqty);
                                                                                                                    double partialItemDiscSubtotal = Double.parseDouble(partialItemDiscSubtotalStr);

                                                                                                                    String partialItemOrigPriceSubtotalStr = String.format("%.2f", servprice * servqty);
                                                                                                                    double partialItemOrigPriceSubtotal = Double.parseDouble(partialItemOrigPriceSubtotalStr);

                                                                                                                    String totalDiscountStr = String.format("%.2f", partialItemDiscSubtotal - partialItemOrigPriceSubtotal);
                                                                                                                    double totalDiscount = Double.parseDouble(totalDiscountStr);


                                                                                                                    customerTransaction.setCustomer_type(selectedcustomertype);
                                                                                                                    customerTransaction.setTotal_item_qty(servqty);
                                                                                                                    customerTransaction.setSubtotal(subtotalVat); //with vat
                                                                                                                    customerTransaction.setTotal_item_discount(totalDiscount); //total discount (if the item is discounted)
                                                                                                                    customerTransaction.setVat(lessVat); //vat off
                                                                                                                    customerTransaction.setVat_exempt_sale(vatExemptSale); //this is the total price minus the 12% vat
                                                                                                                    customerTransaction.setSenior_discount(seniorDiscount); //this is the value of the 20% senior citizen discount
                                                                                                                    customerTransaction.setAmount_due(totalDue); //amount that will be billed to the senior citizen
                                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction").push().setValue(customerTransaction); //creating a new customer transaction node

                                                                                                                    Toast.makeText(getActivity(), "Item has been added to cart.", Toast.LENGTH_SHORT).show();

                                                                                                                }else {
                                                                                                                    String totalAmountDueStr = String.format("%.2f", discountedprice * servqty);
                                                                                                                    double totalAmountDue = Double.parseDouble(totalAmountDueStr);

                                                                                                                    String subtotalStr = String.format("%.2f",  totalAmountDue / 1.12);
                                                                                                                    double subtotal = Double.parseDouble(subtotalStr);

                                                                                                                    String vatStr = String.format("%.2f", totalAmountDue - subtotal);
                                                                                                                    double vat = Double.parseDouble(vatStr);

                                                                                                                    String partialItemDiscSubtotalStr = String.format("%.2f", discountedprice * servqty);
                                                                                                                    double partialItemDiscSubtotal = Double.parseDouble(partialItemDiscSubtotalStr);

                                                                                                                    String partialItemOrigPriceSubtotalStr = String.format("%.2f", servprice * servqty);
                                                                                                                    double partialItemOrigPriceSubtotal = Double.parseDouble(partialItemOrigPriceSubtotalStr);

                                                                                                                    String totalDiscountStr = String.format("%.2f", partialItemDiscSubtotal - partialItemOrigPriceSubtotal);
                                                                                                                    double totalDiscount = Double.parseDouble(totalDiscountStr);

                                                                                                                    customerTransaction.setCustomer_type(selectedcustomertype);
                                                                                                                    customerTransaction.setSubtotal(subtotal);
                                                                                                                    customerTransaction.setTotal_item_qty(servqty);
                                                                                                                    customerTransaction.setVat(vat);
                                                                                                                    customerTransaction.setVat_exempt_sale(0.00);
                                                                                                                    customerTransaction.setAmount_due(totalAmountDue);
                                                                                                                    customerTransaction.setTotal_item_discount(totalDiscount);

                                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction").push().setValue(customerTransaction); //creating a new customer transaction node
                                                                                                                    Toast.makeText(getActivity(), "Item has been added to cart.", Toast.LENGTH_SHORT).show();
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

                                                                            }else {
                                                                                Toast.makeText(getActivity(), "QR Code does not exist", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                        }
                                                                    });

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

    private void startNewSale() {
        // get first from shared preference.
        SharedPreferences shared = getActivity().getSharedPreferences("CustomerIdPref", MODE_PRIVATE);
        int sharedPrefCustId = (shared.getInt("customer_id", 0));
        int newId = sharedPrefCustId + 1;

        // store the new customer id to shared preference.
        SharedPreferences customerIdPref = getActivity().getSharedPreferences("CustomerIdPref", MODE_PRIVATE);
        SharedPreferences.Editor customerIdEditor = customerIdPref.edit();
        customerIdEditor.putInt("customer_id", newId);
        customerIdEditor.commit();

        // go to activity
        Intent intent = new Intent(getActivity(), CashierNavigation.class);
        startActivity(intent);
        Toast.makeText(getActivity(), "Starting a new sale.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int sid = parent.getId();
        switch (sid){
            case R.id.spinner_inventorytype:
                selectedinventorytype = itemListSpinner.getItemAtPosition(position).toString();
                if (selectedinventorytype.equals("Products")){
                    viewAllProducts();
                }else if (selectedinventorytype.equals("Services")){
                    viewAllServices();
                }
                break;
            case R.id.spinner_customertype:
                selectedcustomertype = spinnerCustomerType.getItemAtPosition(position).toString();

                //save date and time
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyy HH:mm:ss a");
                SimpleDateFormat dayOfWeek = new SimpleDateFormat("EEEE");
                Date date = new Date();
                String dayName = dayOfWeek.format(date);
                String transactionDateTime1 = dateFormat.format(calendar.getTime());
                transactionDateTime = dayName+" "+transactionDateTime1;

                SharedPreferences transactionDateTimePref = getActivity().getSharedPreferences("TransactionDateTimePref", MODE_PRIVATE);
                SharedPreferences.Editor dateTimeEditor = transactionDateTimePref.edit();

                dateTime = transactionDateTime;
                dateTimeEditor.putString("trans_dateTime", dateTime);
                dateTimeEditor.commit();

                ////
                SharedPreferences customerTypePref = getActivity().getSharedPreferences("CustomerTypePref", MODE_PRIVATE);
                SharedPreferences.Editor editor = customerTypePref.edit();

                String customertype = selectedcustomertype;
                editor.putString("customer_type", customertype);
                editor.commit();

                break;
        }
    }

    private void viewAllServices() {

        SharedPreferences shared = getActivity().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        SharedPreferences empPref = getActivity().getSharedPreferences("EmpPref", MODE_PRIVATE);
        final String empUsername = empPref.getString("emp_username", "");

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        final String key = ds.getKey();

                        ownerdbreference.child(key+"/business/employee").orderByChild("emp_username").equalTo(empUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                             if (dataSnapshot.exists()){
                                 for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                     String empKey = dataSnapshot1.getKey();

                                     ownerdbreference.child(key+"/business/services").addValueEventListener(new ValueEventListener() {
                                         @Override
                                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                             slist = new ArrayList<>();
                                             for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                                 Services services = dataSnapshot1.getValue(Services.class);
                                                 Serviceslistdata slistdata = new Serviceslistdata();
                                                 String sname = services.getService_name();
                                                 double sprice = services.getService_price();
                                                 double discountedprice = services.getDiscounted_price();
                                                 String status = services.getService_status();
                                                 String image = services.getService_image();

                                                 if (status.equals("Available")){
                                                     slistdata.setServname(sname);
                                                     slistdata.setServprice(sprice);
                                                     slistdata.setDiscounted_price(discountedprice);
                                                     slistdata.setService_image(image);
                                                     slist.add(slistdata);
                                                 }

                                             }
                                             PurchaseInventoryServicesAdapter sadapter = new PurchaseInventoryServicesAdapter(slist, getContext());
                                             GridLayoutManager sgridLayoutManager = new GridLayoutManager(getActivity(),5);
                                             sgridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                             recyclerViewitemlist.setLayoutManager(sgridLayoutManager);
                                             recyclerViewitemlist.setItemAnimator(new DefaultItemAnimator());
                                             recyclerViewitemlist.setAdapter(sadapter);
                                             sadapter.notifyDataSetChanged();

                                             invprogress.setVisibility(View.GONE);

                                             if(slist.isEmpty()){
                                                 emptylayout.setVisibility(View.VISIBLE);
                                             }else{
                                                 emptylayout.setVisibility(View.GONE);
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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}
