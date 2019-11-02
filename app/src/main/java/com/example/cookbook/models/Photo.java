package com.example.cookbook.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


public class Photo {
    @PrimaryKey(autoGenerate = true)
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
