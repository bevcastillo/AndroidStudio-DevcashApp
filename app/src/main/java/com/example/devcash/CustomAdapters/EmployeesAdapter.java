package com.example.devcash.CustomAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.EDIT_UI.EditEmployee;
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
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        final View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customlayout_employee, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.emplname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emplastname =  list.get(viewHolder.getAdapterPosition()).getEmplname();
                String empfirstname = list.get(viewHolder.getAdapterPosition()).getEmpfname();

                Intent intent = new Intent(view.getContext(), EditEmployee.class);
                intent.putExtra("employeelname", emplastname);
                intent.putExtra("employeefname", empfirstname);
                v.getContext().startActivity(intent);
            }
        });


        return viewHolder;



//        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customlayout_employee,viewGroup,false);
//        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Employeelistdata data = list.get(i);
        viewHolder.emplname.setText(data.getEmplname());
        viewHolder.empfname.setText(data.getEmpfname());
        viewHolder.emptask.setText(data.getEmptask());
        viewHolder.acctuname.setText(data.getAcctuname());
        viewHolder.acctemail.setText(data.getAcctemail());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView emplname, empfname, emptask, acctuname, acctemail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            emplname = (TextView) itemView.findViewById(R.id.txtemp_lname);
            empfname = (TextView) itemView.findViewById(R.id.txtemp_fname);
            emptask = (TextView) itemView.findViewById(R.id.txtemp_task);
            acctemail = (TextView) itemView.findViewById(R.id.txtemp_email);
            acctuname = (TextView) itemView.findViewById(R.id.txtemp_username);
        }
    }
}
