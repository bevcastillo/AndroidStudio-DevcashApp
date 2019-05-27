package com.example.devcash.CustomAdapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.devcash.Lists.CategoryList;
import com.example.devcash.R;

import java.util.ArrayList;

public class CategoryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CategoryList> categoryListArrayList;
    LayoutInflater inflater;


    public CategoryAdapter(Context context, ArrayList<CategoryList> categoryListArrayList) {
        this.context = context;
        this.categoryListArrayList = categoryListArrayList;
    }

    @Override
    public int getCount() {
        return categoryListArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryListArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.category_customlayout, parent, false);

            holder.textCategoryName = (TextView) convertView.findViewById(R.id.txtcategory_name);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        //display the data into the CategoryFragment
        holder.textCategoryName.setText(categoryListArrayList.get(position).getCategory_name());

        return convertView;
    }

    private class ViewHolder{
        protected TextView textCategoryName;
    }
}
