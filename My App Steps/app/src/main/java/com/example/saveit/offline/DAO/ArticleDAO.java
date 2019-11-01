package com.example.saveit.offline.DAO;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.example.saveit.models.MyArticle;

import java.util.ArrayList;
import java.util.List;

@Dao
// interface doesn't accept codes( function body)
public interface ArticleDAO {
    @Insert
    public void insert(MyArticle items);
    @Update
    public void update(MyArticle items);
    @Delete
    public void delete(MyArticle item);

    // to get all articles save
    @Query("select * from article")
    List<MyArticle> getAll();

    @Query("select * from article where id= :sid")
    MyArticle getarticlebyid(int sid);

    @Query("select * from article where isfavorite= :isfavorite")
    List<MyArticle> getfavoritearticle (boolean isfavorite);

}

