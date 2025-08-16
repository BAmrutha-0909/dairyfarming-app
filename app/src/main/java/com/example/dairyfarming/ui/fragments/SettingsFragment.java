package com.example.dairyfarming.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dairyfarming.databinding.FragmentSettingsBinding;
import com.example.dairyfarming.auth.AuthService;
import com.example.dairyfarming.ui.ChangePasswordActivity;
import com.example.dairyfarming.ui.LoginActivity;
import com.example.dairyfarming.ui.NotificationActivity;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        // Notifications
        binding.btnNotificationPrefs.setOnClickListener(v ->
                startActivity(new Intent(getContext(), NotificationActivity.class)));

        // Change Password
        binding.btnChangePassword.setOnClickListener(v ->
                startActivity(new Intent(getContext(), ChangePasswordActivity.class)));




        // About App
        binding.btnAboutApp.setOnClickListener(v ->
                Toast.makeText(getContext(), "DairyFarming v1.0", Toast.LENGTH_SHORT).show());

        // Logout
        binding.btnLogout.setOnClickListener(v -> {
            if (getActivity() != null) {
                // Clear current user
                new AuthService(getContext()).clearCurrentUserEmail();

                // Go to LoginActivity
                startActivity(new Intent(getContext(), LoginActivity.class));

                // Close all previous activities
                getActivity().finishAffinity();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
