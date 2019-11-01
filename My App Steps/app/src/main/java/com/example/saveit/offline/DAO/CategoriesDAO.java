package com.example.saveit.offline.DAO;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.saveit.models.Category;

import java.util.List;

@Dao
public interface CategoriesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Category article);

    @Delete
    void delete(Category article);

    @Query("SELECT * FROM categories")
    List<Category> getAll();

}

