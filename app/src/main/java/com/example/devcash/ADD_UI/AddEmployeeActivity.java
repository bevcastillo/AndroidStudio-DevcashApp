package com.example.devcash.ADD_UI;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
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
import com.example.devcash.GettingStarted;
import com.example.devcash.MyUtility;
import com.example.devcash.Object.Account;
import com.example.devcash.Object.Employee;
import com.example.devcash.R;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.UUID;

import org.w3c.dom.Text;

import java.util.Calendar;

public class AddEmployeeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebasedb;
    private StorageReference storageReference;
    private StorageTask uploadTask;

    private DatabaseReference accountFirebaseReference;
    private DatabaseReference ownerdbreference;
    TextInputEditText empLname, empFname, empEmail, empPhone, empbdate, empuname, emppassw, empconfpass, empaddr;
    TextInputLayout empLname_layout, empFname_layout;
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
        empaddr = (TextInputEditText) findViewById(R.id.textinp_empaddress);
        emptask = (Spinner) findViewById(R.id.spinner_emptask);
        gender = (RadioGroup) findViewById(R.id.radiogroup_gender);
        acctstatus = (EditText) findViewById(R.id.acct_status);
        accttype = (EditText) findViewById(R.id.acct_type);
        //

        emppassw = (TextInputEditText) findViewById(R.id.textinput_emppassw);
        empconfpass = (TextInputEditText) findViewById(R.id.textinput_empconfpassw);
        empFname_layout = (TextInputLayout) findViewById(R.id.empfnamLayout);
        empLname_layout = (TextInputLayout) findViewById(R.id.emplnameLayout);

        //add listeners to the textviews
        empbdate.setOnClickListener(this);
        takephoto.setOnClickListener(this);
        choosephoto.setOnClickListener(this);
        emptask.setOnItemSelectedListener(this);

        firebasedb = FirebaseDatabase.getInstance();
        databaseReference = firebasedb.getReference("/datadevcash");
        accountFirebaseReference = firebasedb.getReference("/datadevcash/account");
        ownerdbreference = firebasedb.getReference("/datadevcash/owner");
        storageReference = FirebaseStorage.getInstance().getReference("Employee");//
        EmployeeId = databaseReference.push().getKey();
        AccountId = databaseReference.push().getKey();

    }
    

    public void addRadioGroupListener(){
        int radioid = gender.getCheckedRadioButtonId();
        genderbtn=(RadioButton)findViewById(radioid);
        selectedgender=genderbtn.getText().toString();
    }

    public void addEmployee(final String emp_lname, final String emp_fname, final String emp_task, final String emp_gender,
                             final String emp_phone, final String accountUsername, final String accountEmail, final String accountPassw, final String emp_workfor, final String emp_username){
    Log.i(TAG,"addEmployee()");

    //
        final Employee employee = new Employee(emp_lname, emp_fname, emp_task, emp_gender, emp_phone, emp_workfor, emp_username);
        final Account acct = new Account();

//        // handling image upload of employee avatar.
//        final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
//                + "." + getFileExtension(empimageUri));
//
//        uploadTask = fileReference.putFile(empimageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Handler handler = new Handler();
//
//                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        acct.setAcct_uname(accountUsername);
//                        acct.setAcct_email("");
//                        acct.setAcct_passw(accountPassw);
//                        acct.setAcct_status("Active");
//                        acct.setAcct_type("Employee");
//                        employee.setAccount(acct);
//                        employee.setEmp_imageUrl(uri.toString());
//
//                        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
//                        final String username = (shared.getString("owner_username", ""));
//
//                        SharedPreferences empshared = getSharedPreferences("EmpShared",MODE_PRIVATE);
//                        final SharedPreferences.Editor emp_editor = empshared.edit();
//
//                        SharedPreferences acctshared = getSharedPreferences("AcctShared", MODE_PRIVATE);
//                        final SharedPreferences.Editor acct_editor = acctshared.edit();
//
//                        final Gson gson = new Gson();
//
//                        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                if(dataSnapshot.exists()){
//                                    emp_editor.putString("employee_id", EmployeeId);
//                                    acct_editor.putString("acct_id", AccountId);
//                                    for(DataSnapshot ds: dataSnapshot.getChildren()){
//                                        String key = ds.getKey();
//                                        ownerdbreference.child(key+"/business/account").child(AccountId).setValue(acct);
//                                        String acctJson = gson.toJson(acct);
//                                        acct_editor.putString("employee", acctJson);
//                                        acct_editor.commit();
//
//                                        ownerdbreference.child(key+"/business/employee").child(EmployeeId).setValue(employee);
//                                        String empJson = gson.toJson(employee);
//                                        emp_editor.putString("employee", empJson);
//                                        emp_editor.commit();
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//                });
//            }
//        });
        // end file upload.


        acct.setAcct_uname(accountUsername);
        acct.setAcct_email("");
        acct.setAcct_passw(accountPassw);
        acct.setAcct_status("Active");
        acct.setAcct_type("Employee");
        employee.setAccount(acct);

        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        SharedPreferences empshared = getSharedPreferences("EmpShared",MODE_PRIVATE);
        final SharedPreferences.Editor emp_editor = empshared.edit();

        SharedPreferences acctshared = getSharedPreferences("AcctShared", MODE_PRIVATE);
        final SharedPreferences.Editor acct_editor = acctshared.edit();

        final Gson gson = new Gson();

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    emp_editor.putString("employee_id", EmployeeId);
                    acct_editor.putString("acct_id", AccountId);
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        String key = ds.getKey();
                        ownerdbreference.child(key+"/business/account").child(AccountId).setValue(acct);
                        String acctJson = gson.toJson(acct);
                        acct_editor.putString("employee", acctJson);
                        acct_editor.commit();

                        ownerdbreference.child(key+"/business/employee").child(EmployeeId).setValue(employee);
                        String empJson = gson.toJson(employee);
                        emp_editor.putString("employee", empJson);
                        emp_editor.commit();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    

    public void insertEmployee(){
        Log.d(TAG,"insertEmployee()");
        String lname = empLname.getText().toString();
        String fname = empFname.getText().toString();
        String textphone = empPhone.getText().toString();

        Account account = new Account();
        String newfname = fname.substring(0,1).toLowerCase();
        String newuname = newfname+lname.toLowerCase();
        String generatedNum = Integer.toString(generateRandomNumber(1, 999));
        empuname.setText(newuname+generatedNum);
        final String uname = empuname.getText().toString();
//
        account.setAcct_uname(uname);
        account.setAcct_passw(uname);
        account.setAcct_email("");

        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));


        addEmployee(lname, fname, selectedemptask, selectedgender, textphone, uname, null, uname, username, uname);
        successDialog(account);
//        finish();
    }

    public int generateRandomNumber(int min, int max) {
        int x = (int)(Math.random()*((max-min)+1))+min;
        return x;
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

    public void successDialog(Account account){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.customdia_accountdetails,null);
        builder.setView(dialogView);

        final TextView fullname = (TextView) dialogView.findViewById(R.id.employeename);
        final TextView username = (TextView) dialogView.findViewById(R.id.employeeusername);
        final TextView password = (TextView) dialogView.findViewById(R.id.employeepassword);

        String lname = empLname.getText().toString();
        String fname = empFname.getText().toString();
        String mfullname = fname+" "+lname;
        String uname = account.getAcct_uname();
        String mpassword = account.getAcct_passw();


        fullname.setText(mfullname);
        username.setText(uname);
        password.setText(mpassword);
        builder.setNeutralButton("OKAY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
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
                if (validateDetails()){
                    addRadioGroupListener();
                    insertEmployee();
                    uploadFile();
                }
        }
        return super.onOptionsItemSelected(item);

    }

    private boolean validateDetails() {
        String empLastname = empLname.getText().toString();
        String empFirstname = empFname.getText().toString();

        if (empLastname.isEmpty()) {
            empLname_layout.setError("Fields cannot be empty");
            return false;
        }

        if (empFirstname.isEmpty()) {
            empFname_layout.setError("Fieds cannot be empty");
            return false;
        }

        return true;
//        boolean ok = true;
//
//        if (empLastname.isEmpty()){
//            empLname_layout.setError("Fields can not be empty");
//            ok = false;
//
//            if (empFirstname.isEmpty()){
//                empFname_layout.setError("Fields can not be empty");
//                ok = false;
//            }else {
//                empFname_layout.setError(null);
//                ok = false;
//            }
//        }else {
//            empLname_layout.setError(null);
//            ok = true;
//        }
//        return ok;

    }

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

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //add image to the firebase storage
    private void uploadFile(){
        if (empimageUri!=null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
            + "." + getFileExtension(empimageUri));

            uploadTask = fileReference.putFile(empimageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();

                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
//                            Employee employee = new Employee();
////                            employee.setEmp_imageUrl(empFname.getText().toString());
//                            employee.setEmp_imageUrl(uri.toString());
//
//                            String uploadId = databaseReference.push().getKey();
//                            databaseReference.child(uploadId).setValue(employee);
//                            Toast.makeText(AddEmployeeActivity.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

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
