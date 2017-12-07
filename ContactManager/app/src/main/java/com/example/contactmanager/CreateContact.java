package com.example.contactmanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Page launched when user wants to create or edit a contact.
 */
public class CreateContact extends AppCompatActivity {


    public static final int PROFILE_PICTURE_EDIT = 10;
    public static final int MY_GALLERY_REQUEST = 11;
    EditText nameTxt, phoneTxt, emailTxt, addressTxt, groupTxt;
    ImageView imgSetProfilePic;
    Uri imageUri = null;
    Bundle oldData;
    private int id;
    boolean blockedContact = false; //Determines if contact is already blocked

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        nameTxt = (EditText) findViewById(R.id.txtName);
        phoneTxt = (EditText) findViewById(R.id.txtPhone);
        emailTxt = (EditText) findViewById(R.id.txtEmail);
        addressTxt = (EditText) findViewById(R.id.txtAddress);
        groupTxt = (EditText) findViewById(R.id.txtGroup);
        imgSetProfilePic = (ImageView) findViewById(R.id.imgSetProfilePicture);

        //check version, if version is less than 23 then run, otherwise check for permission to read storage
        AndroidVersionFactory factory;
        if (Build.VERSION.SDK_INT < 23) {
            factory = new LollipopFactory();
        } else {
            factory = new MarshmallowFactory();
        }
        factory.addFileOpenListener(imgSetProfilePic, CreateContact.this);

        getImportedContacts();

        oldData = this.getIntent().getExtras();

        final Button btnSaveContact = (Button) findViewById(R.id.btnSaveContact);
        final Button btnBlockContact = (Button) findViewById(R.id.btnBlockContact);

        if (oldData != null){
            setBlockedOrUnblocked(btnSaveContact, btnBlockContact); //Changes functionality of block button
        }

        btnBlockContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUri == null)
                    imageUri = Uri.parse("android.resource://com.example.contactmanager/drawable/no_photo");

                Contact newContact = new Contact(nameTxt.getText().toString(),
                        phoneTxt.getText().toString(), emailTxt.getText().toString(),
                        addressTxt.getText().toString(), groupTxt.getText().toString(),
                        imageUri.toString(), id);

                if (blockedContact == false) {   //Blocking contact
                    updateBlockedContact(oldData, newContact);
                    displayBlockOrUnblock(true,newContact);
                }

                else {
                    unblockContact(oldData,newContact);
                    displayBlockOrUnblock(false,newContact);
                }
            }
        });

        btnSaveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //if imageUri is null then no image was selected, set to default photo.
            if(imageUri == null)
                imageUri = Uri.parse("android.resource://com.example.contactmanager/drawable/no_photo");

            Contact newContact = null;
            if (oldData == null) {
                newContact = new Contact(nameTxt.getText().toString(),
                        phoneTxt.getText().toString(), emailTxt.getText().toString(),
                        addressTxt.getText().toString(), groupTxt.getText().toString(),
                        imageUri.toString());
            }
            else {
                newContact = new Contact(nameTxt.getText().toString(),
                        phoneTxt.getText().toString(), emailTxt.getText().toString(),
                        addressTxt.getText().toString(), groupTxt.getText().toString(),
                        imageUri.toString(),id);
            }
            updateContact(oldData, newContact);
            putExtraContact(newContact);
            Toast.makeText(getApplicationContext(), nameTxt.getText().toString()+" has been saved to your contacts!", Toast.LENGTH_SHORT).show();
            }
        });

        nameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnSaveContact.setEnabled(!nameTxt.getText().toString().trim().isEmpty());
                btnBlockContact.setEnabled(!nameTxt.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    /**
     * Looks at a contact and determines if they are blocked or unblocked.
     * It then sets the appropriate flags and changes the text of the button
     * to prepare for the right functionality when the button is block/unblock
     * is pressed.
     * @param btnSaveContact The reference to the button that saves contacts
     * @param btnBlockContact The reference to the button that can block/unblock contacts
     */
    private void setBlockedOrUnblocked(Button btnSaveContact, Button btnBlockContact) {
        Contact currentContact = (Contact)oldData.getSerializable("CONTACT");
        if(currentContact != null){
            fillExistingContactInfo(currentContact);
            if (MainActivity.blockedcontacts.contains(currentContact)){
                btnBlockContact.setText("Unblock Contact");
                blockedContact = true;
            }
            else {
                btnBlockContact.setText("Block Contact");
                blockedContact = false;
            }

            btnSaveContact.setEnabled(true);
            btnBlockContact.setEnabled(true);
        }
    }

    /**
     * Fills the Create Contact page with info from a contact if
     * it already exists
     * @param currentContact The contact object to be displayed
     */
    private void fillExistingContactInfo(Contact currentContact) {
        nameTxt.setText(currentContact.getName());
        phoneTxt.setText(currentContact.getPhone());
        emailTxt.setText(currentContact.getEmail());
        addressTxt.setText(currentContact.getAddress());
        groupTxt.setText(currentContact.getGroup());
        imageUri = Uri.parse(currentContact.getImageUri());
        id = currentContact.getId();
        try {
            imgSetProfilePic.setImageBitmap(uriToBitmap(imageUri)); //--------------------------
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a list of all contacts to be imported then imports them
     * into the Contact Manager. Imports to the ArrayList and to the Database.
     */
    private void getImportedContacts() {
        ArrayList<Contact> importList = (ArrayList<Contact>) getIntent().getSerializableExtra("IMPORT_LIST");

        if(importList != null){
            Contact newContact;
            for(int i = 0; i < importList.size(); i++){
                newContact = importList.get(i);
                fillExistingContactInfo(newContact);

                if(imageUri == null || imageUri.toString().isEmpty())
                    imageUri = Uri.parse("android.resource://com.example.contactmanager/drawable/no_photo");
                newContact = new Contact(nameTxt.getText().toString(), phoneTxt.getText().toString(), emailTxt.getText().toString(), addressTxt.getText().toString(),groupTxt.getText().toString(), imageUri.toString());
                updateContact(oldData, newContact);
                putExtraContact(newContact);
            }
        }
    }

    /**
     * Accepts a Uri encoded file path and returns the image data stored there
     *
     * @param imgUri the image file path
     * @return the bitmap image stored at file path
     *
     */
    private Bitmap uriToBitmap(Uri imgUri) throws IOException{
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(imgUri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);

        parcelFileDescriptor.close();
        return bitmap;
    }

    /**
     * Updates a contact and adds them to the blocked list
     *
     * @param oldData intent data containing the old contact, if there is existing contact data to edit
     * @param newContact the new contact to be stored
     */
    private void updateBlockedContact(Bundle oldData,Contact newContact){
        if(oldData != null){ //If there is an old version of the contact, delete it first
            Contact oldContact = (Contact)oldData.getSerializable("CONTACT");
            MainActivity.contacts.remove(oldContact);
            ((ContactManagerApplication)getApplication()).mainActivity.updateContacts();
            MainActivity.db.dao().deleteContact(CreateContact.contactToEntity(oldContact));
            if(!oldContact.getGroup().isEmpty()){ //If they belonged to a group, remove them from it
                try{
                    Group oldGroup = MainActivity.groups.get(existingGroup(oldContact.getGroup()));
                    oldGroup.removeContact(oldContact);
                    if(oldGroup.size == 0){ //The the group is empty now, delete it
                        MainActivity.groups.remove(oldGroup);
                    }
                }
                catch(NonexistentGroupException exception){
                    System.out.print("Tried to remove a contact from a group that doesn't exist: " + exception.groupName);
                }
            }
        }
        MainActivity.blockedcontacts.add(newContact); //Create the contact
        addBlockedContactToDatabase(newContact);
    }

    /**
     * Unblocks a contact add deletes them from blocked list. Add them to normal.
     * @param oldData intent data containing the old contact, if there is existing contact data to edit
     * @param newContact the new contact to be stored
     */
    private void unblockContact(Bundle oldData,Contact newContact){
        Contact oldContact = null;
        if(oldData != null){ //If there is an old version of the contact
            oldContact = (Contact)oldData.getSerializable("CONTACT");
            MainActivity.contacts.add(newContact);
            ((ContactManagerApplication)getApplication()).mainActivity.updateContacts();
            MainActivity.db.dao().insertContact(CreateContact.contactToEntity(newContact));
            //Todo update database
            if(!newContact.getGroup().isEmpty()){ //If they belonged to a group, add them to it again
                try{
                    Group group = MainActivity.groups.get(existingGroup(newContact.getGroup()));
                    group.addContact(newContact);
                }
                catch(NonexistentGroupException exception){
                    //If group does not exist, create it
                    MainActivity.groups.add(new Group(newContact.getGroup(),newContact));
                }
            }
        }
        MainActivity.blockedcontacts.remove(oldContact); //Remove the blocked contact from database
        MainActivity.db2.dao().deleteContact(contactToEntity(oldContact));
    }



    /**
     * Adds a contact to the list of contacts and their designated group. The group is created if it doesn't already exist
     *
     * @param newContact the contact being added to the list
     */
    protected void addContactToArray(Contact newContact) {

        MainActivity.contacts.add(newContact); //Create the contact

        if(!newContact.getGroup().equals(""))  //if grouptxt field has a String
        {
            try{
                MainActivity.groups.get(existingGroup(newContact.getGroup())).addContact(newContact); // if group already exists adds contact to the group
            }
            catch(NonexistentGroupException exception){ // if group doesn't already exist
                MainActivity.groups.add(new Group(exception.groupName,newContact)); //adds contact to the group
            }
        }
    }

    /**
     * Transfers contact data into an object that can be stored in the database
     *
     * @param contact the contact to retireve data from
     * @return an object containing the contact's data which can be stored in the database
     */
    public static ContactEntity contactToEntity(Contact contact){
        ContactEntity newContact = new ContactEntity();
        newContact.setName(contact.getName());
        newContact.setId(contact.getId());
        newContact.setPhone(contact.getPhone());
        newContact.setAddress(contact.getAddress());
        newContact.setGroup(contact.getGroup());
        newContact.setEmail(contact.getEmail());
        newContact.setImage(contact.getImageUri());

        return newContact;
    }

    /**
     * Adds the contact to the contacts database
     * @param newContact The new contact to be added
     */
    protected void addContactToDatabase(Contact newContact){
        MainActivity.db.dao().insertContact(contactToEntity(newContact));
    }

    /**
     * Adds the contact to the blocked database
     * @param newContact The contact to be blocked
     */
    private void addBlockedContactToDatabase(Contact newContact){
        MainActivity.db2.dao().insertContact(contactToEntity(newContact));
    }


    /**
     * Stores the new contact in the list and in the database. If the new contact is an edited version of an existing one, the old version is removed
     *
     * @param oldData intent data containing the old contact, if there is existing contact data to edit
     * @param newContact the new contact to be stored
     */
    private void updateContact(Bundle oldData, Contact newContact){
        if(oldData != null){ //If there is an old version of the contact, delete it first
            Contact oldContact = (Contact)oldData.getSerializable("CONTACT");
            MainActivity.contacts.remove(oldContact);
            if(!oldContact.getGroup().isEmpty()){ //If they belonged to a group, remove them from it
                try{
                    Group oldGroup = MainActivity.groups.get(existingGroup(oldContact.getGroup()));
                    oldGroup.removeContact(oldContact);
                    if(oldGroup.size == 0){ //The the group is empty now, delete it
                        MainActivity.groups.remove(oldGroup);
                    }
                }
                catch(NonexistentGroupException exception){
                    System.out.print("Tried to remove a contact from a group that doesn't exist: " + exception.groupName);
                }
            }

        }
        addContactToArray(newContact); //Add the new contact
        addContactToDatabase(newContact);   //Adds contact to database
        Log.i("Database","Added contact");
    }

    /**
     * Sends a reference to a contact via intents and sets result.
     * @param contact Contact object to be sent
     */
    private void putExtraContact(Contact contact){
        Intent data = new Intent();
        data.putExtra("CONTACT",contact);
        setResult(RESULT_OK, data);
        finish();
    }

    /**
     * Returns the index of the requested group, if it exists.
     *
     * @param groupName the name of the group to look for
     * @return the index of the group in the group list
     * @exception NonexistentGroupException thrown if the group name is not found in the group list
     */
    public int existingGroup(String groupName) throws NonexistentGroupException  //Checks List of groups to see that group has been created
    {
        int x = 0;

            while (x < MainActivity.groups.size()) {
                if (groupName.equals(MainActivity.groups.get(x).getGroupName()))
                    return x;
                x++;
            }

        throw new NonexistentGroupException(groupName);
    }

    /**
     * Sets result of the intent and displays a toast message depending on whethr or not
     * a contact is to be blocked or unblocked.
     * @param isBlocked True if contact is to blocked, false if contact is to be unblocked
     * @param newContact The Contact object of the contact to be blocked/unblocked
     */
    private void displayBlockOrUnblock(boolean isBlocked,Contact newContact){
        putExtraContact(newContact);

        String message = " has been blocked";
        if (!isBlocked){
            message = " has been unblocked";
        }

        Toast.makeText(getApplicationContext(),
                nameTxt.getText().toString() + message,
                Toast.LENGTH_SHORT).show();
    }

    //When returning from an open image activity, get the image to use for the contact
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK);
        if(requestCode == PROFILE_PICTURE_EDIT){ //return from gallery

            if (data == null){
                return;
            }

            //address of the image
            if (data.getData() != null) {
                imageUri = data.getData();
            }
            else {
                imageUri = Uri.parse("android.resource://com.example.contactmanager/drawable/no_photo");
            }

            try {
                imgSetProfilePic.setImageBitmap(uriToBitmap(imageUri));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class NonexistentGroupException extends Exception{
        public String groupName;

        public NonexistentGroupException(String groupName){
            this.groupName = groupName;
        }
    }
}


