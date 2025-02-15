package com.example.camsphere;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StreakTrackerActivity extends AppCompatActivity {

    private TextView tvCurrentStreak;
    private TextView tvMotivationalMessage;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaktracker);

        // Initialize UI components with correct IDs from XML
        tvCurrentStreak = findViewById(R.id.tvCurrentStreak);
        tvMotivationalMessage = findViewById(R.id.tvMotivationalMessage);
        btnBack = findViewById(R.id.btnBack);

        // Get the current streak from Firebase
        getCurrentStreakFromFirebase();

        // Back button click listener
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Go back to the previous activity
            }
        });
    }

    // Function to get the current streak from Firebase
    private void getCurrentStreakFromFirebase() {
        // Get the current user's UID from Firebase Authentication
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Reference to the Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users").child(userId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get the streak and last login date from the database
                    Integer currentStreak = dataSnapshot.child("streak").getValue(Integer.class);
                    String lastLoginDate = dataSnapshot.child("lastLoginDate").getValue(String.class);

                    // Check if the streak is initialized (if not, set to 0)
                    if (currentStreak == null) {
                        currentStreak = 0;
                    }

                    // Get today's date
                    String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                    // If the last login was yesterday, increment streak
                    if (lastLoginDate != null && isConsecutiveDay(lastLoginDate, todayDate)) {
                        currentStreak++;
                        tvMotivationalMessage.setText("You're on a roll! Keep it up!");
                    } else if (lastLoginDate == null || !lastLoginDate.equals(todayDate)) {
                        tvMotivationalMessage.setText("Start your streak today!");
                    }

                    // Update Firebase with the new streak and today's date
                    userRef.child("streak").setValue(currentStreak);
                    userRef.child("lastLoginDate").setValue(todayDate);

                    // Update UI with the streak count
                    tvCurrentStreak.setText("Current Streak: " + currentStreak + " days");
                } else {
                    tvCurrentStreak.setText("No streak data found");
                    tvMotivationalMessage.setText("Start your streak today!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                tvCurrentStreak.setText("Error retrieving streak data");
            }
        });
    }

    // Function to check if two dates are consecutive
    private boolean isConsecutiveDay(String lastLoginDate, String todayDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date lastDate = sdf.parse(lastLoginDate);
            Date today = sdf.parse(todayDate);

            long difference = today.getTime() - lastDate.getTime();
            long oneDayMillis = 1000 * 60 * 60 * 24;
            return difference == oneDayMillis;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}