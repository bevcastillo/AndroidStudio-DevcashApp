package com.example.devcash.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.devcash.R;
import com.example.devcash.Model.SettingsList;

import java.util.ArrayList;

public class SettingsAdapter extends BaseAdapter {

    Context context;
    //data container
    ArrayList<SettingsList> settingslist;
    LayoutInflater inflater;

    public SettingsAdapter(Context context, ArrayList<SettingsList> settingslist) {
        super();
        this.context = context;
        this.settingslist = settingslist;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return settingslist.size();
    }

    @Override
    public Object getItem(int position) {
        return settingslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SettingsHandler handler = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_settings, null);
            handler = new SettingsHandler();
            handler.settings_iv = (ImageView) convertView.findViewById(R.id.settings_icons);
            handler.settings_title = (TextView) convertView.findViewById(R.id.settings_text);
            convertView.setTag(handler);
        }else
            handler = (SettingsHandler) convertView.getTag();

        //fill the view elements
        handler.settings_iv.setImageResource(settingslist.get(position).getSettingsicon());
        handler.settings_title.setText(settingslist.get(position).getSettingstitle());

        return convertView;
    }

    //create static class to handle Settings layout
    static class SettingsHandler{
        ImageView settings_iv;
        TextView settings_title;
    }
}
