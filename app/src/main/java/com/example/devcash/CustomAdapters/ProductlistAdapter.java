package com.example.devcash.CustomAdapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.Object.CustomerCart;
import com.example.devcash.Object.CustomerTransaction;
import com.example.devcash.Object.Product;
import com.example.devcash.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ProductlistAdapter extends RecyclerView.Adapter<ProductlistAdapter.ViewHolder> {

    List<Product> productList;

    private DatabaseReference ownerdbreference;
    private FirebaseDatabase firebaseDatabase;

    private static int itemcount = 0;
    int customerId;

    public ProductlistAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
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


        viewHolder.prodname.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

            @Override
            public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenu.ContextMenuInfo menuInfo) {

                String itemName = productList.get(viewHolder.getAdapterPosition()).getProd_name();
                double itemQty = productList.get(viewHolder.getAdapterPosition()).getProd_qty();

                menu.add("Edit Quantity").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                        dialog.setTitle("Update Item Quantity");
                        final EditText qty = new EditText(v.getContext());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        qty.setLayoutParams(lp);
                        dialog.setView(qty);
                        dialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        return true;
                    }
                });

                menu.add("Delete Item").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        final String productName = productList.get(viewHolder.getAdapterPosition()).getProd_name();
                        final int productQty = productList.get(viewHolder.getAdapterPosition()).getProd_qty();
                        double productDiscountedPrice = productList.get(viewHolder.getAdapterPosition()).getDiscounted_price();
                        final String expdate = productList.get(viewHolder.getAdapterPosition()).getProd_expdate();
                        final String productReference = productName+""+expdate;


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
                                                                .orderByChild("product/prod_reference").equalTo(productReference).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.exists()){
                                                                    for (DataSnapshot dataSnapshot3: dataSnapshot.getChildren()){
                                                                        String customerCartKey = dataSnapshot3.getKey();
                                                                        CustomerCart customerCart = dataSnapshot3.getValue(CustomerCart.class);

                                                                        int productQty = customerCart.getProduct().getProd_qty();
                                                                        double productSubTotal = customerCart.getProduct().getProd_subtotal();
                                                                        double productPrice = customerCart.getProduct().getProd_price();
                                                                        double serviceDiscountedPrice = customerCart.getProduct().getDiscounted_price();

                                                                        //details from customer transaction
                                                                        int totalQty = customerTransaction.getTotal_item_qty();
                                                                        double currentSubtotal = customerTransaction.getSubtotal();
                                                                        double currentTotalPrice = customerTransaction.getAmount_due();
                                                                        double currentTotalDiscount = customerTransaction.getTotal_item_discount();

                                                                        ownerdbreference.child(ownerKey+"/business/customer_transaction/"+customerTransactionKey+"/customer_cart")
                                                                                .child(customerCartKey+"/product").setValue(null); //delete the product node from the customer cart

                                                                        //computation for the customer transaction details after deleting the product
                                                                        int newTotalQty =  totalQty - productQty;

                                                                        String newServDiscValueStr = String.format("%.2f", (productPrice - serviceDiscountedPrice) * productQty); //we get the total discount value of each service
                                                                        double newServDiscValue = Double.parseDouble(newServDiscValueStr);

                                                                        String getPositiveDiscountStr = String.format("%.2f", currentTotalDiscount * -1); //we convert the negative discount amount to positive so that we can deduct it
                                                                        double getPositiveDiscount = Double.parseDouble(getPositiveDiscountStr);

                                                                        String newTotalDiscountStr = String.format("%.2f", getPositiveDiscount - newServDiscValue);
                                                                        double newTotalDiscount = Double.parseDouble(newTotalDiscountStr);

                                                                        String newTotalPriceStr = String.format("%.2f", currentTotalPrice - productSubTotal);
                                                                        double newTotalPrice = Double.parseDouble(newTotalPriceStr);

                                                                        String newVatStr = String.format("%.2f", newTotalPrice * .12);
                                                                        double newVat = Double.parseDouble(newVatStr);

                                                                        String newSubtotalStr = String.format("%.2f", newTotalPrice - newVat);
                                                                        double newSubtotal = Double.parseDouble(newSubtotalStr);

                                                                        ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/total_qty").setValue(newTotalQty);
                                                                        ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/total_discount").setValue(newTotalDiscount);
                                                                        ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/subtotal").setValue(newSubtotal);
                                                                        ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/vat").setValue(newVat);
                                                                        ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/total_price").setValue(newTotalPrice);


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


                        return true;
                    }
                });
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Product product = productList.get(i);

        if (!product.equals(null)) {
            viewHolder.prodname.setText(product.getProd_name());
            viewHolder.prodname.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            viewHolder.prodname.setTextColor(Color.BLACK);
            double discPrice;
//            if ( product.getQrCode() != null )  {
//                discPrice = product.getQrCode().getQr_disc_price();
//            } else {
//                discPrice = product.getDiscounted_price();
//            }
            viewHolder.prodqtyprice.setText(product.getProd_qty() +" X "+ product.getDiscounted_price());
            viewHolder.prodsubtotal.setText(String.valueOf(product.getProd_subtotal()));
            viewHolder.prodsubtotal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            viewHolder.prodsubtotal.setTextColor(Color.BLACK);

        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView prodname, prodqtyprice, prodsubtotal;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            prodname = (TextView) itemView.findViewById(R.id.itemname);
            prodqtyprice = (TextView) itemView.findViewById(R.id.itemqtyprice);
            prodsubtotal = (TextView) itemView.findViewById(R.id.itemsubtotal);

        }
    }
}
