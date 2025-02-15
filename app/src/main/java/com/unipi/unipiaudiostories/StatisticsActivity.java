package com.unipi.unipiaudiostories;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private TextView statisticsTextView;
    private Button backButton, readStatisticsButton;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Initialize the shared preferences, views and buttons
        sharedPreferences = getSharedPreferences("story_preferences", Context.MODE_PRIVATE);
        statisticsTextView = findViewById(R.id.statisticsTextView);
        backButton = findViewById(R.id.backButton);
        readStatisticsButton = findViewById(R.id.readStatisticsButton);

        // Display the statistics
        displayStatistics();

        // back button
        backButton.setOnClickListener(v -> finish());

        // Initialize the text to speech engine
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.getDefault());
            }
        });

        // Read the statistics when the button is clicked
        readStatisticsButton.setOnClickListener(v -> {
            String statisticsText = statisticsTextView.getText().toString();
            textToSpeech.speak(statisticsText, TextToSpeech.QUEUE_FLUSH, null, null);
        });
    }

    // Stop the text to speech when the activity is destroyed
    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    // Display the statistics of the stories
    private void displayStatistics() {
        Map<String, ?> allEntries = sharedPreferences.getAll();
        List<Map.Entry<String, Integer>> storyList = new ArrayList<>();
        String favoriteStory = null;
        int maxHearCount = 0;

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().endsWith("_title")) continue;

            String storyKey = entry.getKey();
            int hearCount = (int) entry.getValue();
            String storyTitle = sharedPreferences.getString(storyKey + "_title", "Unknown Title");

            storyList.add(new AbstractMap.SimpleEntry<>(storyTitle, hearCount));

            if (hearCount > maxHearCount) {
                maxHearCount = hearCount;
                favoriteStory = storyTitle;
            }
        }

        // Sort the list based on hear counts in descending order
        storyList.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        StringBuilder statistics = new StringBuilder();

        // Display the favorite story first
        if (favoriteStory != null) {
            statistics.append(getString(R.string.favorite_story)).append(favoriteStory).append("\n\n");
        }

        // Display the rest of the stories with spaces between them
        for (Map.Entry<String, Integer> entry : storyList) {
            statistics.append(entry.getKey()).append("\n")
                    .append(getString(R.string.heard)).append(" ")
                    .append(entry.getValue()).append(" ")
                    .append(getString(R.string.times)).append("\n\n");
        }

        statisticsTextView.setText(statistics.toString());
    }
}