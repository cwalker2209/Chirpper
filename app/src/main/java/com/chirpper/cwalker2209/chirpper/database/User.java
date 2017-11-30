package com.chirpper.cwalker2209.chirpper.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;

/**
 * Created by CommandModule on 2017-11-26.
 */

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String email;
    public String password;

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }
}