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

public class ProductlistAdapter extends RecyclerView.Adapter<ProductlistAdapter.ViewHolder> {

    List<Product> productList;

    public ProductlistAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customcard_purchaselist, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Product product = productList.get(i);

        if (!product.equals(null)) {
            viewHolder.textprodname.setText(product.getProd_name());
            viewHolder.textprodsubtotal.setText(String.valueOf(product.getProd_subtotal()));
            viewHolder.textprodprice.setText(String.valueOf(product.getProd_price()));
            viewHolder.textprodqty.setText(String.valueOf(product.getProd_qty()));

        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView textprodqty, textprodname, textprodprice, textprodsubtotal;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textprodqty = (TextView) itemView.findViewById(R.id.purchase_prodqtycount);
            textprodname = (TextView) itemView.findViewById(R.id.purchase_prodname);
            textprodprice = (TextView) itemView.findViewById(R.id.purchase_prodprice);
            textprodsubtotal = (TextView) itemView.findViewById(R.id.purchase_prodtotqty);
        }
    }
}
