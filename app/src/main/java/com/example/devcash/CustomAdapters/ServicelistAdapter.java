package com.example.devcash.CustomAdapters;

import android.app.Service;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.Object.CartItem;
import com.example.devcash.Object.CustomerCart;
import com.example.devcash.Object.CustomerTransaction;
import com.example.devcash.Object.Services;
import com.example.devcash.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ServicelistAdapter extends RecyclerView.Adapter<ServicelistAdapter.ViewHolder> {

    List<Services> servicesList;

    private DatabaseReference ownerdbreference;
    private FirebaseDatabase firebaseDatabase;

    private static int itemcount = 0;
    int customerId;

    public ServicelistAdapter(List<Services> servicesList) {
        this.servicesList = servicesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_itemsreceipt, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ownerdbreference = firebaseDatabase.getReference("datadevcash/owner");

        SharedPreferences shared = v.getContext().getSharedPreferences("CustomerIdPref", MODE_PRIVATE);
        int sharedCustId = (shared.getInt("customer_id", 0));

        if (sharedCustId <= 0) {
            customerId = sharedCustId + 1;
        } else {
            customerId = sharedCustId;
        }

        viewHolder.servicename.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add("Edit Quantity").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return true;
                    }
                });

                menu.add("Delete item").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        final String serviceName = servicesList.get(viewHolder.getAdapterPosition()).getService_name();
                        final double serviceQty = servicesList.get(viewHolder.getAdapterPosition()).getService_qty();
                        double serviceDiscountedPrice = servicesList.get(viewHolder.getAdapterPosition()).getDiscounted_price();

                        SharedPreferences shared = v.getContext().getSharedPreferences("OwnerPref", MODE_PRIVATE);
                        final String username = (shared.getString("owner_username", ""));

                        //query to delete item from transaction
                        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                        final String ownerKey = dataSnapshot1.getKey();

                                        ownerdbreference.child(ownerKey+"/business/customer_transaction").orderByChild("customer_id").equalTo(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()){
                                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                                        final String customerTransactionKey = dataSnapshot2.getKey();
                                                        final CustomerTransaction customerTransaction = dataSnapshot2.getValue(CustomerTransaction.class);

                                                        ownerdbreference.child(ownerKey+"/business/customer_transaction/"+customerTransactionKey+"/customer_cart")
                                                                .orderByChild("services/service_name").equalTo(serviceName).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.exists()){
                                                                    for (DataSnapshot dataSnapshot3: dataSnapshot.getChildren()){
                                                                        String customerCartKey = dataSnapshot3.getKey();
                                                                        CustomerCart customerCart = dataSnapshot3.getValue(CustomerCart.class);
                                                                        double servQty = customerCart.getServices().getService_qty();
                                                                        double serviceSubTotal = customerCart.getServices().getService_subtotal();
                                                                        double servicePrice = customerCart.getServices().getService_price();
                                                                        double serviceDiscountedPrice = customerCart.getServices().getDiscounted_price();

                                                                        //details from customer transaction
                                                                        double totalQty = customerTransaction.getTotal_qty();
                                                                        double currentSubtotal = customerTransaction.getSubtotal();
                                                                        double currentTotalPrice = customerTransaction.getTotal_price();
                                                                        double currentTotalDiscount = customerTransaction.getTotal_discount();

                                                                        //this is the computation to delete the selected service from the cart
                                                                        double newTotalQty = totalQty - servQty;

                                                                        String newServDiscValueStr = String.format("%.2f", (servicePrice - serviceDiscountedPrice) * servQty); //we get the total discount value of each service
                                                                        double newServDiscValue = Double.parseDouble(newServDiscValueStr);

                                                                        String getPositiveDiscountStr = String.format("%.2f", currentTotalDiscount * -1); //we convert the negative discount amount to positive so that we can deduct it
                                                                        double getPositiveDiscount = Double.parseDouble(getPositiveDiscountStr);

                                                                        String newTotalDiscountStr = String.format("%.2f", getPositiveDiscount - newServDiscValue);
                                                                        double newTotalDiscount = Double.parseDouble(newTotalDiscountStr);

                                                                        String newTotalPriceStr = String.format("%.2f", currentTotalPrice - serviceSubTotal);
                                                                        double newTotalPrice = Double.parseDouble(newTotalPriceStr);

                                                                        String newVatStr = String.format("%.2f", newTotalPrice * .12);
                                                                        double newVat = Double.parseDouble(newVatStr);

                                                                        String newSubtotalStr = String.format("%.2f", newTotalPrice - newVat);
                                                                        double newSubtotal = Double.parseDouble(newSubtotalStr);


                                                                        ownerdbreference.child(ownerKey+"/business/customer_transaction/"+customerTransactionKey+"/customer_cart")
                                                                                .child(customerCartKey+"/services").setValue(null); //deleting the service node from the customer cart


                                                                        ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/total_qty").setValue(newTotalQty);
                                                                        ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/total_discount").setValue(newTotalDiscount);
                                                                        ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/subtotal").setValue(newSubtotal);
                                                                        ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/vat").setValue(newVat);
                                                                        ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/total_price").setValue(newTotalPrice);

                                                                    }
                                                                    Toast.makeText(v.getContext(), "Item has been successfully deleted.", Toast.LENGTH_SHORT).show();
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


                        return true;
                    }
                });
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Services services = servicesList.get(i);
        if (!services.equals(null)) {
            viewHolder.servicename.setText(services.getService_name());
            viewHolder.servicename.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            viewHolder.servicename.setTextColor(Color.BLACK);
            viewHolder.servicesubtotal.setText(String.valueOf(services.getService_subtotal()));
            viewHolder.servicesubtotal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            viewHolder.servicesubtotal.setTextColor(Color.BLACK);
            viewHolder.serviceqtyprice.setText(services.getService_qty() + " X "+ services.getDiscounted_price());

        }

    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView servicename, serviceqtyprice, servicesubtotal;
        ImageView imgdiscounted;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            servicename = (TextView) itemView.findViewById(R.id.itemname);
            serviceqtyprice = (TextView) itemView.findViewById(R.id.itemqtyprice);
            servicesubtotal = (TextView) itemView.findViewById(R.id.itemsubtotal);
        }
    }
}
