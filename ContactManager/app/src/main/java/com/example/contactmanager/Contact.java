package com.example.contactmanager;

import java.io.Serializable;

/**
 * Created by Artem on 10/21/17.
 */

public class Contact implements Comparable<Contact>, Serializable{

    private String name, phone, email, address, group, imageUri;
    private static int totalContacts;   //Running counter that counts how many contacts there are total
    private int id; //Unique id of the contact for database

    public Contact(String name, String phone, String email, String address, String imageUri) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        group = "";
        this.imageUri = imageUri;

        id = totalContacts++;
    }

    public Contact(String name, String phone, String email, String address, String imageUri, int contactId) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        group = "";
        this.imageUri = imageUri;
        id = contactId;

    }

    public Contact(String name, String phone, String email, String address, String group, String imageUri) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.group = group;
        this.imageUri = imageUri;
        id = totalContacts++;
    }

    public Contact(String name, String phone, String email, String address, String group, String imageUri, int contactId) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.group = group;
        this.imageUri = imageUri;
        id = contactId;
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
            System.out.println(other.getName());
            System.out.println(name);
            return false;
        }
        if(!other.getPhone().equals(phone)) {
            System.out.println("Phone");
            return false;
        }
        if(!other.getEmail().equals(email)) {
            System.out.println("Email");
            return false;
        }
        if(!other.getAddress().equals(address)) {
            System.out.println("Addr");
            return false;
        }
        if(!other.getGroup().equals(group)) {
            System.out.println("Group");
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

    public static void setTotalContacts(int total){
        totalContacts = total;
    }
}
