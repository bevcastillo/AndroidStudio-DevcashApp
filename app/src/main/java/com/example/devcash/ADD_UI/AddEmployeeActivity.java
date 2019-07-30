package com.example.devcash.ADD_UI;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import com.example.devcash.Object.Employee;
import com.example.devcash.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Calendar;

public class AddEmployeeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private DatabaseReference dbreference;
    private FirebaseDatabase firebasedb;
    private String EmployeeId;
    TextInputEditText empLname, empFname, empEmail, empPhone, empDOB;
    private Uri empimageUri;
    ImageView empimage;
    LinearLayout takephoto, choosephoto;
    Spinner emptask;
    String selectedemptask;
    DatePickerDialog bdatePickerDia;

    private static final int PICK_IMAGE = 100;

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
        empEmail = (TextInputEditText) findViewById(R.id.text_input_emp_email_address);
        empPhone = (TextInputEditText) findViewById(R.id.text_input_emp_pnumber);
        empDOB = (TextInputEditText) findViewById(R.id.text_input_dob);
        emptask = (Spinner) findViewById(R.id.spinner_emptask);

//        grpTask = (RadioGroup) findViewById(R.id.radio_group_emp_task);

        //

        //add listeners to the textviews
        empDOB.setOnClickListener(this);
        takephoto.setOnClickListener(this);
        choosephoto.setOnClickListener(this);
        emptask.setOnItemSelectedListener(this);

//        dbreference = firebasedb.getReference("/datadevcash");
//        EmployeeId = dbreference.push().getKey();


    }
//
//    public void addEmployee(Uri empImageUri, String empLname, String empFname, String empTask, String empDOB, String empGender, String empBdate, int empCtcnum){
//        Employee employee = new Employee(empImageUri, empLname, empFname, empTask, empDOB, empGender, empBdate, empCtcnum);
//        dbreference.child("/employees").child(String.valueOf(EmployeeId)).setValue(employee);
//    }

//    public void insertEmployee(){
////        addEmployee(imageUri, txtEmpLname, );
//    }

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

        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }else if(id == R.id.action_save){


        }
        return super.onOptionsItemSelected(item);

    }


    // handles camera clicks
    @Override
    public void onClick(View v) {
        int sid = v.getId();

        switch (sid){
            case R.id.empchoosephoto:
//                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//                startActivityForResult(gallery, PICK_IMAGE);
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                Toast.makeText(getApplicationContext(), "Choose photo!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.emptakephoto:
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, 0);
                Toast.makeText(getApplicationContext(), "Image successfully added!", Toast.LENGTH_SHORT).show();
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
                                empDOB.setText(dayOfMonth + "/"
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
