package com.example.inventory_application;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ItemDao {
    @Insert
    void insert(Item item);

    @Update
    void update(Item item);

    @Query("UPDATE items SET quantity = MAX(quantity + :amount, 0) WHERE id = :id")
    void changeQuantity(int id, int amount);

    @Query("DELETE FROM items WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT * FROM items WHERE id = :id LIMIT 1")
    Item getById(int id);

    @Query("SELECT * FROM items ORDER BY name ASC")
    List<Item> getAll();
}
