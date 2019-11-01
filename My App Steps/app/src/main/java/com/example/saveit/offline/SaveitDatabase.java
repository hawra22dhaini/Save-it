package com.example.saveit.offline;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.saveit.models.Category;
import com.example.saveit.models.MyArticle;
import com.example.saveit.offline.DAO.ArticleDAO;
import com.example.saveit.offline.DAO.CategoriesDAO;

@Database(entities = {MyArticle.class, Category.class}, version = 1)
public abstract class SaveitDatabase extends RoomDatabase{
    public abstract CategoriesDAO getcategoriesDAO();
    public abstract ArticleDAO getarticleDAO();

}
