package com.example.devcash.Fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendReceiptFragment extends Fragment implements View.OnClickListener {

    TextInputEditText custphone;
    TextView sendcustphone;
    String message, phone;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;


    public SendReceiptFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_receipt, container, false);


        ImageView sendemail = view.findViewById(R.id.send_email);
        ImageView sendtext = view.findViewById(R.id.send_text);
        custphone = (TextInputEditText) view.findViewById(R.id.textinput_custphone);
        sendcustphone = (TextView) view.findViewById(R.id.textsendcustphone);
        //handles buttons
        Button btnnewpurchase = view.findViewById(R.id.btn_newPurchase);
        sendcustphone.setEnabled(false);
        if(checkPermission(Manifest.permission.SEND_SMS)){
            sendcustphone.setEnabled(true);
        }else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }

        sendcustphone.setOnClickListener(this);


        btnnewpurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment salesfragment = new SalesFragment();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.finalpurchase_content, salesfragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

//        sendemail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(),"Email successfully sent.", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        sendtext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Text successfully sent.", Toast.LENGTH_SHORT).show();
//            }
//        });


        return view;
    }

    public void sendSMS(){
        phone = custphone.getText().toString();
        message = "Welcome to Devcash!";

        if(phone == null || phone.length() == 0){
            return;
        }
        if(checkPermission(Manifest.permission.SEND_SMS)){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone,null,phone,null,null);
            Toast.makeText(getActivity(), "SMS sent", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getActivity(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
        }

    }

    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(getActivity(), permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    //this is the method for sending the message
    public void sendCustSMS(){
        phone = custphone.getText().toString();
        message = "Welcome to Devcash!";

        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.SEND_SMS)){

            }else{
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }//endif
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_SEND_SMS:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phone,null,message,null,null);
                    Toast.makeText(getActivity(), "SMS sent", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.textsendcustphone:
                sendSMS();
                break;
        }
    }
}
