package com.example.camsphere;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class BreathingExerciseActivity extends AppCompatActivity {

    private TextView tvInstructions;
    private TextView tvTimer;
    private Button btnStart, btnStop, btnBack;
    private CountDownTimer countDownTimer;
    private boolean isExerciseRunning = false;
    private int breathPhase = 0;  // 0 = inhale, 1 = hold, 2 = exhale
    private static final int INHALE_TIME = 4000;  // 4 seconds
    private static final int HOLD_TIME = 4000;    // 4 seconds
    private static final int EXHALE_TIME = 4000;  // 4 seconds
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private long vibrationDuration = 500; // in milliseconds

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathing); // Ensure this is your correct layout file

        tvInstructions = findViewById(R.id.tvInstructions);
        tvTimer = findViewById(R.id.tvTimer);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnBack = findViewById(R.id.btnBack);

        // Initialize MediaPlayer with error handling
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.breath_sound);  // breath_sound.mp3 in res/raw
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing media player: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Initialize Vibrator service with check for null
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator == null) {
            Toast.makeText(this, "Vibration service is not available on this device.", Toast.LENGTH_LONG).show();
        }

        tvInstructions.setText("Tap Start to begin the breathing exercise");
        tvTimer.setText("0.0");

        // Start button click listener
        btnStart.setOnClickListener(v -> startBreathingExercise());

        // Stop button click listener
        btnStop.setOnClickListener(v -> stopBreathingExercise());

        // Back button click listener (finish the activity)
        btnBack.setOnClickListener(v -> finish());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startBreathingExercise() {
        if (isExerciseRunning) {
            Toast.makeText(this, "Breathing exercise is already running", Toast.LENGTH_SHORT).show();
            return;
        }

        isExerciseRunning = true;
        breathPhase = 0;
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
        tvInstructions.setText("Inhale...");

        // Start the countdown timer for the first phase (Inhale)
        startBreathingPhase();
    }

    private void stopBreathingExercise() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isExerciseRunning = false;
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        tvInstructions.setText("Breathing exercise stopped.");
        tvTimer.setText("0.0");  // Reset timer display
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startBreathingPhase() {
        long phaseTime = 0;

        switch (breathPhase) {
            case 0:
                phaseTime = INHALE_TIME;
                tvInstructions.setText("Inhale...");
                break;
            case 1:
                phaseTime = HOLD_TIME;
                tvInstructions.setText("Hold...");
                break;
            case 2:
                phaseTime = EXHALE_TIME;
                tvInstructions.setText("Exhale...");
                break;
        }

        playSoundAndVibrate();

        countDownTimer = new CountDownTimer(phaseTime, 100) {  // Update every 100ms for smoother display
            @Override
            public void onTick(long millisUntilFinished) {
                // Update the timer display
                updateTimerDisplay(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                breathPhase = (breathPhase + 1) % 3;
                startBreathingPhase();
            }
        };

        countDownTimer.start();
    }

    private void updateTimerDisplay(long millisUntilFinished) {
        int seconds = (int) (millisUntilFinished / 1000);
        int tenths = (int) ((millisUntilFinished % 1000) / 100);
        String timeLeftFormatted = String.format("%d.%d", seconds, tenths);
        tvTimer.setText(timeLeftFormatted);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void playSoundAndVibrate() {
        // Play the sound to guide the user
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }

        // Vibrate as feedback for tactile guidance
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(vibrationDuration, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.release();  // Release media player to avoid memory leaks
        }
    }
}