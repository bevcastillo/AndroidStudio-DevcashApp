package com.example.devcash.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeesFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    DatabaseReference dbreference;
    DatabaseReference employeedfirebasereference;
    DatabaseReference ownerdbreference;
    FirebaseDatabase firebaseDatabase;
    ProgressBar empprogress;
    LinearLayout emptylayout;
    Spinner spinneremptask;
    String selectedtask;
    EditText employeesearch;
    Button btnsearch;

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
        empprogress = (ProgressBar) view.findViewById(R.id.emp_progressbar);
        emptylayout = (LinearLayout) view.findViewById(R.id.layout_emptyemp);

        spinneremptask = (Spinner) view.findViewById(R.id.spinner_empassignedtask);
        employeesearch = (EditText) view.findViewById(R.id.employeesarchtext);
        btnsearch = (Button) view.findViewById(R.id.employeesearchbtn);

        btnsearch.setOnClickListener(this);

        spinneremptask.setOnItemSelectedListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbreference = firebaseDatabase.getReference("/datadevcash");
        ownerdbreference = firebaseDatabase.getReference("datadevcash/owner");
        employeedfirebasereference = firebaseDatabase.getReference("/datadevcash/employees");


        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.custom_spinner_item,
                getResources().getStringArray(R.array.task_employee));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinneremptask.setAdapter(myAdapter);

        spinneremptask.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(),
//                        spinneremptask.getSelectedItem().toString(),
//                        Toast.LENGTH_SHORT).show();
                if (spinneremptask.getSelectedItem().toString().equals("All Tasks")){
                    dispAllTask();
                }else if (spinneremptask.getSelectedItem().toString().equals("Cashiering")){

                }else if (spinneremptask.getSelectedItem().toString().equals("Inventory Monitoring")){

                }else if (spinneremptask.getSelectedItem().toString().equals("Unassign Employee")){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        getActivity().setTitle("Employee");
        FloatingActionButton emp_fab = view.findViewById(R.id.addemp_fab);
        emp_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addprod = new Intent(getActivity(), AddEmployeeActivity.class);
                startActivity(addprod);

            }
        });

        //handles search
        employeesearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    public void dispAllTask(){
        SharedPreferences shared = getActivity().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        String key = ds.getKey();
                        dbreference.child("owner/"+key+"/business/employee").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                emplist = new ArrayList<>();
                                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                    Employee employee = dataSnapshot1.getValue(Employee.class);
                                    Employeelistdata employeelistdata = new Employeelistdata();
                                    String lname = employee.getEmp_lname();
                                    String fname = employee.getEmp_fname();
                                    String task = employee.getEmp_task();
                                    String uname = employee.getAccount().getAcct_uname();
                                    String email = employee.getAccount().getAcct_email();
                                    employeelistdata.setEmplname(lname);
                                    employeelistdata.setEmpfname(fname);
                                    employeelistdata.setAcctuname(uname);
                                    employeelistdata.setAcctemail(email);
                                    employeelistdata.setEmptask(task);
                                    emplist.add(employeelistdata);
                                }

                                EmployeesAdapter employeesAdapter = new EmployeesAdapter(emplist);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                emprecyclerview.setLayoutManager(mLayoutManager);
                                emprecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
                                emprecyclerview.setItemAnimator(new DefaultItemAnimator());
                                emprecyclerview.setAdapter(employeesAdapter);
                                employeesAdapter.notifyDataSetChanged();

                                empprogress.setVisibility(View.GONE);

                                if(emplist.isEmpty()){
                                    emptylayout.setVisibility(View.VISIBLE);
                                }else{
                                    emptylayout.setVisibility(View.GONE);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int sid = parent.getId();

        switch (sid){
            case R.id.spinner_empassignedtask:
                selectedtask = spinneremptask.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.employeesearchbtn:
                searchForEmployee();
                break;
        }
    }

    private void searchForEmployee() {
        final String inputText = employeesearch.getText().toString();

        SharedPreferences shared = getActivity().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        String ownerKey = dataSnapshot1.getKey();

                        ownerdbreference.child(ownerKey+"/business/employee").orderByChild("emp_fname").equalTo(inputText).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        emplist = new ArrayList<>();
                                        Employee employee = dataSnapshot2.getValue(Employee.class);
                                        Employeelistdata employeelistdata = new Employeelistdata();
                                        String lname = employee.getEmp_lname();
                                        String fname = employee.getEmp_fname();
                                        String task = employee.getEmp_task();
                                        String uname = employee.getAccount().getAcct_uname();
                                        String email = employee.getAccount().getAcct_email();
                                        employeelistdata.setEmplname(lname);
                                        employeelistdata.setEmpfname(fname);
                                        employeelistdata.setAcctuname(uname);
                                        employeelistdata.setAcctemail(email);
                                        employeelistdata.setEmptask(task);
                                        emplist.add(employeelistdata);
                                    }
                                    EmployeesAdapter employeesAdapter = new EmployeesAdapter(emplist);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                    emprecyclerview.setLayoutManager(mLayoutManager);
                                    emprecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
                                    emprecyclerview.setItemAnimator(new DefaultItemAnimator());
                                    emprecyclerview.setAdapter(employeesAdapter);
                                    employeesAdapter.notifyDataSetChanged();

                                    empprogress.setVisibility(View.GONE);

                                    if(emplist.isEmpty()){
                                        emptylayout.setVisibility(View.VISIBLE);
                                    }else{
                                        emptylayout.setVisibility(View.GONE);

                                    }
                                }else {
                                    Toast.makeText(getActivity(), "does not exist", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
