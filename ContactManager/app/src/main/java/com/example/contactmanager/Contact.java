package com.example.contactmanager;

/**
 * Created by Artem on 10/21/17.
 */

public class Contact {

    private String _name, _phone, _email, _address;

    public Contact(String name, String phone, String email, String address) {
        _name = name;
        _phone = phone;
        _email = email;
        _address = address;

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
}
