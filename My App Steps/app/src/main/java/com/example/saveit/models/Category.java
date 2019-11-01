package com.example.saveit.models;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cat_id")
    public int id;

    @ColumnInfo(name = "cat_name")
    public String name;

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}