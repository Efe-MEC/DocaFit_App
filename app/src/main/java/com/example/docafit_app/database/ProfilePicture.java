package com.example.docafit_app.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "profile_picture")
public class ProfilePicture {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
