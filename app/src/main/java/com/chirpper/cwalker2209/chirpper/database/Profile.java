package com.chirpper.cwalker2209.chirpper.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by CommandModule on 2017-11-29.
 */

@Entity(foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "userId",
        onDelete = CASCADE))
public class Profile {
    @PrimaryKey(autoGenerate = true)
    long id;
    long userId;

    public String name;
    public Date created;
    public String description;

    public Profile(String name, String description, long userId){
        this.name = name;
        created = Calendar.getInstance().getTime();
        this.description = description;
        this.userId = userId;
    }

}
