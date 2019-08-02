package com.example.devcash.CustomAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.devcash.Object.Services;
import com.example.devcash.R;

import java.util.ArrayList;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {
    Context context;
    ArrayList<Services> list;

    public ServicesAdapter(Context c, ArrayList<Services> services) {
        context = c;
        list = services;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customlayout_services, viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.servicename.setText(list.get(i).getService_name());
        viewHolder.serviceprice.setText((int) list.get(i).getService_price());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView servicename, serviceprice;
        ImageView serviceimage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceimage = (ImageView) itemView.findViewById(R.id.service_image);
            servicename = (TextView) itemView.findViewById(R.id.txtservice_name);
            serviceprice = (TextView) itemView.findViewById(R.id.txtservice_price);
        }
    }
}
