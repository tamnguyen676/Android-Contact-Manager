package com.example.contactmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class ViewContact extends AppCompatActivity {



    TextView nameTxt, label1, label2, label3, label4, field1, field2, field3, field4;
    ImageButton buttonText, buttonMail, buttonCall, buttonMap;
    String phone, email, address, group, imageUri;
    ImageView imgProfilePic;
    int numberOfLabelsNeeded,id;   //This keeps track of how many labels we need.
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

        setupButtons();

        final Button buttonEdit = (Button) findViewById(R.id.btnEdit);
        final Button buttonDelete = (Button) findViewById(R.id.btnDelete);

        Bundle extras = this.getIntent().getExtras();

        if (extras != null){    //If creating new contact and not editing
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

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(ViewContact.this)
                            .setTitle("Remove Contact")
                            .setMessage("Are you sure that you want to remove " + contact.getName() + " from your contacts?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteContact();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                }
            });
        }
        else
            this.finish();
    }

    /**
     * Adds click listeners to the dial, sms, email, and map buttons.
     */
    private void setupButtons() {
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
    }

    /**
     * Displays only the fields for which there are values for. Gets all
     * values from the Contact object and displays it accordingly on the page.
     */
    private void updateFields() {
        numberOfLabelsNeeded = 0;
        nameTxt.setText(contact.getName());
        phone = contact.getPhone();
        email = contact.getEmail();
        address = contact.getAddress();
        group = contact.getGroup();
        id = contact.getId();
        imageUri = contact.getImageUri();
        imgProfilePic.setImageURI(Uri.parse(imageUri));

        Log.i("Database","ID #" + Integer.toString(id));    //Show contact ID in log

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

        enableButtons();    //Enable the buttons only if the proper data field is available
    }

    /**
     * Allows the user to click the buttons only if the proper
     * data fields are available to do so. For example, users
     * can only click email if there is an email associated.
     */
    private void enableButtons() {
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

    /**
     * Sets what type of data the label should display, then displays
     * that data underneath in the field.
     * @param label The type data to be displayed (Phone, Group, etc...)
     * @param info The value of the data field itself
     */
    private void setText(String label,String info){
        getLabel(numberOfLabelsNeeded).setText(label);
        getField(numberOfLabelsNeeded).setText(info);
    }

    /**
     * Finds and returns a reference to the label
     * matching the integer passed into it.
     * @param labelNum Which number label is wanted
     * @return A TextView reference to that label
     */
    private TextView getLabel(int labelNum){
        switch (labelNum){
            case 1: return label1;
            case 2: return label2;
            case 3: return label3;
            case 4: return label4;
        }
        return null;
    }

    /**
     * Finds and returns a reference to the text field
     * matching the integer passed into it.
     * @param fieldNum Which number field is wanted
     * @return A TextView reference to that field
     */
    private TextView getField(int fieldNum){
        switch (fieldNum){
            case 1: return field1;
            case 2: return field2;
            case 3: return field3;
            case 4: return field4;
        }

        return null;
    }

    /**
     * Given the number of labels used, makes all of the
     * labels that are not used invisible.
     */
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

    /**
     * Deletes the contact that is being displayed on the current
     * page. Removes it from the list and the database and updates the coutner.
     */
    private void deleteContact(){
        Toast.makeText(ViewContact.this, "Removed " + contact.getName() + " from contacts", Toast.LENGTH_SHORT).show();
        MainActivity.contacts.remove(contact);
        Contact.setTotalContacts(Contact.getTotalContacts() - 1);
        ((ContactManagerApplication)getApplication()).mainActivity.updateContacts();
        MainActivity.db.dao().deleteContact(CreateContact.contactToEntity(contact));
        this.finish();
    }
}
