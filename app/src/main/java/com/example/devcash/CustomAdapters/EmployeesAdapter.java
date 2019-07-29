package com.example.devcash.CustomAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.devcash.Model.EmployeeList;
import com.example.devcash.Object.Employee;
import com.example.devcash.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.ViewHolder> {
    Context context;
    ArrayList<Employee> employeeArrayList;

    public EmployeesAdapter(Context c, ArrayList<Employee> employees) {
        context = c;
        employeeArrayList = employees;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customlayout_employee,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return employeeArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView emplname, empfname, empusername, emptask;
        ImageView empImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            empImage = (ImageView) itemView.findViewById(R.id.imgemp_image);
            emplname = (TextView) itemView.findViewById(R.id.txtemp_lname);
            empfname = (TextView) itemView.findViewById(R.id.txtemp_fname);
            emptask = (TextView) itemView.findViewById(R.id.txtemp_task);
        }
    }

}
