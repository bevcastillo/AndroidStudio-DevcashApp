package com.example.devcash.EDIT_UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.Object.Account;
import com.example.devcash.Object.Employee;
import com.example.devcash.R;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class EditEmployee extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private DatabaseReference databaseReference;
    private DatabaseReference ownerdbreference;
    private FirebaseDatabase firebaseDatabase;

    private TextInputEditText emplname, empfname;
    private LinearLayout layoutdelete;
    private Spinner emptask;
    private String lastname, firstname, selectedtask, gender, phone, empusername;
    private int pos;

    private TextView txtempname, txtempgender, txtempphone, txtempusername, txtdeactivate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //
        emplname = (TextInputEditText) findViewById(R.id.textinput_editemplname);
        empfname = (TextInputEditText) findViewById(R.id.textinput_editempfname);
        layoutdelete = (LinearLayout) findViewById(R.id.layout_delemployee);
        emptask = (Spinner) findViewById(R.id.spinner_emptask);

        //
        txtempname = (TextView) findViewById(R.id.editempname);
        txtempgender = (TextView) findViewById(R.id.editempgender);
        txtempphone = (TextView) findViewById(R.id.editempnumber);
        txtempusername = (TextView) findViewById(R.id.editempusername);
        txtdeactivate = (TextView) findViewById(R.id.txtempdeactivate);



        layoutdelete.setOnClickListener(this);
        emptask.setOnItemSelectedListener(this);
        txtdeactivate.setOnClickListener(this);

        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("/datadevcash");
        ownerdbreference = firebaseDatabase.getReference("/datadevcash/owner");
    }

    @Override
    protected void onStart() {
        super.onStart();

        String [] tasklist = getResources().getStringArray(R.array.task_assign);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null){
            lastname = bundle.getString("employeelname");
            firstname = bundle.getString("employeefname");
            selectedtask = bundle.getString("employeetask");
            gender = bundle.getString("employeegender");
            phone = bundle.getString("employeephone");
            empusername = bundle.getString("employeeusername");


            for (int i=0; i<tasklist.length; i++){
                if(tasklist[i].equals(selectedtask)){
                    pos = i;
                }

                txtempname.setText(lastname+", "+firstname);
                emptask.setSelection(pos);
                txtempusername.setText(empusername);
                showEmpDetails();
            }

        }
    }

    public void showEmpDetails(){
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        final String ownerkey = dataSnapshot1.getKey();
                        ownerdbreference.child(ownerkey+"/business/employee").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        ownerdbreference.child(ownerkey+"/business/employee")
                                                .orderByChild("emp_username").equalTo(empusername).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()){
                                                    for(DataSnapshot dataSnapshot3: dataSnapshot.getChildren()){
                                                        String empkey = dataSnapshot3.getKey();
                                                        Employee employee = dataSnapshot3.getValue(Employee.class);
                                                        gender = employee.getEmp_gender();
                                                        phone = employee.getEmp_phone();

                                                        txtempgender.setText(gender);
                                                        txtempphone.setText(phone);
//                                                        if(employee.getEmp_username().equals(txtempusername.toString())){
//                                                            ownerdbreference.child(ownerkey+"/business/employee").child(empkey+"/emp_task").setValue(selectedtask);
//                                                            ownerdbreference.child(ownerkey+"/business/employee").child(empkey+"/emp_task").setValue(selectedtask);
//                                                        }
                                                    }
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Unsaved changes");
        builder.setMessage("Are you sure you want to leave without saving changes?");
        builder.setPositiveButton("LEAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }else if(id == R.id.action_save){
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.layout_delemployee:
                break;
            case R.id.txtempdeactivate:
                enterPasswDialog();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int sid = parent.getId();
            switch (sid){
                case R.id.spinner_emptask:
                    selectedtask = emptask.getItemAtPosition(position).toString();
                    break;
            }
    }

    public void enterPasswDialog(){
        final AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_password, null);
        dialog.setView(dialogView);

        final TextInputEditText editconfpassw = (TextInputEditText) dialogView.findViewById(R.id.confirmpassword);
        final Button btnconfirm = (Button) dialogView.findViewById(R.id.btnconfirm);

        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtdeactivate.getText().toString().equals("DEACTIVATE EMPLOYEE")){

                    SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
                    final String username = (shared.getString("owner_username", ""));

                    ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                    final String ownerkey = dataSnapshot1.getKey();
                                    ownerdbreference.child(ownerkey+"/business/account").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                                    String acctkey = dataSnapshot2.getKey();
                                                    ownerdbreference.child(ownerkey+"/business/account")
                                                            .orderByChild("/acct_uname").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if(dataSnapshot.exists()){
                                                                for (DataSnapshot dataSnapshot3: dataSnapshot.getChildren()){
                                                                    Account account = dataSnapshot3.getValue(Account.class);
                                                                    String type = account.getAcct_type();
                                                                    String passw = account.getAcct_passw();
                                                                    if(passw.equals(editconfpassw.getText().toString()) && type.equals("Owner")){
                                                                        deactivateDialog();
                                                                    }
                                                                }
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
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else{
                    activateDialog();
                }

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        }
        });
        AlertDialog builder = dialog.create();
        builder.show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void deactivateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Sure to deactivate?");
        builder.setMessage("This employee account will be deactivated. "+txtempname.getText().toString()+" will not be able to login after this deactivation. You can reactivate at any time.");
        builder.setPositiveButton("DEACTIVATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(EditEmployee.this, "Account has been deactivated.", Toast.LENGTH_SHORT).show();
                txtdeactivate.setText("ACTIVATE EMPLOYEE");
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void activateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Sure to Activate?");
        builder.setMessage("This employee account will be activated. "+txtempname.getText().toString()+" will be able to login after this activation.");
        builder.setPositiveButton("ACTIVATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(EditEmployee.this, "Account has been activated.", Toast.LENGTH_SHORT).show();
                txtdeactivate.setText("DEACTIVATE EMPLOYEE");
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
