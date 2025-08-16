package com.example.dairyfarming.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dairyfarming.auth.AuthService;
import com.example.dairyfarming.databinding.ActivityChangePasswordBinding;

public class ChangePasswordActivity extends AppCompatActivity {

    private ActivityChangePasswordBinding bind;
    private AuthService auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        auth = new AuthService(this);

        bind.btnChangePassword.setOnClickListener(v -> handleChangePassword());
    }

    private void handleChangePassword() {
        String oldPass = bind.etCurrentPassword.getText().toString().trim();
        String newPass = bind.etNewPassword.getText().toString().trim();
        String confirmPass = bind.etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(oldPass) || TextUtils.isEmpty(newPass) || TextUtils.isEmpty(confirmPass)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Run in background
        new Thread(() -> {
            boolean changed = auth.changePassword(oldPass, newPass);

            runOnUiThread(() -> {
                if (changed) {
                    Toast.makeText(this, "Password changed successfully", Toast.LENGTH_LONG).show();
                    // Optional: logout or redirect to login screen
                    auth.clearCurrentUserEmail();
                    finish(); // go back to login
                } else {
                    Toast.makeText(this, "Old password is incorrect", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}
