package com.chirpper.cwalker2209.chirpper.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

// example converter for java.util.Date
public class Converters {
    @TypeConverter
    public Date toDate(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public Long toLong(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }
}

