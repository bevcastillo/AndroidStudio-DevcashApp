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

import com.example.devcash.Object.Product;
import com.example.devcash.Object.Productlistdata;
import com.example.devcash.Object.PurchaseTransaction;
import com.example.devcash.Object.PurchasedItem;
import com.example.devcash.Object.Services;
import com.example.devcash.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class PurchaseInventoryProductsAdapter extends RecyclerView.Adapter<PurchaseInventoryProductsAdapter.ViewHolder> {

    private DatabaseReference ownerdbreference;
    private FirebaseDatabase firebaseDatabase;


    List<Productlistdata> plist;

    private static int itemcount = 0;

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

        viewHolder.price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                itemcount++;

                final String PurchaseId = ownerdbreference.push().getKey();
                final String TransId = ownerdbreference.push().getKey();
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
                product.setSubtotal(product.getProd_price() * product.getProd_qty());

                final PurchasedItem purchasedItem = new PurchasedItem();
                purchasedItem.setProduct(product);

                final double subtotal = product.getProd_price()*product.getProd_qty();
                final PurchaseTransaction purchaseTransaction = new PurchaseTransaction();
                purchaseTransaction.setPurchasedItem(purchasedItem);
                purchaseTransaction.setPurch_tot_qty(product.getProd_qty());
                purchaseTransaction.setPurch_tot_price(subtotal);
                purchaseTransaction.setPurchasedItem(purchasedItem);

                SharedPreferences shared = v.getContext().getSharedPreferences("OwnerPref", MODE_PRIVATE);
                final String username = (shared.getString("owner_username", ""));

                ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (final DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                final String acctkey = dataSnapshot1.getKey();
                                ownerdbreference.child(acctkey+"/business/transaction").orderByChild("purchasedItem/product/prod_reference").equalTo(preference).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            for (DataSnapshot dataSnapshot2:dataSnapshot.getChildren()){
                                                String transkey = dataSnapshot2.getKey();
                                                PurchaseTransaction purchaseTransaction1 = dataSnapshot2.getValue(PurchaseTransaction.class);
                                                String reference = purchaseTransaction1.getPurchasedItem().getProduct().getProd_reference();
//
                                                if (reference.equals(preference)){

                                                    ownerdbreference.child(acctkey+"/business/transaction/"+transkey+"/purchasedItem/product/prod_qty").setValue(prodqty);
                                                    ownerdbreference.child(acctkey+"/business/transaction/"+transkey+"/purchasedItem/product/subtotal").setValue(prodqty*product.getProd_price());
                                                    ownerdbreference.child(acctkey+"/business/transaction/"+transkey+"/purch_tot_qty").setValue(prodqty);
                                                    ownerdbreference.child(acctkey+"/business/transaction/"+transkey+"/purch_subtotal").setValue(subtotal);
                                                    ownerdbreference.child(acctkey+"/business/transaction/"+transkey+"/purch_tot_price").setValue(subtotal);
                                                    Toast.makeText(v.getContext(), "Quantity updated.", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                        }else {
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
