package com.example.dairyfarming.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import com.example.dairyfarming.auth.AuthService;
import com.example.dairyfarming.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding bind;
    private AuthService auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        auth = new AuthService(this);

        bind.btnLogin.setOnClickListener(v -> handleLogin());
        bind.btnSignup.setOnClickListener(v ->
                startActivity(new Intent(this, SignupActivity.class)));

        // Handle Forgot Password
        bind.tvForgotPassword.setOnClickListener(v -> showForgotPasswordDialog());
    }

    private void handleLogin() {
        String email = bind.etEmail.getText().toString().trim();
        String pass = bind.etPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            boolean success = false;
            try {
                success = auth.login(email, pass);
            } catch (Exception e) {
                e.printStackTrace();
            }

            boolean finalSuccess = success;
            runOnUiThread(() -> {
                if (finalSuccess) {
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Password");

        // Input for email
        final android.widget.EditText input = new android.widget.EditText(this);
        input.setHint("Enter your email");
        builder.setView(input);

        builder.setPositiveButton("Submit", (dialog, which) -> {
            String email = input.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // For demo, just show a toast. Replace with real reset logic if needed
            boolean exists = auth.getCurrentUserEmail() != null && auth.getCurrentUserEmail().equals(email);
            if (exists) {
                Toast.makeText(this, "Password reset link sent to " + email, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Email not found", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
