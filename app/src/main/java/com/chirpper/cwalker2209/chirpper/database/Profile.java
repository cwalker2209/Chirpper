package com.chirpper.cwalker2209.chirpper.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;

import com.chirpper.cwalker2209.chirpper.R;

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
    public long id;
    public long userId;

    public String name;
    public Date created;
    public String description;

    //@ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public Bitmap image;

    public Profile(String name, String description, Bitmap image, long userId){
        this.name = name;
        created = Calendar.getInstance().getTime();
        this.description = description;
        this.userId = userId;
        this.image = image;
    }

}
