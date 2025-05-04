package com.example.docafit_app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ProfilePicture.class}, version = 1, exportSchema = false)
public abstract class ProfileDatabase extends RoomDatabase {

    private static volatile ProfileDatabase INSTANCE;

    public abstract ProfilePictureDao profilePictureDao();

    public static ProfileDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ProfileDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ProfileDatabase.class, "profile_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
