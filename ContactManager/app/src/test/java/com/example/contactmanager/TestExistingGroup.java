package com.example.contactmanager;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

//Created by Grant Barbee
public class TestExistingGroup {

    CreateContact activity;

    @Before
    public void setupTest(){
        Contact testContact = new Contact("test", "", "", "", "");
        Group[] groups = { new Group ("one", testContact), new Group("two", testContact), new Group("three", testContact) };
        MainActivity.groups = new ArrayList<Group>(Arrays.asList(groups));
        activity = new CreateContact();
    }

    @Test
    public void test1() throws Exception {
        int result = activity.existingGroup("one");
        assertEquals("Index of 'one' : ",0, result);
    }

    @Test
    public void test2() throws Exception {
        int result = activity.existingGroup("two");
        assertEquals("Index of 'two' : ",1, result);
    }

    @Test
    public void test3() throws Exception {
        int result = activity.existingGroup("three");
        assertEquals("Index of 'three' : ",2, result);
    }

    @Test/*(expected = CreateContact.NonexistentGroupException.class)*/
    public void test4() throws Exception {
        try {
            int result = activity.existingGroup("four");
            fail("Expected a NonexistentGroupException to be thrown");
        }
        catch(CreateContact.NonexistentGroupException noGroup){
            assertThat(noGroup.groupName, is("four"));
        }
    }
}