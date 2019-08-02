package com.example.devcash.ADD_UI;

import android.app.ProgressDialog;
import android.app.Service;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.Object.Services;
import com.example.devcash.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddServicesActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference dbreference;
    private FirebaseDatabase firebaseDatabase;
//    private StorageReference storageReference;
    private String ServicesId;


    ImageView servicesphoto;
    TextView takephoto, choosephoto;
    CheckBox chkavail;
    TextInputEditText servicename, serviceprice;

    private static final int PICK_IMAGE = 100;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_services);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //
        servicename = (TextInputEditText) findViewById(R.id.textinput_servname);
        serviceprice = (TextInputEditText) findViewById(R.id.textinput_servprice);
        servicesphoto = (ImageView) findViewById(R.id.services_photo);
        takephoto = (TextView) findViewById(R.id.txt_servicestakephoto);
        choosephoto = (TextView) findViewById(R.id.txt_serviceschoosephoto);
        chkavail = (CheckBox) findViewById(R.id.cbox_serv_avail);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbreference = firebaseDatabase.getReference("/datadevcash");
        ServicesId = dbreference.push().getKey();

        //adding listeners to the textviews
        takephoto.setOnClickListener(this);
        choosephoto.setOnClickListener(this);

    }

    public void addServices(String service_name, double service_price){
//        Services services = new Services(service_name,service_price);
//        dbreference.child("/services").child(ServicesId).setValue(services);
    }

    public void insertServices(){
        addServices(servicename.getText().toString().trim(), Double.parseDouble(serviceprice.getText().toString()));
        Toast.makeText(getApplicationContext(), "Services Successfully added!", Toast.LENGTH_SHORT).show();
        finish();
    }



    public void addCheckBoxListener(){
        if(chkavail.isChecked()){
            String chkres = "Available";
            Toast.makeText(getApplicationContext(), chkres+"", Toast.LENGTH_SHORT).show();
        }else{
            String chkres = "Not Available";
            Toast.makeText(getApplicationContext(), chkres+"", Toast.LENGTH_SHORT).show();
        }
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

        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }else if(id == R.id.action_save){ //if SAVE is clicked
            addCheckBoxListener();
            insertServices();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.txt_serviceschoosephoto:
//                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//                startActivityForResult(gallery, PICK_IMAGE);
                choosePhoto();
                break;
            case R.id.txt_servicestakephoto:
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, 0);
                break;
        }
    }

    //handles opening the camera and gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            servicesphoto.setImageURI(imageUri);
        }else{
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            servicesphoto.setImageBitmap(bitmap);
        }

    }

    private void choosePhoto(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE);
    }

    private void uploadPhoto(){
        if(imageUri!=null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Adding..");
            progressDialog.show();


        }
    }

}
