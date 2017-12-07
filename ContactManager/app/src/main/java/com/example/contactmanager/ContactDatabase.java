package com.example.contactmanager;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * SQLite database that stores contacts so that contacts
 * are saved even when user leaves app. Uses the
 * Room Persistence library provided by Google.
 */

@Database(entities = {ContactEntity.class}, version = 1, exportSchema = false)
public abstract class ContactDatabase extends RoomDatabase{
    /**
     * The Room Persistence finds the Data Access Object using
     * this abstract method so it can automatically get and set fields.
     * @return The Data Access Object to get and set fields in the database
     */
    public abstract Dao dao();
}
