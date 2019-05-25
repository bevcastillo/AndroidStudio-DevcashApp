package com.example.devcash.CustomAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.devcash.Lists.EmployeeList;
import com.example.devcash.R;

import java.util.ArrayList;

public class EmployeesAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<EmployeeList> employeeListArrayList;


    public EmployeesAdapter(Context context, ArrayList<EmployeeList> employeeListArrayList) {
        this.context = context;
        this.employeeListArrayList = employeeListArrayList;
    }

    @Override
    public int getCount() {
        return employeeListArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return employeeListArrayList.get(position);
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
            convertView = inflater.inflate(R.layout.employee_customlayout, null, true);

            holder.emp_img = (ImageView) convertView.findViewById(R.id.imgemp_image);
            holder.text_emplname = (TextView) convertView.findViewById(R.id.txtemp_lname);
            holder.text_empfname = (TextView) convertView.findViewById(R.id.txtemp_fname);
            holder.text_emptask = (TextView) convertView.findViewById(R.id.txtemp_task);

            convertView.setTag(holder);
        }else{
            //the getTag returns the viewHolder object set as a tag
            holder = (ViewHolder)convertView.getTag();
        }

        //display the data into the EmployeeListFragment
        holder.emp_img.setImageResource(employeeListArrayList.get(position).getEmp_img());
        holder.text_emplname.setText(employeeListArrayList.get(position).getEmp_lname());
        holder.text_empfname.setText(employeeListArrayList.get(position).getEmp_fname());

        return convertView;
    }

    private class ViewHolder{
        protected ImageView emp_img;
        protected TextView text_emplname, text_empfname, text_emptask;
    }
}
