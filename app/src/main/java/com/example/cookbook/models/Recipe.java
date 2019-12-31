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
    public int getNumberOfLike() {
        return numberOfLike;
    }
    public Boolean getAlreadyDone() {
        return isAlreadyDone;
    }
    public String getAddDate() {
        return addDate;
    }
    //SETTERS
    public void setId(long id) {
        this.id = id;
    }
    public void setNumberOfLike(int numbeOfike) {
        this.numberOfLike = numbeOfike;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAlreadyDone(Boolean alreadyDone) {
        isAlreadyDone = alreadyDone;
    }
    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }
}
