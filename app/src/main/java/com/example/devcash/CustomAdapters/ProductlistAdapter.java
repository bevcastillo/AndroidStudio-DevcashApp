package com.example.devcash.CustomAdapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
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
import com.itextpdf.text.pdf.PRStream;

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

        viewHolder.layout.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

            @Override
            public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenu.ContextMenuInfo menuInfo) {

                String itemName = productList.get(viewHolder.getAdapterPosition()).getProd_name();
                final int itemQty = productList.get(viewHolder.getAdapterPosition()).getProd_qty();

                menu.add("Edit Quantity").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                        dialog.setTitle("Update Item Quantity");
                        final EditText qty = new EditText(v.getContext());
                        qty.setText(String.valueOf(itemQty));
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        qty.setLayoutParams(lp);
                        dialog.setView(qty);
                        dialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final int newQty = Integer.parseInt(qty.getText().toString()); //this is the quantity that the user has inputted


                                final String productName = productList.get(viewHolder.getAdapterPosition()).getProd_name();
                                final int productQty = productList.get(viewHolder.getAdapterPosition()).getProd_qty();
                                final double productDicsountedPrice = productList.get(viewHolder.getAdapterPosition()).getDiscounted_price();
                                String productExpiration = productList.get(viewHolder.getAdapterPosition()).getProd_expdate();
                                final String productReference = productName+""+productExpiration;

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

                                                                                int currentProductQty = customerCart.getProduct().getProd_qty();
                                                                                double currentProductSubTotal = customerCart.getProduct().getProd_subtotal();
                                                                                double productPrice = customerCart.getProduct().getProd_price();
                                                                                double productDiscountedPrice = customerCart.getProduct().getDiscounted_price();

                                                                                //details from customer transaction
                                                                                int currentTotalQty = customerTransaction.getTotal_item_qty();
                                                                                double currentSubtotal = customerTransaction.getSubtotal();
                                                                                double currentAmountDue = customerTransaction.getAmount_due();
                                                                                double currentTotalDiscount = customerTransaction.getTotal_item_discount();
                                                                                String customerType = customerTransaction.getCustomer_type();

                                                                                double tempQty = 0.00;
                                                                                double newTotalQty = 0.00;

                                                                                if (customerType.equals("Regular Customer")){

                                                                                    if (currentProductQty > newQty){
                                                                                        tempQty = currentProductQty - newQty;
                                                                                        newTotalQty = currentTotalQty - tempQty;
                                                                                    }else {
                                                                                        tempQty = newQty - currentProductQty;
                                                                                        newTotalQty = currentTotalQty + tempQty;
                                                                                    }

                                                                                    String newProductSubtotalStr = String.format("%.2f", productDicsountedPrice * newQty);
                                                                                    double newProductSubtotal = Double.parseDouble(newProductSubtotalStr);

                                                                                    //get new total amount due
                                                                                    String tempAmountDueStr = String.format("%.2f", currentAmountDue - currentProductSubTotal);
                                                                                    double tempAmountDue = Double.parseDouble(tempAmountDueStr);

                                                                                    String newAmountDueStr = String.format("%.2f", tempAmountDue + newProductSubtotal);
                                                                                    double newAmountDue = Double.parseDouble(newAmountDueStr);

                                                                                    String newSubtotalStr = String.format("%.2f", newAmountDue / 1.12);
                                                                                    double newSubtotal = Double.parseDouble(newSubtotalStr);

                                                                                    String newVatStr = String.format("%.2f", newAmountDue - newSubtotal);
                                                                                    double newVat = Double.parseDouble(newVatStr);

                                                                                    //get the new total discounted
                                                                                    String getProdTotalDiscountStr = String.format("%.2f", productPrice - productDicsountedPrice);
                                                                                    double getProdTotalDiscount = Double.parseDouble(getProdTotalDiscountStr);

                                                                                    String newTotalDiscountStr = String.format("%.2f", getProdTotalDiscount * newQty);
                                                                                    double newTotalDiscount = Double.parseDouble(newTotalDiscountStr);


                                                                                    ownerdbreference.child(ownerKey+"/business/customer_transaction/"+customerTransactionKey+"/customer_cart")
                                                                                            .child(customerCartKey+"/product/prod_qty").setValue(newQty); //we update the quantity
                                                                                    ownerdbreference.child(ownerKey+"/business/customer_transaction/"+customerTransactionKey+"/customer_cart")
                                                                                            .child(customerCartKey+"/product/prod_subtotal").setValue(newProductSubtotal);

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
                        final double productDiscountedPrice = productList.get(viewHolder.getAdapterPosition()).getDiscounted_price();
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
                                                        final String customerType = customerTransaction.getCustomer_type();

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
                                                                        double prodcutDiscountedPrice = customerCart.getProduct().getDiscounted_price();

                                                                        //details from customer transaction
                                                                        int totalQty = customerTransaction.getTotal_item_qty();
                                                                        double currentSubtotal = customerTransaction.getSubtotal();
                                                                        double currentAmountDue = customerTransaction.getAmount_due();
                                                                        double currentTotalDiscount = customerTransaction.getTotal_item_discount();

                                                                        if (customerType.equals("Regular Customer")){

                                                                            int newTotalQty = totalQty - productQty;

                                                                            String newTotalAmountDueStr = String.format("%.2f", currentAmountDue - productSubTotal);
                                                                            double newTotalAmountDue = Double.parseDouble(newTotalAmountDueStr);

                                                                            String newSubtotalStr = String.format("%.2f", newTotalAmountDue / 1.12);
                                                                            double newSubtotal = Double.parseDouble(newSubtotalStr);

                                                                            String newVatStr = String.format("%.2f", newTotalAmountDue - newSubtotal);
                                                                            double newVat = Double.parseDouble(newVatStr);

                                                                            //for discount
                                                                            String newProdDiscValueStr = String.format("%.2f", (productPrice - productDiscountedPrice) * productQty); //we get the total discount value of each service
                                                                            double newProdDiscValue = Double.parseDouble(newProdDiscValueStr);

                                                                            String getPositiveDiscountStr = String.format("%.2f", currentTotalDiscount * -1); //we convert the negative discount amount to positive so that we can deduct it
                                                                            double getPositiveDiscount = Double.parseDouble(getPositiveDiscountStr);

                                                                            String newTotalDiscountStr = String.format("%.2f", getPositiveDiscount - newProdDiscValue);
                                                                            double newTotalDiscount = Double.parseDouble(newTotalDiscountStr);

                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/"+customerTransactionKey+"/customer_cart")
                                                                                    .child(customerCartKey+"/product").setValue(null); //delete the product node from the customer cart

                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/total_item_qty").setValue(newTotalQty);
                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/subtotal").setValue(newSubtotal);
                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/vat").setValue(newVat);
                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/amount_due").setValue(newTotalAmountDue);
                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/vet_exempt_sale").setValue(0.00);
                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/total_item_discount").setValue(newTotalDiscount);


                                                                        }else {
                                                                            //if senior citizen

                                                                            int newTotalQty = totalQty - productQty;

                                                                            String newSubtotalVatStr = String.format("%.2f", currentAmountDue - productSubTotal);
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
                                                                            String newProdDiscValueStr = String.format("%.2f", (productPrice - productDiscountedPrice) * productQty); //we get the total discount value of each service
                                                                            double newProdDiscValue = Double.parseDouble(newProdDiscValueStr);

                                                                            String getPositiveDiscountStr = String.format("%.2f", currentTotalDiscount * -1); //we convert the negative discount amount to positive so that we can deduct it
                                                                            double getPositiveDiscount = Double.parseDouble(getPositiveDiscountStr);

                                                                            String newTotalDiscountStr = String.format("%.2f", getPositiveDiscount - newProdDiscValue);
                                                                            double newTotalDiscount = Double.parseDouble(newTotalDiscountStr);

                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/"+customerTransactionKey+"/customer_cart")
                                                                                    .child(customerCartKey+"/product").setValue(null); //delete the product node from the customer cart

                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/amount_due").setValue(newAmountDue);
                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/senior_discount").setValue(newSeniorDiscount);
                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/subtotal").setValue(newSubtotalVat);
                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/total_item_discount").setValue(newTotalDiscount);
                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/total_item_qty").setValue(newTotalQty);
                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/vat").setValue(newVat);
                                                                            ownerdbreference.child(ownerKey+"/business/customer_transaction/").child(customerTransactionKey+"/vat_exempt_sale").setValue(newVatExempt);
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

//        if (!product.equals(null)) {
//            viewHolder.prodname.setText(product.getProd_name());
//            viewHolder.prodname.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
//            viewHolder.prodname.setTextColor(Color.BLACK);
//            double discPrice;
////            if ( product.getQrCode() != null )  {
////                discPrice = product.getQrCode().getQr_disc_price();
////            } else {
////                discPrice = product.getDiscounted_price();
////            }
//            viewHolder.prodqtyprice.setText(product.getProd_qty() +" X "+ product.getDiscounted_price());
//            viewHolder.prodsubtotal.setText(String.valueOf(product.getProd_subtotal()));
//            viewHolder.prodsubtotal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
//            viewHolder.prodsubtotal.setTextColor(Color.BLACK);
//
//        }

        if (!product.equals(null)){
            String prodName = product.getProd_name();
            int prodQty = product.getProd_qty();
            double discountedPrice = product.getDiscounted_price();
            double originalPrice = product.getProd_price();
            double subtotal = product.getProd_subtotal();

            String subtotalDiscountStr = String.format("%.2f", originalPrice - discountedPrice);
            double subtotalDiscount = Double.parseDouble(subtotalDiscountStr);

            viewHolder.prodQtyName.setText(prodQty+"   "+prodName);
            viewHolder.prodQtyName.setTextColor(Color.BLACK);
            viewHolder.prodQtyName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            viewHolder.prodSubtotal.setText(""+subtotal);
            viewHolder.prodSubtotal.setTextColor(Color.BLACK);
            viewHolder.prodSubtotal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            viewHolder.prodDiscountedPrice.setText("@ "+discountedPrice);


            viewHolder.prodOriginalPrice.setText(""+product.getProd_price());
            viewHolder.prodLessDisc.setText(""+subtotalDiscount);

            if (viewHolder.prodOriginalPrice.getText().toString().equals(viewHolder.prodDiscountedPrice.getText().toString())){
                viewHolder.prodOriginalPrice.setVisibility(View.INVISIBLE);
                viewHolder.prodLessDisc.setVisibility(View.INVISIBLE);
            }

            if (!viewHolder.prodOriginalPrice.getText().toString().equals(viewHolder.prodDiscountedPrice.getText().toString())){
                viewHolder.prodOriginalPrice.setPaintFlags(viewHolder.prodOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                viewHolder.prodOriginalPrice.setText(""+originalPrice);
                viewHolder.prodLessDisc.setText("("+subtotalDiscount+")");
            }

        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView prodQtyName, prodSubtotal, prodDiscountedPrice, prodOriginalPrice, prodLessDisc;
        LinearLayout layout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            prodQtyName = (TextView) itemView.findViewById(R.id.qtyItemName);
            prodSubtotal = (TextView) itemView.findViewById(R.id.itemsubtotal);
            prodDiscountedPrice = (TextView) itemView.findViewById(R.id.discountedPrice);
            prodOriginalPrice = (TextView) itemView.findViewById(R.id.originalPrice);
            prodLessDisc = (TextView) itemView.findViewById(R.id.lessDiscount);
            layout = (LinearLayout) itemView.findViewById(R.id.prodLayout);

        }
    }
}
