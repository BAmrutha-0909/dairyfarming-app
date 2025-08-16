package com.example.dairyfarming.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dairyfarming.auth.AuthService;
import com.example.dairyfarming.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding bind;
    private AuthService auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        auth = new AuthService(this);

        // Set the logged-in email beside the profile icon
        String email = auth.getCurrentUserEmail();
        bind.tvProfileEmail.setText(email != null ? email : "Unknown User");

        // Buttons to navigate to different screens
        bind.btnSummary.setOnClickListener(v ->
                startActivity(new Intent(this, SummaryActivity.class)));

        bind.btnSales.setOnClickListener(v ->
                startActivity(new Intent(this, SalesProfitActivity.class)));

        bind.btnAccount.setOnClickListener(v ->
                startActivity(new Intent(this, AccountDetailsActivity.class)));

        bind.btnSettings.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));
    }
}
