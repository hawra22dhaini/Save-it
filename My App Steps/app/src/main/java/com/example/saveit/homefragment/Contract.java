package com.example.saveit.homefragment;

import com.example.saveit.homefragment.chipsAdp.viewholderChips;
import com.example.saveit.homefragment.desArticles.viewholderArticle;
import com.example.saveit.homefragment.slidercards.SliderCard;
import com.example.saveit.models.Category;
import com.example.saveit.offline.SaveitDatabase;

import java.util.List;

public interface Contract {
    interface presenter{
        void getArticleFromLinkAndSaveItOffline(String url,String categories);

        int getArticlesCount();
        void bindarticles(viewholderArticle holder,int position);
        void onarticleclick(int position);

        int getChipsCount();
        void bindchips(viewholderChips holder, int position);
        void onchipsclick(String name );

        void onSliderClick(int position);
        void bindViewHolders(SliderCard sliderCard, int position);
        int getfavsCount();

        void onChipsLongClick(int pos);


    }
    interface view{
        SaveitDatabase getDatabse();
        void showMessage(String text);
        void refreshnews();
        void gotodetails(int id);
        void showNoFav();
        void showNoSaved();

        List<Category> getcategories();
    }

}
