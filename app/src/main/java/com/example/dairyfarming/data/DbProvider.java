// data/DbProvider.java
package com.example.dairyfarming.data;

import android.content.Context;
import androidx.room.Room;

public class DbProvider {
    private static AppDatabase db;
    public static synchronized AppDatabase get(Context ctx){
        if(db == null){
            db = Room.databaseBuilder(ctx.getApplicationContext(),
                            AppDatabase.class, "dairy.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return db;
    }
}
