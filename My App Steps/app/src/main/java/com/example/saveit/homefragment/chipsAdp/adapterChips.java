package com.example.saveit.homefragment.chipsAdp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.saveit.R;
import com.example.saveit.homefragment.Contract;

public class adapterChips extends RecyclerView.Adapter<viewholderChips>{
    public Contract.presenter presenter;


    @NonNull
    @Override
    public viewholderChips onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new viewholderChips(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_chips,viewGroup,false),presenter);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholderChips viewholderArticle, int i) {
        presenter.bindchips(viewholderArticle,i);

    }

    @Override
    public int getItemCount() {
        return presenter.getChipsCount();
    }
}
