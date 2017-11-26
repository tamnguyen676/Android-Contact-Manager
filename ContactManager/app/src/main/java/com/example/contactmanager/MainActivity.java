package com.example.contactmanager;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;




public class MainActivity extends AppCompatActivity implements ContactRecyclerAdapter.ListItemClickListener{

    static ArrayList<Contact> Contacts = new ArrayList<Contact>();
    static ArrayList<Group> Groups = new ArrayList<Group>();
    RecyclerView contactRecyclerView;   //Reference object to the RecyclerView
    private ContactRecyclerAdapter contactAdapter;
    ListView groupListView ;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        Collections.sort(Contacts); //Sorts contacts in alphabetical order
        contactAdapter.updateList(Contacts);    //Sends the updated list to the Adapter
    }

    @Override
    public void onListItemClick(int positionClicked){
        if (toast != null){
            toast.cancel();
        }

        String clickedMessage = "Item #" + positionClicked + " clicked.";
        toast = Toast.makeText(this, clickedMessage, Toast.LENGTH_LONG);
        toast.show();
    }

    public void viewContact(int contact){
        Intent viewContactIntent = new Intent(MainActivity.this, ViewContact.class);
        viewContactIntent.putExtra("CONTACT", Contacts.get(contact));
        startActivity(viewContactIntent);
    }

}
