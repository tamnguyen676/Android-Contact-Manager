package com.example.contactmanager;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

/**
 * Created by Tam on 12/2/2017.
 */
@android.arch.persistence.room.Dao
public interface Dao {
    @Query("select * from ContactEntity")
    public ContactEntity[] loadAllContacts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertContact(ContactEntity contactEntity);

    @Update
    public void updateContact(ContactEntity contactEntity);
}
