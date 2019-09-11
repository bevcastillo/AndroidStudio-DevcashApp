package com.example.devcash.CustomAdapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.devcash.Object.Product;
import com.example.devcash.R;

import java.util.List;

public class ProductlistAdapter extends RecyclerView.Adapter<ProductlistAdapter.ViewHolder> {

    List<Product> productList;

    public ProductlistAdapter(List<Product> productList) {
        this.productList = productList;
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

        Product product = productList.get(i);

        if (!product.equals(null)) {
            viewHolder.prodname.setText(product.getProd_name());
            viewHolder.prodname.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            viewHolder.prodname.setTextColor(Color.BLACK);
            viewHolder.prodqtyprice.setText(String.valueOf(product.getProd_qty()) +" X 341.00");
            viewHolder.prodsubtotal.setText(String.valueOf(product.getProd_subtotal()));
            viewHolder.prodsubtotal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            viewHolder.prodsubtotal.setTextColor(Color.BLACK);

        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView prodname, prodqtyprice, prodsubtotal;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            prodname = (TextView) itemView.findViewById(R.id.itemname);
            prodqtyprice = (TextView) itemView.findViewById(R.id.itemqtyprice);
            prodsubtotal = (TextView) itemView.findViewById(R.id.itemsubtotal);

        }
    }
}
