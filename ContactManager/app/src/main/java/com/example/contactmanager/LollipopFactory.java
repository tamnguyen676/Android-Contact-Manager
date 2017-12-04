package com.example.contactmanager;

import android.app.Activity;
import android.content.Intent;
import android.view.View;


//API level 22
public class LollipopFactory extends AndroidVersionFactory {

    public void addFileOpenListener(View button, Activity context){
        createListener(button, context);
    }
}
