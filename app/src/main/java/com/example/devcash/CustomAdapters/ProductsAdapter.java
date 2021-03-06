package com.example.devcash.CustomAdapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.EDIT_UI.EditProduct;
import com.example.devcash.Object.Productlistdata;
import com.example.devcash.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    List<Productlistdata> list;
    Context context;

//    public ProductsAdapter(List<Productlistdata> list) {
//        this.list = list;
//    }


    public ProductsAdapter(List<Productlistdata> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customlayout_products, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prodname = list.get(viewHolder.getAdapterPosition()).getProd_name();
                String prodbrand = list.get(viewHolder.getAdapterPosition()).getProd_brand();
                String prodstatus = list.get(viewHolder.getAdapterPosition()).getProd_status();
                double prodprice = list.get(viewHolder.getAdapterPosition()).getProd_price();
                int prodstock = list.get(viewHolder.getAdapterPosition()).getProd_stock();
                String prodexpdate = list.get(viewHolder.getAdapterPosition()).getProd_expdate();
                int prodexpcount = list.get(viewHolder.getAdapterPosition()).getProd_expdatecount();
                double rop = list.get(viewHolder.getAdapterPosition()).getProd_rop();
                String imageUrl = list.get(viewHolder.getAdapterPosition()).getProd_image();


                Intent intent = new Intent(v.getContext(), EditProduct.class);
                intent.putExtra("product_name", prodname);
                intent.putExtra("product_brand", prodbrand);
                intent.putExtra("product_status", prodstatus);
                intent.putExtra("product_price", prodprice);
                intent.putExtra("product_stock", prodstock);
                intent.putExtra("product_expdate", prodexpdate);
                intent.putExtra("product_expcount", prodexpcount);
                intent.putExtra("product_rop", rop);
                intent.putExtra("product_image", imageUrl);
                v.getContext().startActivity(intent);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Productlistdata data = list.get(i);
        viewHolder.prodname.setText(data.getProd_name());

        if (viewHolder.prodstatus.getText().toString().equals("Available")){
            viewHolder.prodstatus.setText(data.getProd_status());
            viewHolder.prodimgstatus.setColorFilter(Color.GREEN);
            viewHolder.prodstatus.setTextColor(Color.GREEN);
        }
        if (viewHolder.prodstatus.getText().toString().equals("Not Available")){
            viewHolder.prodstatus.setText(data.getProd_status());
            viewHolder.prodimgstatus.setColorFilter(Color.RED);
            viewHolder.prodstatus.setTextColor(Color.RED);
        }

        viewHolder.prodstock.setText(String.valueOf(data.getProd_stock()));

        if (viewHolder.prodexpdate.getText().toString().equals("")){
            viewHolder.prodexpdate.setText("No Expiration");
        }
        if (viewHolder.prodexpcount.getText().toString().equals(0)){
            viewHolder.prodexpcount.setVisibility(View.INVISIBLE);
            viewHolder.instock.setVisibility(View.INVISIBLE);
        }
        viewHolder.prodexpdate.setText(data.getProd_expdate());
        viewHolder.prodexpcount.setText(String.valueOf(data.getProd_expdatecount()));
        viewHolder.condname.setText(data.getCond_name());

        viewHolder.prodprice.setText("₱"+(data.getProd_price()));
        viewHolder.discountedprice.setText("₱"+(data.getDiscounted_price()));

        if (viewHolder.prodprice.getText().toString().equals(viewHolder.discountedprice.getText().toString())){
            viewHolder.prodprice.setVisibility(View.INVISIBLE);
        }

        if (!viewHolder.prodprice.getText().toString().equals(viewHolder.discountedprice.getText().toString())){
            viewHolder.prodprice.setPaintFlags(viewHolder.prodprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.discountedprice.setText("₱"+(data.getDiscounted_price()));
        }

        Picasso.with(context).load(data.getProd_image()).into(viewHolder.prodImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        TextView prodname, prodstatus, prodprice, prodstock, condname, prodexpdate, prodexpcount, instock, discountedprice;
        ImageView prodimgstatus, prodImage;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            prodname = (TextView) itemView.findViewById(R.id.txtprod_name);
            prodstatus = (TextView) itemView.findViewById(R.id.txtprod_status);
            prodprice = (TextView) itemView.findViewById(R.id.txtprod_price);
            prodstock = (TextView) itemView.findViewById(R.id.txtprod_stock);
            condname = (TextView) itemView.findViewById(R.id.txtprod_condition);
            prodexpdate = (TextView) itemView.findViewById(R.id.txtprod_expdate);
            prodexpcount = (TextView) itemView.findViewById(R.id.txtcount);
            instock = (TextView) itemView.findViewById(R.id.instock);
            prodimgstatus = (ImageView) itemView.findViewById(R.id.imageView_availability);
            discountedprice = (TextView) itemView.findViewById(R.id.txtprod_discprice);
            layout = (LinearLayout) itemView.findViewById(R.id.productLayout);
            prodImage = (ImageView) itemView.findViewById(R.id.prod_image);
        }
    }
}
