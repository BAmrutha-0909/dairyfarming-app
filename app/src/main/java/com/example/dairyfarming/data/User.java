// data/User.java
package com.example.dairyfarming.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @PrimaryKey(autoGenerate = true) public long id;
    public String fullName;
    public String email;
    public String passwordHash; // simple hash; demo only
}
