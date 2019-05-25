package com.example.devcash.ADD_UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.devcash.Database.DatabaseHelper;
import com.example.devcash.R;

public class AddCategoryActivity extends AppCompatActivity {

    TextInputEditText txtCategoryName;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //
        db = new DatabaseHelper(this);

        txtCategoryName = (TextInputEditText) findViewById(R.id.text_categoryname);

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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == android.R.id.home){
//            onBackPressed();
//            return true;
//        }else
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //menu item click handling
        //if back button is clicked
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }else if(id == R.id.action_save){ //if SAVE is clicked
            //collect the inputted data
            String category_name = txtCategoryName.getEditableText().toString();

            //validate
            if(!category_name.equals("")){
                //save to database
                long result = db.addCategory(category_name);
                if(result > 0){
                    Toast.makeText(this, "Category Successfully added.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }else{
                Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
            }


        }
            return super.onOptionsItemSelected(item);

    }
}
