package com.example.saveit;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.chimbori.crux.articles.Article;
import com.chimbori.crux.articles.ArticleExtractor;
import com.example.saveit.homefragment.HomeFragment;
import com.example.saveit.models.Category;
import com.example.saveit.offline.SaveitDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.IllegalFormatConversionException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //https://stackoverflow.com/questions/23978828/how-do-i-use-disk-caching-in-picasso, krml ysayiv l images offline
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
        //

        //to insert default article at the first time we run the app )part 2)
        if (getDatabse().getcategoriesDAO().getAll().size()==0) {
            getDatabse().getcategoriesDAO().insert(new Category("ALL"));
            getDatabse().getcategoriesDAO().insert(new Category("READ"));
            getDatabse().getcategoriesDAO().insert(new Category("BUSINESS"));
            getDatabse().getcategoriesDAO().insert(new Category("TECH"));
            getDatabse().getcategoriesDAO().insert(new Category("ECONOMICS"));
            getDatabse().getcategoriesDAO().insert(new Category("POLITICS"));
        }


        //ask for permissions krml l generate pdf, yesmahlna nsayev bel memory
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                if(!report.areAllPermissionsGranted()){
                    Toast.makeText(MainActivity.this,"You should allow all permissions",Toast.LENGTH_LONG).show();
                }else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.content,new HomeFragment()).commit();

                }
            }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();

            }
        }).check();


        //to put home fragment
    }

    //we build database so we can make part 2
    public SaveitDatabase getDatabse() {
        return Room.databaseBuilder(this,SaveitDatabase.class, "sve")
                .allowMainThreadQueries()
                .build();
    }
}

