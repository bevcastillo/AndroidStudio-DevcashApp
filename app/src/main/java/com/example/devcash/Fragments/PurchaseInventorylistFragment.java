package com.example.devcash.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.AllPurchaseActivity;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseInventorylistFragment extends Fragment implements SearchView.OnQueryTextListener,
        MenuItem.OnActionExpandListener, View.OnClickListener, AdapterView.OnItemSelectedListener {

    //
    DatabaseReference dbreference;
    DatabaseReference productsdbreference;
    DatabaseReference servicesdbreference;
    DatabaseReference ownerdbreference;
    FirebaseDatabase firebaseDatabase;

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
    String selectedinventorytype, selectedcustomertype;
    int customerId;

    SwipeRefreshLayout refreshSwipe;


    public PurchaseInventorylistFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_purchase_item_list, container, false);
        View view = inflater.inflate(R.layout.fragment_purchase_inventory_list, container, false);
        setHasOptionsMenu(true);

        itemListToolbar = (Toolbar) view.findViewById(R.id.toolbar_purchaseitemlist);
        itemListSpinner = (Spinner) view.findViewById(R.id.spinner_inventorytype);

        invprogress = (ProgressBar) view.findViewById(R.id.inv_progressbar);
        emptylayout = (LinearLayout) view.findViewById(R.id.layout_emptyinv);
        qtypricetext = (TextView) view.findViewById(R.id.textqtyprice);
        chargebtnlayout = (LinearLayout) view.findViewById(R.id.btn_chargeitem);
        scanqrcode = (LinearLayout) view.findViewById(R.id.scanqrcode_layout);
        newsale = (LinearLayout) view.findViewById(R.id.layoutnewsale);
        spinnerCustomerType = (Spinner) view.findViewById(R.id.spinner_customertype);

//        refreshSwipe = (SwipeRefreshLayout) view.findViewById(R.id.purchase_pullRefresh);
//
//        refreshSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                displayQtyPrice();
//                refreshSwipe.setRefreshing(false);
//            }
//        });

        chargebtnlayout.setOnClickListener(this);
        scanqrcode.setOnClickListener(this);
        newsale.setOnClickListener(this);
        itemListSpinner.setOnItemSelectedListener(this);
        spinnerCustomerType.setOnItemSelectedListener(this);

        recyclerViewitemlist = (RecyclerView) view.findViewById(R.id.recyclerview_purchitemlist);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbreference = firebaseDatabase.getReference("/datadevcash");
        productsdbreference = firebaseDatabase.getReference("datadevcash/products");
        servicesdbreference = firebaseDatabase.getReference("datadevcash/services");
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


//        itemListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedinventorytype = itemListSpinner.getItemAtPosition(position).toString();
//                if(itemListSpinner.getSelectedItem().equals("Products")){
//                    viewAllProducts();
//                }else if((itemListSpinner.getSelectedItem().equals("Services"))){
//                    viewAllServices();
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        return view;


    }

    @Override
    public void onStart() {
        super.onStart();

        displayQtyPrice(); //display the total price and the total quantity to the button
    }

    //


    public void viewAllProducts(){

        SharedPreferences shared = getActivity().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        String key = ds.getKey();
                        dbreference.child("owner/"+key+"/business/product").addValueEventListener(new ValueEventListener() {
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

                                        if (status.equals("Available")){
                                            if (prodStock > 0){
                                                listdata.setProd_name(prodname);
                                                listdata.setProd_price(prodprice);
                                                listdata.setDiscounted_price(discountedprice);
                                                listdata.setProd_expdate(product.getProd_expdate());
                                                list.add(listdata);
                                            }
                                        }

                                    }
                                        PurchaseInventoryProductsAdapter adapter = new PurchaseInventoryProductsAdapter(list);
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


    public void viewAllServices(){
        SharedPreferences shared = getActivity().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        String key = ds.getKey();
                        dbreference.child("owner/"+key+"/business/services").addValueEventListener(new ValueEventListener() {
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

                                        if (status.equals("Available")){
                                            slistdata.setServname(sname);
                                            slistdata.setServprice(sprice);
                                            slistdata.setDiscounted_price(discountedprice);
                                            slist.add(slistdata);
                                        }

                                    }
                                        PurchaseInventoryServicesAdapter sadapter = new PurchaseInventoryServicesAdapter(slist);
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

    //handles the search menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.searchmenu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search..");

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_chargeitem:
                Intent intent = new Intent(getActivity(), AllPurchaseActivity.class);
                intent.putExtra("selectedCustomerType", selectedcustomertype); //passing the selected customer to the intent
                startActivity(intent);


                break;
            case R.id.scanqrcode_layout:
                scanQrCode();
                break;
            case R.id.layoutnewsale:
                startNewSale();
                break;
        }
    }

    public void startNewSale() {
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
        Intent intent = new Intent(getActivity(), DashboardActivity.class);
        startActivity(intent);
        Toast.makeText(getActivity(), "Starting a new sale.", Toast.LENGTH_SHORT).show();
    }

    public void scanQrCode(){
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


    public void saveToFirebaseUsingScanner(final String qrItem) {
        SharedPreferences shared = getActivity().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        final String CustomerCartId = ownerdbreference.push().getKey();

        // get from product node.
        ownerdbreference.orderByChild("business/owner_username")
                .equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String acctKey = dataSnapshot1.getKey();

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
                                                                                final double currentTotalPrice = customerTransaction1.getAmount_due();
                                                                                final int currentTotalQty = customerTransaction1.getTotal_item_qty();

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

                                                                                                if (prodreference.equals(preference)){
                                                                                                    // we update the product qty if ever we already purchased.
                                                                                                    product.setProd_qty(customerCart1.getProduct().getProd_qty() + 1);

                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/customer_cart/").child(cartkey+"/product/prod_qty").setValue(product.getProd_qty());
                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/customer_cart/").child(cartkey+"/product/prod_subtotal").setValue(product.getProd_qty() * product.getDiscounted_price());
                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/vat").setValue((((product.getDiscounted_price() * product.getProd_qty()) * .12) * 100) / 100);
                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/total_price").setValue(((currentSubtotal + product.getDiscounted_price()) * 100) / 100);
                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/subtotal").setValue(((product.getDiscounted_price() * product.getProd_qty()) - ((product.getDiscounted_price() * product.getProd_qty()) * .12) * 100) / 100);
//                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/total_qty").setValue((currentTotalQty + product.getProd_qty()) - 1);
                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/total_qty").setValue(product.getProd_qty());
                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/total_discount").setValue((product.getDiscounted_price() * product.getProd_qty()) - (product.getProd_price() * product.getProd_qty()));
                                                                                                    cartMap.clear();
                                                                                                }else {
                                                                                                    Toast.makeText(getActivity(), customerTransaction1.getSubtotal()+" is the current subtotal", Toast.LENGTH_SHORT).show();
                                                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/customer_cart").push().setValue(customerCart);
                                                                                                }
                                                                                            }
                                                                                        }else {

                                                                                            double discountedPrice = product.getDiscounted_price();
                                                                                            double productQty = product.getProd_qty();

                                                                                            String vatStr = String.format("%.2f", (discountedPrice * productQty)  * .12);
                                                                                            double vat = Double.parseDouble(vatStr);

                                                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/vat").setValue((((product.getDiscounted_price() * product.getProd_qty()) * .12) * 100) / 100);
                                                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/subtotal").setValue(((product.getDiscounted_price() * product.getProd_qty()) - ((product.getDiscounted_price() * product.getProd_qty()) * .12) * 100) / 100);
                                                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/total_price").setValue(((currentSubtotal + product.getDiscounted_price()) * 100) / 100);
                                                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/total_qty").setValue(currentTotalQty);
                                                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/total_discount").setValue((product.getDiscounted_price() * product.getProd_qty()) - (product.getProd_price() * product.getProd_qty()));
                                                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/customer_cart").updateChildren(cartMap);
                                                                                            cartMap.clear();
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                    }
                                                                                });

                                                                            }
                                                                        }else {
                                                                            // this is the logic for creating new data.

                                                                            double discountedPrice = product.getDiscounted_price();
                                                                            int prodQty = product.getProd_qty();
                                                                            double productPrice = product.getProd_price();

                                                                            String vatStr = String.format("%.2f", (discountedPrice * prodQty) * .12);
                                                                            double vat = Double.parseDouble(vatStr);

                                                                            String totalPriceStr = String.format("%.2f", discountedPrice * prodQty);
                                                                            double totalPrice = Double.parseDouble(totalPriceStr);

                                                                            String subtotalStr = String.format("%.2f", totalPrice - vat);
                                                                            double subtotal = Double.parseDouble(subtotalStr);

                                                                            String partialDiscountedPriceStr = String.format("%.2f", productPrice * prodQty);
                                                                            double partialDiscountedPrice = Double.parseDouble(partialDiscountedPriceStr);

                                                                            String totalDiscountedPriceStr = String.format("%.2f", totalPrice - partialDiscountedPrice);
                                                                            double totalDiscountedPrice = Double.parseDouble(totalDiscountedPriceStr);


                                                                            customerTransaction.setVat(vat);
                                                                            customerTransaction.setSubtotal(subtotal);
                                                                            customerTransaction.setAmount_due(totalPrice);
                                                                            customerTransaction.setTotal_item_qty(prodQty);
                                                                            customerTransaction.setTotal_item_discount(totalDiscountedPrice);
                                                                            ownerdbreference.child(acctkey+"/business/customer_transaction").push().setValue(customerTransaction); //creating a new customer transaction node

                                                                            Toast.makeText(getActivity(), "Item has been added to cart.", Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(getActivity(), "QRCode is not in the database.", Toast.LENGTH_SHORT).show();
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




    public void displayQtyPrice(){

        SharedPreferences shared = getActivity().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

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
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyy HH:mm a");
                String transactionDateTime = dateFormat.format(calendar.getTime());

                SharedPreferences transactionDateTimePref = getActivity().getSharedPreferences("TransactionDateTimePref", MODE_PRIVATE);
                SharedPreferences.Editor dateTimeEditor = transactionDateTimePref.edit();

                String dateTime = transactionDateTime;
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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
