package com.example.devcash.CustomAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.EDIT_UI.EditServices;
import com.example.devcash.Object.Services;
import com.example.devcash.Object.Serviceslistdata;
import com.example.devcash.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {
    Context context;
    List<Serviceslistdata> list;

//    public ServicesAdapter(List<Serviceslistdata> list) {
//        this.list = list;
//    }


    public ServicesAdapter(Context context, List<Serviceslistdata> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customlayout_services, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String servname = list.get(viewHolder.getAdapterPosition()).getServname();
                String servstatus = list.get(viewHolder.getAdapterPosition()).getServstatus();
                double servprice = list.get(viewHolder.getAdapterPosition()).getServprice();

                Intent intent = new Intent(v.getContext(), EditServices.class);
                intent.putExtra("service_name", servname);
                intent.putExtra("service_status", servstatus);
                intent.putExtra("service_price", servprice);
                v.getContext().startActivity(intent);

            }
        });

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Serviceslistdata data = list.get(i);
        viewHolder.servicename.setText(data.getServname());
        viewHolder.servicestatus.setText(data.getServstatus());

        if (viewHolder.servicestatus.getText().toString().equals("Available")){
            viewHolder.servicestatus.setText(data.getServstatus());
            viewHolder.servimgstatus.setColorFilter(Color.GREEN);
            viewHolder.servicestatus.setTextColor(Color.GREEN);
        }
        if (viewHolder.servicestatus.getText().toString().equals("Not Available")){
            viewHolder.servicestatus.setText(data.getServstatus());
            viewHolder.servimgstatus.setColorFilter(Color.RED);
            viewHolder.servicestatus.setTextColor(Color.RED);
        }

        viewHolder.serviceprice.setText("₱"+(data.getServprice()));
        viewHolder.discountedprice.setText("₱"+(data.getDiscounted_price()));

        if (viewHolder.serviceprice.getText().toString().equals(viewHolder.discountedprice.getText().toString())){
            viewHolder.serviceprice.setVisibility(View.INVISIBLE);
        }

        if (!viewHolder.serviceprice.getText().toString().equals(viewHolder.discountedprice.getText().toString())){
            viewHolder.serviceprice.setPaintFlags(viewHolder.serviceprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.discountedprice.setText("₱"+(data.getDiscounted_price()));
        }

        Picasso.with(context).load(data.getService_image()).into(viewHolder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView servicename, serviceprice, servicestatus, discountedprice;
        ImageView servimgstatus, image;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            servicename = (TextView) itemView.findViewById(R.id.txtservice_name);
            serviceprice = (TextView) itemView.findViewById(R.id.txtservice_price);
            servicestatus = (TextView) itemView.findViewById(R.id.txtservice_status);
            servimgstatus = (ImageView) itemView.findViewById(R.id.imageView_servavail);
            discountedprice = (TextView) itemView.findViewById(R.id.txtservice_discprice);
            layout = (LinearLayout) itemView.findViewById(R.id.serviceLayout);
            image = (ImageView) itemView.findViewById(R.id.service_image);

        }
    }
}
