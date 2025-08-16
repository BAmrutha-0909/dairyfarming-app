package com.example.dairyfarming.data;

import androidx.room.*;
import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    User findByEmail(String email);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(User u);

    @Delete
    void delete(User u);

    @Update
    void update(User u);

    // NEW: fetch all users for export
    @Query("SELECT * FROM user")
    List<User> getAllUsers();
}
