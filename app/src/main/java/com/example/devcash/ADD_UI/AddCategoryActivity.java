package com.example.devcash.ADD_UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.devcash.Database.DatabaseHelper;
import com.example.devcash.Fragments.CategoriesFragment;
import com.example.devcash.Object.Category;
import com.example.devcash.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

public class AddCategoryActivity extends AppCompatActivity {

    private DatabaseReference dbreference;
    private DatabaseReference mydbreference;
    private DatabaseReference ownerdbreference;
    private FirebaseDatabase firebaseInstance;
    private String CategoryId;

    TextInputEditText categoryName;
    TextInputLayout categoryNameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //
        categoryName = (TextInputEditText) findViewById(R.id.text_categoryname);
        categoryNameLayout = (TextInputLayout) findViewById(R.id.text_input_category_name);

        firebaseInstance = FirebaseDatabase.getInstance();
        mydbreference = firebaseInstance.getReference("/datadevcash");
        dbreference = firebaseInstance.getReference("/datadevcash/owner/enterprise");
        ownerdbreference = firebaseInstance.getReference("/datadevcash/owner");
        CategoryId = dbreference.push().getKey();

        viewData();

        dbreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
//                    id=(dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private boolean validateFields(){
        String catName = categoryName.getText().toString();
        boolean ok = true;

        if (catName.isEmpty()){
            categoryNameLayout.setError("Fields can not be empty.");
            ok = false;
        }else {
            categoryNameLayout.setError(null);
            ok = true;
        }

        return ok;

    }

    private boolean checkDuplicate(){
        final String catName = categoryName.getText().toString();
        final boolean[] ok = {true};

        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        String ownerKey = dataSnapshot1.getKey();

                        ownerdbreference.child(ownerKey+"/business/category").orderByChild("category_name").equalTo(catName).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    categoryNameLayout.setError("Category already exists.");
                                    ok[0] = false;
                                }else {
                                    categoryNameLayout.setError(null);
                                    ok[0] = true;
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

        return ok[0];
    }

    public void addCategory (String categoryName){
        final Category category = new Category(categoryName);
//        dbreference.child("/category").child(CategoryId).setValue(category);

        SharedPreferences shared = getSharedPreferences("OwnerPref", MODE_PRIVATE);
        final String username = (shared.getString("owner_username", ""));

        SharedPreferences categoryshared = getSharedPreferences("CategoryPref", MODE_PRIVATE);
        final SharedPreferences.Editor category_editor = categoryshared.edit();

        final Gson gson = new Gson();

        ownerdbreference.orderByChild("business/owner_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    category_editor.putString("category_id", CategoryId);
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        String key = ds.getKey();
                            mydbreference.child("owner/"+key+"/business/category").child(CategoryId).setValue(category);
                            String categoryJson = gson.toJson(category);
                            category_editor.putString("category", categoryJson);
                            category_editor.commit();


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void insertCategory(){
        addCategory(categoryName.getText().toString().trim());
        Toast.makeText(getApplicationContext(), "Category Successfully Added!", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void viewData(){
        dbreference.child("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String dbcategoryname = ds.child("categoryName").getValue(String.class);
                    Log.d("TAG", dbcategoryname);
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
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }else if(id == R.id.action_save){

            if (validateFields()){
                if (checkDuplicate()){
                    insertCategory();
                }
            }
        }

        return super.onOptionsItemSelected(item);

    }
}
