package com.example.contactmanager;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

/**
 * Data access object used to perform CRUD operations on database.
 */
@android.arch.persistence.room.Dao
public interface Dao {
    @Query("select * from ContactEntity")
    public ContactEntity[] loadAllContacts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertContact(ContactEntity contactEntity);

    @Update
    public void updateContact(ContactEntity contactEntity);

    @Delete
    public void deleteContact(ContactEntity contactEntity);
}
