package com.example.devcash.EDIT_UI;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.devcash.R;

public class EditEmployee extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText emplname, empfname;
    LinearLayout layoutdelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //
        emplname = (TextInputEditText) findViewById(R.id.textinput_editemplname);
        empfname = (TextInputEditText) findViewById(R.id.textinput_editempfname);
        layoutdelete = (LinearLayout) findViewById(R.id.layout_delemployee);

        layoutdelete.setOnClickListener(this);

        //
        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null){
            String lname = bundle.getString("employeelname");
            String fname = bundle.getString("employeefname");

            emplname.setText(lname);
            empfname.setText(fname);
        }

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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.layout_delemployee:
                SharedPreferences empshared = getSharedPreferences("EmpShared",MODE_PRIVATE);
                final String employeeId =(empshared.getString("employee_id",""));

                break;
        }
    }
}
