package com.example.contactmanager;

import android.support.annotation.NonNull;

import java.io.Serializable;
import android.net.Uri;

/**
 * Created by Artem on 10/21/17.
 */

public class Contact implements Comparable<Contact>, Serializable{

    private String _name, _phone, _email, _address, _group, _imageUri;
    private static int totalContacts;
    private int id;

    public Contact(String name, String phone, String email, String address, String imageUri) {
        _name = name;
        _phone = phone;
        _email = email;
        _address = address;
        _group = "";
        _imageUri = imageUri;
        id = totalContacts++;   //Todo fix bug where id is incremented on edit
    }

    public Contact(String name, String phone, String email, String address, String imageUri, int contactId) {
        _name = name;
        _phone = phone;
        _email = email;
        _address = address;
        _group = "";
        _imageUri = imageUri;
        id = contactId;
    }

    public Contact(String name, String phone, String email, String address, String group, String imageUri) {
        _name = name;
        _phone = phone;
        _email = email;
        _address = address;
        _group = group;
        _imageUri = imageUri;
        id = totalContacts++;
    }

    public Contact(int contactId, String name, String phone, String email, String address, String imageUri, String group) {
        _name = name;
        _phone = phone;
        _email = email;
        _address = address;
        _group = group;
        _imageUri = imageUri;
        id = contactId;
    }

    //Allows for sorting based on name of contact
    @Override
    public int compareTo(Contact contact) {
        return _name.compareTo(contact.getName());
    }

    @Override
    public boolean equals(Object o){
        Contact other = (Contact)o;
        if(!other.getName().equals(_name)) {
            System.out.println(other.getName());
            System.out.println(_name);
            return false;
        }
        if(!other.getPhone().equals(_phone)) {
            System.out.println("Phone");
            return false;
        }
        if(!other.getEmail().equals(_email)) {
            System.out.println("Email");
            return false;
        }
        if(!other.getAddress().equals(_address)) {
            System.out.println("Addr");
            return false;
        }
        if(!other.getGroup().equals(_group)) {
            System.out.println("Group");
            return false;
        }
        return true;
    }

    public String getName(){
        return _name;
    }

    public String getPhone(){
        return _phone;
    }

    public String getEmail(){
        return _email;
    }
    public String getAddress(){
        return _address;
    }
    public String getGroup() { return _group; }

    public String getImageUri(){return _imageUri;}
    public int getId(){return id;}

    public static void setTotalContacts(int total){
        totalContacts = total;
    }
}
