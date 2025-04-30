# Unipi Audio Stories

## Overview

**Unipi Audio Stories** is an Android application designed to read children's stories aloud using the device's built-in text-to-speech engine. It aims to provide an engaging reading experience for young users with a visual and auditory storytelling interface. The app includes a curated collection of stories, multilingual support, and a statistics dashboard for tracking user interaction.

## Features

- A library of at least 5 preloaded children's stories  
- Each story includes:  
  - Full story text  
  - Relevant image  
  - Additional info (e.g., author, year)  
- Text-to-speech functionality to narrate stories  
- Visual display of story images during narration  
- Story content stored in a remote Firebase Cloud Database (preferred) or local SQLite database  
- Multilingual UI support for three languages (e.g., English, Greek, and one more) using string resources  
- Dedicated Statistics Activity that shows:  
  - Total number of story readings  
  - Most frequently played stories  
- (Optional) Voice command support for navigation (e.g., open statistics screen)  
- Optimized layout and UI for both smartphones and tablets using responsive components (e.g., CardView)

## Technical Details

- Developed for Android using Java (and optionally Kotlin)  
- Firebase Cloud Firestore for real-time story content updates  
  - Fallback option: local SQLite database (with score penalty if used)  
- Uses Android's `TextToSpeech` API for narration  
- Language support via `strings.xml` for three UI languages  
- Persistent storage of user interaction data using `SharedPreferences`  
- Material Design components used for consistent UI/UX  
- Tested on multiple Android emulators for phones and tablets

## Installation

1. Clone this repository to your local machine:
   ```bash
   git clone https://github.com/aggsakellariou/UnipiAudioStories.git
   
2. Open the project in Android Studio

3. Build and run the application on an Android emulator or physical device
