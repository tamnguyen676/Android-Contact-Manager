package com.example.contactmanager;

import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertEquals;


//Created by Artem Zhuchkov
public class TestAddContactToBlocked {

    ViewContact activity;
    CreateContact createContactActivity;
    Contact contact1;
    Contact contact2;
    Contact contact3;
    Contact group1contact;
    Bundle oldData;

    @Before
    public void setupTest(){
        contact1 = new Contact("Contact 1", "", "","","","");
        contact2 = new Contact("Contact 2", "", "","","","");

        contact3 = new Contact("Contact 3", "214", "","","","");
        group1contact = new Contact("Contact 4", "", "","","Groupie","");
        MainActivity.blockedcontacts = new ArrayList<Contact>();
        createContactActivity = new CreateContact();
    }

    @Test
    public void addOne(){
        createContactActivity.updateBlockedContact(oldData,contact1);
        assertEquals("Expected list of size 1",1,MainActivity.blockedcontacts.size());
    }

    @Test
    public void addTwo(){
        createContactActivity.updateBlockedContact(oldData,contact1);
        createContactActivity.updateBlockedContact(oldData,contact2);
        assertEquals("Expected list of size 2",2,MainActivity.blockedcontacts.size());
    }

    @Test
    public void addContactWithPhoneNumber(){
        createContactActivity.updateBlockedContact(oldData,contact3);
        assertEquals("Expected list of size 1",1,MainActivity.blockedcontacts.size());
    }

    @Test
    public void addContactsFromDifferentGroups(){
        createContactActivity.updateBlockedContact(oldData,contact1);
        createContactActivity.updateBlockedContact(oldData,group1contact);
        assertEquals("Expected list of size 2",2,MainActivity.blockedcontacts.size());
    }

}