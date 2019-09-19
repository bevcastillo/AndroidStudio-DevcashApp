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
import com.example.devcash.Object.Services;
import com.example.devcash.R;

import java.util.List;

public class ReceiptServiceAdapter extends RecyclerView.Adapter<ReceiptServiceAdapter.ViewHolder>{

    List<Services> list;

    public ReceiptServiceAdapter(List<Services> list) {
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
        Services services = list.get(i);

//        if (!services.equals(null)){
//            viewHolder.itemname.setText(services.getService_name());
//            viewHolder.itemqtyprice.setText(String.valueOf(services.getService_qty())+" X "+services.getDiscounted_price());
//            viewHolder.itemsubtotal.setText(String.valueOf(services.getService_subtotal()));
//        }

        if (!services.equals(null)){
            String prodName = services.getService_name();
            int prodQty = services.getService_qty();
            double discountedPrice = services.getDiscounted_price();
            double originalPrice = services.getService_price();
            double subtotal = services.getService_subtotal();

            String subtotalDiscountStr = String.format("%.2f", originalPrice - discountedPrice);
            double subtotalDiscount = Double.parseDouble(subtotalDiscountStr);

            viewHolder.servQtyName.setText(prodQty+"   "+services.getService_name());
            viewHolder.servQtyName.setTextColor(Color.BLACK);
            viewHolder.servQtyName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            viewHolder.servSubtotal.setText(""+subtotal);
            viewHolder.servSubtotal.setTextColor(Color.BLACK);
            viewHolder.servSubtotal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            viewHolder.servDiscountedPrice.setText("@ "+discountedPrice);


            viewHolder.servOriginalPrice.setText(""+services.getService_price());
            viewHolder.servLessDisc.setText(""+subtotalDiscount);

            if (viewHolder.servOriginalPrice.getText().toString().equals(viewHolder.servDiscountedPrice.getText().toString())){
                viewHolder.servOriginalPrice.setVisibility(View.INVISIBLE);
                viewHolder.servLessDisc.setVisibility(View.INVISIBLE);
            }

            if (!viewHolder.servOriginalPrice.getText().toString().equals(viewHolder.servDiscountedPrice.getText().toString())){
                viewHolder.servOriginalPrice.setPaintFlags(viewHolder.servOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                viewHolder.servOriginalPrice.setText(""+originalPrice);
                viewHolder.servLessDisc.setText("("+subtotalDiscount+")");
            }

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView servQtyName, servSubtotal, servDiscountedPrice, servOriginalPrice, servLessDisc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            servQtyName = (TextView) itemView.findViewById(R.id.qtyItemName);
            servSubtotal = (TextView) itemView.findViewById(R.id.itemsubtotal);
            servDiscountedPrice = (TextView) itemView.findViewById(R.id.discountedPrice);
            servOriginalPrice = (TextView) itemView.findViewById(R.id.originalPrice);
            servLessDisc = (TextView) itemView.findViewById(R.id.lessDiscount);
        }
    }
}
