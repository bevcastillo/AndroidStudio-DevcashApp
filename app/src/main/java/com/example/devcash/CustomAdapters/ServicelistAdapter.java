package com.example.devcash.CustomAdapters;

import android.app.Service;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customcard_purchaselist, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Services services = servicesList.get(i);
        if (!services.equals(null)) {
            viewHolder.textservname.setText(services.getService_name());
            viewHolder.textservsubtotal.setText(String.valueOf(services.getService_subtotal()));
            viewHolder.textservprice.setText("@"+(services.getService_disc_price())+" each");

            viewHolder.textservqty.setText(String.valueOf(services.getService_qty()));

            if (viewHolder.textservprice.getText().toString().equals(services.getService_disc_price())){
                viewHolder.imgdiscounted.setVisibility(View.VISIBLE);
            }

        }

    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView textservqty, textservname, textservprice, textservsubtotal;
        ImageView imgdiscounted;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textservqty = (TextView) itemView.findViewById(R.id.purchase_prodqtycount);
            textservname = (TextView) itemView.findViewById(R.id.purchase_prodname);
            textservprice = (TextView) itemView.findViewById(R.id.purchase_prodprice);
            textservsubtotal = (TextView) itemView.findViewById(R.id.purchase_prodtotqty);
            imgdiscounted = (ImageView) itemView.findViewById(R.id.img_discounted);
        }
    }
}
