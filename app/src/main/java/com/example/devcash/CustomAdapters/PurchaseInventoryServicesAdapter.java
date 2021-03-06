package com.example.devcash.CustomAdapters;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.devcash.Fragments.PurchaseListFragment;
import com.example.devcash.MyUtility;
import com.example.devcash.Object.CustomerCart;
import com.example.devcash.Object.CustomerTransaction;
import com.example.devcash.Object.Item;
import com.example.devcash.Object.PurchaseTransaction;
import com.example.devcash.Object.PurchaseTransactionlistdata;
import com.example.devcash.Object.PurchasedItem;
import com.example.devcash.Object.Services;
import com.example.devcash.Object.Serviceslistdata;
import com.example.devcash.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class PurchaseInventoryServicesAdapter extends RecyclerView.Adapter<PurchaseInventoryServicesAdapter.ViewHolder> {

    private DatabaseReference ownerdbreference;
    private FirebaseDatabase firebaseDatabase;

    List<Serviceslistdata> list;
    Map<String, Object> cartMap;

    private static int itemcount = 0;
    Context context;
    int customerId;

//    public PurchaseInventoryServicesAdapter(List<Serviceslistdata> list) {
//        this.list = list;
//    }


    public PurchaseInventoryServicesAdapter(List<Serviceslistdata> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        View v;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_inventory_cardviewgrid, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ownerdbreference = firebaseDatabase.getReference("datadevcash/owner");
        cartMap = new HashMap<String, Object>();

        SharedPreferences shared = v.getContext().getSharedPreferences("CustomerIdPref", MODE_PRIVATE);
        int sharedCustId = (shared.getInt("customer_id", 0));

        if (sharedCustId <= 0) {
            customerId = sharedCustId + 1;
        } else {
            customerId = sharedCustId;
        }


        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(v.getContext(), "Adding to cart..", Toast.LENGTH_SHORT).show();
                itemcount++;

                final String TransId = ownerdbreference.push().getKey();
                final String CustomerCartId = ownerdbreference.push().getKey();

                final String servname = list.get(viewHolder.getAdapterPosition()).getServname();
                final double servprice = list.get(viewHolder.getAdapterPosition()).getServprice();
                final double discountedprice = list.get(viewHolder.getAdapterPosition()).getDiscounted_price();

                list.get(viewHolder.getAdapterPosition()).setClick(list.get(viewHolder.getAdapterPosition()).getClick() + 1);

                final int servqty = list.get(viewHolder.getAdapterPosition()).getClick();

                final Services services = new Services();
                services.setService_name(servname);
                services.setService_price(servprice);
                services.setDiscounted_price(discountedprice);
                services.setService_reference(servname+servprice);
                services.setService_qty(servqty);
//                services.setService_subtotal(discountedprice * servqty);
//                services.setService_subtotal(services.getService_qty() * services.getService_price());
                services.setService_subtotal(services.getDiscounted_price() * services.getService_qty());


                final CustomerCart customerCart = new CustomerCart();
                customerCart.setServices(services);
                cartMap.put(CustomerCartId, customerCart);

                final CustomerTransaction customerTransaction = new CustomerTransaction();
                customerTransaction.setCustomer_id(customerId);
                customerTransaction.setCustomer_cart(cartMap);

                // save the added customer id to shared pref.
                final SharedPreferences customerIdPref = v.getContext().getSharedPreferences("CustomerIdPref", MODE_PRIVATE);
                final SharedPreferences.Editor customerIdEditor = customerIdPref.edit();
                customerIdEditor.putInt("customer_id", customerId);
                customerIdEditor.commit();

                SharedPreferences shared = v.getContext().getSharedPreferences("OwnerPref", MODE_PRIVATE);
                final String username = (shared.getString("owner_username", ""));

                SharedPreferences customerTypePref = v.getContext().getSharedPreferences("CustomerTypePref", MODE_PRIVATE);
                final String customerType = (customerTypePref.getString("customer_type","Regular Customer"));

                ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                final String acctkey = dataSnapshot1.getKey();

                                ownerdbreference.child(acctkey+"/business/customer_transaction").orderByChild("customer_id").equalTo(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String customertransactionkey = "";

                                        if (dataSnapshot.exists()){
                                            for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                                customertransactionkey = dataSnapshot2.getKey();

                                                final CustomerTransaction customerTransaction1 = dataSnapshot2.getValue(CustomerTransaction.class);
                                                final double currentSubtotal = customerTransaction1.getSubtotal();
                                                final double currentAmountDue = customerTransaction1.getAmount_due();
                                                final int currentTotalQty = customerTransaction1.getTotal_item_qty();
                                                final double currentTotalDiscount = customerTransaction1.getTotal_item_discount();

                                                final String finalCustomertransactionkey = customertransactionkey;
                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/customer_cart")
                                                        .orderByChild("services/service_name")
                                                        .equalTo(servname)
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()){
                                                            for (DataSnapshot dataSnapshot3: dataSnapshot.getChildren()){
                                                                String cartkey = dataSnapshot3.getKey();
                                                                CustomerCart customerCart1 = dataSnapshot3.getValue(CustomerCart.class);
                                                                String itemname = customerCart1.getServices().getService_name();
//                                                                Toast.makeText(v.getContext(), itemname+" is the item name", Toast.LENGTH_SHORT).show();

                                                                //if selected item is already in the customer cart
                                                                if (itemname.equals(servname)){

                                                                    if (customerType.equals("Senior Citizen")){

                                                                        int totalQty = currentTotalQty + 1;


//                                                                        String productSubtotalStr = String.format("%.2f", discountedprice * servqty);
//                                                                        double productSubtotal = Double.parseDouble(productSubtotalStr);
//                                                                        double subtotalVat = Double.parseDouble(productSubtotalStr);
//
//                                                                        String vatExemptSaleStr = String.format("%.2f", subtotalVat / 1.12);
//                                                                        double vatExemptSale = Double.parseDouble(vatExemptSaleStr);
//
//                                                                        String lessVatStr = String.format("%.2f", subtotalVat - vatExemptSale);
//                                                                        double lessVat = Double.parseDouble(lessVatStr);
//
//                                                                        String seniorDiscountStr = String.format("%.2f", vatExemptSale * .20);
//                                                                        double seniorDiscount = Double.parseDouble(seniorDiscountStr);
//
//                                                                        String totalDueStr = String.format("%.2f", vatExemptSale - seniorDiscount);
//                                                                        double totalDue = Double.parseDouble(totalDueStr);
//
//                                                                        String partialItemDiscSubtotalStr = String.format("%.2f", discountedprice * servqty);
//                                                                        double partialItemDiscSubtotal = Double.parseDouble(partialItemDiscSubtotalStr);
//
//                                                                        String partialItemOrigPriceSubtotalStr = String.format("%.2f", servprice * servqty);
//                                                                        double partialItemOrigPriceSubtotal = Double.parseDouble(partialItemOrigPriceSubtotalStr);
//
//                                                                        String totalDiscountStr = String.format("%.2f", partialItemDiscSubtotal - partialItemOrigPriceSubtotal);
//                                                                        double totalDiscount = Double.parseDouble(totalDiscountStr);

                                                                        String productSubtotalStr = String.format("%.2f", discountedprice * servqty);
                                                                        double productSubtotal = Double.parseDouble(productSubtotalStr);
//

//                                                                        double subtotalVat = Double.parseDouble(productSubtotalStr);
                                                                        String subtotalVatStr = String.format("%.2f", currentSubtotal + discountedprice);
                                                                        double subtotalVat = Double.parseDouble(subtotalVatStr);

                                                                        String vatExemptSaleStr = String.format("%.2f", subtotalVat / 1.12);
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


                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/customer_cart/").child(cartkey+"/services/service_qty").setValue(servqty);
                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/customer_cart/").child(cartkey+"/services/service_subtotal").setValue(productSubtotal);

                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/senior_discount").setValue(seniorDiscount);
                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/subtotal").setValue(subtotalVat);
                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/total_item_discount").setValue(totalDiscount);
                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/total_item_qty").setValue(totalQty);
                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/vat").setValue(lessVat);
                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/vat_exempt_sale").setValue(vatExemptSale);
                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/amount_due").setValue(totalDue);
                                                                        cartMap.clear();
                                                                        Toast.makeText(v.getContext(), "Quantity has been updated.", Toast.LENGTH_SHORT).show();

                                                                    }else {
                                                                        //if Regular Customer

                                                                        String productSubtotalStr = String.format("%.2f", discountedprice * servqty);
                                                                        double productSubtotal = Double.parseDouble(productSubtotalStr);

                                                                        String totalDueStr = String.format("%.2f", currentAmountDue + discountedprice);
                                                                        double totalDue = Double.parseDouble(totalDueStr);

                                                                        int totalQty = currentTotalQty + 1;

                                                                        String subtotalStr = String.format("%.2f", totalDue / 1.12);
                                                                        double subtotal = Double.parseDouble(subtotalStr);

                                                                        String vatStr = String.format("%.2f", totalDue - subtotal);
                                                                        double vat = Double.parseDouble(vatStr);

                                                                        String partialDiscount_str = String.format("%.2f", servprice * servqty);
                                                                        //
                                                                        //
                                                                        double partialDiscount = Double.parseDouble(partialDiscount_str);
                                                                        double totalDiscount = productSubtotal - partialDiscount;
//
//
                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/customer_cart/").child(cartkey+"/services/service_qty").setValue(servqty);
                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/customer_cart/").child(cartkey+"/services/service_subtotal").setValue(productSubtotal);
                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/total_item_qty").setValue(totalQty);
                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/vat").setValue(vat);
                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/subtotal").setValue(subtotal);
                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/amount_due").setValue(totalDue);
                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/total_item_discount").setValue(totalDiscount);
                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/vat_exempt_sale").setValue(0.00);
                                                                        cartMap.clear();
                                                                        Toast.makeText(v.getContext(), "Quantity has been updated.", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                }else {
                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/customer_cart").push().setValue(customerCart);
                                                                }

                                                            }
                                                        }else {
//
                                                            // Transaction already exist, but we will add different item..
                                                            if (customerType.equals("Senior Citizen")){

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
                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/total_item_qty").setValue(totalQty);
                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/subtotal").setValue(subtotal);
                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/vat").setValue(vat);
                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/vat_exempt_sale").setValue(vatExempt);
                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/senior_discount").setValue(seniorDiscount);
                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/amount_due").setValue(totalDue);
                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/total_item_discount").setValue(totalDiscount);
                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/customer_cart").updateChildren(cartMap);
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
                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/total_item_qty").setValue(totalQty);
                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/vat").setValue(vat);
                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/subtotal").setValue(subtotal);
                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/amount_due").setValue(totalDue);
                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/total_item_discount").setValue(totalDiscount);
                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/vat_exempt_sale").setValue(0.00);
                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/customer_cart").updateChildren(cartMap);
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
                                            // this is the logic to create new data.

                                            if (customerType.equals("Senior Citizen")){


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


                                                customerTransaction.setCustomer_type(customerType);
                                                customerTransaction.setTotal_item_qty(servqty);
                                                customerTransaction.setSubtotal(subtotalVat); //with vat
                                                customerTransaction.setTotal_item_discount(totalDiscount); //total discount (if the item is discounted)
                                                customerTransaction.setVat(lessVat); //vat off
                                                customerTransaction.setVat_exempt_sale(vatExemptSale); //this is the total price minus the 12% vat
                                                customerTransaction.setSenior_discount(seniorDiscount); //this is the value of the 20% senior citizen discount
                                                customerTransaction.setAmount_due(totalDue); //amount that will be billed to the senior citizen
                                                ownerdbreference.child(acctkey+"/business/customer_transaction").push().setValue(customerTransaction); //creating a new customer transaction node
                                                Toast.makeText(v.getContext(), "Item has been added to cart.", Toast.LENGTH_SHORT).show();

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

                                                customerTransaction.setCustomer_type(customerType);
                                                customerTransaction.setSubtotal(subtotal);
                                                customerTransaction.setTotal_item_qty(servqty);
                                                customerTransaction.setVat(vat);
                                                customerTransaction.setVat_exempt_sale(0.00);
                                                customerTransaction.setAmount_due(totalAmountDue);
                                                customerTransaction.setTotal_item_discount(totalDiscount);

                                                ownerdbreference.child(acctkey+"/business/customer_transaction").push().setValue(customerTransaction); //creating a new customer transaction node
                                                Toast.makeText(v.getContext(), "Item has been added to cart.", Toast.LENGTH_SHORT).show();
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
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Serviceslistdata data = list.get(i);
        viewHolder.servicename.setText(data.getServname());
        viewHolder.serviceprice.setText(String.valueOf(data.getServprice()));
        viewHolder.discountedprice.setText(String.valueOf(data.getDiscounted_price()));

        viewHolder.discountedprice.setText(String.valueOf(data.getDiscounted_price()));

        double originalprice = Double.parseDouble(viewHolder.serviceprice.getText().toString());
        double discountedprice = Double.parseDouble(viewHolder.discountedprice.getText().toString());

        String lessAmountStr = String.format("%.2f", originalprice - discountedprice);
        double lessAmount = Double.parseDouble(lessAmountStr);

        viewHolder.amountOff.setText(String.valueOf(lessAmount)+" off");


        if (viewHolder.serviceprice.getText().toString().equals(viewHolder.discountedprice.getText().toString())){
            viewHolder.serviceprice.setVisibility(View.INVISIBLE);
            viewHolder.amountOff.setVisibility(View.INVISIBLE);
        }

        if (!viewHolder.serviceprice.getText().toString().equals(viewHolder.discountedprice.getText().toString())){
            viewHolder.serviceprice.setPaintFlags(viewHolder.serviceprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.discountedprice.setText(String.valueOf(data.getDiscounted_price()));
            viewHolder.amountOff.setText(String.valueOf(lessAmount)+" off");
        }

        Picasso.with(context).load(data.getService_image()).into(viewHolder.image);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView servicename, serviceprice, expiration, discountedprice, amountOff;
        RelativeLayout layout;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            servicename = (TextView)itemView.findViewById(R.id.prodlist_name);
            serviceprice = (TextView) itemView.findViewById(R.id.prodlist_price);
            discountedprice = (TextView) itemView.findViewById(R.id.prod_discountedprice);
            amountOff = (TextView) itemView.findViewById(R.id.lessOff);
            layout = (RelativeLayout) itemView.findViewById(R.id.cardLayout);
            image = (ImageView) itemView.findViewById(R.id.prodlist_image);
        }
    }



}
