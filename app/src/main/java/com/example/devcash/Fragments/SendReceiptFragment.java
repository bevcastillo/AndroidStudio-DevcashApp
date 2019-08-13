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

    TextInputEditText custphone, custmessage;
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
        custmessage = (TextInputEditText) view.findViewById(R.id.textinput_custmessage);
        sendcustphone = (TextView) view.findViewById(R.id.textsendcustphone);
        //handles buttons
        Button btnnewpurchase = view.findViewById(R.id.btn_newPurchase);
//        sendcustphone.setEnabled(false);
//        if(checkPermission(Manifest.permission.SEND_SMS)){
//            sendcustphone.setEnabled(true);
//        }else{
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
//        }

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
        message = custmessage.getText().toString();

        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),  Manifest.permission.SEND_SMS);
        if(permissionCheck==PackageManager.PERMISSION_GRANTED){
            MyMessage();
        } else{
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.SEND_SMS}, 0);

        }

    }

    private void MyMessage() {
        String phonenumber = custphone.getText().toString().trim();
        String message = custmessage.getText().toString().trim();

        if(!custphone.getText().toString().equals("") || !custmessage.getText().toString().equals("")){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phonenumber,null,message,null,null);

            Toast.makeText(getActivity(), "Message sent.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(), "Fields can not be empty!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 0:
                if(grantResults.length >= 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    MyMessage();
                } else {
                    Toast.makeText(getActivity(), "You do note the permission is the people!", Toast.LENGTH_SHORT).show();
                }
//                break;
        }
    }



    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(getActivity(), permission);
        return (check == PackageManager.PERMISSION_GRANTED);
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
