package com.chirpper.cwalker2209.chirpper.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by CommandModule on 2017-11-29.
 */


@Dao
public interface ProfileDAO {
    @Query("SELECT * FROM profile")
    List<Profile> getAll();

    @Query("SELECT * FROM profile WHERE id IN (:userIds)")
    List<Profile> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM profile WHERE name LIKE :name LIMIT 1")
    Profile findByName(String name);

    @Query("SELECT * FROM profile WHERE userId LIKE :userId LIMIT 1")
    Profile findByUserId(long userId);

    @Insert
    void insertAll(Profile... profiles);

    @Insert
    long insert(Profile profile);

    @Update
    void update(Profile profile);

    @Delete
    void delete(Profile profile);
}

