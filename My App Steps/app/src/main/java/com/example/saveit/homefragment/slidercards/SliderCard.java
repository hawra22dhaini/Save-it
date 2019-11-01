package com.example.saveit.homefragment.slidercards;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saveit.R;
import com.example.saveit.homefragment.Contract;


public class SliderCard extends RecyclerView.ViewHolder {

    private final ImageView imageView;
    private final TextView textView;
    private final TextView textViewTime;


    public SliderCard(View itemView, Contract.presenter presenter) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imArticle);
        textView = itemView.findViewById(R.id.tvTitle);
        textViewTime = itemView.findViewById(R.id.tvEstimatedTime);
        itemView.setOnClickListener(v -> presenter.onSliderClick(getAdapterPosition()));
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getTextView() {
        return textView;
    }

    public TextView getTextViewTime() {
        return textViewTime;
    }
}
