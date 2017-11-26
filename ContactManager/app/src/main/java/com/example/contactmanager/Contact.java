package com.example.contactmanager;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Artem on 10/21/17.
 */

public class Contact implements Comparable<Contact>, Serializable{

    private String _name, _phone, _email, _address, _group;

    public Contact(String name, String phone, String email, String address) {
        _name = name;
        _phone = phone;
        _email = email;
        _address = address;
        _group = "";

    }
    public Contact(String name, String phone, String email, String address, String group) {
        _name = name;
        _phone = phone;
        _email = email;
        _address = address;
        _group = group;
    }

    //Allows for sorting based on name of contact
    @Override
    public int compareTo(Contact contact) {
        return _name.compareTo(contact.getName());
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
}
