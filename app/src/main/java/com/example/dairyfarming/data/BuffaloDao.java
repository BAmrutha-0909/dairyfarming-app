package com.example.dairyfarming.data;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import java.util.List;

@Dao
public interface BuffaloDao {
    @Query("SELECT * FROM buffalo WHERE deleted = 0 ORDER BY name ASC")
    LiveData<List<Buffalo>> liveAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Buffalo b);

    @Update
    void update(Buffalo b);

    @Query("UPDATE buffalo SET deleted = 1, updatedAt = :ts WHERE id = :id")
    void softDelete(long id, long ts);

    @Query("SELECT * FROM buffalo")
    List<Buffalo> rawAll(); // for export & sync
}
