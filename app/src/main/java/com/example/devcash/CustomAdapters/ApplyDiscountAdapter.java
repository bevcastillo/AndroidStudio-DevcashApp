package com.example.devcash.CustomAdapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.devcash.Object.Discountlistdata;
import com.example.devcash.R;

import java.util.List;

public class ApplyDiscountAdapter extends RecyclerView.Adapter<ApplyDiscountAdapter.ViewHolder>{

    List<Discountlistdata> list;

    public ApplyDiscountAdapter(List<Discountlistdata> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customlayout_apply_discount, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Discountlistdata data = list.get(i);

        viewHolder.discountName.setText(data.getDisc_code());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView discountName;
        Switch discountSwitch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            discountName = (TextView) itemView.findViewById(R.id.discount_name_apply);
            discountSwitch = (Switch) itemView.findViewById(R.id.discount_switch);
        }
    }
}
