package com.example.saveit.detailsfragment;

import com.example.saveit.offline.SaveitDatabase;
import com.uttampanchasara.pdfgenerator.CreatePdf;

public interface Contract {

    interface presenter{
        void getarticle(int id);
        void savetopdf();
        void delete();
        void favorite();
        void translate();

    }

    interface view{
        void setArticleDetails(String title, String url, String text,String mainUrl);
        SaveitDatabase getDatabse();
        void showMessage(String message);
        CreatePdf getpdfgenerator();
        void togglefavColor(boolean fav);
    }
}
