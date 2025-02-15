package com.example.camsphere;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import java.util.ArrayList;
import java.util.HashMap;

public class MusicActivity extends AppCompatActivity {

    private ExoPlayer exoPlayer;
    private PlayerView playerView;
    private TextView tvMusicTitle;
    private Button btnBack; // Back button added

    private ArrayList<Integer> playlist;
    private HashMap<String, Integer> rawFilesMap;
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        // Set system volume to maximum
        setSystemVolumeToMax();

        // Initialize UI Elements
        playerView = findViewById(R.id.playerView);
        tvMusicTitle = findViewById(R.id.tvMusicTitle);
        btnBack = findViewById(R.id.btnBack); // Initialize Back button

        // Get the category from the intent
        String category = getIntent().getStringExtra("category");
        tvMusicTitle.setText(category + " Playlist");

        // Initialize the raw files map
        initRawFilesMap();

        // Load playlist based on category
        loadPlaylist(category);

        // Initialize and prepare the ExoPlayer
        initializePlayer();

        // Set up Back button listener
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the current activity and return to the previous screen
            }
        });
    }

    private void setSystemVolumeToMax() {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioManager != null) {
            audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                    0
            );
        }
    }

    private void initRawFilesMap() {
        rawFilesMap = new HashMap<>();
        // Stress Relief
        rawFilesMap.put("stress_relief_track1", R.raw.ommantra);
        rawFilesMap.put("stress_relief_track2", R.raw.birdschirping);

        // Focus
        rawFilesMap.put("focus_track1", R.raw.ommantra);
        rawFilesMap.put("focus_track2", R.raw.birdschirping);

        // Sleep
        rawFilesMap.put("sleep_track1", R.raw.ommantra);
        rawFilesMap.put("sleep_track2", R.raw.birdschirping);

        // Relaxation
        rawFilesMap.put("relaxation_track1", R.raw.ommantra);
        rawFilesMap.put("relaxation_track2", R.raw.birdschirping);
    }

    private void loadPlaylist(String category) {
        playlist = new ArrayList<>();
        switch (category) {
            case "Stress Relief":
                playlist.add(rawFilesMap.get("stress_relief_track1"));
                playlist.add(rawFilesMap.get("stress_relief_track2"));
                break;
            case "Focus":
                playlist.add(rawFilesMap.get("focus_track1"));
                playlist.add(rawFilesMap.get("focus_track2"));
                break;
            case "Sleep":
                playlist.add(rawFilesMap.get("sleep_track1"));
                playlist.add(rawFilesMap.get("sleep_track2"));
                break;
            case "Relaxation":
                playlist.add(rawFilesMap.get("relaxation_track1"));
                playlist.add(rawFilesMap.get("relaxation_track2"));
                break;
            default:
                // Default playlist
                playlist.add(rawFilesMap.get("stress_relief_track1"));
        }
    }

    private void initializePlayer() {
        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);

        // Request audio focus
        requestAudioFocus();

        // Add media items from the raw directory
        for (int resId : playlist) {
            MediaItem mediaItem = MediaItem.fromUri("android.resource://" + getPackageName() + "/" + resId);
            exoPlayer.addMediaItem(mediaItem);
        }

        // Set ExoPlayer volume to maximum
        exoPlayer.setVolume(1.0f); // 1.0 is the maximum volume for ExoPlayer

        // Prepare the player
        exoPlayer.prepare();
        exoPlayer.play();
    }

    private void requestAudioFocus() {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioManager != null) {
            audioManager.requestAudioFocus(
                    focusChange -> {
                        if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                            if (exoPlayer != null) {
                                exoPlayer.pause();
                            }
                        }
                    },
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN
            );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }
}
