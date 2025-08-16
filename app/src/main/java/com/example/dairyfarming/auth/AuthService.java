package com.example.dairyfarming.auth;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.dairyfarming.data.*;
import java.util.concurrent.*;

public class AuthService {

    private final AppDatabase db;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final SharedPreferences prefs;
    private static final String PREFS_NAME = "auth_prefs";
    private static final String KEY_EMAIL = "current_user_email";

    public AuthService(Context ctx) {
        db = DbProvider.get(ctx);
        prefs = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    private String hash(String s) {
        return Integer.toHexString(s.hashCode()); // simple hash for demo
    }

    // Signup a new user
    public boolean signup(String name, String email, String pass) throws ExecutionException, InterruptedException {
        Callable<Boolean> task = () -> {
            if (db.userDao().findByEmail(email) != null) return false;
            User u = new User();
            u.fullName = name;
            u.email = email;
            u.passwordHash = hash(pass);
            db.userDao().insert(u);
            return true;
        };
        return executor.submit(task).get();
    }

    // Login with email & password
    public boolean login(String email, String pass) throws ExecutionException, InterruptedException {
        Callable<Boolean> task = () -> {
            User u = db.userDao().findByEmail(email);
            boolean valid = u != null && u.passwordHash.equals(hash(pass));
            if (valid) setCurrentUserEmail(email);
            return valid;
        };
        return executor.submit(task).get();
    }

    // Change password (verifies old password first)
    public boolean changePassword(String oldPass, String newPass) {
        String email = getCurrentUserEmail();
        if (email == null) return false;

        Callable<Boolean> task = () -> {
            User u = db.userDao().findByEmail(email);
            if (u != null && u.passwordHash.equals(hash(oldPass))) {
                u.passwordHash = hash(newPass);
                db.userDao().update(u);
                return true;
            }
            return false;
        };

        try {
            return executor.submit(task).get();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete the current user
    public boolean deleteCurrentUser() {
        String email = getCurrentUserEmail();
        if (email == null) return false;

        Callable<Boolean> task = () -> {
            User u = db.userDao().findByEmail(email);
            if (u != null) {
                db.userDao().delete(u);
                clearCurrentUserEmail();
                return true;
            }
            return false;
        };

        try {
            return executor.submit(task).get();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get current logged-in user's email
    public String getCurrentUserEmail() {
        return prefs.getString(KEY_EMAIL, null);
    }

    // Set current logged-in user's email
    public void setCurrentUserEmail(String email) {
        prefs.edit().putString(KEY_EMAIL, email).apply();
    }

    // Clear current logged-in user's email
    public void clearCurrentUserEmail() {
        prefs.edit().remove(KEY_EMAIL).apply();
    }
}
