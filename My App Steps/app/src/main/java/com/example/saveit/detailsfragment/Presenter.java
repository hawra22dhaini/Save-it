package com.example.saveit.detailsfragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.example.saveit.models.MyArticle;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Presenter implements Contract.presenter{

    Contract.view view;
    MyArticle article;

    //to save translation status
    boolean istranslated = false;


//    This function is to build our presenter, it takes view as parameter because that;
    public Presenter(Contract.view view) {
        this.view = view;
    }
    //end

    //    This function is to get article from database, it takes id as parameter because we need id of article to get
    // we set that we read this article and then update it in database an we check if it's favorited, if yes then turn color of button to red
    @Override
    public void getarticle(int id) {
        article = view.getDatabse().getarticleDAO().getarticlebyid(id);
        view.setArticleDetails(article.title,article.imageUrl,article.body,article.url);
        //bas datah l article ya3ne saret read fa mnhut is read true w mna3mel update lal article b databse ena saret read krml l mara jey w tetsayav
        article.isread = true;
        view.getDatabse().getarticleDAO().update(article);
        //
        //iza fav hey y8ayrla lawna lal ahmar otherwise khali aswad
        if(article.isfavorite) view.togglefavColor(true);
    }
    //end



    //    This function is to export to PDF, from github;
    @Override
    public void savetopdf() {
        view.getpdfgenerator().setPdfName(article.title).openPrintDialog(false).setContentBaseUrl(null).setContent(article.title+"\nSource: "+article.url+"\n"+article.body).setFilePath(Environment.getExternalStorageDirectory().getAbsolutePath()+"/").create();
        view.showMessage("Saved Successfully to SD-CARD"+Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+article.title);

    }
    //end

    //    This function is to delete article we are currently showing
    @Override
    public void delete() {
        view.getDatabse().getarticleDAO().delete(article);
    }
    //end

    //    This function is to favorite the article and turn the button to red, after we set is favorite to true we update the database
    @Override
    public void favorite() {
        article.isfavorite = !article.isfavorite;
        if(article.isfavorite){
            view.showMessage("Article Is In Favorites");
        }else{
            view.showMessage("Article Removed From Favorites");
        }
        view.togglefavColor(article.isfavorite);

        view.getDatabse().getarticleDAO().update(article);
    }
    //end
    //    This function is to check if article is translated we revert it back , if not we translate it and set variable isTranslated = true so the
    //next time if the user click on translate it will revert it back
    @Override
    public void translate() {
        if(istranslated){
            view.setArticleDetails(article.title,article.imageUrl,article.body,article.url);
            istranslated = false;
        }else{
            translate(article.body,"ar");
        }

    }
    //end

    //network call to translate article, from the internet
    private void translate( String str,String targetLang) {
        if(article.body.split(" ").length / 120 > 3){
            view.showMessage("Can't translate big articles");
            return;
        }

        if(isProbablyArabic(article.body)){
            view.showMessage("Can't translate non-english articles");
            return;
        }
        final String[] rez = new String[1];

        String URL = "https://translate.googleapis.com/translate_a/single?client=gtx&sl="
                + "en" + "&tl=" + targetLang + "&dt=t&q=" +str+"&ie=UTF-8&oe=UTF-8" ;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("content-type","application/json")
                .url(URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                view.showMessage("Failed Translating");
            }

            @Override
            public void onResponse(Call call, Response response) {
                        try {
                            rez[0] = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String str1 = rez[0].replaceAll("[^\u0600-\u06FF\\s]+", "");
                        view.setArticleDetails(article.title, article.imageUrl, str1, article.url);
                        istranslated = true;


            }
        });
    }
    //end

    //from https://stackoverflow.com/questions/15107313/how-to-determine-a-string-is-english-or-arabic
    public  boolean isProbablyArabic(String s) {
        for (int i = 0; i < s.length();) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }
}
