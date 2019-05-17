package com.example.devcash.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.devcash.CategoryList;
import com.example.devcash.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CagtegorylistAdapter extends BaseAdapter {

    Context context;

    //data container
    ArrayList<CategoryList> list;
    LayoutInflater inflater;

    public CagtegorylistAdapter(Context context, ArrayList<CategoryList> list) {
        super();
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CategoryHandler handler = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_categories, null); //name of custom listview xml file
            handler = new CategoryHandler();
            handler.category_name = (TextView) convertView.findViewById(R.id.text_categoryname);
            convertView.setTag(handler);
        }
        else handler = (CategoryHandler) convertView.getTag();

        //fill the view elements
        handler.category_name.setText(list.get(position).getCategory_name());


        return convertView;
    }

    static class CategoryHandler{
        TextView category_name;
    }
}
