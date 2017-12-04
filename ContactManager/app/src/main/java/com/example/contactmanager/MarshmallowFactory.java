package com.example.contactmanager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Toast;

//API level 23
public class MarshmallowFactory extends AndroidVersionFactory {

    public void addFileOpenListener(View button, Activity context){
        if(context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) //check if read storage permission is set
        {
            if(context.shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(context, "Read External Storage permission is needed to select picture from device", Toast.LENGTH_SHORT).show();
            }

            context.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CreateContact.MY_GALLERY_REQUEST); //request permissions


        } else {

            createListener(button, context);
        }
    }
}
