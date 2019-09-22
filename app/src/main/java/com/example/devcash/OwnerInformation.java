package com.example.devcash;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.service.autofill.Dataset;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.EDIT_UI.EditEmployee;
import com.example.devcash.Object.Account;
import com.example.devcash.Object.Business;
import com.example.devcash.Object.Employee;
import com.example.devcash.Object.Owner;
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

public class OwnerInformation extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference dbreference;
    private DatabaseReference ownerdbreference;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference;
    private StorageTask uploadTask;

    private TextInputEditText ownerlname, ownerfname, ownerphone, owneraddr, ownerbdate, acctemail, acctpassw, ownerusername;
    private TextInputLayout ownerLnameLayout, ownerFnameLayout, ownerPasswLayout, ownerUsernameLayout;
    private TextView txtownerusername, txtdeactivate, txtacctstatus;
    private RadioGroup radioGroupgender;
    private RadioButton radioButton, radioMale, radioFemale;
    private String selectedGender;

    private ImageView imageView;
    private Uri imageUri;

    private static final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_information);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ownerlname = (TextInputEditText) findViewById(R.id.owner_lname);
        ownerfname = (TextInputEditText) findViewById(R.id.owner_fname);
        ownerphone = (TextInputEditText) findViewById(R.id.owner_phone);
        ownerbdate = (TextInputEditText)findViewById(R.id.owner_dob);
        ownerusername = (TextInputEditText) findViewById(R.id.owner_username);

        ownerUsernameLayout = (TextInputLayout) findViewById(R.id.layoutOwnerUsername);
        ownerFnameLayout = (TextInputLayout) findViewById(R.id.layoutOwnerFname);
        ownerLnameLayout = (TextInputLayout) findViewById(R.id.layoutOwnerLname);
        ownerPasswLayout = (TextInputLayout) findViewById(R.id.layoutOwnerPassw);

        txtownerusername = (TextView) findViewById(R.id.txtownerusername);
        acctemail = (TextInputEditText) findViewById(R.id.owner_email);
        acctpassw = (TextInputEditText) findViewById(R.id.owner_password);
        radioGroupgender = (RadioGroup) findViewById(R.id.radiogroup_ownergender);
        radioMale = (RadioButton) findViewById(R.id.radio_ownermale);
        radioFemale = (RadioButton) findViewById(R.id.radio_ownerfemale);
        txtdeactivate = (TextView) findViewById(R.id.txt_deactivate);
        txtacctstatus = (TextView) findViewById(R.id.txtacctstatus);

        imageView = (ImageView) findViewById(R.id.owner_photo);

        imageView.setOnClickListener(this);

        ownerbdate.setOnClickListener(this);
        txtdeactivate.setOnClickListener(this);

        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        ownerdbreference = firebaseDatabase.getReference("/datadevcash/owner");
        storageReference = FirebaseStorage.getInstance().getReference("owner");
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences ownerPref = getApplicationContext().getSharedPreferences("OwnerPref", MODE_PRIVATE);
        SharedPreferences businessPref = getApplicationContext().getSharedPreferences("BusinessPref", MODE_PRIVATE);


        Gson gson = new Gson();
        String json = ownerPref.getString("account", "");
        String businessJson = businessPref.getString("business", "");

        Account account = gson.fromJson(json, Account.class);
        Business business = gson.fromJson(businessJson, Business.class);
        ownerlname.setText(business.getOwner_lname());
        ownerfname.setText(business.getOwner_fname());
        ownerphone.setText(business.getOwner_mobileno());

        selectedGender = business.getOwner_gender();
        if(selectedGender.equals(radioMale.getText().toString())){
            radioMale.setChecked(true);
            radioFemale.setChecked(false);
        } else{
            radioFemale.setChecked(true);
            radioMale.setChecked(false);
        }


        txtownerusername.setText(account.getAcct_uname());
        ownerusername.setText(account.getAcct_uname());
        acctemail.setText(account.getAcct_email());
        acctpassw.setText(account.getAcct_passw());

    }

    private boolean checkDuplicate(){
        String owner_user = ownerusername.getText().toString();
        final boolean[] ok = {true};

        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        if (!owner_user.equals(username)) {
            ownerdbreference.orderByChild("business/owner_username").equalTo(owner_user).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        ownerUsernameLayout.setError("Username already exists.");
                    }else {
                        ownerUsernameLayout.setError(null);
                        ok[0] = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        return ok[0];
    }

    private boolean validateFields(){
        String owner_user = ownerusername.getText().toString();
        String owner_lname = ownerlname.getText().toString();
        String owner_fname = ownerfname.getText().toString();
        String acct_passw = acctpassw.getText().toString();

        if (owner_user.isEmpty()) {
            ownerUsernameLayout.setError("Fields cannot be empty");
            return false;
        }

        if (owner_lname.isEmpty()) {
            ownerLnameLayout.setError("Fields cannot be empty");
            return false;
        }

        if (owner_fname.isEmpty()) {
            ownerFnameLayout.setError("Fields cannot be empty");
            return false;
        }

        if (acct_passw.isEmpty()) {
            ownerPasswLayout.setError("Fields cannot be empty");
            return false;
        }

        return true;
//        boolean ok = true;
//
//        if (owner_user.isEmpty()){
//            ownerUsernameLayout.setError("Fields can not be empty.");
//            ok = false;
//            if (owner_lname.isEmpty()){
//                ownerLnameLayout.setError("Fields can not be empty.");
//                ok = false;
//                if (owner_fname.isEmpty()){
//                    ownerFnameLayout.setError("Fields can not be empty.");
//                    ok = false;
//                    if (acct_passw.isEmpty()){
//                        ownerPasswLayout.setError("Fields can not be empty.");
//                        ok = false;
//                    }else {
//                        ownerPasswLayout.setError(null);
//                        ok = true;
//                    }
//                }else {
//                    ownerLnameLayout.setError(null);
//                    ok = true;
//                }
//            }else {
//                ownerLnameLayout.setError(null);
//                ok = true;
//            }
//        }else {
//            ownerUsernameLayout.setError(null);
//            ok = true;
//        }

//        return ok;
    }

    public void addRadioGroupListener(){
        int radioid = radioGroupgender.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(radioid);
        selectedGender = radioButton.getText().toString();
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

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void updateOwner(){

        final StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));

        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("/business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        final String ownerkey = dataSnapshot1.getKey();

                        for (DataSnapshot ds : dataSnapshot1.getChildren()) {
                            if(ds.exists()) {
                                Business business = ds.getValue(Business.class);
//                                Toast.makeText(OwnerInformation.this, business.getOwner_username()+"", Toast.LENGTH_SHORT).show();
                                ownerdbreference.child(ownerkey+"/business/owner_fname").setValue(ownerfname.getText().toString());
                                ownerdbreference.child(ownerkey+"/business/owner_lname").setValue(ownerlname.getText().toString());
                                ownerdbreference.child(ownerkey+"/business/owner_username").setValue(ownerusername.getText().toString());
                                ownerdbreference.child(ownerkey+"/business/owner_mobileno").setValue(ownerphone.getText().toString());

                                uploadTask = fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                ownerdbreference.child(ownerkey+"/business/owner_image").setValue(uri.toString());
                                            }
                                        });
                                    }
                                });

                                addRadioGroupListener();
                                ownerdbreference.child(ownerkey+"/business/owner_gender").setValue(selectedGender);

                                ///
                                ownerdbreference.child(ownerkey+"/business/account")
                                        .orderByChild("acct_uname").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            for(DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                                String acctkey = dataSnapshot2.getKey();

                                                ownerdbreference.child(ownerkey+"/business/account").child(acctkey+"/acct_email").setValue(acctemail.getText().toString());
                                                ownerdbreference.child(ownerkey+"/business/account").child(acctkey+"/acct_passw").setValue(acctpassw.getText().toString());
                                                ownerdbreference.child(ownerkey+"/business/account").child(acctkey+"/acct_uname").setValue(ownerusername.getText().toString());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                Toast.makeText(OwnerInformation.this, "Account is updated!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.home:
                onBackPressed();
                break;
            case R.id.action_save:
                if (validateFields()){
                    if (checkDuplicate()){
                        updateOwner();
                    }
                }
//                checkDuplicate();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    public void enterPasswDialog(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_password, null);
        dialog.setView(dialogView);

        final TextInputEditText editconfpassw = (TextInputEditText) dialogView.findViewById(R.id.confirmpassword);
        final Button btnconfirm = (Button) dialogView.findViewById(R.id.btnconfirm);

        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtdeactivate.getText().toString().equals("deactivate")){

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
                                                                    if (!editconfpassw.getText().toString().equals("")){
                                                                        if(passw.equals(editconfpassw.getText().toString()) && type.equals("Owner")){
                                                                            deactivateDialog();
                                                                        } else{
                                                                            Toast.makeText(OwnerInformation.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    } else{
                                                                        Toast.makeText(OwnerInformation.this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
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
                                                                    if (!editconfpassw.getText().toString().equals("")){
                                                                        if(passw.equals(editconfpassw.getText().toString()) && type.equals("Owner")){
                                                                            activateDialog();
                                                                        } else{
                                                                            Toast.makeText(OwnerInformation.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    } else{
                                                                        Toast.makeText(OwnerInformation.this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
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
                }

            }
        });
        AlertDialog builder = dialog.create();
        builder.show();
    }

    public void activateDialog(){

    }

    public void deactivateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Sure to delete account?");
        builder.setMessage("Your account will be deleted after 30 days.");
        builder.setPositiveButton("DEACTIVATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //queryy

                SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
                final String username = (shared.getString("owner_username", ""));

                ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                final String ownerkey = dataSnapshot1.getKey();
                                ownerdbreference.child(ownerkey+"/business/account")
                                        .orderByChild("acct_uname").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                                String acctkey = dataSnapshot2.getKey();
                                                Account account = dataSnapshot2.getValue(Account.class);

                                                txtacctstatus.setText("Deactivated");
                                                ownerdbreference.child(ownerkey+"/business/account").child(acctkey+"/acct_status").setValue(txtacctstatus.getText().toString());
                                                logoutOwner();
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
        });
        builder.show();
    }

    public void logoutOwner(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pending Deletion");
        builder.setMessage("Your account is pending for deletion. If you will not login within 30 days, your account with Devcash will be permanently deleted.");
        builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.clear();
                editor.commit();
                Intent logout = new Intent(OwnerInformation.this, OwnerLoginActivity.class);
                startActivity(logout);
                Toast.makeText(OwnerInformation.this, "Error. You have been logged out.", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
        builder.show();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.txt_deactivate:
                enterPasswDialog();
                break;

            case R.id.imageView:
                choosePhoto();
                break;
        }
    }

    private void choosePhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }


    }
}
