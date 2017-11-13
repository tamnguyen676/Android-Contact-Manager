package com.example.contactmanager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        nameTxt = (EditText) findViewById(R.id.txtName);
        phoneTxt = (EditText) findViewById(R.id.txtPhone);
        emailTxt = (EditText) findViewById(R.id.txtEmail);
        addressTxt = (EditText) findViewById(R.id.txtAddress);
        groupTxt = (EditText) findViewById(R.id.txtGroup);

        final Button btnCreateContact = (Button) findViewById(R.id.btnCreateContact);

        btnCreateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addContact(nameTxt.getText().toString(), phoneTxt.getText().toString(), emailTxt.getText().toString(), addressTxt.getText().toString(),groupTxt.getText().toString());
                finish();
                Toast.makeText(getApplicationContext(), nameTxt.getText().toString()+" has been added to your Contacts!", Toast.LENGTH_SHORT).show();
            }
        });

        nameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnCreateContact.setEnabled(!nameTxt.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void addContact(String name, String phone, String email, String address, String group) {

        if(!group.equals(""))  //if grouptxt field has a String
        {
            Contact temp = new Contact(name, phone, email, address, group);
            MainActivity.Contacts.add(temp); //add contact to contact list w group field
            if(existingGroup(group) == -1) // if group doesn't already exist
            {

                MainActivity.Groups.add(new Group(group,temp)); //adds contact to the group
                System.out.println("Created a new group");
            }
            else {
                System.out.println("Trying to add to existing group");
                MainActivity.Groups.get(existingGroup(group)).addContact(temp); // if group already exists adds contact to the group
            }
        }
        else
            MainActivity.Contacts.add(new Contact(name, phone, email, address)); // groupfield was left blank so just adds to contactlist

    }

    public int existingGroup(String a)  //Checks List of groups to see that group has been created
    {
        int x = 0;

            while (x < MainActivity.Groups.size()-1) {
                if (a.equals(MainActivity.Groups.get(x).getGroupName()))
                    return x;
                x++;
            }

        return -1;
    }
}
