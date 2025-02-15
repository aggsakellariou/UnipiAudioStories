package com.unipi.unipiaudiostories;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageTextAdapter extends RecyclerView.Adapter<ImageTextAdapter.ViewHolder> {

    private final List<String> imageUrls;
    private final List<String> imageTexts;
    private final Context context;
    private final TextToSpeech textToSpeech;

    public ImageTextAdapter(Context context, List<String> imageUrls, List<String> imageTexts, TextToSpeech textToSpeech) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.imageTexts = imageTexts;
        this.textToSpeech = textToSpeech;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_text_item, parent, false);
        return new ViewHolder(view);
    }

    // Load image using Glide and speak the text
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        String imageText = imageTexts.get(position);

        // Load image using Glide
        Glide.with(context)
                .load(imageUrl)
                .fitCenter()
                .placeholder(R.mipmap.ic_book)
                .into(holder.imageView);

        // Speak the text
        textToSpeech.speak(imageText, TextToSpeech.QUEUE_FLUSH, null, "UTTERANCE_ID_" + position);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}