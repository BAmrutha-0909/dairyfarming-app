// data/SettingDao.java
package com.example.dairyfarming.data;

import androidx.lifecycle.LiveData;
import androidx.room.*;

@Dao
public interface SettingDao {
    @Query("SELECT * FROM settings LIMIT 1")
    LiveData<Setting> liveOne();

    @Query("SELECT * FROM settings LIMIT 1")
    Setting getOne();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Setting s);

    @Update
    void update(Setting s);
}
