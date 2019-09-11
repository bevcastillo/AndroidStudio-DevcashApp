package com.example.devcash.CustomAdapters;

import android.app.Service;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.Object.CartItem;
import com.example.devcash.Object.Services;
import com.example.devcash.R;

import java.util.List;

public class ServicelistAdapter extends RecyclerView.Adapter<ServicelistAdapter.ViewHolder> {

    List<Services> servicesList;

    public ServicelistAdapter(List<Services> servicesList) {
        this.servicesList = servicesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_itemsreceipt, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);


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
            viewHolder.serviceqtyprice.setText(services.getService_qty() + " X "+ services.getService_disc_price());

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
