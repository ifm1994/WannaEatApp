package com.example.ifmfo.wannaeatapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ifmfo.wannaeatapp.Model.Opinion;

import java.util.List;

public class OpinionCardAdapter extends RecyclerView.Adapter<OpinionCardViewHolder> {

    private Context context;
    private List<Opinion> opinions;

    public OpinionCardAdapter(Context context, List <Opinion> opinions){
        this.context = context;
        this.opinions = opinions;
    }

    @NonNull
    @Override
    public OpinionCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.opinion_card, parent, false);
        return new OpinionCardViewHolder(vista, context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OpinionCardViewHolder holder, int position) {
        holder.opinionName.setText(opinions.get(position).getWriterName());
        holder.opinionDescription.setText(opinions.get(position).getDescription());
        holder.opinionDate.setText(opinions.get(position).getDate());
        holder.opinionRating.setText(Double.toString(opinions.get(position).getRating()));
    }

    @Override
    public int getItemCount() {
        return opinions.size();
    }
}
