package com.example.cookbook.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private int numberOfLike;
    private Boolean isAlreadyDone;
    private String addDate;

    //GETTERS
    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    //SETTERS
    public void setId(long id) {
        this.id = id;
    }

    public int getNumberOfLike() {
        return numberOfLike;
    }

    public void setNumberOfLike(int numbeOfike) {
        this.numberOfLike = numbeOfike;
    }

    public Boolean getAlreadyDone() {
        return isAlreadyDone;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setAlreadyDone(Boolean alreadyDone) {
        isAlreadyDone = alreadyDone;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }
}
