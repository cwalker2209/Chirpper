package com.chirpper.cwalker2209.chirpper.database;

import android.arch.persistence.room.TypeConverter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.lang.Byte;

import java.io.ByteArrayOutputStream;
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

    // convert from bitmap to byte array
    @TypeConverter
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    // get the base 64 string
    @TypeConverter
    public Bitmap getBitmapFromBytes(byte[] bytes){
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return  bitmap;
    }


}

