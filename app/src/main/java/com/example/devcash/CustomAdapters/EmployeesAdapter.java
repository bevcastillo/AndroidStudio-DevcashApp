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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.EDIT_UI.EditEmployee;
import com.example.devcash.Model.EmployeeList;
import com.example.devcash.Object.Employee;
import com.example.devcash.Object.Employeelistdata;
import com.example.devcash.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.ViewHolder> {

//    Context context;
    List<Employeelistdata> list;
    List<Employee> employeeList;

    public EmployeesAdapter(List<Employeelistdata> list) {
        this.list = list;
    }


//    public EmployeesAdapter(Context context, List<Employeelistdata> list) {
//        this.context = context;
//        this.list = list;
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        final View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customlayout_employee, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emplastname =  list.get(viewHolder.getAdapterPosition()).getEmplname();
                String empfirstname = list.get(viewHolder.getAdapterPosition()).getEmpfname();
                String emptask = list.get(viewHolder.getAdapterPosition()).getEmptask();
                String empphone = list.get(viewHolder.getAdapterPosition()).getEmp_phone();
                String empgender = list.get(viewHolder.getAdapterPosition()).getEmp_gender();
                String empuser = list.get(viewHolder.getAdapterPosition()).getAcctuname();
                String empstatus = list.get(viewHolder.getAdapterPosition()).getAcctstatus();

                Intent intent = new Intent(view.getContext(), EditEmployee.class);
                intent.putExtra("employeelname", emplastname);
                intent.putExtra("employeefname", empfirstname);
                intent.putExtra("employeetask", emptask);
                intent.putExtra("employeephone", empphone);
                intent.putExtra("employeegender", empgender);
                intent.putExtra("employeeusername", empuser);
                intent.putExtra("employeestatus",empstatus);
                v.getContext().startActivity(intent);
            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Employeelistdata data = list.get(i);
        viewHolder.emplname.setText(data.getEmplname());
        viewHolder.empfname.setText(data.getEmpfname());
        viewHolder.emptask.setText(data.getEmptask());
        viewHolder.acctuname.setText(data.getAcctuname());
        viewHolder.acctemail.setText(data.getAcctemail());
//        viewHolder.empImage.setImageURI(data.getEmp_imageUrl());
//        viewHolder.acct_status.setText("Test");
//        Picasso.with(context)
//                .load(data.getEmp_imageUrl())
//                .into(viewHolder.empImage);
//        Picasso.with(context).load(data.getEmp_imageUrl())
//                .into(viewHolder.empImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView emplname, empfname, emptask, acctuname, acctemail, acct_status;
        ImageView empImage;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            emplname = (TextView) itemView.findViewById(R.id.txtemp_lname);
            empfname = (TextView) itemView.findViewById(R.id.txtemp_fname);
            emptask = (TextView) itemView.findViewById(R.id.txtemp_task);
            acctemail = (TextView) itemView.findViewById(R.id.txtemp_email);
            acctuname = (TextView) itemView.findViewById(R.id.txtemp_username);
            acct_status = (TextView) itemView.findViewById(R.id.txtempstatus);
            layout = (LinearLayout) itemView.findViewById(R.id.employeeLayout);
            empImage = (ImageView) itemView.findViewById(R.id.imgemp_image);
        }
    }
}
