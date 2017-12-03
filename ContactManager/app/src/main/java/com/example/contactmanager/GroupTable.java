package com.example.contactmanager;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Tam on 12/2/2017.
 */

@Entity
public class GroupTable {
    @PrimaryKey
    public int id;

    public String groupName;
}
