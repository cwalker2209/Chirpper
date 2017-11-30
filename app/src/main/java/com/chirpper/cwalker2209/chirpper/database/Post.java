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
public class Post {

        @PrimaryKey(autoGenerate = true)
        long id;
        long userId;

        Date created;
        String text;

        public Post(String text){
            this.text = text;
            created = Calendar.getInstance().getTime();
        }
}
