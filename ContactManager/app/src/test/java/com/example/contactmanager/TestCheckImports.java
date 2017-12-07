package com.example.contactmanager;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by Jesse Cacique on 12/6/2017.
 */

public class TestCheckImports {

    MainActivity testActivity = new MainActivity();
    ArrayList<Contact> testImportedContacts = new ArrayList<Contact>();
    ArrayList<Contact> sameImportedContacts = new ArrayList<Contact>();
    ArrayList<Contact> emptyImports = new ArrayList<Contact>();
    ArrayList<Contact> contactList = new ArrayList<Contact>();
    ArrayList<Contact> emptyContactList = new ArrayList<Contact>();




    @Before
    public void setupTest(){
        for(int i = 0; i < 5; i++){
            testImportedContacts.add(new Contact("test" + i, "", "", "", "", ""));
        }

        sameImportedContacts.add(new Contact("Joe", "", "", "", "", ""));

        contactList.add(new Contact("Joe", "", "", "", "", ""));

    }

    @Test
    public void newImports() throws Exception {
        boolean flag = testActivity.checkImports(testImportedContacts, contactList);
        assertEquals("Expects True, new imports",true, flag);
    }

    @Test
    public void sameContacts() throws Exception {
        boolean flag = testActivity.checkImports(sameImportedContacts, contactList);
        assertEquals("Expects false, no new contacts",false, flag);

    }

    @Test
    public void noImports() throws Exception {
        boolean flag = testActivity.checkImports(emptyImports, contactList);
        assertEquals("Expects false, no imports",false, flag);
    }

    @Test
    public void bothEmpty() throws Exception {
        boolean flag = testActivity.checkImports(emptyImports, emptyContactList);
        assertEquals("Expects false, no imports",false, flag);
    }


}
