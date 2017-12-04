package com.example.contactmanager;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;


public class ViewContact extends AppCompatActivity {



    TextView nameTxt, label1, label2, label3, label4, field1, field2, field3, field4;
    ImageButton buttonText, buttonMail, buttonCall, buttonMap;
    String phone, email, address, group;
    int id;
    ImageView imgProfilePic;
    int numberOfLabelsNeeded;   //This keeps track of how many labels we need.
    Contact contact;


    @Override
    public void onCreate(Bundle SavedInstanceState){

        super.onCreate((SavedInstanceState));
        setContentView((R.layout.activity_view_contact));

        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        nameTxt = (TextView) findViewById(R.id.txtName);
        label1 = (TextView) findViewById(R.id.label1);
        label2 = (TextView) findViewById(R.id.label2);
        label3 = (TextView) findViewById(R.id.label3);
        label4 = (TextView) findViewById(R.id.label4);
        field1 = (TextView) findViewById(R.id.field1);
        field2 = (TextView) findViewById(R.id.field2);
        field3 = (TextView) findViewById(R.id.field3);
        field4 = (TextView) findViewById(R.id.field4);

        buttonText = (ImageButton) findViewById(R.id.btnText);
        buttonText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setData(Uri.parse("smsto:" + phone));
                startActivity(smsIntent);
            }
        });

        buttonCall = (ImageButton) findViewById(R.id.btnCall);
        buttonCall.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phone));
                startActivity(callIntent);
            }
        });

        buttonMail = (ImageButton) findViewById(R.id.btnMail);
        buttonMail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
                mailIntent.setData(Uri.parse("mailto:"));
                mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
                startActivity(mailIntent);
            }
        });

        buttonMap = (ImageButton) findViewById(R.id.btnMap);
        buttonMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent mapIntent = new Intent(Intent.ACTION_VIEW);
                mapIntent.setData(Uri.parse("geo:0,0?q=" + Uri.encode(address)));
                startActivity(mapIntent);
            }
        });

        final Button buttonEdit = (Button) findViewById(R.id.btnEdit);

        Bundle extras = this.getIntent().getExtras();

        if (extras != null){
            contact = (Contact)extras.getSerializable("CONTACT");

            updateFields();

            buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent editContactIntent = new Intent(ViewContact.this, CreateContact.class );
                    editContactIntent.putExtra("CONTACT", contact);
                    startActivityForResult(editContactIntent, 1);
                }
            });
        }
        else
            this.finish();
    }



    private void updateFields() {
        numberOfLabelsNeeded = 0;
        nameTxt.setText(contact.getName());
        phone = contact.getPhone();
        email = contact.getEmail();
        address = contact.getAddress();
        group = contact.getGroup();
        id = contact.getId();
        imgProfilePic.setImageURI(Uri.parse(contact.getImageUri()));

        Log.i("Database","ID #" + Integer.toString(id));

        //If not empty, then set the next available label to say "Phone" and display phone number beneath
        if (phone.compareTo("") != 0){
            numberOfLabelsNeeded++; //Updates counter to know how many labels have been used
            setText("Phone",phone);
        }

        //Same as above, but with email
        if (email.compareTo("") != 0){
            numberOfLabelsNeeded++;
            setText("Email",email);
        }

        if (address.compareTo("") != 0){
            numberOfLabelsNeeded++;
            setText("Address",address);
        }

        if (group.compareTo("") != 0){
            numberOfLabelsNeeded++;
            setText("Group",group);
        }

        hideExtraLabels();  //Makes the labels we don't use invisible.

        if(phone.equals("")){
            buttonCall.setEnabled(false);
            buttonText.setEnabled(false);
        }
        else{
            buttonCall.setEnabled(true);
            buttonText.setEnabled(true);
        }

        if(email.equals("")){
            buttonMail.setEnabled(false);
        }
        else{
            buttonMail.setEnabled(true);
        }

        if(address.equals("")){
            buttonMap.setEnabled(false);
        }
        else{
            buttonMap.setEnabled(true);
        }
    }

    private void setText(String label,String info){
        getLabel(numberOfLabelsNeeded).setText(label);
        getField(numberOfLabelsNeeded).setText(info);
    }

    //Returns the next available label based on how many have been used
    private TextView getLabel(int labelNum){
        switch (labelNum){
            case 1: return label1;
            case 2: return label2;
            case 3: return label3;
            case 4: return label4;
        }
        return null;
    }

    //Returns the next available field depending on how many have been used
    private TextView getField(int fieldNum){
        switch (fieldNum){
            case 1: return field1;
            case 2: return field2;
            case 3: return field3;
            case 4: return field4;
        }

        return null;
    }

    //Hides extra labels that are not used
    private void hideExtraLabels() {  //NOTE: This relies on the fall through of the switch statement
        switch (numberOfLabelsNeeded) {
            case 0:
                label1.setText("");
            case 1:
                label2.setText("");
            case 2:
                label3.setText("");
            case 3:
                label4.setText("");
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ((ContactManagerApplication)getApplication()).mainActivity.updateContacts();
        if(data != null){
            contact = (Contact)data.getExtras().getSerializable("CONTACT");
        }
        updateFields();
    }
}
