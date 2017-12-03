package com.example.contactmanager;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by Tam on 12/2/2017.
 */

@Database(entities = {ContactEntity.class}, version = 1)
public abstract class ContactDatabase extends RoomDatabase{
    public abstract Dao dao();
}
