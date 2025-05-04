package com.example.docafit_app.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ProfilePictureDao {

    @Insert
    void insertProfilePicture(ProfilePicture profilePicture);

    @Query("SELECT * FROM profile_picture LIMIT 1")
    ProfilePicture getProfilePicture();
}
