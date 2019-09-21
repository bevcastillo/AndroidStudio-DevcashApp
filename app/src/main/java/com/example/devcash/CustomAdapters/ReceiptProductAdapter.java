package com.example.devcash.CustomAdapters;

import android.graphics.Color;
import android.graphics.Paint;
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

//        if (!product.equals(null)){
//            viewHolder.itemname.setText(product.getProd_name());
//            viewHolder.itemqtyprice.setText(String.valueOf(product.getProd_qty())+" X "+product.getDiscounted_price());
//            viewHolder.itemsubtotal.setText(String.valueOf(product.getProd_subtotal()));
//        }

        if (!product.equals(null)){
            String prodName = product.getProd_name();
            int prodQty = product.getProd_qty();
            double discountedPrice = product.getDiscounted_price();
            double originalPrice = product.getProd_price();
            double subtotal = product.getProd_subtotal();

            String subtotalDiscountStr = String.format("%.2f", originalPrice - discountedPrice);
            double subtotalDiscount = Double.parseDouble(subtotalDiscountStr);

            viewHolder.prodQtyName.setText(prodQty+"   "+prodName);

            viewHolder.prodSubtotal.setText(""+subtotal);


            viewHolder.prodOriginalPrice.setText(""+product.getProd_price());
            viewHolder.prodLessDisc.setText(""+subtotalDiscount);
            viewHolder.prodDiscountedPrice.setText("@ "+product.getDiscounted_price());

            if (viewHolder.prodOriginalPrice.getText().toString().equals(viewHolder.prodDiscountedPrice.getText().toString())){
                viewHolder.prodOriginalPrice.setVisibility(View.INVISIBLE);
                viewHolder.prodLessDisc.setVisibility(View.INVISIBLE);
            }

            if (!viewHolder.prodOriginalPrice.getText().toString().equals(viewHolder.prodDiscountedPrice.getText().toString())){
                viewHolder.prodOriginalPrice.setPaintFlags(viewHolder.prodOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                viewHolder.prodOriginalPrice.setText(""+originalPrice);
                viewHolder.prodLessDisc.setText("("+subtotalDiscount+")");
            }

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView prodQtyName, prodSubtotal, prodDiscountedPrice, prodOriginalPrice, prodLessDisc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            prodQtyName = (TextView) itemView.findViewById(R.id.qtyItemName);
            prodSubtotal = (TextView) itemView.findViewById(R.id.itemsubtotal);
            prodDiscountedPrice = (TextView) itemView.findViewById(R.id.discountedPrice);
            prodOriginalPrice = (TextView) itemView.findViewById(R.id.originalPrice);
            prodLessDisc = (TextView) itemView.findViewById(R.id.lessDiscount);
        }
    }
}
