package com.example.devcash.CustomAdapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.devcash.Object.Product;
import com.example.devcash.R;

import java.util.List;

public class ReceiptProductAdapter extends RecyclerView.Adapter<ReceiptProductAdapter.ViewHolder> {
    List<Product> list;

    public ReceiptProductAdapter(List<Product> list) {
        this.list = list;
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
        Product product = list.get(i);

        if (!product.equals(null)){
            viewHolder.itemname.setText(product.getProd_name());
            viewHolder.itemqtyprice.setText(String.valueOf(product.getProd_qty())+" X "+product.getDiscounted_price());
            viewHolder.itemsubtotal.setText(String.valueOf(product.getProd_subtotal()));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView itemname, itemqtyprice, itemsubtotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemname = (TextView) itemView.findViewById(R.id.itemname);
            itemqtyprice = (TextView) itemView.findViewById(R.id.itemqtyprice);
            itemsubtotal = (TextView) itemView.findViewById(R.id.itemsubtotal);
        }
    }
}
