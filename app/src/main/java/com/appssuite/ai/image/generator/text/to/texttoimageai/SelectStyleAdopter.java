package com.appssuite.ai.image.generator.text.to.texttoimageai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectStyleAdopter extends RecyclerView.Adapter<SelectStyleAdopter.ViewHolder>{

    ImageClicked activity;
    private ArrayList<SelectStyle> styles;
    Context context;


    public SelectStyleAdopter(ArrayList<SelectStyle> styles, Context context) {
        this.styles = styles;
        activity = (ImageClicked) context;

    }

    public interface ImageClicked{
        void OnImageClicked(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.style_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(styles.get(position));

        holder.style_image.setImageResource(styles.get(position).image);
        holder.styleName.setText(styles.get(position).name);



    }

    @Override
    public int getItemCount() {
        return styles.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView style_image;
        TextView styleName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            style_image = itemView.findViewById(R.id.imageStyle);
            styleName =itemView.findViewById(R.id.styleName);
            itemView.setOnClickListener(view -> {
                activity.OnImageClicked(styles.indexOf((SelectStyle) view.getTag()));
            });

        }

        }
    }
