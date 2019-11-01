package com.example.saveit.homefragment.slidercards;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.saveit.R;
import com.example.saveit.homefragment.Contract;


public class SliderAdapter extends RecyclerView.Adapter<SliderCard> {

    private Contract.presenter presenter;

    public SliderAdapter(Contract.presenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public SliderCard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_slider_card, parent, false);
        return new SliderCard(view,presenter);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderCard holder, int position) {
        presenter.bindViewHolders(holder,position);
        ViewCompat.setTransitionName(holder.getImageView(),holder.getTextView().getText().toString());
    }

    @Override
    public int getItemCount() {
        return presenter.getfavsCount();
    }

}
