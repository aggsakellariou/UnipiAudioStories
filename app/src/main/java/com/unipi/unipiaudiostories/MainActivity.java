package com.unipi.unipiaudiostories;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private final List<Stories> storyList = new ArrayList<>();
    private StoryAdapter storyAdapter;
    private Button statisticsButton;
    private Spinner languageSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Apply saved language setting before inflating layout
        applySavedLanguage();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter
        storyAdapter = new StoryAdapter(this, storyList);
        recyclerView.setAdapter(storyAdapter);

        // Create a Stories object with the details from the story
//        Stories abTheServiceDog = new Stories(
//                "T. Albert",
//                10,
//                "Captain Fantastic",
//                "2023",
//                "\"Captain Fantastic\" is an exciting children's story about a young hero on thrilling adventures. With vibrant illustrations and an engaging plot, it inspires bravery, imagination, and belief in one's potential.",
//                "https://cdn.shopify.com/s/files/1/2081/8163/files/002_86a9c8a3-d746-4bc3-bf41-4471627a7cfb.jpg?v=1613126577",
//                Arrays.asList(
//                        "https://cdn.shopify.com/s/files/1/2081/8163/files/01_db6b886e-a902-488d-876e-bc3b74c1f358.jpg?v=1606383265"
//                        ),
//                Arrays.asList(
//                        "Princess Aura always has the best birthday parties and this year friends from all over the galaxy were heading to her planet to enjoy the celebrations. Captain Fantastic and his wonder dog Winston had picked the perfect present for their inspiring friend. However, Captain Fantastic’s arch nemesis, the evil Doctor Zob was determined that there would be no birthday cake for the pair at the end of this trip.",
//                        "When"
//                ));

        // Add the story to the Firestore database
        //addStory(abTheServiceDog);

        // Initialize the statistics button
        statisticsButton = findViewById(R.id.statisticsButton);
        statisticsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            startActivity(intent);
        });

        // Initialize spinner
        languageSpinner = findViewById(R.id.languageSpinner);
        setupLanguageSpinner();

        // Fetch the products from Firestore
        fetchStories();
    }

    // Apply saved language setting
    private void applySavedLanguage() {
        SharedPreferences sharedPreferences = getSharedPreferences("appPreferences", MODE_PRIVATE);
        String languageCode = sharedPreferences.getString("language", "en");

        assert languageCode != null;
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    // Fetch stories from Firestore
    @SuppressLint("NotifyDataSetChanged")
    private void fetchStories() {
        db.collection("stories")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        storyList.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Stories story = document.toObject(Stories.class);
                            storyList.add(story);
                        }
                        storyAdapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, getString(R.string.error_getting_documents), task.getException());
                    }
                });
    }

    private String getLanguageCode(int position) {
        switch (position) {
            case 1:
                return "el";
            case 2:
                return "es";
            default:
                return "en";
        }
    }

    // Change the language of the app
    private void changeLanguage(String languageCode) {
        SharedPreferences sharedPreferences = getSharedPreferences("appPreferences", MODE_PRIVATE);
        String currentLanguage = sharedPreferences.getString("language", "en");

        assert currentLanguage != null;
        if (!currentLanguage.equals(languageCode)) {
            // Save new language preference
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("language", languageCode);
            editor.apply();

            // Restart activity to apply changes
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    // Setup the language spinner
    private void setupLanguageSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.language_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        // Set the current language selection
        SharedPreferences sharedPreferences = getSharedPreferences("appPreferences", MODE_PRIVATE);
        String currentLanguage = sharedPreferences.getString("language", "en");
        assert currentLanguage != null;
        int spinnerPosition;
        switch (currentLanguage) {
            case "el":
                spinnerPosition = 1;
                break;
            case "es":
                spinnerPosition = 2;
                break;
            default:
                spinnerPosition = 0;
                break;
        }
        languageSpinner.setSelection(spinnerPosition);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = getLanguageCode(position);
                if (!selectedLanguage.equals(currentLanguage)) {
                    changeLanguage(selectedLanguage);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    // Add a story to the Firestore database
//    private void addStory(Stories story) {
//        db.collection("stories")
//                .document(String.valueOf(story.getId()))
//                .set(story, SetOptions.merge())
//                .addOnSuccessListener(aVoid -> {
//                    // Successfully added story
//                    Toast.makeText(MainActivity.this, getString(R.string.story_added_successfully), Toast.LENGTH_SHORT).show();
//                })
//                .addOnFailureListener(e -> {
//                    // Failed to add story
//                    Toast.makeText(MainActivity.this, getString(R.string.failed_to_add_story), Toast.LENGTH_SHORT).show();                });
//    }
}