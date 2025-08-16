package com.example.dairyfarming.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dairyfarming.databinding.ActivityAboutAppBinding;

public class AboutAppActivity extends AppCompatActivity {

    private ActivityAboutAppBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set app information
        binding.textViewAppName.setText("DairyFarming");
        binding.textViewVersion.setText("Version 1.0");
        binding.textViewDescription.setText("DairyFarming helps dairy farmers manage their daily operations efficiently, including tracking sales, profits, and animal health.");
        binding.textViewDeveloper.setText("Developed by: Your Name / Company");
        binding.textViewContact.setText("Contact: support@dairyfarming.com");

        // Privacy Policy click
        // Replace with actual URL


        // Optional: show a toast when clicking the app icon
        binding.imgAppIcon.setOnClickListener(v ->
                Toast.makeText(this, "DairyFarming v1.0", Toast.LENGTH_SHORT).show()
        );
    }
}
