package com.appssuite.ai.image.generator.text.to.texttoimageai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PremiumList extends RecyclerView.Adapter<PremiumList.ImageHolder> {
    ImageClicked activity;
    ArrayList<PremiumModel> models;

    public PremiumList(ArrayList<PremiumModel> models, Context context) {
        this.models = models;
        activity = (ImageClicked) context;

    }

    public interface ImageClicked{
        void OnImageClicked(int position);
    }



    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view, parent, false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        holder.itemView.setTag(models.get(position));

        holder.credit_btn.setImageResource(models.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder{

        ImageView credit_btn;
        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            credit_btn = itemView.findViewById(R.id.btn_prem);

            itemView.setOnClickListener(view -> {
                activity.OnImageClicked(models.indexOf((PremiumModel) view.getTag()));
            });
        }
    }
}
