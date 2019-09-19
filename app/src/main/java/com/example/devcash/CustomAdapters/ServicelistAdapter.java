package com.example.devcash.CustomAdapters;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

        viewHolder.layout.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

            @Override
            public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenu.ContextMenuInfo menuInfo) {

                final int myquantity = servicesList.get(viewHolder.getAdapterPosition()).getService_qty(); //get the quantity of the purchased service

                menu.add("Edit Quantity").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Edit Item Quantity");
                        final EditText qty = new EditText(v.getContext());

                        qty.setText(String.valueOf(myquantity));
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        qty.setLayoutParams(lp);
                        builder.setView(qty);
                        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //code here to update quantity

                                final int newQty = Integer.parseInt(qty.getText().toString()); //this is the quantity that the user has inputted


                                final String serviceName = servicesList.get(viewHolder.getAdapterPosition()).getService_name();
                                final int serviceQty = servicesList.get(viewHolder.getAdapterPosition()).getService_qty();
                                double serviceDiscountedPrice = servicesList.get(viewHolder.getAdapterPosition()).getDiscounted_price();

                                SharedPreferences shared = v.getContext().getSharedPreferences("OwnerPref", MODE_PRIVATE);
                                final String username = (shared.getString("owner_username", ""));

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

                                                                                int currentServiceQty = customerCart.getServices().getService_qty();
                                                                                double currentServiceSubTotal = customerCart.getServices().getService_subtotal();
                                                                                double servicePrice = customerCart.getServices().getService_price();
                                                                                double serviceDiscountedPrice = customerCart.getServices().getDiscounted_price();

                                                                                //details from customer transaction
                                                                                int currentTotalQty = customerTransaction.getTotal_item_qty();
                                                                                double currentSubtotal = customerTransaction.getSubtotal();
                                                                                double currentAmountDue = customerTransaction.getAmount_due();
                                                                                double currentTotalDiscount = customerTransaction.getTotal_item_discount();
                                                                                String customerType = customerTransaction.getCustomer_type();

                                                                                double tempQty = 0.00;
                                                                                double newTotalQty = 0.00;

                                                                                if (customerType.equals("Regular Customer")){

                                                                                    if (currentServiceQty > newQty){
                                                                                        tempQty = currentServiceQty - newQty;
                                                                                        newTotalQty = currentTotalQty - tempQty;
                                                                                    }else {
                                                                                        tempQty = newQty - currentServiceQty;
                                                                                        newTotalQty = currentTotalQty + tempQty;
                                                                                    }

                                                                                    String newServiceSubtotalStr = String.format("%.2f", serviceDiscountedPrice * newQty);
                                                                                    double newServiceSubtotal = Double.parseDouble(newServiceSubtotalStr);

                                                                                    //get new total amount due
                                                                                    String tempAmountDueStr = String.format("%.2f", currentAmountDue - currentServiceSubTotal);
                                                                                    double tempAmountDue = Double.parseDouble(tempAmountDueStr);

                                                                                    String newAmountDueStr = String.format("%.2f", tempAmountDue + newServiceSubtotal);
                                                                                    double newAmountDue = Double.parseDouble(newAmountDueStr);

                                                                                    String newSubtotalStr = String.format("%.2f", newAmountDue / 1.12);
                                                                                    double newSubtotal = Double.parseDouble(newSubtotalStr);

                                                                                    String newVatStr = String.format("%.2f", newAmountDue - newSubtotal);
                                                                                    double newVat = Double.parseDouble(newVatStr);

                                                                                    //get the new total discounted
                                                                                    String getServTotalDiscountStr = String.format("%.2f", servicePrice - serviceDiscountedPrice);
                                                                                    double getServTotalDiscount = Double.parseDouble(getServTotalDiscountStr);

                                                                                    String newTotalDiscountStr = String.format("%.2f", getServTotalDiscount * newQty);
                                                                                    double newTotalDiscount = Double.parseDouble(newTotalDiscountStr);


                                                                                    ownerdbreference.child(ownerKey+"/business/customer_transaction/"+customerTransactionKey+"/customer_cart")
                                                                                            .child(customerCartKey+"/services/service_qty").setValue(newQty); //we update the quantity
                                                                                    ownerdbreference.child(ownerKey+"/business/customer_transaction/"+customerTransactionKey+"/customer_cart")
                                                                                            .child(customerCartKey+"/services/service_subtotal").setValue(newServiceSubtotal);

                                                                                    ownerdbreference.child(ownerKey+"/business/customer_transaction/"+customerTransactionKey+"/total_item_qty").setValue(newTotalQty);
                                                                                    ownerdbreference.child(ownerKey+"/business/customer_transaction/"+customerTransactionKey+"/amount_due").setValue(newAmountDue);
                                                                                    ownerdbreference.child(ownerKey+"/business/customer_transaction/"+customerTransactionKey+"/subtotal").setValue(newSubtotal);
                                                                                    ownerdbreference.child(ownerKey+"/business/customer_transaction/"+customerTransactionKey+"/total_item_discount").setValue(newTotalDiscount);
                                                                                    ownerdbreference.child(ownerKey+"/business/customer_transaction/"+customerTransactionKey+"/vat").setValue(newVat);
                                                                                    ownerdbreference.child(ownerKey+"/business/customer_transaction/"+customerTransactionKey+"/vat_exempt_sale").setValue(0.00);

                                                                                }else {
                                                                                    //for senior citizen
                                                                                }
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
                        });

                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builder.show();

                        return true;
                    }
                });

                menu.add("Delete item").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        final String serviceName = servicesList.get(viewHolder.getAdapterPosition()).getService_name();
                        final int serviceQty = servicesList.get(viewHolder.getAdapterPosition()).getService_qty();
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
                                                        final String customerType = customerTransaction.getCustomer_type();

                                                        ownerdbreference.child(ownerKey+"/business/customer_transaction/"+customerTransactionKey+"/customer_cart")
                                                                .orderByChild("services/service_name").equalTo(serviceName).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.exists()){
                                                                    for (DataSnapshot dataSnapshot3: dataSnapshot.getChildren()){
                                                                        String customerCartKey = dataSnapshot3.getKey();

                                                                        CustomerCart customerCart = dataSnapshot3.getValue(CustomerCart.class);
                                                                        int servQty = customerCart.getServices().getService_qty();

                                                                        double serviceSubTotal = customerCart.getServices().getService_subtotal();
                                                                        double servicePrice = customerCart.getServices().getService_price();
                                                                        double serviceDiscountedPrice = customerCart.getServices().getDiscounted_price();

                                                                        //details from customer transaction
                                                                        int totalQty = customerTransaction.getTotal_item_qty();
                                                                        double currentSubtotal = customerTransaction.getSubtotal();
                                                                        double currentAmountDue = customerTransaction.getAmount_due();
                                                                        double currentTotalDiscount = customerTransaction.getTotal_item_discount();

                                                                        if (customerType.equals("Regular Customer")){
                                                                            int newTotalQty = totalQty - servQty;

                                                                            String newTotalAmountDueStr = String.format("%.2f", currentAmountDue - serviceSubTotal);
                                                                            double newTotalAmountDue = Double.parseDouble(newTotalAmountDueStr);

                                                                            String newSubtotalStr = String.format("%.2f", newTotalAmountDue / 1.12);
                                                                            double newSubtotal = Double.parseDouble(newSubtotalStr);

                                                                            String newVatStr = String.format("%.2f", newTotalAmountDue - newSubtotal);
                                                                            double newVat = Double.parseDouble(newVatStr);

                                                                            //for discount
                                                                            String newServDiscValueStr = String.format("%.2f", (servicePrice - serviceDiscountedPrice) * servQty); //we get the total discount value of each service
                                                                            double newServDiscValue = Double.parseDouble(newServDiscValueStr);

                                                                            String getPositiveDiscountStr = String.format("%.2f", currentTotalDiscount * -1); //we convert the negative discount amount to positive so that we can deduct it
                                                                            double getPositiveDiscount = Double.parseDouble(getPositiveDiscountStr);

                                                                            String newTotalDiscountStr = String.format("%.2f", getPositiveDiscount - newServDiscValue);
                                                                            double newTotalDiscount = Double.parseDouble(newTotalDiscountStr);

                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/"+customerTransactionKey+"/customer_cart")
                                                                                .child(customerCartKey+"/services").setValue(null); //deleting the service node from the customer cart

                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/total_item_qty").setValue(newTotalQty);
                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/subtotal").setValue(newSubtotal);
                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/vat").setValue(newVat);
                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/amount_due").setValue(newTotalAmountDue);
                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/vet_exempt_sale").setValue(0.00);
                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/total_item_discount").setValue(newTotalDiscount);
                                                                        }else {
                                                                            //for senior citizen
                                                                            int newTotalQty = totalQty - servQty;

                                                                            String newSubtotalVatStr = String.format("%.2f", currentAmountDue - serviceSubTotal);
                                                                            double newSubtotalVat = Double.parseDouble(newSubtotalVatStr);

                                                                            String newVatExemptStr = String.format("%.2f", newSubtotalVat / 1.12);
                                                                            double newVatExempt = Double.parseDouble(newVatExemptStr);

                                                                            String newVatStr = String.format("%.2f", newSubtotalVat - newVatExempt);
                                                                            double newVat = Double.parseDouble(newVatStr);

                                                                            String newSeniorDiscountStr = String.format("%.2f", newVatExempt * .20);
                                                                            double newSeniorDiscount = Double.parseDouble(newSeniorDiscountStr);

                                                                            String newAmountDueStr = String.format("%.2f", newVatExempt + newSeniorDiscount);
                                                                            double newAmountDue = Double.parseDouble(newAmountDueStr);

                                                                            //for discount
                                                                            String newServDiscValueStr = String.format("%.2f", (servicePrice - serviceDiscountedPrice) * servQty); //we get the total discount value of each service
                                                                            double newServDiscValue = Double.parseDouble(newServDiscValueStr);

                                                                            String getPositiveDiscountStr = String.format("%.2f", currentTotalDiscount * -1); //we convert the negative discount amount to positive so that we can deduct it
                                                                            double getPositiveDiscount = Double.parseDouble(getPositiveDiscountStr);

                                                                            String newTotalDiscountStr = String.format("%.2f", getPositiveDiscount - newServDiscValue);
                                                                            double newTotalDiscount = Double.parseDouble(newTotalDiscountStr);

                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/"+customerTransactionKey+"/customer_cart")
                                                                                    .child(customerCartKey+"/services").setValue(null); //delete the product node from the customer cart

                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/amount_due").setValue(newAmountDue);
                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/senior_discount").setValue(newSeniorDiscount);
                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/subtotal").setValue(newSubtotalVat);
                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/total_item_discount").setValue(newTotalDiscount);
                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/total_item_qty").setValue(newTotalQty);
                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/vat").setValue(newVat);
                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/vat_exempt_sale").setValue(newVatExempt);
                                                                        }
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
//        if (!services.equals(null)) {
//            viewHolder.servicename.setText(services.getService_name());
//            viewHolder.servicename.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
//            viewHolder.servicename.setTextColor(Color.BLACK);
//            viewHolder.servicesubtotal.setText(String.valueOf(services.getService_subtotal()));
//            viewHolder.servicesubtotal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
//            viewHolder.servicesubtotal.setTextColor(Color.BLACK);
//            viewHolder.serviceqtyprice.setText(services.getService_qty() + " X "+ services.getDiscounted_price());
//
//        }


        if (!services.equals(null)){
            String prodName = services.getService_name();
            int prodQty = services.getService_qty();
            double discountedPrice = services.getDiscounted_price();
            double originalPrice = services.getService_price();
            double subtotal = services.getService_subtotal();

            String subtotalDiscountStr = String.format("%.2f", originalPrice - discountedPrice);
            double subtotalDiscount = Double.parseDouble(subtotalDiscountStr);

            viewHolder.servQtyName.setText(prodQty+"   "+services.getService_name());
            viewHolder.servQtyName.setTextColor(Color.BLACK);
            viewHolder.servQtyName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            viewHolder.servSubtotal.setText(""+subtotal);
            viewHolder.servSubtotal.setTextColor(Color.BLACK);
            viewHolder.servSubtotal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            viewHolder.servDiscountedPrice.setText("@ "+discountedPrice);


            viewHolder.servOriginalPrice.setText(""+services.getService_price());
            viewHolder.servLessDisc.setText(""+subtotalDiscount);

            if (viewHolder.servOriginalPrice.getText().toString().equals(viewHolder.servDiscountedPrice.getText().toString())){
                viewHolder.servOriginalPrice.setVisibility(View.INVISIBLE);
                viewHolder.servLessDisc.setVisibility(View.INVISIBLE);
            }

            if (!viewHolder.servOriginalPrice.getText().toString().equals(viewHolder.servDiscountedPrice.getText().toString())){
                viewHolder.servOriginalPrice.setPaintFlags(viewHolder.servOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                viewHolder.servOriginalPrice.setText(""+originalPrice);
                viewHolder.servLessDisc.setText("("+subtotalDiscount+")");
            }

        }

    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView servQtyName, servSubtotal, servDiscountedPrice, servOriginalPrice, servLessDisc;
        LinearLayout layout;
        ImageView imgdiscounted;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            servQtyName = (TextView) itemView.findViewById(R.id.qtyItemName);
            servSubtotal = (TextView) itemView.findViewById(R.id.itemsubtotal);
            servDiscountedPrice = (TextView) itemView.findViewById(R.id.discountedPrice);
            servOriginalPrice = (TextView) itemView.findViewById(R.id.originalPrice);
            servLessDisc = (TextView) itemView.findViewById(R.id.lessDiscount);
            layout = (LinearLayout) itemView.findViewById(R.id.prodLayout);
        }
    }
}
