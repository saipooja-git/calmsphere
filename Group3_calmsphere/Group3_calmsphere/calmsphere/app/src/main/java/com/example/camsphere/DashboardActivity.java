package com.example.camsphere;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class DashboardActivity extends BaseActivity {

    private Button btnMeditation, btnViewProfile, btnStreakTracker, btnBreathingExercise, btnBreathingSettings; // Added new button
    private static final String TAG = "DashboardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard); // Use the updated dashboard layout
        Log.d(TAG, "onCreate called");

        // Initialize buttons
        btnMeditation = findViewById(R.id.btnMeditation);
        btnViewProfile = findViewById(R.id.btnViewProfile);
        btnStreakTracker = findViewById(R.id.btnStreakTracker);
        btnBreathingExercise = findViewById(R.id.btnBreathingExercise);
        // Set up navigation intents
        btnMeditation.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, CategoryActivity.class);
            startActivity(intent);
        });

        btnViewProfile.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ViewProfileActivity.class);
            startActivity(intent);
        });

        btnStreakTracker.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, StreakTrackerActivity.class);
            startActivity(intent);
        });

        btnBreathingExercise.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, BreathingExerciseActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }
}