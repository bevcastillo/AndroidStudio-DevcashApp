package com.example.devcash.CustomAdapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.Object.CartItem;
import com.example.devcash.Object.CustomerTransactionlistdata;
import com.example.devcash.Object.PurchaseTransactionlistdata;
import com.example.devcash.R;

import java.util.List;

public class AllPurchaseAdapter extends RecyclerView.Adapter<AllPurchaseAdapter.ViewHolder> {

//    List<CustomerTransactionlistdata> list;

//    public AllPurchaseAdapter(List<CustomerTransactionlistdata> list) {
//        this.list = list;
//    }

    List<CartItem> cartItemList;

    public AllPurchaseAdapter(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customcard_purchaselist, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        Toast.makeText(view.getContext(), cartItemList.size()+" is the size", Toast.LENGTH_SHORT).show();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
//        CustomerTransactionlistdata data = list.get(i);
//        viewHolder.txtservname.setText(data.getPurchasedItem().getServices().getService_name());
//        viewHolder.txttotqty.setText(String.valueOf(data.getPurch_tot_qty()));
//        viewHolder.txtservprice.setText("@ "+ data.getPurchasedItem().getServices().getService_price() +" each");
//        viewHolder.txtsubtotal.setText(String.valueOf(data.getPurchasedItem().getServices().getService_subtotal()));

        CartItem cartItem = cartItemList.get(i);
//        viewHolder.txtitemname.setText(cartItem.getProduct().getProd_name());

        if (!cartItem.getServices().equals(null)) {
            viewHolder.txtitemname.setText(cartItem.getServices().getService_name());
        }

//        if (!cartItem.getProduct().equals(null)) {
//            viewHolder.txtitemname.setText(cartItem.getProduct().getProd_name());
//        }


//        if (!cartItem.getServices().equals(null)){
//            if (!cartItem.getServices().getService_name().equals("")){
//                viewHolder.txtitemname.setText(cartItem.getServices().getService_name());
//                viewHolder.txttotqty.setText(cartItem.getServices().getService_qty());
//                viewHolder.txtitemprice.setText(String.valueOf(cartItem.getServices().getService_price()));
//                viewHolder.txtsubtotal.setText(String.valueOf(cartItem.getServices().getService_subtotal()));
//            }else {
//                viewHolder.txtitemname.setText(cartItem.getProduct().getProd_name());
//                viewHolder.txttotqty.setText(String.valueOf(cartItem.getProduct().getProd_qty()));
//                viewHolder.txtitemprice.setText(String.valueOf(cartItem.getProduct().getProd_price()));
//                viewHolder.txtsubtotal.setText(String.valueOf(cartItem.getProduct().getProd_subtotal()));
//            }
//        }

//        if (!cartItem.getProduct().equals(null)){
//            if (!cartItem.getProduct().getProd_name().equals("")){
//                viewHolder.txtitemname.setText(cartItem.getProduct().getProd_name());
//                viewHolder.txttotqty.setText(String.valueOf(cartItem.getProduct().getProd_qty()));
//                viewHolder.txtitemprice.setText(String.valueOf(cartItem.getProduct().getProd_price()));
//                viewHolder.txtsubtotal.setText(String.valueOf(cartItem.getProduct().getProd_subtotal()));
//            }else {
//                viewHolder.txtitemname.setText(cartItem.getServices().getService_name());
//                viewHolder.txttotqty.setText(cartItem.getServices().getService_qty());
//                viewHolder.txtitemprice.setText(String.valueOf(cartItem.getServices().getService_price()));
//                viewHolder.txtsubtotal.setText(String.valueOf(cartItem.getServices().getService_subtotal()));
//            }
//        }

    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView txttotqty, txtitemname, txtitemprice, txtsubtotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txttotqty = (TextView) itemView.findViewById(R.id.purchase_prodqtycount);
            txtitemname = (TextView) itemView.findViewById(R.id.purchase_prodname);
            txtitemprice = (TextView) itemView.findViewById(R.id.purchase_prodprice);
            txtsubtotal = (TextView) itemView.findViewById(R.id.purchase_prodtotqty);


        }
    }
}
