package com.example.contactmanager;

import android.app.Activity;
import android.content.Intent;
import android.view.View;


//API level 22
public class LollipopFactory extends AndroidVersionFactory {

    /**
     * Adds a click listener to the button that will allow the user to select an image
     *
     * @param button The view to add the listener to
     * @param context The calling activity
     */
    public void addFileOpenListener(View button, Activity context){
        createListener(button, context);
    }
}
