package com.example.devcash.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.devcash.Lists.EnterpriseList;
import com.example.devcash.R;

import java.util.ArrayList;

public class EnterpriseAdapter extends BaseAdapter {

    Context context;
    //data container
    ArrayList<EnterpriseList> enterpriselist;
    LayoutInflater inflater;

    //constructor


    public EnterpriseAdapter(Context context, ArrayList<EnterpriseList> enterpriselist) {
        super();
        this.context = context;
        this.enterpriselist = enterpriselist;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return enterpriselist.size();
    }

    @Override
    public Object getItem(int position) {
        return enterpriselist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        EnterpriseHandler handler = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_enterprise, null);
            handler = new EnterpriseHandler();
            handler.enterprise_title = (TextView) convertView.findViewById(R.id.enterprise_title);
            handler.enterprise_details = (TextView) convertView.findViewById(R.id.enterprise_details);
            handler.icon = (ImageView) convertView.findViewById(R.id.enterprise_icon);
            convertView.setTag(handler);
        }else
            handler = (EnterpriseHandler) convertView.getTag();

        //fill the view elements
        handler.enterprise_title.setText(enterpriselist.get(position).getEnterpriseTitle());
        handler.enterprise_details.setText(enterpriselist.get(position).getEnterpriseDetails());
        handler.icon.setImageResource(enterpriselist.get(position).getIcon());

        return convertView;
    }

    //create a static class to handle Enterprise layout
    static class EnterpriseHandler{
        TextView enterprise_title, enterprise_details;
        ImageView icon;
    }
}
