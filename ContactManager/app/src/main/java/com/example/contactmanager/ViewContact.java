package com.example.contactmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ViewContact extends AppCompatActivity {

    TextView nameTxt, phoneTxt, emailTxt, addressTxt, groupTxt;
    Contact contact;

    @Override
    public void onCreate(Bundle SavedInstanceState){

        super.onCreate((SavedInstanceState));
        setContentView((R.layout.activity_view_contact));

        nameTxt = (TextView) findViewById(R.id.txtName);
        phoneTxt = (TextView) findViewById(R.id.txtPhone);
        emailTxt = (TextView) findViewById(R.id.txtEmail);
        addressTxt = (TextView) findViewById(R.id.txtAddress);
        groupTxt = (TextView) findViewById(R.id.txtGroup);

        final Button buttonEdit = (Button) findViewById(R.id.btnEdit);

        Bundle extras = this.getIntent().getExtras();
        if(extras != null){
            contact = (Contact)extras.getSerializable("CONTACT");
            nameTxt.setText(contact.getName());
            phoneTxt.setText(contact.getPhone());
            emailTxt.setText(contact.getEmail());
            addressTxt.setText(contact.getAddress());
            groupTxt.setText(contact.getGroup());

            buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent editContactIntent = new Intent(ViewContact.this, CreateContact.class );
                    editContactIntent.putExtra("CONTACT", contact);
                    startActivityForResult(editContactIntent, 1);
                }
            });
        }
        else{
            this.finish();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ((ContactManagerApplication)getApplication()).mainActivity.updateContacts();
    }
}
