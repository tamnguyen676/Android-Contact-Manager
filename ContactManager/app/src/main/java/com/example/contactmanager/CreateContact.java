package com.example.contactmanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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


public class CreateContact extends AppCompatActivity {


    public static final int PROFILE_PICTURE_EDIT = 10;
    public static final int MY_GALLERY_REQUEST = 11;
    EditText nameTxt, phoneTxt, emailTxt, addressTxt, groupTxt;
    ImageView imgSetProfilePic;
    Uri imageUri = null;
    Bundle oldData;
    private int id;

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
        if (Build.VERSION.SDK_INT < 23) {
            imgSetProfilePic.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent intent =  new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PROFILE_PICTURE_EDIT);

                }

            });
        } else {
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) //check if read storage permission is set
            {
                if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Toast.makeText(this, "Read External Storage permission is needed to select picture from device", Toast.LENGTH_SHORT).show();
                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_GALLERY_REQUEST); //request permissions


            } else {

                imgSetProfilePic.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Intent intent =  new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, PROFILE_PICTURE_EDIT);

                    }

                });


            }

        }




        oldData = this.getIntent().getExtras();

        final Button btnSaveContact = (Button) findViewById(R.id.btnSaveContact);

        if(oldData != null){
            Contact currentContact = (Contact)oldData.getSerializable("CONTACT");
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
            btnSaveContact.setEnabled(true);
        }



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
            Intent data = new Intent();
            data.putExtra("CONTACT",newContact);
            setResult(RESULT_OK, data);
            finish();
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
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK);
            if(requestCode == PROFILE_PICTURE_EDIT){ //return from gallery

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

    //accepts a Uri and converts it into a bitmap
    private Bitmap uriToBitmap(Uri imgUri) throws IOException{
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(imgUri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);

        parcelFileDescriptor.close();
        return bitmap;
    }

    private void addContactToArray(Contact newContact) {

        MainActivity.contacts.add(newContact); //Create the contact

        if(!newContact.getGroup().equals(""))  //if grouptxt field has a String
        {
            if(existingGroup(newContact.getGroup()) == -1) // if group doesn't already exist
            {

                MainActivity.groups.add(new Group(newContact.getGroup(),newContact)); //adds contact to the group
                System.out.println("Created a new group");
            }
            else {
                System.out.println("Trying to add to existing group");
                MainActivity.groups.get(existingGroup(newContact.getGroup())).addContact(newContact); // if group already exists adds contact to the group
            }
        }
    }

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

    private void addContactToDatabase(Contact newContact){
        MainActivity.db.dao().insertContact(contactToEntity(newContact));
    }

    private void updateContact(Bundle oldData, Contact newContact){
        if(oldData != null){ //If there is an old version of the contact, delete it first
            Contact oldContact = (Contact)oldData.getSerializable("CONTACT");
            MainActivity.contacts.remove(oldContact);
            //Todo update database
            if(!oldContact.getGroup().isEmpty()){ //If they belonged to a group, remove them from it
                Group oldGroup = MainActivity.groups.get(existingGroup(oldContact.getGroup()));
                oldGroup.removeContact(oldContact);
                if(oldGroup.size == 0){ //The the group is empty now, delete it
                    MainActivity.groups.remove(oldGroup);
                }
            }

        }
        addContactToArray(newContact); //Add the new contact
        addContactToDatabase(newContact);   //Adds contact to database
        Log.i("Database","Added contact");
    }


    public int existingGroup(String a)  //Checks List of groups to see that group has been created
    {
        int x = 0;

            while (x < MainActivity.groups.size()) {
                if (a.equals(MainActivity.groups.get(x).getGroupName()))
                    return x;
                x++;
            }

        return -1;
    }
}
