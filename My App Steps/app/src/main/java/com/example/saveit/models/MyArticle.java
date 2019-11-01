package com.example.saveit.models;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


//li badna nsayveon b database
@Entity(tableName = "article")
public class MyArticle {
    // some atributes will be added later after Dr.'s confirming

    public String title;
    public String url;
    public String imageUrl;
    public String body;
    public String category;
    public boolean isfavorite;
    public int readingtime;
    public String sourceurl;
    public boolean isread;

    // primary key is unique
    @PrimaryKey(autoGenerate = true)
    public int id;

}
