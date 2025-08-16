// app/src/main/java/com/example/dairyfarming/data/AppDatabase.java
package com.example.dairyfarming.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

// Add exportSchema = false to avoid schema warnings
@Database(
        entities = {Buffalo.class, User.class, Setting.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BuffaloDao buffaloDao();
    public abstract UserDao userDao();
    public abstract SettingDao settingDao();
}
