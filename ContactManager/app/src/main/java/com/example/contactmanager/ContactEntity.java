package com.example.contactmanager;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * An entity in the Room Persistence library, acts as the
 * SQLite table. Stores all contact fields as separate columns.
 */

@Entity
public class ContactEntity {
    @PrimaryKey
    public int id;

    public String name;
    public String phone;
    public String email;
    public String address;
    public String image;
    public String group;
    
    public int getId(){
        return id;
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

    public String getImage(){
        return image;
    }

    public String getGroup() { return group; }
    
    public void setId(int id){
        this.id = id;
    }
    
    public void setName(String name){
        this.name = name;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }
    
    public void setEmail(String email){
        this.email = email;
    }
    
    public void setAddress(String address){
        this.address = address;
    }
    
    public void setImage(String image){
        this.image = image;
    }

    public void setGroup(String group) { this.group = group;}

}
