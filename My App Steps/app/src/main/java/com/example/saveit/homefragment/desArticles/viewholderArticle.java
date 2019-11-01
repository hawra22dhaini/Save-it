package com.example.saveit.homefragment.desArticles;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saveit.R;

public class viewholderArticle  extends RecyclerView.ViewHolder {
    public TextView title, category,estimatedTime;
    public ImageView articleimage;


    public viewholderArticle(@NonNull View itemView) {
        super(itemView);

        title=itemView.findViewById(R.id.tvtitle);
        articleimage=itemView.findViewById(R.id.imgarticle);
        category=itemView.findViewById(R.id.tvcategory);
        estimatedTime = itemView.findViewById(R.id.tvEstimatedTime);
    }
}
