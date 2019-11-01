package com.example.saveit;

import android.annotation.SuppressLint;
import android.app.Service;
import android.arch.persistence.room.Room;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.chimbori.crux.articles.Article;
import com.chimbori.crux.articles.ArticleExtractor;
import com.example.saveit.models.MyArticle;
import com.example.saveit.offline.SaveitDatabase;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SaveHead  extends Service {

    private WindowManager mWindowManager;
    private View mChatHeadView;

    public SaveHead() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        super.onCreate();
        //our part: hun mnlzu2 our design b chathead
        mChatHeadView = LayoutInflater.from(this).inflate(R.layout.save_head, null);
        //end our part:



        //internet
        //Add the view to the window.
        final WindowManager.LayoutParams params;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        } else {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }

        //Specify the chat head position
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mChatHeadView, params);
        //internettt end


        //our part:
       //.la njeeb l text l 3amelo copy ekher shi li bikun link usually
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        //Set the close button.
        ImageView closeButton = mChatHeadView.findViewById(R.id.close_btn);
        final Button btn= mChatHeadView.findViewById(R.id.addbtn);
        final ImageView chatHeadImage = mChatHeadView.findViewById(R.id.chat_head_profile_iv);

        closeButton.setOnClickListener(v -> {
            //lama ykbus close ysaker l chat head
            stopSelf();
        });

        //lama ykbus add yjib l text l 3amelo copy mn clipboard w yeb3ato 3al functions getArticleFromLinkAndSaveItOffline 3malina abel
        //b ekher l file hun
        btn.setOnClickListener(v-> {
                    getArticleFromLinkAndSaveItOffline(clipboard.getText().toString());
                }
        );

        ///end our part:

        //internet
        //Drag and move chat head using user's touch action.

        chatHeadImage.setOnTouchListener(new View.OnTouchListener() {
            private int lastAction;
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_BUTTON_PRESS:
                        break;
                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        lastAction = event.getAction();
                        return true;
                    case MotionEvent.ACTION_UP:
                        //As we implemented on touch listener with ACTION_MOVE,
                        //we have to check if the previous action was ACTION_DOWN
                        //to identify if the user clicked the view or not.
                        if (lastAction == MotionEvent.ACTION_DOWN) {
                            //Open the chat conversation click.
                            //close the service and remove the chat heads
                        }
                        lastAction = event.getAction();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mChatHeadView, params);
                        lastAction = event.getAction();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatHeadView != null) mWindowManager.removeView(mChatHeadView);
    }
    //internet


    //our part:
    //our method bas kel l toast b handler krml hek lzm b chat head nfarje l toast , le? krml l save head bysht8l bel background
    public void getArticleFromLinkAndSaveItOffline(final String url) {
        for(MyArticle myArticle: getDatabse().getarticleDAO().getAll()){
            if(myArticle.url.equals(url)){
                //
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> Toast.makeText(getBaseContext(),"Article Already Added Before",Toast.LENGTH_LONG).show());
                //
                return;
            }
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> Toast.makeText(getBaseContext(),"Failed Adding",Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response)  {

                Article article = null;
                MyArticle myArticle = new MyArticle();
                try {
                    article = ArticleExtractor.with(url, response.body().string())
                            .extractMetadata()
                            .extractContent()
                            .article();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(),"You can't save this article, it's not compatible with the app",Toast.LENGTH_SHORT).show();
                }
                if(article == null || article.document.text() == null){
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> Toast.makeText(getBaseContext(),"Failed Adding",Toast.LENGTH_SHORT).show());
                    return;
                }
                Log.d("article", article.ampUrl +" " + article.canonicalUrl + " " + article.feedUrl+" "+article.faviconUrl);
                myArticle.body=(article.document.text());
                myArticle.title=(article.title);
                myArticle.url=url;
                myArticle.category=("ALL");
                myArticle.imageUrl = article.imageUrl;

                getDatabse().getarticleDAO().insert(myArticle);
                //
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> Toast.makeText(getBaseContext(),"Added Article,it will show in next time you launch the app ",Toast.LENGTH_LONG).show());
                //
            }
        });
    }
    public SaveitDatabase getDatabse() {
        return Room.databaseBuilder(getBaseContext(),SaveitDatabase.class, "sve")
                .allowMainThreadQueries()
                .build();
    }
    //end our part:


}
