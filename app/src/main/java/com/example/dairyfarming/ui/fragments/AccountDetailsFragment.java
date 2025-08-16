package com.example.dairyfarming.ui.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.example.dairyfarming.auth.AuthService;
import com.example.dairyfarming.R;
import com.example.dairyfarming.ui.LoginActivity;

public class AccountDetailsFragment extends Fragment {

    private AuthService auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_account_details, container, false);

        auth = new AuthService(getContext());

        TextView tvEmail = view.findViewById(R.id.tvEmail);
        Button btnDelete = view.findViewById(R.id.btnDeleteAccount);

        // Get email from fragment arguments (passed from DashboardActivity)
        String email = getArguments() != null ? getArguments().getString("email") : null;

        // Fallback to AuthService if email not passed
        if (email == null || email.isEmpty()) {
            email = auth.getCurrentUserEmail();
        }

        tvEmail.setText(email != null ? email : "Unknown User");

        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete Account")
                    .setMessage("Are you sure you want to delete your account? This cannot be undone.")
                    .setPositiveButton("Yes", (dialog, which) -> deleteAccount())
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        return view;
    }

    private void deleteAccount() {
        new Thread(() -> {
            boolean deleted = auth.deleteCurrentUser();
            if (getActivity() == null) return;

            getActivity().runOnUiThread(() -> {
                if (deleted) {
                    Toast.makeText(getContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "Failed to delete account", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}
