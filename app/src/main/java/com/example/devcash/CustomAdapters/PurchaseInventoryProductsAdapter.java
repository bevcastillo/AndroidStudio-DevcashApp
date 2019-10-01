package com.example.devcash.CustomAdapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.Object.CustomerCart;
import com.example.devcash.Object.CustomerTransaction;
import com.example.devcash.Object.Item;
import com.example.devcash.Object.Product;
import com.example.devcash.Object.Productlistdata;
import com.example.devcash.Object.PurchaseTransaction;
import com.example.devcash.Object.PurchasedItem;
import com.example.devcash.Object.Services;
import com.example.devcash.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class PurchaseInventoryProductsAdapter extends RecyclerView.Adapter<PurchaseInventoryProductsAdapter.ViewHolder> {

    private DatabaseReference ownerdbreference;
    private FirebaseDatabase firebaseDatabase;

    Context context;
    List<Productlistdata> plist;
    Map<String, PurchasedItem> purchasedItemMap;
    Map<String, Object> cartMap;

    private static int itemcount = 0;
    int customerId;

//    public PurchaseInventoryProductsAdapter(List<Productlistdata> plist) {
//        this.plist = plist;
//    }


    public PurchaseInventoryProductsAdapter(Context context, List<Productlistdata> plist) {
        this.context = context;
        this.plist = plist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
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
                Toast.makeText(v.getContext(), "Adding to Cart...", Toast.LENGTH_SHORT).show();
                itemcount++;

                final String TransId = ownerdbreference.push().getKey();
                final String CustomerCartId = ownerdbreference.push().getKey();

                final String prodname = plist.get(viewHolder.getAdapterPosition()).getProd_name();
                final String prodexpdate = plist.get(viewHolder.getAdapterPosition()).getProd_expdate();
                final double prodprice = plist.get(viewHolder.getAdapterPosition()).getProd_price();
                final double discountedprice = plist.get(viewHolder.getAdapterPosition()).getDiscounted_price();
                final int prodStock = plist.get(viewHolder.getAdapterPosition()).getProd_stock();

                final String preference = prodname+prodexpdate;

                plist.get(viewHolder.getAdapterPosition()).setProdclick(plist.get(viewHolder.getAdapterPosition()).getProdclick() + 1);


                final int prodqty = plist.get(viewHolder.getAdapterPosition()).getProdclick();

                final Product product = new Product();
                product.setProd_name(prodname);
                product.setProd_expdate(prodexpdate);
                product.setProd_price(prodprice);
                product.setProd_qty(prodqty);
                product.setProd_stock(prodStock);
                product.setProd_reference(preference);
                product.setDiscounted_price(discountedprice);
                product.setProd_subtotal(product.getDiscounted_price() * product.getProd_qty());


                final CustomerCart customerCart = new CustomerCart();
                customerCart.setProduct(product);

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

                SharedPreferences customerTypePref = v.getContext().getSharedPreferences("CustomerTypePref", MODE_PRIVATE);
                final String customerType = (customerTypePref.getString("customer_type","Regular Customer"));


                SharedPreferences transactionDateTimePref = v.getContext().getSharedPreferences("TransactionDateTimePref", MODE_PRIVATE);
                final String transDateTime = (transactionDateTimePref.getString("trans_dateTime", ""));


                // query to customer transaction.
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


                                                final String finalCustomertransactionkey = customertransactionkey;
                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/customer_cart")
                                                        .orderByChild("product/prod_reference").equalTo(preference).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()){
                                                            for (DataSnapshot dataSnapshot3: dataSnapshot.getChildren()){
                                                                String cartkey = dataSnapshot3.getKey();

                                                                CustomerCart customerCart1 = dataSnapshot3.getValue(CustomerCart.class);
                                                                String prodreference = customerCart1.getProduct().getProd_reference();

                                                                //if selected item is already in the customer cart
                                                                if (prodreference.equals(preference)){

                                                                    if (customerType.equals("Senior Citizen")){

                                                                        int totalQty = currentTotalQty + 1;

                                                                        String productSubtotalStr = String.format("%.2f", discountedprice * prodqty);
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

                                                                        String partialItemDiscSubtotalStr = String.format("%.2f", discountedprice * prodqty);
                                                                        double partialItemDiscSubtotal = Double.parseDouble(partialItemDiscSubtotalStr);

                                                                        String partialItemOrigPriceSubtotalStr = String.format("%.2f", prodprice * prodqty);
                                                                        double partialItemOrigPriceSubtotal = Double.parseDouble(partialItemOrigPriceSubtotalStr);

                                                                        String totalDiscountStr = String.format("%.2f", partialItemDiscSubtotal - partialItemOrigPriceSubtotal);
                                                                        double totalDiscount = Double.parseDouble(totalDiscountStr);


                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/customer_cart/").child(cartkey+"/product/prod_qty").setValue(prodqty);
                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/customer_cart/").child(cartkey+"/product/prod_subtotal").setValue(productSubtotal);

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

                                                                        String productSubtotalStr = String.format("%.2f", discountedprice * prodqty);
                                                                        double productSubtotal = Double.parseDouble(productSubtotalStr);

                                                                        String totalDueStr = String.format("%.2f", currentAmountDue + discountedprice);
                                                                        double totalDue = Double.parseDouble(totalDueStr);

                                                                        int totalQty = currentTotalQty + 1;

                                                                        String subtotalStr = String.format("%.2f", totalDue / 1.12);
                                                                        double subtotal = Double.parseDouble(subtotalStr);

                                                                        String vatStr = String.format("%.2f", totalDue - subtotal);
                                                                        double vat = Double.parseDouble(vatStr);

                                                                        String partialDiscount_str = String.format("%.2f", prodprice * prodqty);
    //
    //
                                                                        double partialDiscount = Double.parseDouble(partialDiscount_str);
                                                                        double totalDiscount = productSubtotal - partialDiscount;
//
                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/customer_cart/").child(cartkey+"/product/prod_qty").setValue(prodqty);
                                                                        ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/customer_cart/").child(cartkey+"/product/prod_subtotal").setValue(productSubtotal);
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

                                                                String partialSubtotalStr = String.format("%.2f", discountedprice * prodqty);
                                                                double partialSubtotal = Double.parseDouble(partialSubtotalStr);

                                                                String partialDiscountStr = String.format("%.2f", prodprice * prodqty);
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
                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/total_item_qty").setValue(totalQty);
                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/vat").setValue(vat);
                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/subtotal").setValue(subtotal);
                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/amount_due").setValue(totalDue);
                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/total_item_discount").setValue(totalDiscount);
                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/vat_exempt_sale").setValue(0.00);
                                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/customer_cart").updateChildren(cartMap);
                                                                cartMap.clear();
                                                            }
//                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+ finalCustomertransactionkey +"/customer_cart").updateChildren(cartMap);
//                                                            cartMap.clear();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        }else {
                                            // this is the logic for creating new data.

                                            if (customerType.equals("Senior Citizen")){

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


                                                customerTransaction.setTransaction_datetime(transDateTime);
                                                customerTransaction.setCustomer_type(customerType);
                                                customerTransaction.setTotal_item_qty(prodqty);
                                                customerTransaction.setSubtotal(subtotalVat); //with vat
                                                customerTransaction.setTotal_item_discount(totalDiscount); //total discount (if the item is discounted)
                                                customerTransaction.setVat(lessVat); //vat off
                                                customerTransaction.setVat_exempt_sale(vatExemptSale); //this is the total price minus the 12% vat
                                                customerTransaction.setSenior_discount(seniorDiscount); //this is the value of the 20% senior citizen discount
                                                customerTransaction.setAmount_due(totalDue); //amount that will be billed to the senior citizen
                                                ownerdbreference.child(acctkey+"/business/customer_transaction").push().setValue(customerTransaction); //creating a new customer transaction node
                                                Toast.makeText(v.getContext(), "Item has been added to cart.", Toast.LENGTH_SHORT).show();

                                            } else {
                                                //if the customer type is Regular Customer

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

                                                customerTransaction.setTransaction_datetime(transDateTime);
                                                customerTransaction.setCustomer_type(customerType);
                                                customerTransaction.setSubtotal(subtotal);
                                                customerTransaction.setTotal_item_qty(prodqty);
                                                customerTransaction.setVat(vat);
                                                customerTransaction.setVat_exempt_sale(0.00);
                                                customerTransaction.setAmount_due(totalAmountDue);
                                                customerTransaction.setTotal_item_discount(totalDiscount);

                                                ownerdbreference.child(acctkey+"/business/customer_transaction").push().setValue(customerTransaction); //creating a new customer transaction node
                                                Toast.makeText(v.getContext(), "Item has been added to cart.", Toast.LENGTH_SHORT).show();
                                            } //// end

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
        Productlistdata data = plist.get(i);
        viewHolder.name.setText(data.getProd_name());
        viewHolder.price.setText(String.valueOf(data.getProd_price()));
        viewHolder.discountedprice.setText(String.valueOf(data.getDiscounted_price()));
        viewHolder.expiration.setText(data.getProd_expdate());

        double originalprice = Double.parseDouble(viewHolder.price.getText().toString());
        double discountedprice = Double.parseDouble(viewHolder.discountedprice.getText().toString());

        String lessAmountStr = String.format("%.2f", originalprice - discountedprice);
        double lessAmount = Double.parseDouble(lessAmountStr);

        viewHolder.amountOff.setText(String.valueOf(lessAmount)+" off");

        if (viewHolder.price.getText().toString().equals(viewHolder.discountedprice.getText().toString())){
            viewHolder.price.setVisibility(View.INVISIBLE);
            viewHolder.amountOff.setVisibility(View.INVISIBLE);
        }

        if (!viewHolder.price.getText().toString().equals(viewHolder.discountedprice.getText().toString())){
            viewHolder.price.setPaintFlags(viewHolder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.discountedprice.setText(String.valueOf(data.getDiscounted_price()));
            viewHolder.amountOff.setText(String.valueOf(lessAmount)+" off");
        }

        if (viewHolder.expiration.getText().toString().equals("No Expiration")){
            viewHolder.expiration.setVisibility(View.INVISIBLE);

        }

        Picasso.with(context).load(data.getProd_image()).into(viewHolder.image);

//        viewHolder.price.setPaintFlags(viewHolder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


//        viewHolder.expiration.setVisibility(View.VISIBLE);
//        viewHolder.expiration.setText(data.getProd_expdate());
//        viewHolder.discountedprice.setText(String.valueOf(data.getDiscounted_price()));

//        if (viewHolder.discountedprice.getText().toString().equals())
    }

    @Override
    public int getItemCount() {
        return plist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, price, expiration, discountedprice, amountOff;
        ImageView image;
        RelativeLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.prodlist_name);
            price = (TextView) itemView.findViewById(R.id.prodlist_price);
            expiration = (TextView) itemView.findViewById(R.id.prodlist_expdate);
            discountedprice = (TextView)itemView.findViewById(R.id.prod_discountedprice);
            amountOff = (TextView) itemView.findViewById(R.id.lessOff);
            layout = (RelativeLayout) itemView.findViewById(R.id.cardLayout);
            image = (ImageView) itemView.findViewById(R.id.prodlist_image);

        }
    }
}
