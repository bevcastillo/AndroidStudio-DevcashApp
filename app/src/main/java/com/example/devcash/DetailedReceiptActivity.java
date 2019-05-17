package com.example.devcash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class DetailedReceiptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_receipt);

        //handles back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //back button is clicked
//        if(id == android.R.id.home){
//            onBackPressed();
//            return true;
//        }

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_refund:
                Toast.makeText(getApplicationContext(),"You have pressed refund menu.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.send_email:
                Toast.makeText(getApplicationContext(), "You have pressed send via email menu.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.send_text:
                Toast.makeText(getApplicationContext(), "You have pressed send via SMS", Toast.LENGTH_SHORT).show();
                break;
        }

//        return super.onOptionsItemSelected(item);
        return false;
    }

    //handles the action bar menus

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refundsettingsmenu, menu);

        return true;
    }


}
