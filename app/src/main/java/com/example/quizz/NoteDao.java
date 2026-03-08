package com.example.quizz;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.lang.reflect.Array;
import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    void insert(Nota persona);

    @Update
    void update(Nota persona);

    @Delete
    void delete(Nota persona);

    @Query("DELETE FROM note_table")
    void deleteall();

    @Query("SELECT * FROM note_table ORDER BY nombre DESC")
    LiveData<List<Nota>> getAll();
}
