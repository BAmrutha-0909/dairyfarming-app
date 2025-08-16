
package com.example.dairyfarming.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "buffalo")
public class Buffalo {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;
    public double litersPerDay;

    // optional ML fields
    public Double predictedLitersNextDay; // nullable

    public long updatedAt;
    public boolean deleted; // for soft delete & sync
}
