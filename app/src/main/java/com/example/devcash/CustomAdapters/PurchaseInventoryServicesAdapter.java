package com.example.devcash.CustomAdapters;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public PurchaseInventoryServicesAdapter(List<Serviceslistdata> list) {
        this.list = list;
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


        viewHolder.servicename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                itemcount++;

                final String TransId = ownerdbreference.push().getKey();
                final String CustomerCartId = ownerdbreference.push().getKey();

                final String servname = list.get(viewHolder.getAdapterPosition()).getServname();
                final double servprice = list.get(viewHolder.getAdapterPosition()).getServprice();

                list.get(viewHolder.getAdapterPosition()).setClick(list.get(viewHolder.getAdapterPosition()).getClick() + 1);

                final int servqty = list.get(viewHolder.getAdapterPosition()).getClick();

                final Services services = new Services();
                services.setService_name(servname);
                services.setService_price(servprice);
                services.setService_qty(servqty);
                services.setService_subtotal(services.getService_qty() * services.getService_price());

                final CustomerCart customerCart = new CustomerCart();
                customerCart.setServices(services);
                cartMap.put(CustomerCartId, customerCart);

                final CustomerTransaction customerTransaction = new CustomerTransaction();
                customerTransaction.setCustomer_id(customerId);
                customerTransaction.setCustomer_cart(cartMap);

                // save the added customer id to shared pref.
                SharedPreferences customerIdPref = v.getContext().getSharedPreferences("CustomerIdPref", MODE_PRIVATE);
                final SharedPreferences.Editor customerIdEditor = customerIdPref.edit();
                customerIdEditor.putInt("customer_id", customerId);
                customerIdEditor.commit();

                SharedPreferences shared = v.getContext().getSharedPreferences("OwnerPref", MODE_PRIVATE);
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
                                                final String customertransactionkey = dataSnapshot2.getKey();
                                                final CustomerTransaction customerTransaction1 = dataSnapshot2.getValue(CustomerTransaction.class);
                                                final double currentSubtotal = customerTransaction1.getSubtotal();
                                                final double currentTotalQty = customerTransaction1.getTotal_qty();

                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/customer_cart")
                                                        .orderByChild("services/service_name").equalTo(servname).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()){
                                                            for (DataSnapshot dataSnapshot3: dataSnapshot.getChildren()){
                                                                String cartkey = dataSnapshot3.getKey();
                                                                CustomerCart customerCart1 = dataSnapshot3.getValue(CustomerCart.class);
                                                                String itemname = customerCart1.getServices().getService_name();
                                                                Toast.makeText(v.getContext(), itemname+" is the item name", Toast.LENGTH_SHORT).show();

                                                                if (itemname.equals(servname)){
                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/customer_cart/").child(cartkey+"/services/service_qty").setValue(servqty);
                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/customer_cart/").child(cartkey+"/services/service_subtotal").setValue(servqty*servprice);
                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/subtotal").setValue(currentSubtotal + servprice);
                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/total_qty").setValue((currentTotalQty + servqty) - 1);
                                                                    Toast.makeText(v.getContext(), servname+" quantity has been updated", Toast.LENGTH_SHORT).show();
                                                                    cartMap.clear();
                                                                }else {
                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/customer_cart").push().setValue(customerCart);
                                                                }

                                                            }
                                                        }else {
                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/subtotal").setValue(currentSubtotal + servprice);
                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/total_qty").setValue((currentTotalQty + servqty));
//                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/total_qty").setValue(currentSubtotal + servprice);
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
                                            // this is the logic to create new data.
                                            customerTransaction.setSubtotal(servprice * servqty);
                                            customerTransaction.setTotal_qty(servqty);
                                            ownerdbreference.child(acctkey+"/business/customer_transaction").push().setValue(customerTransaction);
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView servicename, serviceprice, expiration;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            servicename = (TextView)itemView.findViewById(R.id.prodlist_name);
            serviceprice = (TextView) itemView.findViewById(R.id.prodlist_price);
        }
    }



}
