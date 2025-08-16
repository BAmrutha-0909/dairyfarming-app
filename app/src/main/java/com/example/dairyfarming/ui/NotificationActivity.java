package com.example.dairyfarming.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dairyfarming.data.AppDatabase;
import com.example.dairyfarming.data.DbProvider;
import com.example.dairyfarming.data.Setting;
import com.example.dairyfarming.databinding.ActivityNotificationBinding;

public class NotificationActivity extends AppCompatActivity {

    private ActivityNotificationBinding binding;
    private AppDatabase db;
    private Setting setting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = DbProvider.get(this);

        // Load settings from DB
        new Thread(() -> {
            setting = db.settingDao().getOne();
            if (setting == null) {
                setting = new Setting();
                setting.updatedAt = System.currentTimeMillis();
                db.settingDao().insert(setting);
            }
            runOnUiThread(this::applyUiFromModel);
        }).start();

        // Listeners
        binding.swVaccination.setOnCheckedChangeListener((btn, isChecked) -> saveToggles());
        binding.swMilk.setOnCheckedChangeListener((btn, isChecked) -> saveToggles());
        binding.swFeeding.setOnCheckedChangeListener((btn, isChecked) -> saveToggles());

        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void applyUiFromModel() {
        binding.swVaccination.setChecked(setting.notifVaccination);
        binding.swMilk.setChecked(setting.notifMilk);
        binding.swFeeding.setChecked(setting.notifFeeding);
    }

    private void saveToggles() {
        new Thread(() -> {
            setting.notifVaccination = binding.swVaccination.isChecked();
            setting.notifMilk = binding.swMilk.isChecked();
            setting.notifFeeding = binding.swFeeding.isChecked();
            setting.updatedAt = System.currentTimeMillis();
            db.settingDao().update(setting);

            runOnUiThread(() ->
                    Toast.makeText(this, "Notification preferences saved", Toast.LENGTH_SHORT).show());
        }).start();
    }
}
