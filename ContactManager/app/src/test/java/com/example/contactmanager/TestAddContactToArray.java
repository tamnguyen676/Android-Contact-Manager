package com.example.contactmanager;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


//Created by Tam Nguyen
public class TestAddContactToArray {

    ViewContact activity;
    CreateContact createContactActivity;
    Contact contact1;
    Contact contact2;
    Contact group1contact;
    Contact group2contact;

    @Before
    public void setupTest(){
        contact1 = new Contact("Contact 1", "", "","","","");
        contact2 = new Contact("Contact 2", "", "","","","");
        group1contact = new Contact("Contact 3", "", "","","Fake Group","");
        group2contact = new Contact("Contact 3", "", "","","Different Group","");
        MainActivity.contacts = new ArrayList<Contact>();
        createContactActivity = new CreateContact();
    }

    @Test
    public void addOne(){
        createContactActivity.addContactToArray(contact1);
        assertEquals("Expected list of size 1",1,MainActivity.contacts.size());
    }

    @Test
    public void addTwo(){
        createContactActivity.addContactToArray(contact1);
        createContactActivity.addContactToArray(contact2);
        assertEquals("Expected list of size 2",2,MainActivity.contacts.size());
    }

    @Test
    public void addContactWithNonexistantGroup(){
        createContactActivity.addContactToArray(group1contact);
        assertEquals("Expected list of size 1",1,MainActivity.contacts.size());
    }

    @Test
    public void addContactsFromDifferentGroups(){
        createContactActivity.addContactToArray(group1contact);
        createContactActivity.addContactToArray(group2contact);
        assertEquals("Expected list of size 2",2,MainActivity.contacts.size());
    }

}
