package com.example.contactmanager;

import java.util.ArrayList;
import java.io.Serializable;

/**
 * Group class that holds the name of the group, the size, and a list of contacts
 * in the group.
 */

public class Group implements Comparable <Group>, Serializable{
    ArrayList<Contact> contacts;    //Holds list of contacts for a group
    private String name;    //Name of the group
    public int size;


    public Group(String name,Contact contact) {
        this.name = name;
        size = 0;
        contacts = new ArrayList<Contact>();
        addContact(contact);
    }

    /**
     * Used by the Collections class to sort.
     * @param group Compares the group name of the object invoking to the group name of
     *              the group object parameter.
     * @return 1 if the first group string is lexicographically greater than the
     * second string else the result would be -1. 0 if equal.
     */
    @Override
    public int compareTo(Group group) {
        return name.compareTo(group.getGroupName());
    }

    /**
     * Returns group name.
     * @return
     */
    public String getGroupName(){
        return name;
    }

    /**
     * Adds a contact to the group
     * @param contact Contact object to be added to the group
     */
    public void addContact(Contact contact) {
        contacts.add(new Contact(contact.getName(), contact.getPhone(), contact.getEmail(), contact.getAddress(), contact.getGroup(), contact.getImageUri()));
        size++;
    }

    /**
     * Removes a contact from the group
     * @param toRemove Contact object to be removed from the group
     */
    public void removeContact(Contact toRemove){
        if(contacts.contains(toRemove)){
            contacts.remove(toRemove);
            size--;
        }
    }
}
