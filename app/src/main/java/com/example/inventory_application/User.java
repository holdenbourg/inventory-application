package com.example.inventory_application;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "users",
        indices = {
                @Index(value = {"username"}, unique = true),
                @Index(value = {"email"}, unique = true)
        }
)

public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String firstName;
    public String lastName;
    public String email;
    public String username;
    public String password;


    public User() {}
}
