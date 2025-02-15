package com.unipi.unipiaudiostories;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;
import java.util.Locale;

public class ImageTextActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private List<String> imageUrls;
    private List<String> imageTexts;
    private TextToSpeech textToSpeech;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_text);

        viewPager = findViewById(R.id.viewPager);
        backButton = findViewById(R.id.backButton);

        // Get the image URLs and texts from the intent
        imageUrls = getIntent().getStringArrayListExtra("imageUrls");
        imageTexts = getIntent().getStringArrayListExtra("imageTexts");

        if (imageUrls == null || imageTexts == null) {
            Toast.makeText(this, getString(R.string.no_data_available), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize TextToSpeech
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.US);
                setupViewPager();
            } else {
                Toast.makeText(this, getString(R.string.tts_initialization_failed), Toast.LENGTH_SHORT).show();            }
        });

        backButton.setOnClickListener(v -> finish());
    }

    // Set up the ViewPager with the image URLs and texts
    private void setupViewPager() {
        ImageTextAdapter adapter = new ImageTextAdapter(this, imageUrls, imageTexts, textToSpeech);
        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Speak the text for the selected page
                String imageText = imageTexts.get(position);
                textToSpeech.speak(imageText, TextToSpeech.QUEUE_FLUSH, null, "UTTERANCE_ID_" + position);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}