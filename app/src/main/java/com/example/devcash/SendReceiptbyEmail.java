package com.example.devcash;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/*
* created by Beverly Castillo on September 11, 2019
* */

public class SendReceiptbyEmail extends AppCompatActivity implements View.OnClickListener {

    LinearLayout sendemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_receiptby_email);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sendemail.setOnClickListener(this);


        //
//        ActivityCompat.requestPermissions(SendReceiptbyEmail.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                PackageManager.PERMISSION_GRANTED);




    }


    public void convertReceiptToPdf(View view){
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Paint paint = new Paint();
        String mString = "Sample";
        int x=10;
        int y=25;
        page.getCanvas().drawText(mString, x,y, paint);
        pdfDocument.finishPage(page);

        String mfilePath = Environment.getExternalStorageDirectory().getPath() + "/receipt.pdf";
        File file = new File(mfilePath);

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

        pdfDocument.close();
    }


    public void sendReceipt(){

    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.btnsendemail:

                break;
        }
    }
}
