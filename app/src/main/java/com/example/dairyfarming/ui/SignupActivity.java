package com.example.dairyfarming.ui;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dairyfarming.auth.AuthService;
import com.example.dairyfarming.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding bind;
    private AuthService auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        auth = new AuthService(this);

        bind.btnSignUp.setOnClickListener(v -> handleSignup());
        bind.tvBackToLogin.setOnClickListener(v -> finish());
    }

    private void handleSignup() {
        String name = bind.etFullName.getText().toString().trim();
        String email = bind.etEmail.getText().toString().trim();
        String pass = bind.etPassword.getText().toString();

        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Run signup in a background thread
        new Thread(() -> {
            boolean success = false;
            try {
                success = auth.signup(name, email, pass);
            } catch (Exception e) {
                e.printStackTrace();
            }

            boolean finalSuccess = success;
            runOnUiThread(() -> {
                if (finalSuccess) {
                    Toast.makeText(SignupActivity.this, "Signup success. Please login.", Toast.LENGTH_SHORT).show();
                    finish(); // back to login
                } else {
                    Toast.makeText(SignupActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}
