package com.example.contactmanager;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;




public class MainActivity extends AppCompatActivity {

    public static Context context;
    static ArrayList<Contact> Contacts = new ArrayList<Contact>();
    static ArrayList<Group> Groups = new ArrayList<Group>();
    RecyclerView contactRecyclerView;   //Reference object to the RecyclerView
    private ContactRecyclerAdapter contactAdapter;
    ListView groupListView ;

    private TextView label1, label2, label3, label4;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((ContactManagerApplication)getApplication()).mainActivity = this;

        contactRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        groupListView = (ListView) findViewById(R.id.grouplistView);

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        //sets up tab1
        TabHost.TabSpec tabSpec =  tabHost.newTabSpec("list");
        tabSpec.setContent(R.id.tabContactList);
        tabSpec.setIndicator("List");
        tabHost.addTab(tabSpec);

        //sets up tab2
        tabSpec =  tabHost.newTabSpec("group");
        tabSpec.setContent(R.id.tabGroupList);
        tabSpec.setIndicator("Group");
        tabHost.addTab(tabSpec);

        //sets up tab3
        tabSpec =  tabHost.newTabSpec("blocked");
        tabSpec.setContent(R.id.tabBlockedList);
        tabSpec.setIndicator("Blocked");
        tabHost.addTab(tabSpec);

        if (Build.VERSION.SDK_INT < 23) {}
        else if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) //check if read storage permission is set
        {
            if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(this, "Read External Storage permission is needed to select picture from device", Toast.LENGTH_SHORT).show();
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 13); //request permissions


        }

        //Gets RecyclerView ready for contact list
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        contactRecyclerView.setLayoutManager(layoutManager);
        contactAdapter = new ContactRecyclerAdapter(0,this);
        contactRecyclerView.setAdapter(contactAdapter);

        final Button btnAdd = (Button) findViewById(R.id.btnAdd);
        //if add was clicked, then start new activity
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent createContactIntent = new Intent(MainActivity.this, CreateContact.class );
            startActivityForResult(createContactIntent, 1);
            }
        });
    }

    //after returning from activity update list view
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Todo Fix bug that causes sort do display "Name"  before "abc" due to uppercase letters
        updateContacts();
    }


    public void viewContact(int contact){
        Intent viewContactIntent = new Intent(MainActivity.this, ViewContact.class);
        viewContactIntent.putExtra("CONTACT", Contacts.get(contact));
        startActivity(viewContactIntent);
    }

    public void updateContacts(){
        Collections.sort(Contacts); //Sorts contacts in alphabetical order
        contactAdapter.updateList(Contacts);
    }

}
