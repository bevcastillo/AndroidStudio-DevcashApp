package com.example.devcash.CustomAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.devcash.Object.PurchaseTransaction;
import com.example.devcash.Object.Serviceslistdata;
import com.example.devcash.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class PurchaseInventoryServicesAdapter extends RecyclerView.Adapter<PurchaseInventoryServicesAdapter.ViewHolder> {
    List<Serviceslistdata> list;
    private static int itemcount = 0;
    Context context;

    public PurchaseInventoryServicesAdapter(List<Serviceslistdata> list) {
        this.list = list;
    }

    public PurchaseInventoryServicesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        View v;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_inventory_cardviewgrid, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        viewHolder.servicename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemcount++;

                String servname = list.get(viewHolder.getAdapterPosition()).getServname();
                double servprice = list.get(viewHolder.getAdapterPosition()).getServprice();
                int servqty = itemcount;

                Intent intent = new Intent(v.getContext(), PurchaseListFragment.class);
                intent.putExtra("service_name", servname);
                intent.putExtra("service_price", servprice);
                intent.putExtra("service_qty", servqty);
//                v.getContext().startActivity(intent); //save to intent

//                addTransaction(null,servname,servqty,servprice);
                addTransaction();

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

    public void addTransaction(){

//    public void addTransaction(String datetime, String itemName, int itemQty, double itemPrice){
//        final PurchaseTransaction transaction = new PurchaseTransaction(datetime, null,
//                null, null, itemQty, 0.0, 0.0, 0.0,
//        0.0, 0.0, 0.0, 0.0);

//        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
//        final String username = (shared.getString("owner_username", ""));
        SharedPreferences sharedPreferences = context.getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (sharedPreferences.getString("owner_username",""));

        Toast.makeText(context, username+" is the username", Toast.LENGTH_SHORT).show();
    }
}
