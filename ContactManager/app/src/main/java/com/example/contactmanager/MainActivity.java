package com.example.contactmanager;


import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends AppCompatActivity {

    public static Context context;
    static ArrayList<Contact> contacts = new ArrayList<Contact>();  //Stores all contacts in a list
    static ArrayList<Group> groups = new ArrayList<Group>();    //Stores all groups in a list
    static ArrayList<Contact> blockedcontacts = new ArrayList<Contact>();  //Stores all contacts in a list
    RecyclerView contactRecyclerView;   //Reference object to the RecyclerView
    RecyclerView groupRecyclerView;
    RecyclerView blockedRecyclerView;
    private ContactRecyclerAdapter contactAdapter;
    private GroupRecyclerAdapter groupAdapter;
    private BlockedRecyclerAdapter blockedcontactAdapter;
    int numberOfContacts;
    public static ContactDatabase db, db2;

    private TextView label1, label2, label3, label4;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((ContactManagerApplication)getApplication()).mainActivity = this;

        contactRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        groupRecyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
        blockedRecyclerView = (RecyclerView) findViewById(R.id.recyclerView3);
        setupAllTabs();

        checkPermissions();

        final Button btnImport = (Button) findViewById(R.id.btnImport);
        final ArrayList<Contact> importedContacts = new ArrayList<Contact>();

        btnImport.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Import Contacts")
                        .setMessage("Would you like to import existing contacts from the default contact app?")
                        .setIcon(android.R.drawable.ic_menu_save)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                importContacts(importedContacts);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();

            }
        });

        setupRecyclerViews();

        final Button btnAdd = (Button) findViewById(R.id.btnAdd);
        //if add was clicked, then start new activity
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent createContactIntent = new Intent(MainActivity.this, CreateContact.class );
            startActivityForResult(createContactIntent, 1);
            }
        });

        //SQLite database that stores all contacts
        db = Room.databaseBuilder(getApplicationContext(),
                ContactDatabase.class, "contacts-database").allowMainThreadQueries().build();
        db2 = Room.databaseBuilder(getApplicationContext(),
                ContactDatabase.class, "contacts-database2").allowMainThreadQueries().build();

        //Used for debugging purposes. Uncomment to start app with fresh database.
        //deleteAllContacts();
        //deleteAllBlocked();

        fillContactListWithDatabase();
        fillBlockedListWithDatabase();
        Contact.setTotalContacts(contacts.size());
        updateContacts();
    }   //End onCreate()

    /**
     * Sets up the ReyclerViews for the contacts and the groups. ReyclerViews allows for smooth scrolling
     * even with large amounts of items. This method is called via once in onCreate() in MainActivity.
     */
    private void setupRecyclerViews() {
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


        LinearLayoutManager layoutManager3
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        blockedRecyclerView.setLayoutManager(layoutManager3);
        blockedcontactAdapter = new BlockedRecyclerAdapter(0,this);
        blockedRecyclerView.setAdapter(blockedcontactAdapter);
    }

    /**
     * Checks permissions and asks the user for storage and contact access.
     */
    private void checkPermissions() {
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
    }

    /**
     * Sets up all the tabs in the MainActivity page
     */
    private void setupAllTabs() {
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec;

        //Sets up tabs
        setupTab(tabHost,"list",R.id.tabContactList);
        setupTab(tabHost,"group",R.id.tabGroupList);
        setupTab(tabHost,"blocked",R.id.tabBlockedList);
    }

    /**
     * Sets up individual tabs for the MainActivity page. Called via onCreate() at launch.
     *
     * @param tabHost The tabHost reference object created in the MainActivity
     * @param tag The tag and name of the tab to be set up
     * @param tabId The ID value of the tab from the XML layout
     */
    private void setupTab(TabHost tabHost, String tag, int tabId) {
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

    //After returning from activity update list view
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateContacts();
    }

    /**
     * Imports contacts
     * @param importedContacts An ArrayList that contains imported contacts
     */
    private void importContacts(ArrayList<Contact> importedContacts) {
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
    public void viewBlockedContact(int contact){
        Intent viewContactIntent = new Intent(MainActivity.this, ViewContact.class);
        viewContactIntent.putExtra("CONTACT", blockedcontacts.get(contact));
        startActivity(viewContactIntent);
    }

    /**
     * Updates the main page with newly added or edited contacts and groups by
     * sending the updated list to the adapter. Sorts in alphabetical order
     * before displaying.
     */
    public void updateContacts(){
        Collections.sort(contacts); //Sorts contacts in alphabetical order
        Collections.sort(groups);
        Collections.sort(blockedcontacts);
        contactAdapter.updateList(contacts);
        groupAdapter.updateList(groups);
        blockedcontactAdapter.updateList(blockedcontacts);
    }

    /**
     * Accesses the SQLite database and fills the contacts ArrayList with all users from
     * from the database.
     */
    public void fillContactListWithDatabase(){
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

    public void fillBlockedListWithDatabase() {
        ContactEntity[] contactEntityList = db2.dao().loadAllContacts();
        for (int i = 0; i < contactEntityList.length; i++) {
            blockedcontacts.add(entityToContact(contactEntityList[i]));
        }
    }
    /**
     * Converts a ContactEntity object used by the database to a Contact object used
     * by the rest of the program. All fields of the ContactEntity object is transferred
     * over to the Contact object.
     * @param entity A ContactEntity object that the SQLite database uses.
     * @return A Contact object that can be stored in the ArrayList.
     */
    public static Contact entityToContact(ContactEntity entity){
        Contact contact = new Contact(entity.getName(),entity.getPhone(),entity.getEmail(),
                entity.getAddress(),entity.getGroup(),entity.getImage(),entity.getId());

        return contact;
    }

    /**
     * Deletes all entries from the SQLite database. Useful for debugging.
     */
    public void deleteAllContacts(){
        ContactEntity[] contactEntities = db.dao().loadAllContacts();
        for (int i = 0; i < contactEntities.length; i++){
            db.dao().deleteContact(contactEntities[i]);
        }
    }
    public void deleteAllBlocked(){
        ContactEntity[] contactEntities = db2.dao().loadAllContacts();
        for (int i = 0; i < contactEntities.length; i++){
            db2.dao().deleteContact(contactEntities[i]);
        }
    }
    /**
     * Finds the index of a group in the ArrayList given the name.
     *
     * @param groupName The name of the group to be found
     * @return The index of the group in the ArrayList, or -1 if the group was not found.
     */
    public int existingGroup(String groupName)
    {
        int x = 0;
        while (x < groups.size()) {
            if (groupName.equals(groups.get(x).getGroupName()))
                return x;
            x++;
        }
        return -1;
    }
}
