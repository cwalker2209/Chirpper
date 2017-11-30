package com.chirpper.cwalker2209.chirpper.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by CommandModule on 2017-11-26.
 */

@Dao
public interface UserDAO {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE id IN (:ids)")
    List<User> loadAllByIds(int[] ids);

    @Query("SELECT * FROM user WHERE email LIKE :email LIMIT 1")
    User findByEmail(String email);

    @Insert
    void insertAll(User... users);

    @Insert
    long insert(User user);

    @Delete
    void delete(User user);
}
