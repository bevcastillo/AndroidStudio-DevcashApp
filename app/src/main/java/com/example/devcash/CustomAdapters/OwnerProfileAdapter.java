package com.example.devcash.CustomAdapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.devcash.OwnerProfileList;
import com.example.devcash.R;

import java.util.ArrayList;

public class OwnerProfileAdapter extends BaseAdapter {

    Context context;
    //data container
    ArrayList<OwnerProfileList> ownerprofilelist;
    LayoutInflater inflater;

    public OwnerProfileAdapter(Context context, ArrayList<OwnerProfileList> ownerprofilelist) {
        super();
        this.context = context;
        this.ownerprofilelist = ownerprofilelist;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return ownerprofilelist.size();
    }

    @Override
    public Object getItem(int position) {
        return ownerprofilelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        OwnerProfileHandler handler = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_profile, null);
            handler = new OwnerProfileHandler();
            handler.profile_title = (TextView) convertView.findViewById(R.id.profile_title);
            handler.profile_details = (TextView) convertView.findViewById(R.id.profile_details);
            convertView.setTag(handler);
        }else
            handler = (OwnerProfileHandler) convertView.getTag();

        //fill the view elemets
        handler.profile_title.setText(ownerprofilelist.get(position).getProfileTitle());
        handler.profile_details.setText(ownerprofilelist.get(position).getProfileDetails());

        return convertView;
    }

    //create static class to handle Owner Profile layout
    static class OwnerProfileHandler{
        TextView profile_title, profile_details;
    }
}
