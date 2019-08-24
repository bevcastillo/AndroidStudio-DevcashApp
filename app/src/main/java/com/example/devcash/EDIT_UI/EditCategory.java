package com.example.devcash.EDIT_UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.devcash.Object.Category;
import com.example.devcash.R;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class EditCategory extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference dbreference;
    private DatabaseReference ownerdbreference;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference rootRef;



    TextInputEditText editcatname;
    LinearLayout layoutdelete;

    String name;

    String category_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editcatname = (TextInputEditText) findViewById(R.id.text_categoryname);
        layoutdelete = (LinearLayout) findViewById(R.id.layout_delcategory);

        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbreference = firebaseDatabase.getReference("/datadevcash");
        ownerdbreference = firebaseDatabase.getReference("/datadevcash/owner");
        rootRef = firebaseDatabase.getReference("/datadevcash");



        //
        layoutdelete.setOnClickListener(this);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null){
            name = bundle.getString("categoryname");
            editcatname.setText(name);
        }
    }


    public void updateCategory(){
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        SharedPreferences categoryshared = getSharedPreferences("CategoryPref", MODE_PRIVATE);
        final String catId = (categoryshared.getString("category_id",""));


        ownerdbreference.orderByChild("business/owner_username")
                .equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                final String ownerKey = ds.getKey();
                                ownerdbreference.child(ownerKey+"/business/category")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                        ownerdbreference.child(ownerKey+"/business/category")
                                                                .orderByChild("category_name")
                                                                .equalTo(name)
                                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull final DataSnapshot snapshot) {
                                                                        if (snapshot.exists()) {
                                                                            for (DataSnapshot dSnapshot : snapshot.getChildren()) {
                                                                                Category category = dSnapshot.getValue(Category.class);
                                                                                if(category.getCategory_name().equals(name)){
//                                                                                    category.setCategory_name(editcatname.getText().toString());
                                                                                    dbreference.child("owner/"+ownerKey+"/business/category").child(dSnapshot.getKey()+"/category_name").setValue(editcatname.getText().toString());
//                                                                                    dbreference.child("owner/"+ownerKey+"/business/product").child("category/category_name").setValue(editcatname.getText().toString());
                                                                                    ownerdbreference.child(ownerKey+"/business/product")
                                                                                            .orderByChild("category/category_name")
                                                                                            .equalTo(name)
                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                            if(dataSnapshot.exists()){
                                                                                                for(DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                                                                                    dbreference.child("owner/"+ownerKey+"/business/product").child(dataSnapshot2.getKey()+"/category/category_name").setValue(editcatname.getText().toString());
                                                                                                }
                                                                                            } else {
                                                                                                Toast.makeText(EditCategory.this, dataSnapshot.getChildrenCount()+"", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                        }
                                                                                    });


                                                                                    Toast.makeText(EditCategory.this, name+" is updated.", Toast.LENGTH_SHORT).show();
                                                                                    finish();
                                                                                }
                                                                            }

                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                    }
                                                                });
//
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

    public void deleteCategory(){
        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        SharedPreferences categoryshared = getSharedPreferences("CategoryPref", MODE_PRIVATE);
        final String catId = (categoryshared.getString("category_id",""));

        ownerdbreference.orderByChild("business/owner_username")
                .equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                final String ownerKey = ds.getKey();
                                ownerdbreference.child(ownerKey+"/business/category")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                        ownerdbreference.child(ownerKey+"/business/category")
                                                                .orderByChild("category_name")
                                                                .equalTo(name)
                                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull final DataSnapshot snapshot) {
                                                                        if (snapshot.exists()) {
                                                                            for (DataSnapshot dSnapshot : snapshot.getChildren()) {
                                                                                Category category = dSnapshot.getValue(Category.class);
                                                                                if(category.getCategory_name().equals(name)){
//                                                                                    category.setCategory_name(editcatname.getText().toString());
                                                                                    dbreference.child("owner/"+ownerKey+"/business/category").child(dSnapshot.getKey()+"/category_name").setValue(null);
                                                                                    ownerdbreference.child(ownerKey+"/business/product")
                                                                                            .orderByChild("category/category_name")
                                                                                            .equalTo(name)
                                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                    if(dataSnapshot.exists()){
                                                                                                        for(DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                                                                                            dbreference.child("owner/"+ownerKey+"/business/product").child(dataSnapshot2.getKey()+"/category/category_name").setValue("No Category");
                                                                                                        }
                                                                                                    } else {
                                                                                                        Toast.makeText(EditCategory.this, dataSnapshot.getChildrenCount()+"", Toast.LENGTH_SHORT).show();
                                                                                                    }
                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                }
                                                                                            });
                                                                                    Toast.makeText(EditCategory.this, name+" is deleted.", Toast.LENGTH_SHORT).show();
                                                                                    finish();
                                                                                }
                                                                            }

                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                    }
                                                                });
//
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
                updateCategory();
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.layout_delcategory:
                deleteCategory();
                break;
        }
    }
}
