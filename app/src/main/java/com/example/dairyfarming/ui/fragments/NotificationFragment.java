package com.example.dairyfarming.ui.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dairyfarming.R;

import java.util.Calendar;

public class NotificationFragment extends Fragment {

    private Switch swVaccination, swMilk, swFeeding;
    private SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notification, container, false);

        swVaccination = root.findViewById(R.id.swVaccination);
        swMilk = root.findViewById(R.id.swMilk);
        swFeeding = root.findViewById(R.id.swFeeding);

        prefs = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);

        // Load saved states
        swVaccination.setChecked(prefs.getBoolean("notify_vaccination", false));
        swMilk.setChecked(prefs.getBoolean("notify_milk", false));
        swFeeding.setChecked(prefs.getBoolean("notify_feeding", false));

        // Listeners
        swVaccination.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("notify_vaccination", isChecked).apply();
            scheduleNotification("Vaccination Reminder", "Don't forget to vaccinate your buffalo!", 100, isChecked);
        });

        swMilk.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("notify_milk", isChecked).apply();
            scheduleNotification("Milk Reminder", "Time to milk your buffalo!", 101, isChecked);
        });

        swFeeding.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("notify_feeding", isChecked).apply();
            scheduleNotification("Feeding Reminder", "Feed your buffalo now!", 102, isChecked);
        });

        return root;
    }

    private void scheduleNotification(String title, String text, int reqCode, boolean enable) {
        Intent intent = new Intent(getContext(), NotificationReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("text", text);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), reqCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        if(enable){
            // Schedule daily notification at 8 AM
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 8);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            // Cancel notification
            alarmManager.cancel(pendingIntent);
        }
    }
}
