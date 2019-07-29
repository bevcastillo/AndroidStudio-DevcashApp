package com.example.devcash.CustomAdapters;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.devcash.Model.CategoryList;
import com.example.devcash.Object.Category;
import com.example.devcash.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Context context;
    ArrayList<Category> categoryArrayList;

    public CategoryAdapter(Context c, ArrayList<Category> categories){
        context = c;
        categoryArrayList = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customlayout_category, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.categoryname.setText(categoryArrayList.get(i).getCategory_name());
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView categoryname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryname = (TextView) itemView.findViewById(R.id.txtcategory_name);
        }
    }

}
