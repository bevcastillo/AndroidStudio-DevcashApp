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
import com.example.devcash.Object.Employeelistdata;
import com.example.devcash.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.ViewHolder> {

    List<Employeelistdata> list;

    public EmployeesAdapter(List<Employeelistdata> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customlayout_employee,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Employeelistdata data = list.get(i);
        viewHolder.emplname.setText(data.getEmplname());
        viewHolder.empfname.setText(data.getEmpfname());
        viewHolder.emptask.setText(data.getEmptask());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView emplname, empfname, emptask;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            emplname = (TextView) itemView.findViewById(R.id.txtemp_lname);
            empfname = (TextView) itemView.findViewById(R.id.txtemp_fname);
            emptask = (TextView) itemView.findViewById(R.id.txtemp_task);
        }
    }

}
