package com.example.contactmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Collections;


public class ViewGroupActivity extends AppCompatActivity {


    public static Context context;
    static ArrayList<Contact> contacts = new ArrayList<Contact>();
    RecyclerView contactRecyclerView2;   //Reference object to the RecyclerView
    private ContactRecyclerAdapter contactAdapter2;
    int numberOfContacts;

    TextView nameTxt, label1, label2, label3, label4, field1, field2, field3, field4;
    //ImageButton buttonText, buttonMail, buttonCall, buttonMap;
   // String phone, email, address, group;
    int id;
    //ImageView imgProfilePic;
    //int numberOfLabelsNeeded;   //This keeps track of how many labels we need.
    Group group;


    @Override
    public void onCreate(Bundle SavedInstanceState) {

        super.onCreate((SavedInstanceState));
        setContentView((R.layout.activity_view_group_activity));
        contactRecyclerView2 = (RecyclerView) findViewById(R.id.recyclerView3);
        // imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        contactRecyclerView2.setLayoutManager(layoutManager);
        contactAdapter2 = new ContactRecyclerAdapter(0, this);
        contactRecyclerView2.setAdapter(contactAdapter2);


        Bundle extras = this.getIntent().getExtras();

        if (extras != null)
            group = (Group) extras.getSerializable("GROUP");

        else
            this.finish();

        TextView BarTitle = (TextView) findViewById(R.id.toolBarTitle);
        BarTitle.setText(group.getGroupName()+ " Members");
        contacts = group.Contacts;
        updateContacts();
     }

        //final Button buttonEdit = (Button) findViewById(R.id.btnEdit);

    public void viewContact(int contact){
        Intent viewContactIntent = new Intent(ViewGroupActivity.this, ViewContact.class);
        viewContactIntent.putExtra("CONTACT", contacts.get(contact));
        startActivity(viewContactIntent);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            //Todo Fix bug that causes sort do display "Name"  before "abc" due to uppercase letters
            updateContacts();
        }
        public void updateContacts()
        {
        Collections.sort(contacts); //Sorts contacts in alphabetical order
        contactAdapter2.updateList(contacts);

         }
}
