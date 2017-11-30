package com.chirpper.cwalker2209.chirpper.database;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;

import com.chirpper.cwalker2209.chirpper.App;

/**
 * Created by CommandModule on 2017-11-26.
 */

@Database(entities = {User.class, Profile.class, Post.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDAO userDao();
    public abstract ProfileDAO profileDAO();
    public abstract PostDAO postDAO();

    /**public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE product "
                    + " ADD COLUMN price INTEGER");

            // enable flag to force update products
            App.get().setForceUpdate(true);
        }
    };**/
}
