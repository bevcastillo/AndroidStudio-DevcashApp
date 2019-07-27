package com.example.devcash.CustomAdapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.devcash.Object.CategoryObj;
import com.example.devcash.R;

import java.util.ArrayList;

public class CategoryCustomAdapter extends RecyclerView.Adapter<CategoryCustomAdapter.CategoryViewHolder> {

    private ArrayList<CategoryObj> mCatArrayList;


    public static class CategoryViewHolder extends RecyclerView.ViewHolder{

        public TextView mCatName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            mCatName = (TextView)itemView.findViewById(R.id.txtcategory_name);
        }
    }



    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customlayout_category, viewGroup, false);
        CategoryViewHolder cvholder = new CategoryViewHolder(view);

        return cvholder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int i) {
        CategoryObj currentItem = mCatArrayList.get(i);

        categoryViewHolder.mCatName.setText(currentItem.getCategory_name());

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
