package com.example.saveit.homefragment;

import android.support.annotation.NonNull;
import com.chimbori.crux.articles.Article;
import com.chimbori.crux.articles.ArticleExtractor;
import com.example.saveit.homefragment.chipsAdp.viewholderChips;
import com.example.saveit.homefragment.desArticles.viewholderArticle;
import com.example.saveit.homefragment.slidercards.SliderCard;
import com.example.saveit.models.MyArticle;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Presenter implements Contract.presenter {
    // object view
    Contract.view view;
    List<MyArticle>articles,filtered,fav;
    int speed = 120;

    public Presenter( Contract.view view) {
        this.view  = view;
        //kel shi bado new
        articles=new ArrayList<>();
        filtered=new ArrayList<>();
        fav=new ArrayList<>();
        //

        //3am njeeb l articles mn database w nhutun b arraylist
        articles=view.getDatabse().getarticleDAO().getAll();
        Collections.reverse(articles);

        //
        //iza size lal list 0 se3eta ma fi articles
        if(articles.size()==0){
            view.showNoSaved();
        }
        //

        //njeeb l favorited w nhutun b list l fav
        fav= view.getDatabse().getarticleDAO().getfavoritearticle(true);
        Collections.reverse(fav);
        if(fav.size()==0){
            view.showNoFav();
        }

    }

    @Override
    public void getArticleFromLinkAndSaveItOffline(String url,String cat) {
        //iza mawjud 3nde link l article bel articles database table abel ya3ne 3amela save fa ma fi ysayva....
        for(MyArticle myArticle: view.getDatabse().getarticleDAO().getAll()){
            if(myArticle.url.equals(url)){
                view.showMessage("Can't add duplicate articles");
                return;
            }
        }
        ///
        //internet w hyda l by3mlna internet call ref(e31)
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                //e31 huwe l url li ha ysawilna call 3le
                .url(url)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //iza feshlit l 3amaliye
                view.showMessage("Error..Saving article");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                Article article;
                //iza fi error w reji3 null l html
                if(response.body() == null){
                    view.showMessage("Error Saving Article");
                    return;
                }
                //

                //hun ma3neta kelo tammem  w ma3na l html
                try {

                    //mna3te l html lal crux library li nazalneha la yeshab l article mn l html
                    article = ArticleExtractor.with(url, response.body().string())
                            .extractMetadata()
                            .extractContent()
                            .article();
                    //
                    //now mnekhla2 our own article krml ma fina nsayev l crux article b database
                    MyArticle ToBeSaved= new MyArticle();
                    ToBeSaved.title=article.title;
                    ToBeSaved.url=url;
                    ToBeSaved.body=article.document.text();
                    ToBeSaved.category = cat;
                    ToBeSaved.isfavorite=false;
                    ToBeSaved.imageUrl = article.imageUrl;
                    //

                    //mnfaweta 3a database
                    view.getDatabse().getarticleDAO().insert(ToBeSaved);

                    //mnzida 3a list li sahabne bel awal
                    articles.add(0,ToBeSaved);
                    //mna3mel refresh
                    view.refreshnews();


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });
    }

    @Override
    public int getArticlesCount() {
        return articles.size();

    }

    //mnlzu2 data b design hun ref here 231, 3am nlzu2 l views according lal data l b list articles
    @Override
    public void bindarticles(viewholderArticle holder, int position) {
        MyArticle article=articles.get(position);
        holder.title.setText(article.title);
        Picasso.get().load(article.imageUrl).into(holder.articleimage);
        holder.category.setText(article.category);
        holder.estimatedTime.setText(article.body.split(" ").length/speed+" min" );

    }

    @Override
    public void onarticleclick(int position) {
        List<MyArticle> articles  = view.getDatabse().getarticleDAO().getAll();
        Collections.reverse(articles);
        MyArticle article=articles.get(position);
        view.gotodetails(article.id);

    }

    @Override
    public int getChipsCount() {
        return view.getcategories().size();
    }

    @Override
    public void bindchips(viewholderChips holder, int position) {
        holder.title.setText(view.getcategories().get(position).getName());

    }

    @Override
    public void onchipsclick(String name) {
        articles = view.getDatabse().getarticleDAO().getAll();
        //mnshil kel l results li abel l filtered
        filtered.clear();

        //iza kabas 3a read mnjib l articles l is read
        if(name.equals("READ")){
            for(MyArticle article:articles){
                if(article.isread){
                    filtered.add(article);
                }
            }
            articles = filtered;
            //refresh
            view.refreshnews();
            //mnedhar mn l function
            return;
        }

        //all ma badna nsewe shi
        if(name.equals("ALL")){
            view.refreshnews();
            return;
        }

        //8er hek mnjib l esem l kabas w 3le w mnshuf min 3ndo mtlo mn l  articles la n3rodon filtered
        for(MyArticle article:articles){

            if(article.category.equals(name)){
                filtered.add(article);

            }
        }

        //li2an l articles l ma3rodin jeyin mn list articles mnhut l articles = filtered w mn3ml refresh, recycler view 3m tekhod data li ella mn
        //array list articles ref(321)
        articles = filtered;
        view.refreshnews();
    }


    @Override
    public void onSliderClick(int position) {


        MyArticle article=fav.get(position);
        view.gotodetails(article.id);
    }

    //mnlzu2 data b design hun
    @Override
    public void bindViewHolders(SliderCard sliderCard, int position) {

        sliderCard.getTextViewTime().setText((fav.get(position).body.split(" ").length / speed)+" min");

        sliderCard.getTextView().setText(fav.get(position).title);

        //load lal image mn url li ella bel image view
        Picasso.get().load(fav.get(position).imageUrl).into(sliderCard.getImageView());
    }

    @Override
    public int getfavsCount() {
        return fav.size();
    }

    @Override
    public void onChipsLongClick(int pos) {
        if(view.getcategories().get(pos).name.equalsIgnoreCase("all")){
            view.showMessage("Cannot Delete All Category!");
        }else {
            view.getDatabse().getcategoriesDAO().delete(view.getcategories().get(pos));
            view.showMessage("Category Deleted");
            view.refreshnews();
        }
    }
}
