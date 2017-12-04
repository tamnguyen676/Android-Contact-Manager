package com.example.contactmanager;

import java.io.Serializable;

/**
 * Created by Artem on 10/21/17.
 */

public class Contact implements Comparable<Contact>, Serializable{

    private String name, phone, email, address, group, imageUri;
    private static int totalContacts;   //Running counter that counts how many contacts there are total
    private int id; //Unique id of the contact for database

    public Contact(){}

    /**
     * Constructor for when a new contact is created from scratch.
     * Unique contact id autoupdates.
     * @param name Name of the contact
     * @param phone Phone number of the contact
     * @param email Email address of the contact
     * @param address Address of the contact
     * @param group Group name of the contact
     * @param imageUri The path to the image of the contact
     */
    public Contact(String name, String phone, String email, String address, String group, String imageUri) {
        setContactData(name, phone, email, address, group, imageUri);
        id = totalContacts++;
    }

    /**
     * Constructor for when a new contact comes from an update
     * Id must be manually set, usually by the old contact its replacing
     * @param name Name of the contact
     * @param phone Phone number of the contact
     * @param email Email address of the contact
     * @param address Address of the contact
     * @param group Group name of the contact
     * @param imageUri The path to the image of the contact
     * @param contactId The id of the contact being replaced
     */
    public Contact(String name, String phone, String email, String address, String group, String imageUri, int contactId) {
        setContactData(name, phone, email, address, group, imageUri);
        id = contactId;
    }

    /**
     * Used by constructors to set the data fields for the contact.
     * @param name Name of the contact
     * @param phone Phone number of the contact
     * @param email Email address of the contact
     * @param address Address of the contact
     * @param group Group name of the contact
     * @param imageUri The path to the image of the contact
     */
    private void setContactData(String name, String phone, String email, String address, String group, String imageUri) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.group = group;
        this.imageUri = imageUri;
    }

    //Allows for sorting based on name of contact
    @Override
    public int compareTo(Contact contact) {
        return name.compareTo(contact.getName());
    }


    @Override
    public boolean equals(Object o){
        Contact other = (Contact)o;
        if(!other.getName().equals(name)) {
            return false;
        }
        if(!other.getPhone().equals(phone)) {
            return false;
        }
        if(!other.getEmail().equals(email)) {
            return false;
        }
        if(!other.getAddress().equals(address)) {
            return false;
        }
        if(!other.getGroup().equals(group)) {
            return false;
        }
        return true;
    }

    public String getName(){
        return name;
    }

    public String getPhone(){
        return phone;
    }

    public String getEmail(){
        return email;
    }
    public String getAddress(){
        return address;
    }
    public String getGroup() { return group; }
    public String getImageUri(){return imageUri;}
    public int getId(){return id;}
    public static int getTotalContacts(){
        return totalContacts;
    }

    /**
     * Sets the total number of contacts so that the
     * contacts can accurately keep track of their ids
     * @param total The total amount of contacts in the database
     */
    public static void setTotalContacts(int total){
        totalContacts = total;
    }
}
