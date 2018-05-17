package com.example.ifmfo.wannaeatapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OpinionCardViewHolder extends RecyclerView.ViewHolder {

    TextView opinionName;
    TextView opinionDescription;
    TextView opinionDate;
    TextView opinionRating;
    View view;


    OpinionCardViewHolder(View itemView, Context context) {
        super(itemView);

        view = itemView;
        opinionName = itemView.findViewById(R.id.opinion_name);
        opinionDescription = itemView.findViewById(R.id.opinion_description);
        opinionDate = itemView.findViewById(R.id.opinion_date);
        opinionRating = itemView.findViewById(R.id.opinion_rating);

    }

}
