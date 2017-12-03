package com.example.contactmanager;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Tam on 12/2/2017.
 */

@Entity
public class ContactTable {
    @PrimaryKey
    public int id;

    public String name;
    public String email;
    public String address;
    public String image;

}
