package com.example.devcash.CustomAdapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.devcash.Object.ProductConditionlistdata;
import com.example.devcash.Object.Productslistdata;
import com.example.devcash.R;

import org.w3c.dom.Text;

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
        viewHolder.prodstock.setText(String.valueOf(data.getProd_stock()));
        Log.i("TAG STOCK", String.valueOf(data.getProd_stock()));
        viewHolder.condname.setText(data.getCond_name());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        TextView prodname, prodstatus, prodprice, prodstock, condname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            prodname = (TextView) itemView.findViewById(R.id.txtprod_name);
            prodstatus = (TextView) itemView.findViewById(R.id.txtprod_status);
            prodprice = (TextView) itemView.findViewById(R.id.txtprod_price);
            prodstock = (TextView) itemView.findViewById(R.id.txtprod_stock);
            condname = (TextView) itemView.findViewById(R.id.txtprod_condition);
        }
    }
}
