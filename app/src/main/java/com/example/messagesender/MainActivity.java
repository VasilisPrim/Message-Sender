package com.example.messagesender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button smsBtn,emailBtn,clearBtn;
    EditText editText1,editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1 = findViewById(R.id.sendTextTo);
        editText2 = findViewById(R.id.content);
        smsBtn = findViewById(R.id.sms);
        emailBtn = findViewById(R.id.email);
        clearBtn = findViewById(R.id.clear);

        smsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSms();
            }
        });
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail();
            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearEntries();
            }
        });

    }

    private void clearEntries() {
        editText1.getText().clear();
        editText2.getText().clear();
    }

    private void sendSms() {
        String number = editText1.getText().toString();
        String message = editText2.getText().toString();
        if(!isValidNumber(number)) return;

        Uri uri = Uri.parse("smsto:"+number);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_TEXT,message);
        startActivity(intent);

    }

    private boolean isValidNumber( String number) {
        boolean validNumber = PhoneNumberUtils.isGlobalPhoneNumber(number);
        if (!validNumber) {
            Toast.makeText(this, "This is not a valid phone number!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void sendMail(){
        String receiverList = editText1.getText().toString();
        String[] receivers = receiverList.split(",");
        String message = editText2.getText().toString();
        for(int i=0;i<receivers.length;i++){
            if(!isValidEmail(receivers[i])){
                Toast.makeText(this, receivers[i]+" is not a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,receivers);
        intent.putExtra(Intent.EXTRA_TEXT,message);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Choose an email client"));
    }
    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}