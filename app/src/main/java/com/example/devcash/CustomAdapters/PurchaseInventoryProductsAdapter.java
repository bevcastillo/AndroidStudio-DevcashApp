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

public class PurchaseInventoryProductsAdapter extends RecyclerView.Adapter<PurchaseInventoryProductsAdapter.ViewHolder> {
    List<Productslistdata> list;

    public PurchaseInventoryProductsAdapter(List<Productslistdata> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_inventory_cardviewgrid, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Productslistdata data = list.get(i);
        viewHolder.prodname.setText(data.getProd_name());
        viewHolder.prodprice.setText(String.valueOf(data.getProd_price()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView prodname, prodprice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            prodname = (TextView) itemView.findViewById(R.id.prodlist_name);
            prodprice = (TextView) itemView.findViewById(R.id.prodlist_price);
        }
    }
}
