package com.example.devcash.CustomAdapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.Object.Categorylistdata;
import com.example.devcash.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    List<Categorylistdata> list;

    public CategoryAdapter(List<Categorylistdata> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customlayout_category, viewGroup,false);
        final ViewHolder viewHolder = new ViewHolder(v);

        viewHolder.categoryname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(viewGroup.getContext(), (list.get(viewHolder.getAdapterPosition()).getCategory_name()), Toast.LENGTH_SHORT).show();
            }
        });


        return viewHolder;

        //

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Categorylistdata data = list.get(i);
        viewHolder.categoryname.setText(data.getCategory_name());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView categoryname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryname = (TextView) itemView.findViewById(R.id.txtcatname);
        }
    }

}
