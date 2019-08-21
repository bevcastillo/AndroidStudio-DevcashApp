package com.example.devcash.EDIT_UI;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.R;

import org.w3c.dom.Text;

public class EditEmployee extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TextInputEditText emplname, empfname;
    private LinearLayout layoutdelete;
    private Spinner emptask;
    private String lastname, firstname, selectedtask, gender, phone;
    private int pos;

    private TextView txtempname, txtempgender, txtempphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //
        emplname = (TextInputEditText) findViewById(R.id.textinput_editemplname);
        empfname = (TextInputEditText) findViewById(R.id.textinput_editempfname);
        layoutdelete = (LinearLayout) findViewById(R.id.layout_delemployee);
        emptask = (Spinner) findViewById(R.id.spinner_emptask);

        //
        txtempname = (TextView) findViewById(R.id.editempname);
        txtempgender = (TextView) findViewById(R.id.editempgender);
        txtempphone = (TextView) findViewById(R.id.editempnumber);

        layoutdelete.setOnClickListener(this);
        emptask.setOnItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        String [] tasklist = getResources().getStringArray(R.array.task_assign);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null){
            lastname = bundle.getString("employeelname");
            firstname = bundle.getString("employeefname");
            selectedtask = bundle.getString("employeetask");
            gender = bundle.getString("employeegender");
            phone = bundle.getString("employeephone");

            for (int i=0; i<tasklist.length; i++){
                if(tasklist[i].equals(selectedtask)){
                    pos = i;
                }

                txtempname.setText(lastname+", "+firstname);
                emptask.setSelection(pos);
                txtempgender.setText(gender);
                txtempphone.setText(phone);
            }

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
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int sid = parent.getId();
            switch (sid){
                case R.id.spinner_emptask:
                    selectedtask = emptask.getItemAtPosition(position).toString();
                    break;
            }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
