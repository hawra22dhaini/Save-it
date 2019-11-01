package com.example.saveit.homefragment.chipsAdp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saveit.R;
import com.example.saveit.homefragment.Contract;

public class viewholderChips extends RecyclerView.ViewHolder  {
        public TextView title;
        public Contract.presenter presenter;

    public viewholderChips(@NonNull View itemView, Contract.presenter presenter) {
        super(itemView);
        this.presenter = presenter;
        title=itemView.findViewById(R.id.tvchips);

        itemView.setOnClickListener(v ->{
            presenter.onchipsclick(title.getText().toString());
        });

        itemView.setOnLongClickListener(v -> {
            presenter.onChipsLongClick(getAdapterPosition());
            return true;
        });
    }


}
