package com.example.devcash.CustomAdapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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


        viewHolder.prodname.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

            @Override
            public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenu.ContextMenuInfo menuInfo) {

                String itemName = productList.get(viewHolder.getAdapterPosition()).getProd_name();
                double itemQty = productList.get(viewHolder.getAdapterPosition()).getProd_qty();

                Toast.makeText(v.getContext(), itemName+itemQty+" are the clicked items", Toast.LENGTH_SHORT).show();

                menu.add("Edit Quantity").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                        dialog.setTitle("Update Item Quantity");
                        final EditText qty = new EditText(v.getContext());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        qty.setLayoutParams(lp);
                        dialog.setView(qty);
                        dialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        return true;
                    }
                });

                menu.add("Delete Item").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(v.getContext(), "This is delete", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Product product = productList.get(i);

        if (!product.equals(null)) {
            viewHolder.prodname.setText(product.getProd_name());
            viewHolder.prodname.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            viewHolder.prodname.setTextColor(Color.BLACK);
            double discPrice;
//            if ( product.getQrCode() != null )  {
//                discPrice = product.getQrCode().getQr_disc_price();
//            } else {
//                discPrice = product.getDiscounted_price();
//            }
            viewHolder.prodqtyprice.setText(product.getProd_qty() +" X "+ product.getDiscounted_price());
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
