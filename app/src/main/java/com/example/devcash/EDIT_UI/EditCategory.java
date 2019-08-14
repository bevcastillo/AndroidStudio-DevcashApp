package com.example.devcash.EDIT_UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.devcash.R;

public class EditCategory extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText editcatname;
    LinearLayout layoutdelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editcatname = (TextInputEditText) findViewById(R.id.text_categoryname);
        layoutdelete = (LinearLayout) findViewById(R.id.layout_delcategory);

        //
        layoutdelete.setOnClickListener(this);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null){
            String cat = bundle.getString("categoryname");
            editcatname.setText(cat);
        }
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.layout_delcategory:
                Toast.makeText(this, "You have clicked delete!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
