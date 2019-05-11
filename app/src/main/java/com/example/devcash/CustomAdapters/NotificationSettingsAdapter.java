package com.example.devcash.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.devcash.NotificationsSettingsList;
import com.example.devcash.R;

import java.util.ArrayList;

public class NotificationSettingsAdapter extends BaseAdapter {

    Context context;
    //data container
    ArrayList<NotificationsSettingsList> notificationslist;
    LayoutInflater inflater;

    //constructor


    public NotificationSettingsAdapter(Context context, ArrayList<NotificationsSettingsList> notificationslist) {
        super();
        this.context = context;
        this.notificationslist = notificationslist;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return notificationslist.size();
    }

    @Override
    public Object getItem(int position) {
        return notificationslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        NotificationsHandler handler = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_notification_settings, null);
            handler = new NotificationsHandler();
            handler.notification_title = (TextView) convertView.findViewById(R.id.notifications_title);
            handler.notification_details = (TextView) convertView.findViewById(R.id.notifications_details);
            convertView.setTag(handler);
        }else
            handler = (NotificationsHandler) convertView.getTag();

        //fill the view elements
        handler.notification_title.setText(notificationslist.get(position).getNotificationTitle());
        handler.notification_details.setText(notificationslist.get(position).getNotificationDetails());

        return convertView;
    }

    //create static class to handle Notifications layout
    static class NotificationsHandler{
        TextView notification_title, notification_details;
    }
}
