package com.example.devcash.CustomAdapters;

import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class PurchaseInventoryProductsAdapter extends RecyclerView.Adapter<PurchaseInventoryProductsAdapter.ViewHolder> {

    private DatabaseReference ownerdbreference;
    private FirebaseDatabase firebaseDatabase;


    List<Productlistdata> plist;
    Map<String, PurchasedItem> purchasedItemMap;
    Map<String, Object> cartMap;

    private static int itemcount = 0;
    int customerId;

    public PurchaseInventoryProductsAdapter(List<Productlistdata> plist) {
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

        viewHolder.price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                itemcount++;

                final String TransId = ownerdbreference.push().getKey();
                final String CustomerCartId = ownerdbreference.push().getKey();

                final String prodname = plist.get(viewHolder.getAdapterPosition()).getProd_name();
                final String prodexpdate = plist.get(viewHolder.getAdapterPosition()).getProd_expdate();
                final double prodprice = plist.get(viewHolder.getAdapterPosition()).getProd_price();
                final String preference = prodname+prodexpdate;

                plist.get(viewHolder.getAdapterPosition()).setProdclick(plist.get(viewHolder.getAdapterPosition()).getProdclick() + 1);


                final int prodqty = plist.get(viewHolder.getAdapterPosition()).getProdclick();

                final Product product = new Product();
                product.setProd_name(prodname);
                product.setProd_expdate(prodexpdate);
                product.setProd_price(prodprice);
                product.setProd_qty(prodqty);
                product.setProd_reference(preference);
                product.setProd_subtotal(product.getProd_price() * product.getProd_qty());

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
                                                        .orderByChild("product/prod_reference").equalTo(preference).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()){
                                                            for (DataSnapshot dataSnapshot3: dataSnapshot.getChildren()){
                                                                String cartkey = dataSnapshot3.getKey();

                                                                CustomerCart customerCart1 = dataSnapshot3.getValue(CustomerCart.class);
                                                                String prodreference = customerCart1.getProduct().getProd_reference();

                                                                if (prodreference.equals(preference)){
                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/customer_cart/").child(cartkey+"/product/prod_qty").setValue(prodqty);
                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/customer_cart/").child(cartkey+"/product/prod_subtotal").setValue(prodqty*prodprice);
                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/subtotal").setValue(currentSubtotal + prodprice);
                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/total_qty").setValue((currentTotalQty + prodqty) - 1);
                                                                    cartMap.clear();
                                                                }else {
                                                                    Toast.makeText(v.getContext(), customerTransaction1.getSubtotal()+" is the current subtotal", Toast.LENGTH_SHORT).show();
                                                                    ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/customer_cart").push().setValue(customerCart);
                                                                }
                                                            }
                                                        }else {
                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/subtotal").setValue(currentSubtotal + prodprice);
                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/total_qty").setValue((currentTotalQty + prodqty));
                                                            ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/customer_cart").updateChildren(cartMap);
                                                            cartMap.clear();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

//                                                ownerdbreference.child(acctkey+"/business/customer_transaction/"+customertransactionkey+"/customer_cart")
//                                                        .orderByChild("services/service_name").equalTo(servname)
                                            }
                                        }else {
                                            // this is the logic for creating new data.
                                            customerTransaction.setSubtotal(product.getProd_price() * prodqty);
                                            customerTransaction.setTotal_qty(prodqty);
                                            ownerdbreference.child(acctkey+"/business/customer_transaction").push().setValue(customerTransaction); //creating a new customer transaction node
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

        viewHolder.expiration.setVisibility(View.VISIBLE);
        viewHolder.expiration.setText(data.getProd_expdate());
    }

    @Override
    public int getItemCount() {
        return plist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, price, expiration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.prodlist_name);
            price = (TextView) itemView.findViewById(R.id.prodlist_price);
            expiration = (TextView) itemView.findViewById(R.id.prodlist_expdate);

        }
    }
}
