package com.example.contactmanager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artem on 10/21/17.
 */

public class Group {
    List<Contact> Contacts;
    private String _name;
    public int size;

    public Group(String name,Contact a) {
        _name = name;
        size = 0;
        Contacts = new ArrayList<Contact>();
        addContact(a);


    }

    public String getGroupName(){
        return _name;
    }

    public void addContact(Contact a) {
        Contacts.add(new Contact(a.getName(), a.getPhone(), a.getEmail(), a.getAddress(), a.getGroup()));
        size++;
    }

    public void removeContact(Contact toRemove){
        if(Contacts.contains(toRemove)){
            Contacts.remove(toRemove);
            size--;
        }
    }
}
