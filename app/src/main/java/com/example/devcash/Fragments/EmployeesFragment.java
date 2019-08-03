package com.example.devcash.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.devcash.ADD_UI.AddEmployeeActivity;
import com.example.devcash.ADD_UI.AddProductActivity;
import com.example.devcash.CustomAdapters.CategoryAdapter;
import com.example.devcash.CustomAdapters.EmployeesAdapter;
import com.example.devcash.Object.Categorylistdata;
import com.example.devcash.Object.Employee;
import com.example.devcash.Object.Employeelistdata;
import com.example.devcash.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeesFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    DatabaseReference dbreference;
    DatabaseReference employeedfirebasereference;
    FirebaseDatabase firebaseDatabase;

    RecyclerView emprecyclerview;

    List<Employeelistdata> emplist;
    ArrayList<Employee> employeeArrayList;


    public EmployeesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employees, container, false);

        emprecyclerview = (RecyclerView) view.findViewById(R.id.emplist_recyclerview);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbreference = firebaseDatabase.getReference("/datadevcash");
        employeedfirebasereference = firebaseDatabase.getReference("/datadevcash/employees");

        employeedfirebasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                emplist = new ArrayList<>();
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Employee employee = dataSnapshot1.getValue(Employee.class);
                    Employeelistdata employeelistdata = new Employeelistdata();
                    String lname = employee.getEmp_lname();
                    String fname = employee.getEmp_fname();
                    String task = employee.getEmp_task();
                    employeelistdata.setEmplname(lname);
                    employeelistdata.setEmpfname(fname);
                    employeelistdata.setEmptask(task);
                    emplist.add(employeelistdata);
                }

                EmployeesAdapter employeesAdapter = new EmployeesAdapter(emplist);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                emprecyclerview.setLayoutManager(mLayoutManager);
                emprecyclerview.setItemAnimator(new DefaultItemAnimator());
                emprecyclerview.setAdapter(employeesAdapter);
                employeesAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Something is wrong, please try that again.", Toast.LENGTH_SHORT).show();
            }
        });


        //
        getActivity().setTitle("Employee");

        //add floating action button
        FloatingActionButton emp_fab = view.findViewById(R.id.addemp_fab);
        emp_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                // when add fab is pressed, go to add product activity
                Intent addprod = new Intent(getActivity(), AddEmployeeActivity.class);
                startActivity(addprod);

            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.searchmenu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search..");

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }
}
