package com.unipi.unipiaudiostories;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class StoryDetailsActivity extends AppCompatActivity {

    private ImageView storyImage;
    private TextView storyTitle;
    private TextView storyDescription;
    private Button hearStoryButton, backButton;
    private FirebaseFirestore db;
    private List<String> imageUrls;
    private List<String> imageTexts;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        storyImage = findViewById(R.id.storyDetailsImage);
        storyTitle = findViewById(R.id.storyDetailsTitle);
        storyDescription = findViewById(R.id.storyDetailsDescription);
        hearStoryButton = findViewById(R.id.hearStoryButton);
        backButton = findViewById(R.id.backButton);
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("story_preferences", Context.MODE_PRIVATE);

        // Get the story ID from the intent
        int storyId = getIntent().getIntExtra("storyId", -1);
        if (storyId == -1) {
            Toast.makeText(this, getString(R.string.invalid_story_id), Toast.LENGTH_SHORT).show();
            return;
        }

        // Load the story details
        fetchStoryDetails(storyId);

        hearStoryButton.setOnClickListener(v -> {
            if (imageUrls != null && imageTexts != null) {
                // Fetch the story title
                String storyTitleText = storyTitle.getText().toString();
                incrementHearCount(storyId, storyTitleText);
                Intent intent = new Intent(StoryDetailsActivity.this, ImageTextActivity.class);
                intent.putStringArrayListExtra("imageUrls", new ArrayList<>(imageUrls));
                intent.putStringArrayListExtra("imageTexts", new ArrayList<>(imageTexts));
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(v -> finish());
    }

    // Fetch the story details from Firestore
    private void fetchStoryDetails(int storyId) {
        if (storyId == -1) {
            Toast.makeText(this, getString(R.string.invalid_story_id), Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("stories")
                .document(String.valueOf(storyId))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Stories story = task.getResult().toObject(Stories.class);
                        if (story != null) {
                            storyTitle.setText(story.getTitle());
                            storyDescription.setText(story.getDescription());
                            imageUrls = story.getImageUrls();
                            imageTexts = story.getImageTexts();

                            // Load image using Glide
                            String url = story.getImageURL();
                            if (url != null && !url.isEmpty()) {
                                Glide.with(this)
                                        .load(url)
                                        .fitCenter()
                                        .placeholder(R.mipmap.ic_book)
                                        .into(storyImage);
                            } else {
                                storyImage.setImageResource(R.mipmap.ic_book);
                            }
                        } else {
                            Toast.makeText(this, getString(R.string.story_not_found), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.story_not_found), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, getString(R.string.error_fetching_story) + e.getMessage(), Toast.LENGTH_SHORT).show();                });
    }

    // Increment the hear count for the story
    private void incrementHearCount(int storyId, String storyTitle) {
        String storyKey = "story_" + storyId;
        int hearCount = sharedPreferences.getInt(storyKey, 0);
        sharedPreferences.edit().putInt(storyKey, hearCount + 1).apply();
        sharedPreferences.edit().putString(storyKey + "_title", storyTitle).apply();
    }
}