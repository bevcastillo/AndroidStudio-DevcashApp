package com.example.devcash.CustomAdapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.devcash.Object.PurchaseTransactionlistdata;
import com.example.devcash.R;

import java.util.List;

public class AllPurchaseAdapter extends RecyclerView.Adapter<AllPurchaseAdapter.ViewHolder> {

    List<PurchaseTransactionlistdata> list;

    public AllPurchaseAdapter(List<PurchaseTransactionlistdata> list) {
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
        viewHolder.txtservname.setText(data.getPurchasedItem().getServices().getService_name());
        viewHolder.txttotqty.setText(String.valueOf(data.getPurch_tot_qty()));
        viewHolder.txtservprice.setText("@ "+ data.getPurchasedItem().getServices().getService_price() +" each");
        viewHolder.txtsubtotal.setText(String.valueOf(data.getPurchasedItem().getServices().getService_subtotal()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView txttotqty, txtservname, txtservprice, txtsubtotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txttotqty = (TextView) itemView.findViewById(R.id.purchase_prodqtycount);
            txtservname = (TextView) itemView.findViewById(R.id.purchase_prodname);
            txtservprice = (TextView) itemView.findViewById(R.id.purchase_prodprice);
            txtsubtotal = (TextView) itemView.findViewById(R.id.purchase_prodtotqty);


        }
    }
}
