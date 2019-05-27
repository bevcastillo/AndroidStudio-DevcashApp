package com.example.devcash.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.devcash.Lists.InventoryList;
import com.example.devcash.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class InventoryListAdapter extends BaseAdapter {

    Context context;
    //data container
    ArrayList<InventoryList> inventoryList;
    LayoutInflater inflater;

    //constructor
    public InventoryListAdapter(Context context, ArrayList<InventoryList> inventoryList) {
        super();
        this.context = context;
        this.inventoryList = inventoryList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return inventoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return inventoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        InventoryListHandler handler = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.customlayout_inventorylist, null);
            handler = new InventoryListHandler();
            handler.inventory_title = (TextView) convertView.findViewById(R.id.inventorylist_title);
            handler.icon = (ImageView) convertView.findViewById(R.id.inventorylist_icon);

            convertView.setTag(handler);
        }else
            handler = (InventoryListHandler) convertView.getTag();

        //fill the view elements
        handler.inventory_title.setText(inventoryList.get(position).getInventory_title());
        handler.icon.setImageResource(inventoryList.get(position).getIcon());

        return convertView;
    }


    static class InventoryListHandler {
        TextView inventory_title;
        ImageView icon;
    }
}
