package com.unipi.unipiaudiostories;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {

    private final Context context;
    private final List<Stories> stories;

    public StoryAdapter(Context context, List<Stories> stories) {
        this.context = context;
        this.stories = stories;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.storie_item, parent, false);
        return new StoryViewHolder(view);
    }

    // Bind data to ViewHolder
    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        Stories story = stories.get(position);

        // Load image using Glide
        String url = story.getImageURL();
        if (url != null && !url.isEmpty()) {
            Glide.with(context)
                    .load(url)
                    .fitCenter()
                    .placeholder(R.mipmap.ic_book)
                    .into(holder.storyImage);
        } else {
            holder.storyImage.setImageResource(R.mipmap.ic_book);
        }

        // Set product details to TextViews
        holder.storyTitle.setText(story.getTitle());
        holder.storyAuthor.setText(story.getAuthor());
        holder.storyYear.setText(String.format(story.getYear()));

        // Set click listener for navigation
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, StoryDetailsActivity.class);
            intent.putExtra("storyId", story.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    // ViewHolder class
    public static class StoryViewHolder extends RecyclerView.ViewHolder {
        ImageView storyImage;
        TextView storyTitle, storyAuthor, storyYear;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            storyImage = itemView.findViewById(R.id.storyImage);
            storyTitle = itemView.findViewById(R.id.storyTitle);
            storyAuthor = itemView.findViewById(R.id.storyAuthor);
            storyYear = itemView.findViewById(R.id.storyYear);
        }
    }
}