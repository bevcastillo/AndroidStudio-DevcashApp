package com.example.devcash.CustomAdapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.devcash.EDIT_UI.EditDiscount;
import com.example.devcash.Object.Discountlistdata;
import com.example.devcash.R;

import java.util.List;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.ViewHolder> {
    List<Discountlistdata> list;

    public DiscountAdapter(List<Discountlistdata> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customlayout_discount, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        viewHolder.disccode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(v.getContext(), EditDiscount.class);
                v.getContext().startActivity(edit);
            }
        });


        return viewHolder;

//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customlayout_discount, viewGroup, false);
//        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Discountlistdata data = list.get(i);
        viewHolder.disccode.setText(data.getDisc_code());
        viewHolder.disctype.setText(data.getDisc_type());
        viewHolder.discstart.setText(data.getDisc_start());
        viewHolder.discend.setText(data.getDisc_end());
        viewHolder.discvalue.setText(String.valueOf(data.getDisc_value()));
        viewHolder.discstatus.setText(data.getDisc_status());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView disccode, disctype, discvalue, discstart, discend, discstatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            disccode = (TextView) itemView.findViewById(R.id.text_disccode);
            discstart = (TextView) itemView.findViewById(R.id.text_discstart);
            discend = (TextView) itemView.findViewById(R.id.txt_discend);
            disctype = (TextView) itemView.findViewById(R.id.txt_disctype);
            discvalue = (TextView) itemView.findViewById(R.id.txt_discvalue);
            discstatus = (TextView) itemView.findViewById(R.id.textdisc_status);

        }
    }
}
