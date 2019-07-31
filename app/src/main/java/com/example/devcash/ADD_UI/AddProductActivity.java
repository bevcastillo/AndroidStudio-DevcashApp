package com.example.devcash.ADD_UI;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.R;

import java.util.Calendar;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    ImageView prodphoto;
    TextView takephoto, choosephoto;
    TextInputEditText prodexpdate;
    Spinner prodcondition, produnit, discname;
    RadioGroup soldby;
    RadioButton soldbybtn;
    String selectedprodcond, selectedprodunit, selecteddisc, selectedsoldby;

    private static final int PICK_IMAGE = 100;

    Uri imageUri;
    DatePickerDialog expdatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prodphoto = (ImageView) findViewById(R.id.prod_photo);

        takephoto = (TextView) findViewById(R.id.txt_prodtakephoto);
        choosephoto = (TextView) findViewById(R.id.txt_prodchoosephoto);

        prodexpdate = (TextInputEditText) findViewById(R.id.textprod_exp_date);

        prodcondition = (Spinner) findViewById(R.id.spinner_prod_condition);
        produnit = (Spinner) findViewById(R.id.spinner_unit);
        discname = (Spinner) findViewById(R.id.spinner_prod_discount);

        soldby = (RadioGroup) findViewById(R.id.radio_group_soldby);

        //adding listeners to the textviews
        takephoto.setOnClickListener(this);
        choosephoto.setOnClickListener(this);
        prodexpdate.setOnClickListener(this);

        prodcondition.setOnItemSelectedListener(this);
        produnit.setOnItemSelectedListener(this);
        discname.setOnItemSelectedListener(this);


        //
        addRadioGroupListener();

    }

    public void addRadioGroupListener(){
        int radioid=soldby.getCheckedRadioButtonId();
        soldbybtn=(RadioButton)findViewById(radioid);
        selectedsoldby=soldbybtn.getText().toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //inflate SAVE menu
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

        //menu item click handling
        //if back button is clicked
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }else if(id == R.id.action_save){ //if SAVE is clicked
            Toast.makeText(this, "Product Successfully added.", Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.txt_prodchoosephoto:
//                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//                startActivityForResult(gallery, PICK_IMAGE);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
                break;
            case R.id.txt_prodtakephoto:
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, 0);
                break;
            case R.id.textprod_exp_date:
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                //date picker dialog
                expdatePicker = new DatePickerDialog(AddProductActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //set day, month and year in the textinputedittext
                                prodexpdate.setText(dayOfMonth + "/"
                                                    + (month + 1) + "/" + year);
                            }
                        },mYear,mMonth,mDay);
                            expdatePicker.show();
                break;
        }
    }


    //handles opening the camera and gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            prodphoto.setImageURI(imageUri);
        }else{
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            prodphoto.setImageBitmap(bitmap);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int sid = parent.getId();

        switch (sid){
            case R.id.spinner_prod_condition:
                selectedprodcond = this.prodcondition.getItemAtPosition(position).toString();
                break;
            case R.id.spinner_unit:
                selectedprodunit = this.produnit.getItemAtPosition(position).toString();
                break;
            case R.id.spinner_prod_discount:
                selecteddisc = this.discname.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
