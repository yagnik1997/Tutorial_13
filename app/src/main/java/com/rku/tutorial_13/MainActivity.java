package com.rku.tutorial_13;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.time.Instant;

public class MainActivity extends AppCompatActivity {

    EditText edtNumber, edtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtNumber = findViewById(R.id.edtNumber);
        edtMessage = findViewById(R.id.edtMessage);
    }


    public void makeCall() {
        String valNumber = edtNumber.getText().toString();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + valNumber));
        startActivity(intent);
    }

    // CALL onClick Event
    public void callToNumber(View view) {
        String valNumber = edtNumber.getText().toString();

        if (valNumber.isEmpty() || valNumber.length() < 10) {
            edtNumber.setError("Enter 10 Digit Number");
            edtNumber.requestFocus();
        } else {
            if (isPermissionGranted(1)) {
                makeCall();
            }
        }
    }


    // SMS onClick Event
    public void sendSMS(View view) {
        String valMessage = edtMessage.getText().toString();
        String valNumber = edtNumber.getText().toString();

        if (valNumber.isEmpty() || valNumber.length() < 10) {
            edtNumber.setError("Enter 10 Digit Number");
            edtNumber.requestFocus();
        } else {
            if (valMessage.equals("")) {
                edtMessage.setError("Message cant be Empty");
                edtMessage.requestFocus();
            } else {
                if (isPermissionGranted(2)) {
                    makeSMS();
                }
            }
        }
    }

    public void makeSMS() {
        String valMessage = edtMessage.getText().toString();
        String valNumber = edtNumber.getText().toString();

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(valNumber, null, valMessage, null, null);
        Toast.makeText(getApplicationContext(), "Message Send Successfully", Toast.LENGTH_SHORT).show();
    }

    public boolean isPermissionGranted(int permission) {
        switch (permission) {
            case 1:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {
                        Log.v("TAG", "Call Permission is granted");
                        return true;
                    } else {

                        Log.v("TAG", "Permission is revoked");
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                        return false;
                    }
                } else { //permission is automatically granted on sdk<23 upon installation
                    Log.v("TAG", "Permission is granted");
                    return true;
                }

            case 2:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(Manifest.permission.SEND_SMS)
                            == PackageManager.PERMISSION_GRANTED) {
                        Log.v("TAG", "SMS Permission is granted");
                        return true;
                    } else {

                        Log.v("TAG", " Permission is revoked");
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 2);
                        return false;
                    }
                } else { //permission is automatically granted on sdk<23 upon installation
                    Log.v("TAG", "Permission is granted");
                    return true;
                }
            default:
                return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    makeCall();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}


