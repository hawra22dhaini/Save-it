package com.example.saveit.homefragment.desArticles;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.saveit.R;
import com.example.saveit.homefragment.Contract;

public class adapterNews extends RecyclerView.Adapter<viewholderArticle> {
    public Contract.presenter presenter;


    @NonNull
    @Override
    public viewholderArticle onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new viewholderArticle(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_article,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewholderArticle viewholderArticle, int i) {
        presenter.bindarticles(viewholderArticle,i);
        viewholderArticle.itemView.setOnClickListener(view -> {
            presenter.onarticleclick(i);
        });


    }

    @Override
    public int getItemCount() {
        return presenter.getArticlesCount();
    }
}
