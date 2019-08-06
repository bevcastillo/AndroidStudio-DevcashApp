package com.example.devcash.ADD_UI;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.Database.DatabaseHelper;
import com.example.devcash.Object.Account;
import com.example.devcash.Object.Employee;
import com.example.devcash.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.UUID;

import org.w3c.dom.Text;

import java.util.Calendar;

public class AddEmployeeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebasedb;
    private DatabaseReference accountFirebaseReference;
    TextInputEditText empLname, empFname, empEmail, empPhone, empbdate, empuname, emppassw, empconfpass;
    EditText acctstatus, accttype;
    private Uri empimageUri;
    ImageView empimage;
    LinearLayout takephoto, choosephoto;
    Spinner emptask;
    String selectedemptask, selectedgender;
    DatePickerDialog bdatePickerDia;
    RadioGroup gender;
    RadioButton genderbtn;
    private String EmployeeId, AccountId;

    private static final int PICK_IMAGE = 100;

    private static final String TAG = "AddEmployeeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //
        empimage = (ImageView) findViewById(R.id.emp_photo) ;
        takephoto = (LinearLayout) findViewById(R.id.emptakephoto);
        choosephoto = (LinearLayout) findViewById(R.id.empchoosephoto);
        empLname = (TextInputEditText) findViewById(R.id.text_input_emp_lname);
        empFname = (TextInputEditText) findViewById(R.id.text_input_emp_fname);
        empuname = (TextInputEditText) findViewById(R.id.textinput_empuname);
        empEmail = (TextInputEditText) findViewById(R.id.textinput_empemail);
        empPhone = (TextInputEditText) findViewById(R.id.text_input_emp_pnumber);
        empbdate = (TextInputEditText) findViewById(R.id.text_input_dob);
        emptask = (Spinner) findViewById(R.id.spinner_emptask);
        gender = (RadioGroup) findViewById(R.id.radiogroup_gender);
        acctstatus = (EditText) findViewById(R.id.acct_status);
        accttype = (EditText) findViewById(R.id.acct_type);
        //

        emppassw = (TextInputEditText) findViewById(R.id.textinput_emppassw);
        empconfpass = (TextInputEditText) findViewById(R.id.textinput_empconfpassw);

        //add listeners to the textviews
        empbdate.setOnClickListener(this);
        takephoto.setOnClickListener(this);
        choosephoto.setOnClickListener(this);
        emptask.setOnItemSelectedListener(this);

        firebasedb = FirebaseDatabase.getInstance();
        databaseReference = firebasedb.getReference("/datadevcash");
        accountFirebaseReference = firebasedb.getReference("/datadevcash/account");
        EmployeeId = databaseReference.push().getKey();
        AccountId = databaseReference.push().getKey();

    }

    public void addRadioGroupListener(){
        int radioid = gender.getCheckedRadioButtonId();
        genderbtn=(RadioButton)findViewById(radioid);
        selectedgender=genderbtn.getText().toString();
    }

    public void addEmployee(final String emp_lname, final String emp_fname, final String emp_task, final String emp_gender, final String emp_bdate, final String emp_phone, final String accountEmail){
        Log.d(TAG,"addEmployee()");

        accountFirebaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String acctEmail;
                String acctUname;
                String acctPassw;
                String acctType;
                String acctStatus;

                Employee employee = new Employee(emp_lname, emp_fname, emp_task, emp_gender, emp_bdate, emp_phone);

                // create an account object
                Account acct = new Account();

               for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                   if (childSnapshot.child("acct_email").getValue().equals(accountEmail)) {
                       acctEmail = childSnapshot.child("acct_email").getValue().toString();
                       acctUname = childSnapshot.child("acct_uname").getValue().toString();
                       acctPassw = childSnapshot.child("acct_passw").getValue().toString();
                       acctType = childSnapshot.child("acct_type").getValue().toString();
                       acctStatus = childSnapshot.child("acct_status").getValue().toString();

                       acct.setAcct_email(acctEmail);
                       acct.setAcct_uname(acctUname);
                       acct.setAcct_passw(acctPassw);
                       acct.setAcct_type(acctType);
                       acct.setAcct_status(acctStatus);
                       employee.setAccount(acct);

                       databaseReference.child("employees").child(EmployeeId).setValue(employee);
                   }
               }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void addAccount(String acct_uname, final String acct_email, final String acct_passw, final String acct_type, final String acct_status){
        final Account account = new Account(acct_uname, acct_email, acct_passw, acct_type, acct_status);
        databaseReference.child("account").child(AccountId).setValue(account);
        databaseReference.child("employees").child(EmployeeId).child("account").child(AccountId).setValue(account);

    }

    public void insertEmployee(){
        Log.d(TAG,"insertEmployee()");
        String lname = empLname.getText().toString();
        String fname = empFname.getText().toString();
        String bdate = empbdate.getText().toString();
        String textphone = empPhone.getText().toString();
        String email = empEmail.getText().toString();
        acctstatus.setText("Active");
        accttype.setText("Employee");
        //
        addEmployee(lname, fname, selectedemptask, selectedgender, bdate, textphone, email);
        Toast.makeText(getApplicationContext(), "Employee Successfully Added!", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void insertAccount(){
//        String uname = empuname.getText().toString();
        String email = empEmail.getText().toString();
//        String passw = emppassw.getText().toString();
        acctstatus.setText("Active");
        accttype.setText("Employee");
        String status = acctstatus.getText().toString();
        String type = accttype.getText().toString();
        String lname = empLname.getText().toString();
        String fname = empFname.getText().toString();
        String newfname = fname.substring(0,1).toLowerCase();
        String newuname = newfname+lname.toLowerCase();
        empuname.setText(newuname);
        String uname = empuname.getText().toString();
        String passw = UUID.randomUUID().toString().substring(0,5);
        emppassw.setText(passw);
        String newpassw = emppassw.getText().toString();
        //
//        addAccount(uname, email, passw, type, status);
        addAccount(uname, email, newpassw, type, status);

    }

    public boolean checkPassw(){
        String passw = emppassw.getText().toString();
        String confpass = empconfpass.getText().toString();
        if(!passw.equals(confpass)){
            errorDialog();
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.savemenu, menu);
        return true;
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

//    public void errorDialog(){
//        AlertDialog.Builder errorbuilder = new AlertDialog.Builder(this);
//
//        errorbuilder.setMessage("Password did not match!");
//        errorbuilder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//    }

    public void errorDialog(){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);

        builder.setMessage("Password did not match");
        builder.setNeutralButton("OKAY", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }else if(id == R.id.action_save){
            addRadioGroupListener();
            if(checkPassw()){
                insertAccount();
                insertEmployee();
            }else{
                errorDialog();
            }
        }
        return super.onOptionsItemSelected(item);

    }


    // handles camera clicks
    @Override
    public void onClick(View v) {
        int sid = v.getId();

        switch (sid){
            case R.id.empchoosephoto:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
                break;
            case R.id.emptakephoto:
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, 0);
                break;
            case R.id.text_input_dob:
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); //current year
                int mMonth = c.get(Calendar.MONTH); //current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); //current date of the month

                //date picker dialog
                bdatePickerDia = new DatePickerDialog(AddEmployeeActivity.this,
                         new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //set day month, month and year value in the textinputedittext
                                empbdate.setText(dayOfMonth + "/"
                                                + (month + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                            bdatePickerDia.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode!=0){
            if(data!=null){
                empimageUri = data.getData();
                empimage.setImageURI(empimageUri);
            }
        }else{
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            empimage.setImageBitmap(bitmap);
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int sid = parent.getId();
            switch (sid){
                case R.id.spinner_emptask:
                    selectedemptask = this.emptask.getItemAtPosition(position).toString();
                    break;
            }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
