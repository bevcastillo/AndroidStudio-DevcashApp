package com.example.devcash.CustomAdapters;

import android.app.Activity;
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
import com.example.devcash.Object.PurchaseTransaction;
import com.example.devcash.Object.PurchaseTransactionlistdata;
import com.example.devcash.Object.PurchasedItem;
import com.example.devcash.Object.Services;
import com.example.devcash.Object.Serviceslistdata;
import com.example.devcash.R;
import com.google.android.gms.tasks.OnCompleteListener;
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
    List<PurchaseTransactionlistdata> purchaseTransactionlistdata;
//    List<PurchasedItem> purchasedItemList;
    Map<String, PurchasedItem> purchasedItemMap;

    private static int itemcount = 0;
    Context context;

    public PurchaseInventoryServicesAdapter(List<Serviceslistdata> list) {
        this.list = list;
    }

//    public PurchaseInventoryServicesAdapter(List<Serviceslistdata> list, List<PurchaseTransactionlistdata> purchaseTransactionlistdata) {
//        this.list = list;
//        this.purchaseTransactionlistdata = purchaseTransactionlistdata;
//    }

//    public PurchaseInventoryServicesAdapter(List<Serviceslistdata> list, Context context) {
//        this.list = list;
//        this.context = context;
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        View v;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_inventory_cardviewgrid, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ownerdbreference = firebaseDatabase.getReference("datadevcash/owner");
//        purchasedItemList = new ArrayList<PurchasedItem>();
        purchasedItemMap = new HashMap<String, PurchasedItem>();

        viewHolder.servicename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                itemcount++;

//                String owner= MyUtility.myitem.toString();
                final String PurchaseId = ownerdbreference.push().getKey();
                final String TransId = ownerdbreference.push().getKey();
                final String servname = list.get(viewHolder.getAdapterPosition()).getServname();
                final double servprice = list.get(viewHolder.getAdapterPosition()).getServprice();

                list.get(viewHolder.getAdapterPosition()).setClick(list.get(viewHolder.getAdapterPosition()).getClick() + 1);

                final int servqty = list.get(viewHolder.getAdapterPosition()).getClick();

                final Services services = new Services();
                services.setService_name(servname);
                services.setService_price(servprice);
                services.setService_qty(servqty);

                final PurchasedItem purchasedItem = new PurchasedItem();
                purchasedItem.setServices(services);
//                purchasedItemList.add(purchasedItem);
//                purchasedItemMap.put(PurchaseId, purchasedItem);

                final PurchaseTransaction purchaseTransaction = new PurchaseTransaction();
                purchaseTransaction.setPurchasedItem(purchasedItem);
//                purchaseTransaction.setPurchasedItem(purchasedItem);
//                purchaseTransaction.setPurchasedItem(purchasedItemList);

//                purchaseTransaction.setPurchasedItem(purchasedItemMap);
//                purchaseTransaction.setCust_cash(0.0);


//                Services services = new Services();
//                services.setService_name(servname);
//                services.setService_price(servprice);
//                purchaseTransaction.setPurch_qty(servqty);

//                purchaseTransaction.setPurch_qty(servqty);
//                purchaseTransaction.setServices(services);

//                itemcount = 0



                SharedPreferences shared = v.getContext().getSharedPreferences("OwnerPref", MODE_PRIVATE);
                final String username = (shared.getString("owner_username", ""));

                ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                final String acctkey =  dataSnapshot1.getKey();

//                                ownerdbreference.child(acctkey+"/business/transaction").child(PurchaseId).setValue(purchaseTransaction);

                                ownerdbreference.child(acctkey+"/business/transaction").orderByChild("purchasedItem/services/service_name").equalTo(servname).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            for(DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                                String transkey = dataSnapshot2.getKey();
                                                PurchaseTransaction purchaseTransaction1 = dataSnapshot2.getValue(PurchaseTransaction.class);
                                                String name = purchaseTransaction1.getPurchasedItem().getServices().getService_name();

                                                if(name.equals(servname)){
//                                                    ownerdbreference.child(acctkey+"/business/transaction")
//                                                            .child(transkey+"/purchasedItem/services/service_qty").setValue(servqty);

                                                    ownerdbreference.child(acctkey+"/business/transaction/"+transkey+"/purchasedItem/services/service_qty").setValue(servqty);
                                                    Toast.makeText(v.getContext(), "Quantity updated.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }else{
                                            ownerdbreference.child(acctkey+"/business/transaction").child(TransId).setValue(purchaseTransaction);
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
        TextView servicename, serviceprice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            servicename = (TextView)itemView.findViewById(R.id.prodlist_name);
            serviceprice = (TextView) itemView.findViewById(R.id.prodlist_price);
        }
    }

}
