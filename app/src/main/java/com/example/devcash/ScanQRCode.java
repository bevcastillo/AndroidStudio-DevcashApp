package com.example.devcash;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanQRCode extends AppCompatActivity implements View.OnClickListener {

    Button btnscan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);

        btnscan = (Button) findViewById(R.id.scan_btn);
        btnscan.setOnClickListener(this);
        final Activity activity = this;

//        scanQR();
    }

//    public void scanQR(){
//        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
//        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
//        intentIntegrator.setPrompt("Scan");
//        intentIntegrator.setCameraId(0);
//        intentIntegrator.setBeepEnabled(false);
//        intentIntegrator.setBarcodeImageEnabled(false);
//        intentIntegrator.initiateScan();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if(result != null){
//            if(result.getContents()==null){
//                Toast.makeText(this, "Scanning was cancelled", Toast.LENGTH_LONG).show();
//            }
//            else {
//                Toast.makeText(this, result.getContents(),Toast.LENGTH_LONG).show();
//            }
//        }
//        else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.scan_btn:
//                scanQR();
                break;
        }
    }
}
