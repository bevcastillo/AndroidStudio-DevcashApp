package com.example.devcash;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeesFragment extends Fragment {


    public EmployeesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employees, container, false);

        //add floating action button
        FloatingActionButton emp_fab = view.findViewById(R.id.addemp_fab);
        emp_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // when add fab is pressed, go to add product activity
                Intent addprod = new Intent(getActivity(), AddEmployeeActivity.class);
                startActivity(addprod);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Employees");
    }
}
