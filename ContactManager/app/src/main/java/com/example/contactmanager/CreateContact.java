package com.example.contactmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class CreateContact extends AppCompatActivity {

    EditText nameTxt, phoneTxt, emailTxt, addressTxt, groupTxt;
    Bundle oldData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        nameTxt = (EditText) findViewById(R.id.txtName);
        phoneTxt = (EditText) findViewById(R.id.txtPhone);
        emailTxt = (EditText) findViewById(R.id.txtEmail);
        addressTxt = (EditText) findViewById(R.id.txtAddress);
        groupTxt = (EditText) findViewById(R.id.txtGroup);

        oldData = this.getIntent().getExtras();

        final Button btnSaveContact = (Button) findViewById(R.id.btnSaveContact);

        if(oldData != null){
            Contact currentContact = (Contact)oldData.getSerializable("CONTACT");
            nameTxt.setText(currentContact.getName());
            phoneTxt.setText(currentContact.getPhone());
            emailTxt.setText(currentContact.getEmail());
            addressTxt.setText(currentContact.getAddress());
            groupTxt.setText(currentContact.getGroup());

            btnSaveContact.setEnabled(true);
        }



        btnSaveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact newContact = new Contact(nameTxt.getText().toString(), phoneTxt.getText().toString(), emailTxt.getText().toString(), addressTxt.getText().toString(),groupTxt.getText().toString());
                updateContact(oldData, newContact);
                Intent data = new Intent();
                data.putExtra("CONTACT",newContact);
                setResult(RESULT_OK, data);
                finish();
                Toast.makeText(getApplicationContext(), nameTxt.getText().toString()+" has been saved to your Contacts!", Toast.LENGTH_SHORT).show();
            }
        });

        nameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnSaveContact.setEnabled(!nameTxt.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void addContact(Contact newContact) {

        MainActivity.Contacts.add(newContact); //Create the contact

        if(!newContact.getGroup().equals(""))  //if grouptxt field has a String
        {
            if(existingGroup(newContact.getGroup()) == -1) // if group doesn't already exist
            {

                MainActivity.Groups.add(new Group(newContact.getGroup(),newContact)); //adds contact to the group
                System.out.println("Created a new group");
            }
            else {
                System.out.println("Trying to add to existing group");
                MainActivity.Groups.get(existingGroup(newContact.getGroup())).addContact(newContact); // if group already exists adds contact to the group
            }
        }

    }

    private void updateContact(Bundle oldData, Contact newContact){
        if(oldData != null){ //If there is an old version of the contact, delete it first
            Contact oldContact = (Contact)oldData.getSerializable("CONTACT");
            MainActivity.Contacts.remove(oldContact);
            if(oldContact.getGroup() != ""){ //If they belonged to a group, remove them from it
                Group oldGroup = MainActivity.Groups.get(existingGroup(oldContact.getGroup()));
                oldGroup.removeContact(oldContact);
                if(oldGroup.size == 0){ //The the group is empty now, delete it
                    MainActivity.Groups.remove(oldGroup);
                }
            }

        }
        addContact(newContact); //Add the new contact
    }

    public int existingGroup(String a)  //Checks List of groups to see that group has been created
    {
        int x = 0;

            while (x < MainActivity.Groups.size()) {
                if (a.equals(MainActivity.Groups.get(x).getGroupName()))
                    return x;
                x++;
            }

        return -1;
    }
}
