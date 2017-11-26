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

        Bundle extras = this.getIntent().getExtras();
        if(extras != null){
            Contact contact = (Contact)extras.getSerializable("CONTACT");
            nameTxt.setText(contact.getName());
            phoneTxt.setText(contact.getPhone());
            emailTxt.setText(contact.getEmail());
            addressTxt.setText(contact.getAddress());
            groupTxt.setText(contact.getGroup());
        }
    }

}
