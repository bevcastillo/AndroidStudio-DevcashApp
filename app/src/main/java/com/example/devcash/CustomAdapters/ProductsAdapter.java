package com.example.devcash.CustomAdapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.devcash.Object.Productslistdata;
import com.example.devcash.R;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    List<Productslistdata> list;

    public ProductsAdapter(List<Productslistdata> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customlayout_products, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Productslistdata data = list.get(i);
        viewHolder.prodname.setText(data.getProd_name());
        viewHolder.prodstatus.setText(data.getProd_status());
        viewHolder.prodprice.setText(String.valueOf(data.getProd_price()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        TextView prodname, prodstatus, prodprice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            prodname = (TextView) itemView.findViewById(R.id.txtprod_name);
            prodstatus = (TextView) itemView.findViewById(R.id.txtprod_status);
            prodprice = (TextView) itemView.findViewById(R.id.txtprod_price);
        }
    }
}
