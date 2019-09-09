package com.example.devcash.CustomAdapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.devcash.Object.PurchaseTransaction;
import com.example.devcash.Object.PurchaseTransactionlistdata;
import com.example.devcash.R;

import java.util.List;

public class PurchaseListAdapter extends RecyclerView.Adapter<PurchaseListAdapter.ViewHolder> {
    List<PurchaseTransactionlistdata> list;

    public PurchaseListAdapter(List<PurchaseTransactionlistdata> list) {
        this.list = list;
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
        PurchaseTransactionlistdata data = list.get(i);
        viewHolder.purchqty.setText(String.valueOf(data.getPurch_qty()));
        viewHolder.itemname.setText(data.getProduct().getProd_name());
        viewHolder.itemprice.setText("@ "+(data.getProduct().getProd_price())+" each");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView purchqty, itemname, itemprice;
        ImageView delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            purchqty = (TextView) itemView.findViewById(R.id.purchase_prodqtycount);
            itemname = (TextView) itemView.findViewById(R.id.purchase_prodname);
            itemprice = (TextView) itemView.findViewById(R.id.purchase_prodtotqty);
            delete = (ImageView) itemView.findViewById(R.id.imgdelete);

        }
    }


}
