package com.example.contactmanager;


import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends AppCompatActivity {

    public static Context context;
    static ArrayList<Contact> contacts = new ArrayList<Contact>();  //Stores all contacts in a list
    static ArrayList<Group> groups = new ArrayList<Group>();    //Stores all groups in a list
    RecyclerView contactRecyclerView;   //Reference object to the RecyclerView
    RecyclerView groupRecyclerView;
    private ContactRecyclerAdapter contactAdapter;
    private GroupRecyclerAdapter groupAdapter;
    int numberOfContacts;
    public static ContactDatabase db;

    private TextView label1, label2, label3, label4;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((ContactManagerApplication)getApplication()).mainActivity = this;
        //context.deleteDatabase("contact-database");

        contactRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        groupRecyclerView = (RecyclerView) findViewById(R.id.recyclerView2);

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec;

        //Sets up tabs
        setUpTab(tabHost,"list",R.id.tabContactList);
        setUpTab(tabHost,"group",R.id.tabGroupList);
        setUpTab(tabHost,"blocked",R.id.tabBlockedList);


        if (Build.VERSION.SDK_INT < 27) {}
        else if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) //check if read storage permission is set
        {
            if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(this, "Read External Storage permission is needed to select picture from device", Toast.LENGTH_SHORT).show();
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 13); //request permissions
        }
        if(checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) //check if read storage permission is set
        {
            if(shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)){
                Toast.makeText(this, "Contact permissions needed to import", Toast.LENGTH_SHORT).show();
            }

            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 13); //request permissions
        }

        final Button btnImport = (Button) findViewById(R.id.btnImport);
        final ArrayList<Contact> importedContacts = new ArrayList<Contact>();


        btnImport.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                loadContacts(importedContacts);
                Collections.sort(importedContacts);

                for(int i = importedContacts.size() - 1; i >= 0; i--){
                    if(contacts.contains(importedContacts.get(i)))
                        importedContacts.remove(i);
                }
                if(importedContacts.size() == 0)
                    Toast.makeText(MainActivity.this, "Contacts are up to date", Toast.LENGTH_LONG).show();
                else{
                    Intent importContacts = new Intent(MainActivity.this, CreateContact.class);
                    importContacts.putExtra("IMPORT_LIST", importedContacts);
                    startActivityForResult(importContacts, 1);
                }
            }
        });

        //Gets RecyclerView ready for contact list
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        contactRecyclerView.setLayoutManager(layoutManager);
        contactAdapter = new ContactRecyclerAdapter(0,this);
        contactRecyclerView.setAdapter(contactAdapter);

        //Gets RecyclerView ready for group list
        LinearLayoutManager layoutManager2
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        groupRecyclerView.setLayoutManager(layoutManager2);
        groupAdapter = new GroupRecyclerAdapter(0,this);
        groupRecyclerView.setAdapter(groupAdapter);

        final Button btnAdd = (Button) findViewById(R.id.btnAdd);
        //if add was clicked, then start new activity
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent createContactIntent = new Intent(MainActivity.this, CreateContact.class );
            startActivityForResult(createContactIntent, 1);
            }
        });

        //context.deleteDatabase("contact-database");
        db = Room.databaseBuilder(getApplicationContext(),
                ContactDatabase.class, "contacts-database").allowMainThreadQueries().build();
        //deleteAllContacts();
        fillListWithDatabase();
        Contact.setTotalContacts(contacts.size());
        updateContacts();
    }

    private void setUpTab(TabHost tabHost,String tag,int tabId) {
        TabHost.TabSpec tabSpec;
        tabSpec =  tabHost.newTabSpec(tag);
        tabSpec.setContent(tabId);
        tabSpec.setIndicator(tag.substring(0,1).toUpperCase() + tag.substring(1));
        tabHost.addTab(tabSpec);
    }

    private void loadContacts(ArrayList<Contact> importedContacts) {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = null;

        try{
            cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        } catch (Exception e){
            Log.e("Error on contact", e.getMessage());
        }

        if(cursor.moveToFirst()){
            do{
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                if(hasPhoneNumber > 0){
                    Cursor cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                    while(cursor2.moveToNext()) {
                        String phoneNumber = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        importedContacts.add(new Contact(name, phoneNumber, "", "", "", ""));
                    }
                    cursor2.close();
                }
            }while(cursor.moveToNext());


        }
        cursor.close();
    }

    //after returning from activity update list view
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Todo Fix bug that causes sort do display "Name"  before "abc" due to uppercase letters
        updateContacts();
    }

    public void viewContact(int contact){
        Intent viewContactIntent = new Intent(MainActivity.this, ViewContact.class);
        viewContactIntent.putExtra("CONTACT", contacts.get(contact));
        startActivity(viewContactIntent);
    }
    public void viewGroup(int group){
        Intent viewContactIntent = new Intent(MainActivity.this, ViewGroupActivity.class);
        viewContactIntent.putExtra("GROUP", groups.get(group));
        startActivity(viewContactIntent);
    }
    public void updateContacts(){
        Collections.sort(contacts); //Sorts contacts in alphabetical order
        Collections.sort(groups);
        contactAdapter.updateList(contacts);
        groupAdapter.updateList(groups);
    }
    public void fillListWithDatabase(){
        ContactEntity[] contactEntityList = db.dao().loadAllContacts();
        for (int i = 0; i < contactEntityList.length; i++){
            contacts.add(entityToContact(contactEntityList[i]));

            //loads contacts to their groups
            if(!entityToContact(contactEntityList[i]).getGroup().equals(""))  //if grouptxt field has a String
            {
                if(existingGroup(entityToContact(contactEntityList[i]).getGroup()) == -1) // if group doesn't already exist
                    groups.add(new Group(entityToContact(contactEntityList[i]).getGroup(),entityToContact(contactEntityList[i]))); //adds contact to the group

                else
                    groups.get(existingGroup(entityToContact(contactEntityList[i]).getGroup())).addContact(entityToContact(contactEntityList[i])); // if group already exists adds contact to the group

            }
        }
    }

    public static Contact entityToContact(ContactEntity entity){
        //Todo fix case where there is no group/image
        Contact contact = new Contact(entity.getName(),entity.getPhone(),entity.getEmail(),
                entity.getAddress(),entity.getGroup(),entity.getImage(),entity.getId());

        return contact;
    }

    public void deleteAllContacts(){
        ContactEntity[] contactEntities = db.dao().loadAllContacts();
        for (int i = 0; i < contactEntities.length; i++){
            db.dao().deleteContact(contactEntities[i]);
        }
    }

    public int existingGroup(String a)  //Checks List of groups to see that group has been created
    {
        int x = 0;
        while (x < groups.size()) {
            if (a.equals(groups.get(x).getGroupName()))
                return x;
            x++;
        }
        return -1;
    }
}
