package com.example.contactmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class ViewContact extends AppCompatActivity {

    TextView nameTxt, phoneTxt, emailTxt, addressTxt, groupTxt;

    @Override
    public void onCreate(Bundle SavedInstanceState){

        super.onCreate((SavedInstanceState));
        setContentView((R.layout.activity_view_contact));

        nameTxt = (TextView) findViewById(R.id.txtName);
        phoneTxt = (TextView) findViewById(R.id.txtPhone);
        emailTxt = (TextView) findViewById(R.id.txtEmail);
        addressTxt = (TextView) findViewById(R.id.txtAddress);
        groupTxt = (TextView) findViewById(R.id.txtGroup);
    }

}
