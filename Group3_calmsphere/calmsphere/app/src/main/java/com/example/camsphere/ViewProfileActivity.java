package com.example.camsphere;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewProfileActivity extends AppCompatActivity {

    private EditText editName, editEmail, editPhone;
    private CheckBox editLanguageEnglish, editLanguageHindi, editLanguageSpanish;
    private Button btnEdit, btnSave, btnBack, btnLogout;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewprofile);

        // Initialize UI elements
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);
        editLanguageEnglish = findViewById(R.id.editLanguageEnglish);
        editLanguageHindi = findViewById(R.id.editLanguageHindi);
        editLanguageSpanish = findViewById(R.id.editLanguageSpanish);
        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        btnLogout = findViewById(R.id.btnLogout); // Initialize Logout button

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = auth.getCurrentUser().getUid();

        // Load user data
        loadUserProfile();

        // Set up Edit button
        btnEdit.setOnClickListener(v -> enableEditing(true));

        // Set up Save button
        btnSave.setOnClickListener(v -> saveUserProfile());

        // Set up Back button
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ViewProfileActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });

        // Set up Logout button
        btnLogout.setOnClickListener(v -> {
            auth.signOut(); // Sign out the user
            Intent intent = new Intent(ViewProfileActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadUserProfile() {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        editName.setText(documentSnapshot.getString("name"));
                        editEmail.setText(documentSnapshot.getString("email"));
                        editPhone.setText(documentSnapshot.getString("phone"));

                        ArrayList<String> languages = (ArrayList<String>) documentSnapshot.get("languages");
                        if (languages != null) {
                            editLanguageEnglish.setChecked(languages.contains("English"));
                            editLanguageHindi.setChecked(languages.contains("Hindi"));
                            editLanguageSpanish.setChecked(languages.contains("Spanish"));
                        }
                    } else {
                        Toast.makeText(this, "No profile data found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void enableEditing(boolean enable) {
        editName.setEnabled(enable);
        editEmail.setEnabled(enable);
        editPhone.setEnabled(enable);
        editLanguageEnglish.setEnabled(enable);
        editLanguageHindi.setEnabled(enable);
        editLanguageSpanish.setEnabled(enable);
        btnSave.setVisibility(enable ? View.VISIBLE : View.GONE);
        btnEdit.setVisibility(enable ? View.GONE : View.VISIBLE);
    }

    private void saveUserProfile() {
        String name = editName.getText().toString();
        String email = editEmail.getText().toString();
        String phone = editPhone.getText().toString();

        ArrayList<String> languages = new ArrayList<>();
        if (editLanguageEnglish.isChecked()) languages.add("English");
        if (editLanguageHindi.isChecked()) languages.add("Hindi");
        if (editLanguageSpanish.isChecked()) languages.add("Spanish");

        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("name", name);
        updatedData.put("email", email);
        updatedData.put("phone", phone);
        updatedData.put("languages", languages);

        db.collection("users").document(userId)
                .update(updatedData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    enableEditing(false); // Disable editing mode
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
