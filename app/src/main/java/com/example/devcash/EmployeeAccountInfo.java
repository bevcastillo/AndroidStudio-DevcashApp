package com.example.devcash;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.Object.Account;
import com.example.devcash.Object.Employee;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EmployeeAccountInfo extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference dbreference;
    private DatabaseReference ownerdbreference;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference;
    private StorageTask uploadTask;

    private TextView txtname, txtusername, txttask;
    private TextInputEditText editemail, editphone, editpassword;
    private TextInputLayout empPasswordLayout, empPhoneLayout;
    private RadioGroup radioGroupgender;
    private RadioButton radioButtongender, radioButtonMale, radioButtonFemale;
    String employeename, employeeusername, employeephone, selectedgender, employeetask, employeeacctpassw, employeemeail, employeeImageUrl;
    Uri imageUri;
    ImageView imageView;

    private static final int PICK_IMAGE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_account_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //
        txtname = (TextView) findViewById(R.id.txtemployeename);
        txttask = (TextView) findViewById(R.id.txtemployeetask);
        txtusername = (TextView) findViewById(R.id.txtemployeeusername);
        editemail = (TextInputEditText) findViewById(R.id.edittext_empemail);
        editphone = (TextInputEditText) findViewById(R.id.edittext_empphone);
        editpassword = (TextInputEditText) findViewById(R.id.edittext_empassword);
        radioGroupgender = (RadioGroup) findViewById(R.id.radiogroup_empgender);
        radioButtonMale = (RadioButton) findViewById(R.id.radioempmale);
        radioButtonFemale = (RadioButton) findViewById(R.id.radioempfemale);
        empPasswordLayout = (TextInputLayout) findViewById(R.id.inputlayout_empassword);
        empPhoneLayout = (TextInputLayout) findViewById(R.id.employeePhone_layout);
        imageView = (ImageView) findViewById(R.id.emp_photo);


        //
        imageView.setOnClickListener(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbreference = firebaseDatabase.getReference("/datadevcash");
        ownerdbreference = firebaseDatabase.getReference("/datadevcash/owner");
    }

    @Override
    protected void onStart() {
        super.onStart();

        showEmpDetails(); //show employee details
    }

    public void addRadioGroupListener(){
        int radioid = radioGroupgender.getCheckedRadioButtonId();
        radioButtongender = (RadioButton) findViewById(radioid);
        selectedgender = radioButtongender.getText().toString();
    }

    private boolean validateEmailPassw(){
        String empPassword = editpassword.getText().toString().trim();
        String empPhone = editphone.getText().toString().trim();
        boolean ok = true;

        if(empPassword.isEmpty()){
            empPasswordLayout.setError("Passwords can not be empty!");
            ok = false;
            if (empPhone.length() > 11 && empPhone.length() < 11){
                empPhoneLayout.setError("Invalid phone number");
                ok = false;
            }else if (empPhone.equals("")){
                empPhoneLayout.setError(null);
                ok = true;
            }else {
                empPhoneLayout.setError(null);
                ok = true;
            }
        }else {
            empPasswordLayout.setError(null);
            ok = true;
        }

        return ok;
    }

    public void showEmpDetails(){
        SharedPreferences empPref = getApplicationContext().getSharedPreferences("EmpPref", MODE_PRIVATE);
        final String empusername = (empPref.getString("emp_username", ""));

        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("/business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        final String ownerkey = dataSnapshot1.getKey();
                        ownerdbreference.child(ownerkey+"/business/employee")
                                .orderByChild("emp_username").equalTo(empusername).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        final String empkey = dataSnapshot2.getKey();
                                        Employee employee = dataSnapshot2.getValue(Employee.class);

                                        employeename = employee.getEmp_fname()+" "+employee.getEmp_lname();
                                        employeeusername = employee.getEmp_username();
                                        employeephone = employee.getEmp_phone();
                                        employeetask = employee.getEmp_task();
                                        selectedgender = employee.getEmp_gender();
                                        employeeImageUrl = employee.getEmp_imageUrl();

                                        txtname.setText(employeename);
                                        txttask.setText(employeetask);
                                        txtusername.setText(employeeusername);
                                        editphone.setText(employeephone);

                                        Picasso.with(EmployeeAccountInfo.this).load(employeeImageUrl).into(imageView);

                                        if (selectedgender.equals(radioButtonMale.getText())){
                                            radioButtonMale.setChecked(true);
                                            radioButtonFemale.setChecked(false);
                                        } else{
                                            radioButtonFemale.setChecked(true);
                                            radioButtonMale.setChecked(false);
                                        }

                                        ownerdbreference.child(ownerkey+"/business/employee")
                                                .orderByChild("/account/acct_uname")
                                                .equalTo(empusername).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.exists()){
                                                    for (DataSnapshot dataSnapshot3: dataSnapshot.getChildren()){
                                                        Employee employee1 = dataSnapshot3.getValue(Employee.class);

                                                        employeeacctpassw = employee1.account.getAcct_passw();
                                                        employeemeail = employee1.account.getAcct_email();
                                                        editpassword.setText(employeeacctpassw);
                                                        editemail.setText(employeemeail);
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

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    public void updateEmployee(){
        SharedPreferences empPref = getApplicationContext().getSharedPreferences("EmpPref", MODE_PRIVATE);
        final String empusername = (empPref.getString("emp_username", ""));

        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

//        final StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));

        ownerdbreference.orderByChild("/business/owner_username")
                .equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        final String ownerkey = dataSnapshot1.getKey();
                        ownerdbreference.child(ownerkey+"/business/account")
                                .orderByChild("acct_uname").equalTo(empusername).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        String acctkey = dataSnapshot2.getKey();

                                        //updating from account
                                        ownerdbreference.child(ownerkey+"/business/account").child(acctkey+"/acct_passw").setValue(editpassword.getText().toString());
                                        ownerdbreference.child(ownerkey+"/business/account").child(acctkey+"/acct_email").setValue(editemail.getText().toString());

                                        //
                                        ownerdbreference.child(ownerkey+"/business/employee").orderByChild("account/acct_uname").equalTo(empusername).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.exists()){
                                                    for(DataSnapshot dataSnapshot3: dataSnapshot.getChildren()){
                                                        final String empkey = dataSnapshot3.getKey();
                                                        Employee employee = dataSnapshot3.getValue(Employee.class);
                                                        selectedgender = employee.getEmp_gender();

//                                                        uploadTask = fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                                            @Override
//                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                                                    @Override
//                                                                    public void onSuccess(Uri uri) {
//                                                                        ownerdbreference.child(ownerkey+"/business/employee").child(empkey+"/employee_image").setValue(uri.toString());
//                                                                    }
//                                                                });
//                                                            }
//                                                        });

                                                        ownerdbreference.child(ownerkey+"/business/employee").child(empkey+"/account/acct_passw").setValue(editpassword.getText().toString());
                                                        ownerdbreference.child(ownerkey+"/business/employee").child(empkey+"/account/acct_email").setValue(editemail.getText().toString());
                                                        ownerdbreference.child(ownerkey+"/business/employee").child(empkey+"/emp_phone").setValue(editphone.getText().toString());
                                                        addRadioGroupListener();
                                                        ownerdbreference.child(ownerkey+"/business/employee").child(empkey+"/emp_gender").setValue(selectedgender);

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
                        Toast.makeText(EmployeeAccountInfo.this, "Account has been successfully updated.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.home:
                onBackPressed();
                return true;
            case R.id.action_save:
                if (validateEmailPassw()){
                    updateEmployee();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.emp_photo:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }else{
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
    }
}
