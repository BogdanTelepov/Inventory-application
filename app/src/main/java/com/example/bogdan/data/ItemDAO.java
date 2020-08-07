package com.example.bogdan.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bogdan.model.Item;

import java.util.List;

@Dao
public interface ItemDAO {
    @Insert
    void insert(Item item);

    @Update
    void update(Item item);

    @Delete
    void delete(Item item);

    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    @Query("SELECT * FROM note_table WHERE id=:id")
    LiveData<Item> getItem(int id);

    @Query("SELECT * FROM note_table")
    LiveData<List<Item>> getAllNotes();
}
