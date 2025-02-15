package com.example.camsphere;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class CategoryActivity extends BaseActivity {

    private Button btnStressRelief, btnFocus, btnSleep, btnRelaxation, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Initialize buttons
        btnStressRelief = findViewById(R.id.btnStressRelief);
        btnFocus = findViewById(R.id.btnFocus);
        btnSleep = findViewById(R.id.btnSleep);
        btnRelaxation = findViewById(R.id.btnRelaxation);
        btnBack = findViewById(R.id.btnBack);

        // Navigate to MusicActivity with category name
        btnStressRelief.setOnClickListener(v -> navigateToMusicActivity("Stress Relief"));
        btnFocus.setOnClickListener(v -> navigateToMusicActivity("Focus"));
        btnSleep.setOnClickListener(v -> navigateToMusicActivity("Sleep"));
        btnRelaxation.setOnClickListener(v -> navigateToMusicActivity("Relaxation"));

        // Back button to return to the previous activity
        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void navigateToMusicActivity(String category) {
        Intent intent = new Intent(CategoryActivity.this, MusicActivity.class);
        intent.putExtra("category", category); // Pass the category name
        startActivity(intent);
    }
}
