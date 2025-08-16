// data/Setting.java
package com.example.dairyfarming.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "settings")
public class Setting {
    @PrimaryKey(autoGenerate = true) public long id;
    public boolean notifVaccination;
    public boolean notifMilk;
    public boolean notifFeeding;
    public long updatedAt;
}
