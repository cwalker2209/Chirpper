package com.chirpper.cwalker2209.chirpper.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by CommandModule on 2017-11-29.
 */

@Dao
public interface PostDAO {
    @Query("SELECT * FROM user")
    List<Post> getAll();

    @Query("SELECT * FROM post WHERE uid IN (:userIds)")
    List<Post> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM post WHERE name LIKE :name LIMIT 1")
    Post findByName(String name);

    @Insert
    void insertAll(Post... posts);

    @Delete
    void delete(Post posts);
}
