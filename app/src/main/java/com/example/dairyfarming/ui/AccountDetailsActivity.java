package com.example.dairyfarming.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dairyfarming.auth.AuthService;
import com.example.dairyfarming.databinding.ActivityAccountDetailsBinding;

public class AccountDetailsActivity extends AppCompatActivity {

    private ActivityAccountDetailsBinding bind;
    private AuthService auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityAccountDetailsBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        auth = new AuthService(this);

        // Get email from intent
        String email = getIntent().getStringExtra("email");
        if (email == null || email.isEmpty()) {
            email = auth.getCurrentUserEmail(); // fallback if not passed
        }

        bind.tvEmail.setText(email != null ? email : "Unknown User");

        // Delete account button
        bind.btnDeleteAccount.setOnClickListener(v -> showDeleteConfirmation());
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This cannot be undone.")
                .setPositiveButton("Yes", (dialog, which) -> deleteAccount())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteAccount() {
        new Thread(() -> {
            boolean deleted = auth.deleteCurrentUser();
            runOnUiThread(() -> {
                if (deleted) {
                    Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}
