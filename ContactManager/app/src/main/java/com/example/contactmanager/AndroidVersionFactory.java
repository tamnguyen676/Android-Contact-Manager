package com.example.contactmanager;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public abstract class AndroidVersionFactory {

    abstract void addFileOpenListener(View button, Activity context);

    protected void createListener(View button, final Activity context){
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent =  new Intent(Intent.ACTION_OPEN_DOCUMENT,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                context.startActivityForResult(intent, CreateContact.PROFILE_PICTURE_EDIT);
            }
        });
    }
}
